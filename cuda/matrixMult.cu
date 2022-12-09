
%%cu


#include "cuda_runtime.h"
#include "device_launch_parameters.h"

#include <stdio.h>
#include<math.h>
#include <stdlib.h>

void multiply(int* matrix1,int* matrix2, int* matrix3,int n);

void printMatrix(int * matrix,int n);

int check(int* matrix1,int* matrix2,int n);

__global__ void matrixMultiplication(int *A_dev,int* B_dev,int * C_dev,int m, int n)
{
  int i = (blockIdx.x * blockDim.x + threadIdx.x)*m;
  int startpoint=i;
  int endpoint = startpoint+m;
 
  int y=i/n;
  int x=i%n;
 //if m is greater than 1
  if(m>1){
     if(x<n){
      if(y<n){
        for(int j=startpoint;j<endpoint;j++){
          for(int k=0;k<n;k++){
            C_dev[(y*n+x)]+= A_dev[(y*n+k)]*B_dev[(k*n+x)];
          }
          i++;
          x=i%n;
          y=i/n;
        }
      }
    }
  }
 //if m is equal to 1
 else{
      for(int k=0;k<n;k++){
          C_dev[(y*n+x)]+= A_dev[(y*n+k)]*B_dev[(k*n+x)];
      }
    }
 }

int main(){
    int n=64;
    int m=8;
    if((n*n)>m){
        int blockNumber = ceil((double)n * n/ 32*m);
        int* A_dev;
        int* B_dev;
        int* C_dev;
        srand(time(NULL));
        int random;

        //create matrices

        int *A = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            random = rand() % 3;
            A[i]=random;
        }
        cudaMalloc((void**) &A_dev, n*n*sizeof(int));
        cudaMemcpy(A_dev, A, n * n * sizeof(int), cudaMemcpyHostToDevice);
        
        int *B = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            random = rand() % 3;
            B[i]=random;
        }
        cudaMalloc((void**) &B_dev, n*n*sizeof(int));
        cudaMemcpy(B_dev, B, n * n * sizeof(int), cudaMemcpyHostToDevice);
        
//make zero matrices

        int *C = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            C[i]=0;
        }
        cudaMalloc((void**) &C_dev, n*n*sizeof(int));
        cudaMemcpy(C_dev, C, n * n * sizeof(int), cudaMemcpyHostToDevice);

        int *D = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            D[i]=0;
        }

        //cuda matrix multiply
        //finds length of time to cuda multiply.
        clock_t start = clock();
        matrixMultiplication<<<blockNumber,32>> >(A_dev, B_dev,C_dev,m,n);
        clock_t end = clock();
        
        double cudaTime=(double)(end - start) / CLOCKS_PER_SEC;
        
        cudaMemcpy(C, C_dev, n * n * sizeof(int), cudaMemcpyDeviceToHost);

        //normal matrix multiply
        start = clock();
        multiply(A,B,D,n);
        end = clock();
        double cTime= (double)(end - start) / CLOCKS_PER_SEC;
        

        int boolean=check(C,D,n);
        if(boolean==-1){
            printf("Error");
        }
        else{
            printf("Correct\n");
            printf("C matrix multiplication time is: %f seconds\n", cTime);
            printf("Cuda matrix multiplication time is: %f seconds\n", cudaTime);
          //print matrixes
          //printMatrix(A,n);
          //printMatrix(B,n);
          //printMatrix(C,n);
          //printMatrix(D,n);
        }

        //free matrixes
        cudaFree(A_dev);
        cudaFree(B_dev);
        cudaFree(C_dev);
        free(A);
        free(B);
        free(C);
        free(D);

    return 0;
    }
}

void printMatrix(int* matrix,int n){
    for (int i = 0; i < n*n; i++){
        if(i%n==0){
          printf("\n");
           printf("%d ",matrix[i]);
        }
        else{
          printf("%d ",matrix[i]);
        }
    }
    printf("\n\n");
}

void multiply(int* matrix1,int* matrix2, int* matrix3,int n){
    for(int i=0;i<n;i++){
        for(int j = 0; j<n; j++)
          for(int k=0;k<n;k++){
            matrix3[i*n+j]+=matrix1[i*n+k]*matrix2[k*n+j];
        }
    }
}
int check(int* matrix1,int* matrix2,int n){
    for(int i=0;i<n*n;i++){
        if(matrix1[i]!=matrix2[i]){
            return -1;
        }
    }
    return 1;
}

