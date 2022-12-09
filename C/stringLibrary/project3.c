/*******************************
 *
 *  Project Name: Project 3: String 'Em Up
 *  Description: In this project we made standard string library in C. This includes strcpy,
 *  strncpy, strcmp, strncmp, strlen, strcat, strncat,strcmp and strncmp.
 *  Date: February 18, 2022 
 *  Authors: Ahmed Shabib and Johnathan Kneice
 *
 *******************************/

#include <stddef.h>

//Returns the number of characters in string before the null character.
size_t new_strlen(const char* string){
    int index=0;
    //Adds one to index until string at index is a null value.
    while (string[index]!='\0'){
        index++;
    }
    return index;
}

//Copies the characters from the source into the destination.
char* new_strcpy(char* destination, const char* source){
    int index=0;
    //While source at index is not null, copy source over to the destination.
    while(source[index] != '\0'){
        destination[index] = source[index];
        index++;
    }
    //Add null to mark the end of the string.
    destination[index]='\0';
    return destination;
}

//Copies the first n characters from source into destination. If the function hits a null 
//character in source before it has copied n characters, the remaining characters are filled 
//with null characters.
char* new_strncpy(char* destination, const char* source, size_t n){
   int index=0;
   //While source at index is not null and index is less than n.
    while(index < n && source[index]!='\0'){
        //Copy source over to the destination.
        destination[index] = source[index];
        index++;
    }
    //Add null to mark end of string.
    destination[index]='\0';
    return destination;
}

//Compares two strings. The return value is positive if string1 comes after string2
// alphabetically,negative if string1 comes before string2 alphabetically, and 0 if the 
//two strings are equal.
int new_strcmp(const char* string1, const char* string2){
    int index=0;
    //while both strings are not null.
    while(string1[index] != '\0' && string2[index]!= '\0'){
        //If both strings are not equal.
        if (string1[index]!=string2[index])
        //Return the difference of the two stings at index.
          return string1[index]-string2[index];
        index++;    
    }
    //If string1's value not null than return the value of the char at index.
    if(string1[index]!= '\0')
        return string1[index];
    //If string2's value not null than return the value of the char at index as a negative.
    if(string2[index]!= '\0')
        return 0-string2[index];
    return 0;
}

//Compares two strings exactly like new_strcmp(), except comparing at most the first n
//characters.
int new_strncmp(const char* string1, const char* string2, size_t n){
    int index=0;
    //While both strings are not null and index is less than n.
    while(index<n && string1[index] != '\0' && string2[index]!= '\0'){
        //If strings are not equal.
        if (string1[index]!=string2[index])
        //Return the difference of the two strings at value index.
          return string1[index]-string2[index];
        index++;        
    }
    return 0;
}

//Adds the string contained in source to the end of the string contained in destination.
// The values in destination are modified, but a pointer to destination is also returned.
char* new_strcat(char* destination, const char* source){
    //Finds the length of the two stings using new_strlen.
    int dLength=new_strlen(destination);
    int sLength=new_strlen(source);
    int index=0;
    //Loop through source and add each char to the end of the destination array.
    for(int j=0;j<sLength;j++){
        destination[dLength+j]=source[j];
    }
    return destination;
}

//Adds the string contained in source to the end of the string contained in destination, 
//but adding a maximum of n characters.
char* new_strncat(char* destination, const char* source, size_t n){
    int dLength=new_strlen(destination);
    int index=0;
    //While the index is less than n and the source at index is not null.
    while(index<n&&source[index]!='\0'){
        //Add each char from source into the distination array.
        destination[dLength+index]=source[index];
        index++;
    }
    return destination;
}

//Returns a pointer to the first occurrence of character in string or a NULL
//Pointer if character cannot be found.
char* new_strchr(const char* string, int character){
    int index=0;
    //While string at index is not null.
    while(string[index]!='\0'){
        // If sting at index matches character return a pointer of string at index.
        if(string[index]==character){
           return (char*)&string[index];
        }
        index++;
    }
    //If not found return null
    return NULL;
}

//Returns a pointer to the first occurrence of the string contained in needle in
//haystack or a NULL pointer if needle cannot be found.
char* new_strstr(const char* haystack, const char* needle){
  int index=0;
  //If needle's length is 0 return a pointer of haystack at index.
  if (new_strlen(needle)==0)
  {
      return (char*)&haystack[index];
  }
  //While haystack at index is not null.
    while(haystack[index]!='\0'){
        int compare=0;
        //If haystack at index matches nedle at 0.
         if(haystack[index] == needle[0]){
             compare = 1; 
             //Check if haystack at index maches needle at location i.
             for(int i = 0;i<new_strlen(needle); i++){
                //If they do not match compare is set to 0.
                 if(needle[i]!= haystack[index+i]){
                     compare=0;
                 }
             }
         }
         //If they do match return a pointer at haystack at index.
         if(compare ==1){
            return (char*)&haystack[index];

         }

        index++;
    }
    //If no match found return null.
    return NULL;
}
