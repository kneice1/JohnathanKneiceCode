/*******************************
 *
 *  Project Name: Project 3: String 'Em Up
 *  Description: In this project we made standard string library in C. This includes strcpy,
 *  strncpy, strcmp, strncmp, strlen, strcat, strncat,strcmp and strncmp.
 *  Date: February 18, 2022 
 *  Authors: Ahmed Shabib and Johnathan Kneice
 *
 *******************************/

#ifndef NEWSTRING_H_
#define NEWSTRING_H_

//Copies the characters from the source into the destination.
char* new_strcpy(char* destination, const char* source);

//Copies the first n characters from source into destination. If the function hits a null 
//character in source before it has copied n characters, the remaining characters are filled 
//with null characters.
char* new_strncpy(char* destination, const char* source, size_t n);

//Compares two strings. The return value is positive if string1 comes after string2
// alphabetically,negative if string1 comes before string2 alphabetically, and 0 if the 
//two strings are equal.
int new_strcmp(const char* string1, const char* string2);

//Compares two strings exactly like new_strcmp(), except comparing at most the first n
//characters.
int new_strncmp(const char* string1, const char* string2, size_t n);

//Adds the string contained in source to the end of the string contained in destination.
// The values in destination are modified, but a pointer to destination is also returned.
char* new_strcat(char* destination, const char* source);

//Adds the string contained in source to the end of the string contained in destination, 
//but adding a maximum of n characters.
char* new_strncat(char* destination, const char* source, size_t n);

//Returns the number of characters in string before the null character.
size_t new_strlen(const char* string);

//Returns a pointer to the first occurrence of character in string or a NULL
//Pointer if character cannot be found.
char* new_strchr(const char* string, int character);

//Returns a pointer to the first occurrence of the string contained in needle in
//haystack or a NULL pointer if needle cannot be found.
char* new_strstr(const char* haystack, const char* needle);

#endif