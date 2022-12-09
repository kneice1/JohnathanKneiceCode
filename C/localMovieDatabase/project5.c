/*******************************
 *
 *  Project Name: Project 5
 *  Description: This program will allow you to search for movies with a 
 *               particular name as well as display the highest earning movie in each 
 *               genre and give overall statistics for each genre. Of course, it will 
 *               also let you add and delete movies from the database.
 *  Date: April 1 2022
 *  Authors: Tony Nguyen Jonathan Kneice Caroline Heller
 *
 *******************************/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "movie.h"
#define LENGTH 100
extern const char *GENRE_NAMES[GENRES];
/*
   Description:	The main function in which loads up the choices when reading in
   the movie.

*/

int main() {
  Movie *movies[GENRES];
  for (int i = 0; i < GENRES; ++i) {
    movies[i] = NULL;
  }
  char choice[LENGTH];
  char name[1024];
  while (strcmp(choice, "quit")) {
    printf("Enter command: ");
    scanf("%s", choice);
    getchar();
    /*
    Adds a movie to the current list. This command will read in the name, year,
    length, genre, and revenue (if any) on separate lines. Follow the formatting
    in the sample executable. Use the genre information to decide which of the
    25 binary search trees to add the movie to. The movie name will be no longer
    than 1024 charaters, including a null character. The genre name will be no
    longer than 100 characters, including a null character.
    */
    //If the user enters in the add command the prompts will come up to have them enter
    //a movie and the data about it.
    if (strcmp(choice, "add") == 0) {
      char name[1024];
      int year;
      int minutes;
      char genre[LENGTH];
      long long revenue;
      printf("\nEnter name: ");
      readLine(name, stdin);
      printf("Enter year: ");
      scanf("%d", &year);
      printf("Enter length in minutes: ");
      scanf("%d", &minutes);
      printf("Enter genre: ");
      scanf("%s", genre);
      printf("Enter revenue: ");
      scanf("%lld", &revenue);
      int number = genreNum(genre);
      if (year < 1900) {
        // Printed if year is invalid for a movie.
        printf("Add failed: Invalid year %d\n", year);
      } else if (minutes <= 0) {
        // Printed if length is negative.
        printf("Add failed: Invalid length of %d minutes\n", minutes);
      } else if (number == -1) {
        // Printed if genre is not one of the 25 supported genres.
        printf("Add failed: Invalid genre %s\n", genre);
      } else if (revenue < 0) {
        // Printed if revenue is negative.
        printf("Add failed: Invalid revenue %lld\n", revenue);
      } else {
        movies[number] =
            insert(movies[number], name, year, minutes, number, revenue);
        printf("Add succeeded\n\n");
      }
      /*
      Clears the current list of movies. This command will free all the memory
      allocated in all 25 binary search trees and set each of those pointers to
      NULL.
      */
    } else if (strcmp(choice, "clear") == 0) {
      for (int i = 0; i < GENRES; ++i) {
        clear(movies[i]);
      }
      printf("\nAll data cleared.\n\n");
      /*
      Deletes a movie from the current list. This command will read in the name,
      year, and genre of a movie and attempt to remove it from the binary search
      corresponding to its genre. It should also free the memory associated with
      the movie.
      */
    } else if (strcmp(choice, "delete") == 0) {
      char name[1024];
      int year;
      char genre[LENGTH];
      printf("\nEnter name: ");
      readLine(name, stdin);
      printf("Enter year: ");
      scanf("%d", &year);
      printf("Enter genre: ");
      scanf("%s", genre);
      int number = genreNum(genre);
      Movie *temp = search(movies[number], name, year);
      if (year < 1900) {
        // Printed if year is invalid for a movie.
        printf("Delete failed: Invalid year %d\n", year);
      } else if (genreNum(genre) == -1) {
        // Printed if genre is not one of the 25 supported genres.
        printf("Delete failed: Invalid genre %s\n", genre);
      } else if (temp == NULL) {
        // Printed if movie cannot be found.
        printf("Delete failed: Movie %s (%d) not found in genre %s\n", name,
               year, genre);
      } else {
        movies[number] = delete (movies[number], name, year);
        printf("Delete succeeded\n\n");
      }
      /*
      Searches for a movie. This command will read in a name and print all the
      movies that match, starting with the first genre and moving to the last.
      In each binary search tree, use an inorder traversal so that matching
      movies are printed in sorted order by year. It should also print the total
      number of movies whose names match.
      */
    } else if (strcmp(choice, "find") == 0) {
      printf("\nEnter name: ");
      readLine(name, stdin);
      int total = 0;
      for (int i = 0; i < GENRES; ++i) {
        total += printMatches(movies[i], name);
      }
      printf("%d matches found.\n\n", total);
      /*
      Prints the highest revenue movie for each genre. The movie name (if known)
      will take up 20 spaces, followed by a space. For more details, follow the
      formatting in the sample executable.
      */
    } else if (strcmp(choice, "highest") == 0) {
      printf("\n");
      for (int i = 0; i < GENRES; i++) {
        Movie *movie = highestGrossing(movies[i]);
        if (movie != NULL) {
          printf("%-12s\t%-20s %d ($%lld)\n", GENRE_NAMES[i], movie->name,
                 movie->year, movie->revenue);
        } else {
          printf("%-12s\tUnknown\n", GENRE_NAMES[i]);
        }
      }
      printf("\n");
      /*
      Adds the contents of a file to the list of movies. This command will
      prompt for the name of a file. The fields of each movie will be found in
      the file in this order: name, year, length, genre, and revenue. These
      fields will be separated by tabs, followed by a newline after all the data
      in each movie. The data is guaranteed to be correctly formatted, but some
      of the values might not be legal. The file name will be no longer than 100
      characters, including a null character. The movie name will be no longer
      than 1024 charaters, including a null character. The genre name will be no
      longer than 100 characters, including a null character.
      */
    } else if (strcmp(choice, "load") == 0) {
      char filename[100];
      printf("\nName file: ");
      scanf("%s", filename);
      FILE *file = fopen(filename, "r");
      int count = 0;
      int errorCount = 0;
      if (file) {
        char name[1024];
        while (readLine(name, file) > 0) {
          int year;
          int minutes;
          char genre[LENGTH];
          long long revenue;
          fscanf(file, "%d", &year);
          fscanf(file, "%d", &minutes);
          fscanf(file, "%s", genre);
          fscanf(file, "%lld", &revenue);
          fgetc(file);
          int number = genreNum(genre);
          if (year < 1900) {
            ++errorCount;
          } else if (minutes <= -1) {
            ++errorCount;
          } else if (number == -1) {
            ++errorCount;
          } else if (revenue < 0) {
            ++errorCount;
          } else {
            ++count;
            movies[number] =
                insert(movies[number], name, year, minutes, number, revenue);
          }
        }
        fclose(file);
        printf("Loaded %d movies with %d errors\n\n", count, errorCount);
      } else {
        // Printed if file to load cannot be opened for reading.
        printf("Load failed: File %s not found\n\n", filename);
      }
    }
    /*
    Changes the revenue of a movie to the specified amount. This command will
    read in the name, year, genre, and new revenue value of a movie. Follow the
    formatting in the sample executable.
    */
    else if (strcmp(choice, "revenue") == 0) {
      char name[1024];
      char genre[LENGTH];
      int year = 0;
      long long newRevenue = 0;
      printf("\nEnter name: ");
      readLine(name, stdin);
      printf("Enter year: ");
      scanf("%d", &year);
      printf("Enter genre: ");
      scanf("%s", genre);
      printf("Enter revenue: ");
      scanf("%lld", &newRevenue);
      int number = genreNum(genre);
      Movie *temp = search(movies[number], name, year);
      if (year < 1900) {
        // Printed if year is invalid for a movie.
        printf("Revenue change failed: Invalid year %d", year);
      } else if (number == -1) {
        // Printed if genre is not one of the 25 supported genres.
        printf("Revenue change failed: Invalid genre %s", genre);
      } else if (newRevenue < 0) {
        // Printed if revenue is negative.
        printf("Revenue change failed: Invalid revenue %lld", newRevenue);
      } 
      else if (temp == NULL) {
        // Printed if movie cannot be found.
        printf(
            "Revenue change failed: Movie %s (%d) not found in "
            "genre %s\n\n",
            name, year, genre);
      } else {
        search(movies[genreNum(genre)], name, year)->revenue = newRevenue;
        printf("Revenue change successful\n\n");
      }
      /*
      Saves the current list of movies to a file. This command will prompt for
      the name of a file. The fields of each movie should be printed in the same
      format as they are read in the load command. Print the tree for each genre
      in order, and then use a preorder traversal within each tree.
      */
    } else if (strcmp(choice, "save") == 0) {
      char filename[100];
      printf("\nName file: ");
      scanf("%s", filename);
      FILE *file = fopen(filename, "w");
      if (file != NULL) {
        for (int i = 0; i < GENRES; ++i) {
          printAll(file, movies[i]);
        }
        fclose(file);
        printf("Save Succeeded\n\n");
      } else {
        printf("Save failed: File %s not writable\n\n", filename);
      }
      /*
      Prints out statistics for each genre. The genre name will take up 12
      spaces, followed by a tab. The count number will take up 10 spaces,
      followed by a tab. For more details, follow the formatting in the sample
      executable.
      */
    } else if (strcmp(choice, "statistics") == 0) {
      printf("\n");
      for (int i = 0; i < GENRES; ++i) {
        printf("%-12s\tCount:%11d\tTotal revenue: $%lld\n", GENRE_NAMES[i],
               count(movies[i]), totalRevenue(movies[i]));
      }
      printf("\n");
      // Quits the program.
    } else if (strcmp(choice, "quit") == 0) {
      for (int i = 0; i < GENRES; ++i) {
        clear(movies[i]);
      }
      printf("\n\nAll data cleared.\n");
      // The user manual for the general user who cannot go inside the code.
      // Print out all the commands and what each command does.
    } else if (strcmp(choice, "help") == 0) {
      printf("\nCommand\n");
      printf("\nadd\n");
      printf("\tAdds a movie to the current list\n");
      printf("\nclear\n");
      printf("\tClears the current list of movies\n");
      printf("\ndelete\n");
      printf("\tDeletes a movie frome the current list\n");
      printf("\nfind\n");
      printf("\tSearches for a movie\n");
      printf("\nhelp\n");
      printf("\tPrints the list of commands\n");
      printf("\nhighest\n");
      printf("\tPrints the highest revenue movie for eace genre\n");
      printf("\nload\n");
      printf("\tAdds the contents of a file to the list of movies\n");
      printf("\nquit\n");
      printf("\tQuits the program\n");
      printf("\nrevenue\n");
      printf("\tChanges the revenue of a movie to the specified amount\n");
      printf("\nsave\n");
      printf("\tSaves the current list of movies to a file\n");
      printf("\nstatistics\n");
      printf("\tPrints out statistics for each genre\n\n\n");
      // None of the commands above.
    } else {
      // If the command is not a known command, print out "Unknown command".
      printf("Unknown command: %s\n\n", choice);
    }
  }
  return 0;
}
/*
   Description:	//Reads in chars till it reaches a tab, EOF, or newline. Takes in a char array and a file.


   Parameters:	buffer[]     - Adjusts the size according to the variable input when called.
                file 		 - The file in which all/most of the movies exists.

   Return: Returns the number of chars it has read in.	
*/

int readLine(char buffer[], FILE *file) {
  int i = 0;
  int c;
  int count = 0;
  // Reads in till fgetc of a file is a tab, EOF, or newline. Adds one to count each time and adds
  //c to char array.
  while ((c = fgetc(file)) != '\n' && c != EOF && c != '\t') {
    buffer[i++] = c;
    count++;
  }
  buffer[i] = '\0';
  return count;
}
/*
   Description:	strcomp the names but if the names are the same returns the difference of the years.

   Parameters:	name1   - First movie name
                name2   - Second movie name
                year1   - First movie year
                year2   - Second movie year

   Returns a positive number if name1 comes before name2. Else, a negative number is returned.
*/

int compare(char *name1, char *name2, int year1, int year2) {
  int compare = strcmp(name1, name2);
  if (compare == 0) {
    return year1 - year2;
  }
  return compare;
}
/*
   Description:	Insert a movie into a tree, obeying ordering constraints.

   Parameters:	root 	    - The very first movie in the tree/root
                name 	    - Name of the movie
                year        - The year the movie was made
                minutes     - The length of the movie
                genre	    - The genre of the movie
                
             

   Returns:	A new movie that was added.
*/

Movie *insert(Movie *root, char *name, int year, int minutes, int genre,
              long long revenue) {
  // If the root is null, then malloc a new movie and insert the information given into the new movie. 
  if (root == NULL) {
    root = malloc(sizeof(Movie));
    root->name = strdup(name);
    root->year = year;
    root->minutes = minutes;
    root->genre = genre;
    root->revenue = revenue;
    // Make left and right movie null.
    root->left = NULL;
    root->right = NULL;
    // If the new movie comes before the current root alphabetically, then go left.
  } else if (compare(name, root->name, year, root->year) < 0) {
    root->left = insert(root->left, name, year, minutes, genre, revenue);
    // If the new movie comes after the current root alphabetically, then go right.
  } else if (compare(name, root->name, year, root->year) > 0) {
    root->right = insert(root->right, name, year, minutes, genre, revenue);
  }
  return root;
}
/*
   Description:	Search in a tree for a movie with a given name and year.
    
   
   Parameters:	root - The very first movie in the tree
   				name - Name of the movie
   				year - The year the was made movie

   Returns:	Return a pointer to the movie if found, NULL otherwise. Uses recurtion to move down the tree.
*/

Movie *search(Movie *root, char *name, int year) {
  // Returns the current root if compare returns 0 or root is null.
  if (root == NULL || compare(name, root->name, year, root->year) == 0) {
    return root;
  } else if (compare(name, root->name, year, root->year) < 0) {
    return search(root->left, name, year);
  } 
  else {
    return search(root->right, name, year);
  }
}
/*
   Description:	Print all movies (using the previous function) whose name
   matches the given name.

   Parameters:	root - The very first movie in the tree/root
                name - Name of the movie

   Returns:	Prints out the total amount of matches in which the movies have same names.
*/

int printMatches(Movie *root, char *name) {
  int total = 0;
  if (root == NULL) {
    return total;
  }
  //Recursively go left, adding to the total.
  total += printMatches(root->left, name);
  if (strcmp(root->name, name) == 0) {
    printMovie(root);
    total += printMatches(root->right, name) + 1;
    //Recursively go right, adding to the total.
  } else {
    total += printMatches(root->right, name);
  }
  return total;
}
/*
   Description:	Delete a movie in the tree with a given name and year, freeing
   the memory. If the tree doesn't contain a matching movie, this function has
   no effect.

   Parameters:	root    - The very first movie in the tree/root
                name    - Name of the movie
                year    - The year the movie was made
   Returns:	The movie tree with one of the movies deleted, else returns NULL if no movies exists.
*/

Movie *delete (Movie *root, char *name, int year) {
  if (root == NULL) {
    return root;
    // Go left if compare return is less than 0.
  } else if (compare(name, root->name, year, root->year) < 0) {
    root->left = delete (root->left, name, year);
    // Go right if compare return is greater than 0.
  } else if (compare(name, root->name, year, root->year) > 0) {
    root->right = delete (root->right, name, year);
  } else {
    Movie *temp;
    //If root left is null. Set temp to root right.
    if (root->left == NULL) {
      temp = root->right;
      //If root right is null. Set temp to root left.
    } else if (root->right == NULL) {
      temp = root->left;
    } 
    //If root right left is null. Set root right left equal to root left set temp to root right.
    else {
      if (root->right->left == NULL) {
        root->right->left = root->left;
        temp = root->right;
      } 
      else {
        Movie *parent = root->right;
        Movie *sub = parent->left;
        while (sub->left != NULL) {
          parent = sub;
          sub = parent->left;
        }
        parent->left = sub->right;
        sub->left = root->left;
        sub->right = root->right;
        temp = sub;
      }
    }
    free(root->name);
    free(root);
    root = temp;
  }
  return root;
}
/*
   Description:	Print all movies to the given open file. The fields of each
   movie should be printed in the order: name, year, length, genre, and revenue.

   Parameters:	file    - The file made in the save comand.
                root    - The very first movie in the tree/root

*/

void printAll(FILE *file, Movie *root) {
  // If the root is not null, add the name, year, minutes, genre, and revenue separated by tabs to the file.
  if (root != NULL) {
    fprintf(file, "%s\t", root->name);
    fprintf(file, "%d\t", root->year);
    fprintf(file, "%d\t", root->minutes);
    fprintf(file, "%s\t", GENRE_NAMES[root->genre]);
    fprintf(file, "%lld\n", root->revenue);
    // Get the empty char
    fgetc(file);
    // Recursively call printAll on the left and right of the root.
    printAll(file, root->left);
    printAll(file, root->right);
  }
}
/*
   Description:	Print information about the given movie to stdout. This
   information includes name, year, length, genre, and revenue.

   Parameters:	movie   - the movie tree
                
*/

void printMovie(Movie *movie) {
  // Print out all the information of a given movie.
  printf("Name:\t\t%s\n", movie->name);
  printf("Year:\t\t%d\n", movie->year);
  printf("Length:\t\t%d minutes\n", movie->minutes);
  printf("Genre:\t\t%s\n", GENRE_NAMES[movie->genre]);
  if (movie->revenue) {
    printf("Revenue:\t$%lld\n\n", movie->revenue);
  }
  else {
    printf("Revenue \tUnknown\n\n");
  }
}
/*
   Description:	Free all the nodes in a tree.

   Parameters:	root    - The very first movie in the tree/root

*/

void clear(Movie *root) {
  if (root != NULL) {
    clear(root->left);
    clear(root->right);
    free(root->name);
    free(root);
    root = NULL;
  }
}
/*
   Description:	Return the number of nodes in the tree.

   Parameters:	root   - The very first movie in the tree/root

   Returns:	how many nodes from the left and right.
*/

int count(Movie *root) {
  if (root == NULL) {
    return 0;
  } else {
    return 1 + count(root->left) + count(root->right);
  }
}
/*
   Description:	Return the total revenue in the tree.

   Parameters:	root    - The very first movie in the tree/root

   Returns:	The total revenue of all of the movies in each genres, else return NULL if movie doesn exist.
*/

long long totalRevenue(Movie *root) {
  if (root == NULL) {
    return 0;
  }
  // Recursively call totalRevenue on the left and right of the root. 
  else {
    // Add and return
    return root->revenue + totalRevenue(root->left) + totalRevenue(root->right);
  }
}
/*
   Description:	Find the movie with the highest revenue in the tree or NULL if
   no movie has a revenue greater than 0.

   Parameters:	root    - The very first movie in the tree/root
 
   Returns: the movie with the highest revenue, else Null if no movies exist in the tree.
*/

Movie *highestGrossing(Movie *root) {
  if (root == NULL) {
    return NULL;
  }
  // Recursively call highestGrossing on the left and the right. 
  else {
    Movie *highest = highestGrossing(root->left);
    Movie *highestRight = highestGrossing(root->right);
    // Highest is null or highest revenue is less than root revenue. Set root to be highest. 
    if (highest == NULL || root->revenue > highest->revenue) {
      highest = root;
    }
    // HighestRight is not null and highestRight revenue is greater than highest revenue. Set highestRight to be highest.
    if (highestRight != NULL && highestRight->revenue > highest->revenue) {
      highest = highestRight;
    }
    // If highest revenue is 0 return null else return highest.
    if (highest->revenue == 0) {
      return NULL;
    } else {
      return highest;
    }
  }
}
/*
   Description:	Finds the number location of a string in genre.

   Parameters:	genre   - The genre of the movie

   Returns: Returns a genre at the index it returns else -1 if it doesn't exist.
*/

int genreNum(char *genre) {
  // Loops throgh GENRES and returns the value of i if found.
  for (int i = 0; i < GENRES; ++i) {
    if (strcmp(genre, GENRE_NAMES[i]) == 0) {
      return i;
    }
  }
  return -1;
}
