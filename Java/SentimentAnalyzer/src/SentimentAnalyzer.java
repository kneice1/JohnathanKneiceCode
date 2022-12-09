import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SentimentAnalyzer {
    public static void main(String[] args) throws FileNotFoundException {
        /*
         * Prompting the user for stop word and review file and printing out an appropriate error and redirecting user to enter another valid file.
         */
        boolean success = false;
        Scanner input = new Scanner(System.in);
        Scanner stopInput = null;
        while (!success) {
            System.out.print("Enter a stop word file: "); //Prompting user to enter a stop word file.
            try {
                String stopWord = input.nextLine();
                stopInput = new Scanner(new File(stopWord));
                success = true;

            } catch (FileNotFoundException e) {
                System.out.println("Stop file cannot be opened (perhaps it doesn't exist)."); // If file does not exist can enter a new file.

            }
        }
        String reviewFile = null;
        success = false;
        Scanner reviewInput = null;
        while (!success) {
            System.out.print("Enter a review file: "); //Prompting user to enter a review file.
            try {
                reviewFile = input.nextLine();
                reviewInput = new Scanner(new File(reviewFile));
                success = true;


            } catch (FileNotFoundException e) {
                System.out.println("Review file cannot be opened (perhaps it doesn't exist)."); // If file does not exist they can enter a new file.
            }
        }

        /*
         * adding in all the stop word to the word table.
         */
        WordTable stopWordTable = new WordTable();
        WordTable reviewWordTable = new WordTable();
        while (stopInput.hasNextLine()) {
            stopWordTable.add(stopInput.next(), 1); // adding in the stop words to the word table we build.
        }
        /*
         * Reading in the next line.
         * Takes the value of the word.
         * Then it goes character by character checking to see if it is (-) , ('\'') or a letter.
         * Then add the letter to the word.
         * Checks to see if it contains a word table or not.
         */
        while (reviewInput.hasNextLine()) {
            int wordValue = reviewInput.nextInt();
            String line = reviewInput.nextLine().toLowerCase();
            String word = "";
            for (int i = 0; i < line.length(); i++) {
                if (Character.isLetter(line.charAt(i)) || line.charAt(i) == ('\'') || line.charAt(i) == ('-')) {

                    word = word + line.charAt(i); // Adds the letter to the word.

                } else {
                    //Check if it contains a word or not
                    word = cleanWord(word);
                    if (word.length() > 0 && !stopWordTable.contains(word)) {
                        reviewWordTable.add(word, wordValue);
                    }
                    word = "";
                }
            }
            word = cleanWord(word); // Calls the method to clean the words.
            if (word.length() > 0 && !stopWordTable.contains(word)) {
                reviewWordTable.add(word, wordValue);
            }

        }
        outputStatistics(reviewFile, reviewWordTable);

        String analyzePhrase;
        boolean repeat = true;

        /*
         * Ask if the user would like to analyze a phrase with a yes or a no.
         * Reads the next line.
         * Evaluate the line and gives it a sentiment value.
         */
        while (repeat) {
            System.out.print("Would you like to analyze a phrase? (yes/no) ");
            analyzePhrase = input.nextLine();
            if (analyzePhrase.equals("yes")) {
                double score = 0.0;
                int words = 0;
                System.out.print("Enter phrase: ");
                String phrase = input.nextLine().toLowerCase(); // Reading the next line.
                String word = "";
                for (int i = 0; i < phrase.length(); i++) {
                    if (Character.isLetter(phrase.charAt(i)) || phrase.charAt(i) == ('\'') || phrase.charAt(i)
                            == ('-')) {

                        word = word + phrase.charAt(i);

                    } else {
                        //Check if it contains a word or not.
                        word = cleanWord(word);
                        if (word.length() > 0 && !stopWordTable.contains(word)) {
                            score += reviewWordTable.getScore(word);
                            words++;
                        }

                        word = "";
                    }

                }
                word = cleanWord(word);
                if (word.length() > 0 && !stopWordTable.contains(word)) { //Cleans the word and check to see if the word is in the stop table.
                    score += reviewWordTable.getScore(word);
                    words++;
                }

                /*
                 * Prints the sentiment value and gives it a rating
                 */
                if (words == 0) {
                    System.out.println("Your phrase contained no words that affect sentiment.");
                } else {
                    double averageScores = score / words;
                    System.out.format("Score: %.3f%n", averageScores); // formatting the score.
                    if (averageScores > 2.00) {
                        System.out.println("Your phrase was positive.");
                    } else if (averageScores < 2.00) {
                        System.out.println("Your phrase was negative.");
                    } else {
                        System.out.println("Your phrase was perfectly neutral.");
                    }
                }


            } else if (analyzePhrase.equals("no")) { // Exists the program.
                repeat = false;

            }
        }
    }

    /*
     * This clean method filters out the (' and -) in the beginning and the end.
     * It remains there if it is contained in the word itself.
     *
     */
    private static String cleanWord(String word) {
        while (word.length() > 0) {
            if (word.charAt(0) == '-' || word.charAt(0) == '\'')
                word = word.substring(1);
            else if (word.charAt(word.length() - 1) == '-' || word.charAt(word.length() - 1) == '\'') { // Takes character in between legal character.
                word = word.substring(0, word.length() - 1);
            } else {
                return word;
            }
        }
        return word;
    }

    private static void outputStatistics(String reviewFile, WordTable wordTable) {
        PrintWriter printWriter = null;

        try {
            File newFile = new File(reviewFile + ".words");
            printWriter = new PrintWriter(newFile); // creating the new file.
            wordTable.print(printWriter);
            System.out.println("Word statistics written to file " + reviewFile + ".words\n"); // The results.
        } catch (IOException e) {
            System.out.print("Unable to print out word statistics."); // If it is an error it will print this message.
        } finally {
            if (printWriter != null) {
                printWriter.close(); // Closes the printWriter.
            }
        }
        //wordTable.printHeights();
    }

}