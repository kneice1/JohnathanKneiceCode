/* This class sorts 3 arrays of length 10000, 100000, and 1000000 in 4 different sorting methods.
Each is filled with random values. This class also records how long each sorting method takes in each array.
 *
 * Author:	Johnathan Kneice
 * Course:	COMP 2100
 * Assignment:	Assignment 7
 * Date:	11/23/2021
 */

import java.util.Random;

public class Sorting {
    public static void main(String[] args) {
        int arrayLength = 10000;
        while (arrayLength<=1000000) {
            int[] array1 = new int[arrayLength];
            Random random = new Random();
            for (int i = 0; i < arrayLength; i++) {
                array1[i] = random.nextInt(arrayLength);
            }

            int[] array2=array1.clone();
            int[] array3=array1.clone();
            int[] array4=array1.clone();

            //quickSort
            System.out.println("Array length:       "+arrayLength);
            long start = System.nanoTime();
            quickSort(array1,0,array1.length);
            long end = System.nanoTime();
            double time = (end - start)/1000000000.0;
            System.out.format("Quicksort:          %.4f seconds%n",time);
            //System.out.println(isSorted(array1));

            //Merge Sort
            int[] scratch= new int[array1.length];
            start = System.nanoTime();
            mergeSort(array2,scratch,0,arrayLength);
            end = System.nanoTime();
            time = (end - start)/1000000000.0;
            System.out.format("Merge Sort:         %.4f seconds%n", time);
            //System.out.println(isSorted(array2));

            //Radix Sort
            start = System.nanoTime();
            radixSort(array3,6);
            end = System.nanoTime();
            time = (end - start)/1000000000.0;
            System.out.format("Radix Sort:         %.4f seconds%n", time);
            //System.out.println(isSorted(array3));

            //Heap Sort
            start = System.nanoTime();
            heapSort(array4);
            end = System.nanoTime();
            time = (end - start)/1000000000.0;
            System.out.format("Heap Sort:          %.4f seconds%n", time);
            //System.out.println(isSorted(array4));

            System.out.println("");
            arrayLength*=10;
        }




   }
    public static void quickSort(int[] array, int start, int end){
        if (start<end-1) {
            int partition=partition(array, start, start, end-1);
            quickSort(array, start, partition);
            quickSort(array, partition+1, end);
        }

    }

    public static int partition(int[]array, int index, int left, int right){
        int pivot=array[index];
        int temp=array[index];
        array[index]=array[right];
        array[right]=temp;
        index=left;
        for (int i=left;i<right;i++){
            if(array[i] <= pivot){
                temp=array[i];
                array[i]=array[index];
                array[index]=temp;
                index++;
            }
        }
        temp=array[index];
        array[index]=array[right];
        array[right]=temp;
        return index;
    }

    public static void mergeSort(int[] array, int[] scratch, int start, int end){
        if(end-start>1){
            int mid=(start+end)/2;
            mergeSort(array,scratch,start,mid);
            mergeSort(array,scratch,mid,end);
            merge(array,scratch,start,mid,end);
        }
    }

    private static void merge(int[] array, int[] scratch, int start, int mid, int end) {
        for (int i=start;i< end;i++){
            scratch[i]=array[i];
        }
        int a=start;
        int b=mid;
        int index=start;
        while (a<mid&& b< end){
            if (scratch[a]<=scratch[b]){
                array[index]=scratch[a];
                a++;
            }
            else{
                array[index]=scratch[b];
                b++;
            }
            index++;

        }
        while(a<mid){
            array[index]=scratch[a];
            a++;
            index++;
        }
        while (b<end){
            array[index]=scratch[b];
            ++b;
            ++index;
        }
    }

    public static void heapSort(int[] array) {
        for (int i= (array.length-2)/2;i>=0;i--) {
            bubbleDown(array.length, array, i);
        }
        for (int pos= array.length-1;pos>0;pos--) {
            int temp=array[0];
            array[0]=array[pos];
            array[pos]=temp;
            bubbleDown(pos, array, 0);
        }
    }

    private static void bubbleDown(int size,int[] array,int index) {
        while (2*index+1<size){
            int tempSwap=index;
            if (array[index]<array[2*index+1]){
                tempSwap=2*index+1;
            }
            if (2*index+2<size&&array[tempSwap]<array[2*index+2]){
                tempSwap=2*index+2;
            }
            if (tempSwap==index){
                return;
            }
            int temp=array[tempSwap];
            array[tempSwap]=array[index];
            array[index]=temp;
            index=tempSwap;
        }

    }



    public static void radixSort(int[] array, int digits){
        int[] scratch= new int[array.length];
        int mod = 1;
        int position=0;
        for (int digit=0;digit< digits;++digit) {
            int[] bucket = new int[11];
            for (int i = 0; i < array.length; i++) {
                position= array[i] / mod % 10;
                bucket[position + 1] += 1;

            }
            for (int i = 1; i < bucket.length; i++) {
                bucket[i] = bucket[i] + bucket[i - 1];
            }
            for (int i = 0; i < array.length; i++) {
                position= array[i] / mod % 10;
                int location=bucket[position];
                scratch[location]=array[i];
                bucket[position]+=1;
            }
            for (int i=0;i<array.length;i++){
                array[i]=scratch[i];
            }
            mod*=10;
        }
    }


    public static boolean isSorted(int[] array){
        for (int i=0;i<array.length-1;i++){
            if (array[i]>array[i+1]){
                return false;
            }
        }
        return true;
    }
}


