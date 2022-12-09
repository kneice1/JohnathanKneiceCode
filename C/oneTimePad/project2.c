/*******************************
 *
 *  Project Name: Project 2
 *  Description: In this project, we have made an encrypter and decrypter that takes a message and a key and returns a
 *  cipher. It does this by first reading in the message and key. Then repeating the key till the key is the same length
 *  as the message. Next, rotate each character in the key, then XOR the result with the message.
 *  Date: February 4, 2022 
 *  Authors: Johnathan Kneice, Tony Nguyen
 *
 *******************************/
#include <stdio.h>
#define LENGTH 2048
#define DELIMITER 255
/*
	Description: Prototype function of rotate
*/
unsigned char rotate(unsigned char c, int count);
/*
	Description: Prototype function of bits
*/
int bits(unsigned char character);
/*
	Description: Main function in which reads in the file and also does the chaining.
*/
int main()
{
	unsigned char message [LENGTH];
	unsigned char key [LENGTH];
	int input = getchar();
	int messageCount = 0;
	int keyCount = 0;
	// Reading in the message into an array.
	while (input != DELIMITER && messageCount<LENGTH)
	{
		message[messageCount]=input;
		messageCount++;
		input = getchar();
	}
	// If the input == the DELIMITER, then begin to read in the key. Else read in till input == DELIMITER.
	while(input != DELIMITER)
	{
		input = getchar();
	}
	input = getchar();
	// Read in the key until the key is equal to the message's length or EFO is read in.
	while(input != EOF && keyCount<messageCount)
	{
		
		key[keyCount] = input;
		keyCount++;
		input = getchar();
	}
	// Duplicate the key till the key's length is equal to the message's length.
	int temp = keyCount;
	while(keyCount < messageCount)
	{
	// If the key runs out of characters, loop back to the beginning of the key by using the % opperator.
		key[keyCount]=key[keyCount%temp];
		keyCount++;
	}
	int sum= key[messageCount-1]%messageCount;
	for (int i = 0; i < messageCount; ++i)
	{
	// If i == 0 then rotate each character in the key XOR the key at location sum by the bits in the key at messageCount-1.
		if (i==0)
		{
			key[i]=rotate(key[i]^key[sum],bits(key[messageCount-1]));
		}
	// Else rotate each character in the key XOR the key at location sum by the bits in the key at location i-1.
		else
		{
			key[i]=rotate(key[i]^key[sum],bits(key[i-1]));
		}
	// Set sum to be the sum + key at location i. Then mod by messageCount.
		sum=(sum+key[i])%messageCount;
	}
	// Output the key XOR messageCount
	for (int i = 0; i < messageCount; ++i)
	{
		putchar(key[i]^message[i]);
	}
	return 0;
}
/*
	Description: Rotate takes in an unsigned charand an int for how much you will shift the bit string of the char by.

	Parameters:  character - a single character in file/text file
				 count     - Is the counter and/or amount in needs to rotated.
	
	Returns: returns the character that was rotated.

*/
unsigned char rotate(unsigned char character, int count) 
{
	// Temp is used to store a one if the one is in the first position of a bit string.
	unsigned char temp = 0;
	for (int i = 0; i < count; ++i)
	{
		// If there is a one in the first location of the bit string.
		if (character & 1)
		{
			// Put a one in the front of bit string temp.
			temp = 1 << 7;
			// Bit or the original bit string with the temp bit string.
			character = character | temp;

		}
		// Right shift the bit string by one.
		character >>= 1;

	}
	return character;
}
/*
	Description: Bit takes in an unsigned char and returns an int of how many ones are in the char's bit string.

	Parameters:  character - a single character in a file/text file

	Returns:     How many bits are in a character.
*/
int bits(unsigned char character)
{
	int count = 0;
	while (character != 0)
	{
		if (character & 1)
		{
			count++;
		}
		character >>= 1;
	}
	return count;
}