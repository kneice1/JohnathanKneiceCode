/*******************************
 *
 *  Project Name: Project 4
 *  Description: In this project, we read in a wave file to modify it. We can modify the file by reversing the audio, flipping the 
 *  channels, fading in and out, changing the speed, volume and adding an echo. The outcome of these changes are outputed as output.wav.
 *  Date: March 16, 2022
 *  Authors: Ian Johnston, Johnathan Kneice, Suraj Kopparam 
 *
 *******************************/

#include <stdio.h>
#include "wave.h"
#include "prototypes.h"
#include <stdlib.h>
#include <string.h>
#include <limits.h>

/* 
   Description: Decides what process to do based on the command line prompt. 
   				-r for reverse, -s to change the speed, -f to flip the left 
   				and right stereo channels, -o to fade out, -i to fade in, 
   				-v to change the volume and -e to add echo. Throws various errrors aswell. 
				
   Parameters:	argc	- number of characters in the prompt
   				**argv 	- array of characters in the prompt
*/
int main(int argc,char **argv)
{
	WaveHeader header;
	//Read in the header.
	readHeader(&header);
	//Checks if the header ID contains RIFF
	if(strncmp((char*)header.ID, "RIFF", 4) != 0)
	{
		fprintf(stderr, "File is not a RIFF file\n");
		return 1;
	}
	//Checks if the format chunks ID contains fmt 
	if(strncmp((char*)header.formatChunk.ID, "fmt ", 4) != 0)
	{
		fprintf(stderr, "Format chunk is corrupted\n");
		return 1;
	}
	//Checks if the data chunk ID contains data
	if(strncmp((char*)header.dataChunk.ID, "data", 4) != 0)
	{
		fprintf(stderr, "Format chunk is corrupted\n");
		return 1;
	}
	//Checks if the .wav has a left and a right channel
	if(header.formatChunk.channels != 2)
	{
		fprintf(stderr, "File is not stereo\n");
		return 1;
	}
	//Checks if the .wav files sample rate is 44,100
	if(header.formatChunk.sampleRate != 44100)
	{
		fprintf(stderr, "File does not use 44,100Hz sample rate\n");
		return 1;
	}
	//Checks if the .wav files bits per sample is 16
	if(header.formatChunk.bitsPerSample != 16)
	{
		fprintf(stderr, "File does not have 16-bit samples\n");
		return 1;
	}
	//Make two arrays of size size.
	//Size is the header's dataCunk's size divided by 4.
	int size=header.dataChunk.size /4;
	short *leftArray = calloc(size, sizeof(short));
	short *rightArray = calloc(size, sizeof(short));
	//Checks to see if calloc was called correctly
	if (leftArray == NULL || rightArray == NULL)
	{
		fprintf(stderr, "Program out of memory\n");
		return 1;
	}

	//Reads in the iformation into the arrays.
	for(int i = 0;i<size;i++){
		leftArray[i] = readShort();
		rightArray[i] = readShort();
	}
	// Checks to see what the user wants to be done to the file.
	for (int i = 1; i < argc; ++i)
	{
		//If the user puts a -r, reverse the arrays.
		if (strcmp(argv[i], "-r") == 0)
		{
			reverse(size, leftArray);
			reverse(size, rightArray);
		// If the user puts a -s, change the speed by given amount.
		}else if(strcmp(argv[i], "-s") == 0){
			++i;
			double factor = atof(argv[i]);
			// If the speed inputted is less than 0, print out an error in stderr.
			if (factor <= 0)
			{
				fprintf(stderr, "A positive number must be supplied for the speed change\n");
				return 1;
			}else{
				//Speed up both arrays by factor and then change size by dividing by factor.
				leftArray = changeSpeed(size, leftArray, factor);
				rightArray = changeSpeed(size, rightArray, factor);
				size = size / factor;
			}
		//If the user puts a -f, switch the arrays using a 3 line swap.
		}else if(strcmp(argv[i], "-f") == 0){
			short *temp = leftArray;
			leftArray = rightArray;
			rightArray = temp;
		//If the user puts a -o, fade out the wave file at given seconds.
		}else if(strcmp(argv[i], "-o") == 0){
			i++;
			// If the number of seconds inputted is less than 0, print out an error in stderr.
			double outSeconds = atof(argv[i]);
			if (outSeconds <= 0)
			{
				fprintf(stderr, "A positive number must be supplied for the fade in and fade out time\n");
				return 1;
			}else{
				fadeOut(outSeconds, size, leftArray);
				fadeOut(outSeconds, size, rightArray);
			}
		//If the user puts a -i, fade in the wave file starting at given seconds.
		}else if(strcmp(argv[i], "-i") == 0){

			i++;
			double inSeconds = atof(argv[i]);
			// If the number of seconds inputted is less than 0, print out an error in stderr.
			if (inSeconds <= 0)
			{
				fprintf(stderr, "A positive number must be supplied for the fade in and fade out time\n");
				return 1;
			}else{
				fadeIn(inSeconds, size, leftArray);
				fadeIn(inSeconds, size, rightArray);
			}
		//If the user puts a -v, change volume by given ammount.
		}else if(strcmp(argv[i], "-v") == 0){
			i++;
			double volScale = atof(argv[i]);
			//If input is less than or equal to 0 print out an error in stderr.
			if (volScale <= 0)
			{
				fprintf(stderr, "A positive number must be supplied for the volume scale\n");
				return 1;
			}else{
				changeVolume(size, leftArray, volScale);
				changeVolume(size, rightArray, volScale);
			}
		//If the user puts an -e add an echo at a give ammount.
		}else if(strcmp(argv[i], "-e") == 0){
			i++;
			double delay = atof(argv[i]);
			i++;
			double echoScale = atof(argv[i]);
			//If input is less than or equal to 0 print out an error in stderr.
			if (delay <= 0 || echoScale <= 0)
			{
				fprintf(stderr, "Positive numbers must be supplied for the echo delay and scale parameters\n");
				return 1;
			}else{
				leftArray = echo(size, leftArray, delay, echoScale);
				rightArray = echo(size, rightArray, delay, echoScale);
				//Resize the size by the ammount of seconds added.
				size = size + delay * 44100;
				
			}
		}else{
			//If user input is something other than one of the comands pirnt out in stderr.
			fprintf(stderr, "Usage: wave [[-r][-s factor][-f][-o delay][-i delay][-v scale][-e delay scale] < input > output\n");
			return 1;	
		}
	}
	//Output output.wav.
	header.dataChunk.size = size * 4;
	header.size = 36 + header.dataChunk.size;
	writeHeader(&header);
	for(int i = 0;i<size;i++){
		writeShort(leftArray[i]);
		writeShort(rightArray[i]);
	}
	//Free the arrays.
	free(leftArray);
	free(rightArray);
}

/* 
   Description: uses getchar() and bitwise operations to read in a short.
				
   Returns: 	short
*/
short readShort()
{
	//Get the char.
	int first = getchar();
	//Checking to see if the file size is the correct size
	if(first == EOF){
		fprintf(stderr, "File size does not match size in header\n");
		exit(1);
	}
	//Left shift it by 8 into a new variable.
	int second = getchar();
	if(second == EOF){
		fprintf(stderr, "File size does not match size in header\n");
		exit(1);
	}
	second <<= 8;
	//Return the or of the two.
	return first|second;
}

/* 
   Description: uses putchar() and bitwise operations to write a short.
*/
void writeShort(short s)
{
	//And s with 255.
	char first = s&255;
	//Right shift by 8 into new variable.
	char second= s>>8;
	// Putchar the two.
	putchar(first); 
	putchar(second);
}

/* 
   Description: takes each array of short values representing samples and 
   				reverses the order of its elements. The new arrays will effectively contain the audio backwards.
				
   Parameters: 	size 	- the size of the array being reversed
   				array 	- array of shorts that will be converted to samples and reversed
				
   Returns: 	the array of shorts reversed
*/
void reverse(int size,short* array)
{
	//Reverse array.
	//Run through the array till i = size/2.
	for (int i = 0; i < size/2; ++i)
	{
		//Three line swap of the variable moving the front to the back and back to the front.
		int temp = array[i];
		array[i] = array[size - i -1];
		array[size - i -1] = temp;
	}
}

/* 
   Description: Allocates a new array of shorts whose lengths is length/factor, where length 
   				is the original length of the array and factor is the amount by which you are speeding up 
   				the audio. Then, loop through the new arrays filling them with values from the original arrays. 
   				Element i in the new arrays will correspond to element i*factor from the old arrays. 

   Parameters: 	size 	- size of the array being processed
   				array 	- array of shorts that will be converted to samples and then processed
   				factor 	- how much the sound will be sped up or down
				
   Returns:		a new memory allocated array of shorts
*/
short* changeSpeed(int size,short* array, double factor)
{
	//Find the new size of the sped-up array.
	int length=size/factor;
	// Make new array called temp with the new size.
	short *temp= calloc(length,sizeof(short));
	//Checks to see if calloc was called correctly
	if (temp == NULL)
	{
		fprintf(stderr, "Program out of memory\n");
		exit(1);
	}
	//Loop through the array.
	for (int i = 0; i < length; ++i)
	{
		//Put the value at i * factor into the new array temp.
		temp[i]=array[(int)(i*factor)];
	}
	//Free the old array and return the temp array.
	free(array);
	return temp;
}

/* 
   Description: As the song finishes, the volume will slowly decrease to silence. This uses a 
   				quadratic factor to decrease each sample by a number in between 1.0 and 0.0 as it decreases.
				
   Parameters:	seconds - the amount of seconds the audio will fade out for
   				size 	- size of the array being processed
   				array 	- array of shorts that will be converted to samples and then processed
*/
void fadeOut(double seconds, int size, short* array)
{
	//Convert the seconds input into seconds and put it into new variable out.
	int out = 44100 * seconds;
	//Loop through the array while i is less than out and i is less than size.
	for (int i = 0; i < out && i < size; ++i)
	{
	// Calculate the scale.
	double scale = i / (double)out;
	//Square scale.
	scale *= scale;
	//Set array at size-i-1 to scale * array at location.
	array[size - i - 1] *= scale;
	}

}

/* 
   Description: As the song starts, the volume will slowly increase from silence to full volume. 
   				This uses a quadratic factor to increase the samples from 0.0 to 1.0 for the amount of seconds.
				
   Parameters:	seconds - the amount of seconds the audio will fade in for
   				size 	- size of the array being processed
   				array 	- array of shorts that will be converted to samples and then processed
*/
void fadeIn(double seconds, int size, short* array)
{
	//Convert the seconds input into seconds and put it into new variable in.
	int in = 44100 * seconds;
	//Loop through the array while i is less than in and i is less than size.
	for (int i = 0; i < in && i < size; ++i)
	{
	//Calculate the scale.
	double scale = i / (double)in;	
	//square scale.
	scale *= scale;
	//Set array at size-i-1 to scale * array at location.
	array[i] *= scale;
	}
}

/* 
   Description: Changes the volume of the audio. Multiplys each sample in the array by the factor, 
   				if factor is above 1, it will get louder and quiter if it is under 1. Makes sure the sample isnt above the 
   				short maximum or below the short minimum.
				
   Parameters:	size 	- size of the array being processed
   				array 	- array of shorts that will be converted to samples and then processed
   				factor 	- the factor changing the volume
*/
void changeVolume(int size, short* array, double factor)
{
	//Loop through array.
	for (int i = 0; i < size; ++i)
	{
		//Calculate sample by increasing array at i by factor.
		double sample = array[i] * factor;
		//Make sure sample is not over the MAX or less than the MIN SHRT.
		if (sample > SHRT_MAX)
		{
			sample = SHRT_MAX;
		}else if(sample < SHRT_MIN){
			sample = SHRT_MIN;
		}
		// Set array at i to be sample
		array[i] = sample;
	}
}

/* 
   Description: Adds echo the audio. Allocates memory for a new array that will hold the offset 
   				version of the original array. The new array will be offset by the delay. The old array will be
				added to the new array scaled by the scale factor. This new array will now have the original audio 
				and the new offset audio making it echoed. 
				
   Parameters:	size - size of the array being processed
   				array - array of shorts that will be converted to samples and then processed
   				delay - the amaount of offset for the echo
   				scale - the scale factor for the amount of echo
				
   Returns:		a new memory allocated array of shorts
*/
short* echo(int size, short* array, double delay, double scale)
{
	// Turn the delay into seconds.
	int samples = delay * 44100;
	//Make new array with the size + delay.
	short* echoArray = calloc(size + samples, sizeof(short));
	//Checks to see if calloc was called correctly
	if (echoArray == NULL)
	{
		fprintf(stderr, "Program out of memory\n");
		exit(1);
	}
	//Loop through original array and copy it over.
	for (int i = 0; i < size; ++i)
	{
		echoArray[i] = array[i];
	}
	// Add echo.
	//Loop through original array.
	for (int i = 0; i < size; ++i)
	{
		// Calculate the echoSample at i.
		double echoSample = echoArray[i + samples] + array[i] * scale;
		// Check to see if the echoSample is greater than SHRT_MAX. If so set sample equal 
		//to SHRT_MAX.
		if (echoSample > SHRT_MAX)
		{
			echoSample = SHRT_MAX;
		// Check to see if the echoSample is less than SHRT_MIN. If so set sample equal 
		//to SHRT_MIN.
		}else if(echoSample < SHRT_MIN){
			echoSample = SHRT_MIN;
		}
		//Add echoSample to echoArray.
		echoArray[i + samples] = echoSample;
	}
	//Free original array than return new array.
	free(array);
	return echoArray;
}