/*******************************
 *
 *  Project Name: Project 1
 * 
 *  Date: January 21, 2022  
 *  Authors: Shabib Ahmed, Johnathan Kneice
 *
 *******************************/

#include <stdio.h>
#define COUNT 50

int isHappyNumber(int num){
    int sum; 
    int tempNum=num;
    int ones;
    int tens; 
    int hundreds;

    hundreds = tempNum/100;
    tempNum=tempNum-(hundreds*100);
    tens = tempNum/10;
    tempNum=tempNum-(tens*10);
    ones = tempNum;
    sum = (hundreds * hundreds) + (tens*tens) + (ones * ones);

    if (sum==1){
        return 1;
    }
    if (sum==4){
        return 0;
    }
    return isHappyNumber(sum);
}

int main(){
    printf ("Lazy Caterer's Sequence:\n");
    for (int i =0; i<COUNT; i++){
        int number = (i*i+i+2)/2;
        printf("%d ", number);
    }
    printf ("\n\n");

    printf ("Prime Numbers:\n");

    int numPrint = 0;
    int currentPrime = 2;
    int isPrime = 1; 

    while (numPrint< COUNT){
        isPrime = 1;
       
        for (int i = 2; i*i<=currentPrime; i++){
            if (currentPrime%i==0){
                isPrime=0;
            }
        }

        if(isPrime==1){
            numPrint = numPrint + 1;
            printf("%d ", currentPrime);

        }
        currentPrime = currentPrime+1;

    }     
    printf ("\n\n");

/*Description: From 1 to 50 find the number of times it takes to get the value to one by divding by 2 
  if it is an even number or if it is odd multiply by 3 and add 1.*/
    long long pre =0;
	long long curent=1;
	printf("Fibonacci Sequence:\n");

	for (int i = 0; i < COUNT; ++i)
	{
		printf("%lld ", curent);
		//make pre be the value of the current value and make current be the sum of pre and current.
		//temp is used to keep track of the un added current.
		long long temp=curent;
		curent= pre+curent;
		pre= temp;
	}
    printf ("\n\n");

	printf("Collatz Stopping Times:\n");
	for (int i = 1; i <= COUNT; ++i)
	{
		int callatz=i; // the value.
		int times=0; //curent number of times going through loop.
		while (callatz!=1)
		{
			if (callatz%2==0)
			{
				callatz=callatz/2;
				times++;
			}
			else{
				callatz=(3*callatz)+1;
				times++;
			}
		}
		printf("%d ", times);
		
	}
    printf ("\n\n");


        printf ("Happy Numbers:\n");
        numPrint = 0;
        int currentNum=1;

        while(numPrint<COUNT){

            if(isHappyNumber(currentNum)){
                printf("%d ", currentNum);
                numPrint = numPrint+1;
            }
        currentNum=currentNum+1;


        }
        printf ("\n");




    return 0;
}