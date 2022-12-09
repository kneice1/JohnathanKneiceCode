
import java.io.PrintWriter;

public class WordTable {

    private WordTree[] array = new WordTree[26];

    public void printHeights() {
        for (int i = 0; i < array.length; i++) {
            System.out.println((char) ('A' + i) + ": " + array[i].height());
        }
    }

    /*
     * /Creating one tree for each letter of the alphabet.
     */
    public WordTable() {
        for (int i = 0; i < array.length; i++) {
            array[i] = new WordTree();
        }
    }

    /*
     * Adds word to the appropriate WordTree, with the given score.
     */
    public void add(String word, int score) {
        array[word.charAt(0) - 'a'].add(word, score);
    }

    /*
     * Gets the average score of a word from the appropriate WordTree.
     */
    public double getScore(String word) {
        return array[word.charAt(0) - 'a'].getScore(word);
    }

    /*
     * Asks the appropriate WordTree if it contains word.
     */

    public boolean contains(String word) {
        return array[word.charAt(0) - 'a'].contains(word);
    }

    /*
     * Loops through all 26 WordTree objects and print out their data.
     */
    public void print(PrintWriter out) {
        for (int i = 0; i < array.length; i++) {
            array[i].print(out);
        }
    }
}