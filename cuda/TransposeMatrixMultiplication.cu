
%%cu


#include "cuda_runtime.h"
#include "device_launch_parameters.h"
#include <stdio.h>
#include<math.h>
#include <stdlib.h>

void printMatrix(int * matrix,int n);

void diaganalize(int* matrix1,int* matrix2,int n);

void transposeMult(int* A,int* B, int* C,int n);

void diTransposeMult(int* PA,int* PB, int* PC,int n);

int check(int* matrix1,int* matrix2,int n);

__global__ void tranposemult(int *A_dev,int* B_dev,int* tranMult_dev, int n){
    int index = blockIdx.x * blockDim.x + threadIdx.x;
    int i=index/n;
    int j= index%n;
    tranMult_dev[index]=0;
    if(index<n*n){
      tranMult_dev[i*n+j]=0;
      for(int k=0;k<n;k++)
      {
        tranMult_dev[i*n+j]+=A_dev[i*n+k]*B_dev[j*n+k];
     }
  }
}
__global__ void diTranposeMult(int *PA_dev,int* PB_dev,int* diTranMult_dev, int n){
    int t = blockIdx.x * blockDim.x + threadIdx.x;
    if (t < n*n){
      int k = t/n;
      int x = t %n;
      for(int i = 0; i < n; ++i){
        int a = (n-k+i)%n;
        if( a < 0)
          a = n -a;
        a = a*n+(x+k) %n;
        diTranMult_dev[t] += PA_dev[i*n+x] *PB_dev[a];
    }
  }
}
int main(){
    int n=16;
        int blockNumber = ceil((double)n * n/ 32);
        int* A_dev;
        int* B_dev;
        int* PA_dev;
        int* PB_dev;
        int* tranMult_dev;
        int* diTranMult_dev;

        srand(time(NULL));
        int random;

        //create matrixes
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
        
        int *P_A = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            P_A[i]=0;
        }
        cudaMalloc((void**) &PA_dev, n*n*sizeof(int));
        cudaMemcpy(PA_dev, P_A, n * n * sizeof(int), cudaMemcpyHostToDevice);

        int *P_B = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            P_B[i]=0;
        }
        cudaMalloc((void**) &PB_dev, n*n*sizeof(int));
        cudaMemcpy(PB_dev, P_B, n * n * sizeof(int), cudaMemcpyHostToDevice);

        int *tranMult = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            tranMult[i]=0;
        }
        cudaMalloc((void**) &tranMult_dev, n*n*sizeof(int));
        cudaMemcpy(tranMult_dev, tranMult, n * n * sizeof(int), cudaMemcpyHostToDevice);

        int *diTranMult = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            diTranMult[i]=0;
        }
        cudaMalloc((void**) &diTranMult_dev, n*n*sizeof(int));
        cudaMemcpy(diTranMult_dev, diTranMult, n * n * sizeof(int), cudaMemcpyHostToDevice);

        //diaganalize
        diaganalize(B,P_B,n);
        diaganalize(A,P_A,n);
        cudaMemcpy(PB_dev,P_B, n * n * sizeof(int), cudaMemcpyHostToDevice);
        cudaMemcpy(PA_dev, P_A, n * n * sizeof(int), cudaMemcpyHostToDevice);

        tranposemult<<<blockNumber,32>>>(A_dev,B_dev,tranMult_dev,n);
        cudaMemcpy(tranMult,tranMult_dev, n * n * sizeof(int), cudaMemcpyDeviceToHost);

        diTranposeMult<<<blockNumber,32>>>(PA_dev,PB_dev,diTranMult_dev,n);
        cudaMemcpy(diTranMult,diTranMult_dev, n * n * sizeof(int), cudaMemcpyDeviceToHost);

          //checking
        int *checkTran = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            checkTran[i]=0;
        }

        transposeMult(A,B,checkTran,n);
        
        int *diCheckTran = (int*)malloc(n *n* sizeof(int*));
        for(int i=0;i<n*n;i++){
            diCheckTran[i]=0;
        }

        diTransposeMult(P_A,P_B,diCheckTran,n);

        if((check(tranMult,checkTran,n)==1)&&(check(diTranMult,diCheckTran,n)==1)){
          printf("Correct\n");
      //print matrixes
          //printf("matrix A\n");
          //printMatrix(A,n);
          printf("matrix A diaganalized\n");
          printMatrix(P_A,n);
          //printf("matrix B\n");
          //printMatrix(B,n);
          printf("matrix B diaganalized\n");
          printMatrix(P_B,n);
          printf("A*B^T Matrix\n");
          printMatrix(tranMult,n);
          printf("P_A*P_B^T Matrix\n");
          printMatrix(diTranMult,n);
        }
        else{
            printf("error");
        }

      //free matrixes
        cudaFree(A_dev);
        cudaFree(B_dev);
        cudaFree(PA_dev);
        cudaFree(PB_dev);
        cudaFree(tranMult_dev);
        cudaFree(diTranMult_dev);
        free(A);
        free(B);
        free(P_A);
        free(P_B);
        free(tranMult);
        free(diTranMult);
        free(checkTran);
        free(diCheckTran);

    return 0;
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

void diaganalize(int* matrix1,int* matrix2,int n){
    for (int i = 0; i < n; ++i)
    {
        for (int j = 0; j < n; ++j)
        {
            matrix2[i*n+j]=matrix1[j*n+(i+j)%n];
        }
    }
}

int check(int* matrix1,int* matrix2,int n){
    for (int i = 0; i < n*n; ++i)
    {
        if(matrix2[i]!=matrix1[i]){
            return -1;
        }
     
    }
    return 1;
}
void transposeMult(int* A,int* B, int* C,int n){
    int *temp = (int*)malloc(n *n* sizeof(int*));
    for(int i=0;i<n*n;i++){
        temp[i]=0;
      }
    for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
            temp[i*n+j]=B[j*n+i];
        }
    }
    for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
            for(int k=0;k<n;k++){
                C[i*n+j]+=A[i*n+k]*temp[k*n+j];
            }
        }
    }
    free(temp);
}


void diTransposeMult(int* PA,int* PB, int* PC,int n){
    int *temp = (int*)malloc(n *n* sizeof(int*));
    for(int i=0;i<n*n;i++){
        temp[i]=0;
      }

     for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
            temp[i*n+j]=PB[j*n+i];
        }
    } 
    for(int k=0;k<n;k++){
        for(int x=0;x<n;x++){
            for(int i=0;i<n;i++){
                int a = (n-k+i)%n;
                if( a < 0)
                  a = n -a;
                a = ((x+k)%n)*n+a;
                PC[k*n+x] += PA[i*n+x] *temp[a];
            }
        }
    }
    free(temp);
}
