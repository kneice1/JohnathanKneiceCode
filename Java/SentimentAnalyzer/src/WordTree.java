import java.io.PrintWriter;

/**
 * This class is for the word tree it holds the data for each element of the array
 */
public class WordTree {
    private static class Node {
        public String word;
        public int score;
        public int count;
        public boolean black;
        public Node left;
        public Node right;
        public boolean color;
    }

    private Node root = null;

    /*
     * Adds a String to a WordTree with a given score.
     * If word is already contained in the WordTree, this method should add
     * the supplied score to the score of the given Node and increment its count.
     * Otherwise, this method should insert a new Node at the appropriate place
     * in the WordTree, with the supplied score and a count of 1.
     * rebalance and recoloring the tree if necessary to maintain the red-black properties.
     *
     */

    private static final boolean BLACK = true;
    private static final boolean RED = false;

    private static boolean isRed(Node node) { // To test the color of a node's link from its parents.
        if(node == null) {
            return false;

        }
        return node.color == RED;
    }
    private static void flipColor(Node node) { // It flips the color to prevent a black node with two red nodes.
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    private static Node rotateLeft(Node node) { // It moves the red node from a right to left.
        Node temp=node.right;
        node.right=temp.left;
        temp.left=node;
        temp.color= node.color;
        node.color= RED;
        return temp;
    }

    private static Node rotateRight(Node node) { // It moves the red node from a left to right.
        Node temp=node.left;
        node.left=temp.right;
        temp.right=node;
        temp.color= node.color;
        node.color= RED;
        return temp;
    }

    public void add(String word, int score) { // Creates the root and sets it to be black.
        root = add(root, word, score);
        root.color= BLACK;
    }
    private static Node add(Node node, String word, int score) { // Creates the node and sets it in the place.
        if (node == null) {
            node = new Node();
            node.score = score;
            node.word = word;
            node.count = 1;
        } else if (word.compareTo(node.word) < 0) { // Moves the nodes left if word is less than 0.
            node.left = add(node.left, word, score);
        } else if (word.compareTo(node.word) > 0) { // Moves the nodes left if word is greater than 0.
            node.right = add(node.right, word, score);
        } else { // If the word is already in the tre it takes the score of word in the tree and word we are adding.
            node.score += score;
            node.count++;// Adds one to count.
        }
        if (isRed(node.right)&&!isRed(node.left)){ //check to see the right node is red and left node is not red.
           node=rotateLeft(node);
        }
        if(isRed(node.left)&& isRed(node.left.left)){ // Checks to see if the left node and the left left node is red.
            node=rotateRight(node);
        }
        if (isRed(node.left)&& isRed(node.right)){  //check to see the right node and left node is red.
            flipColor(node);
        }
        return node; //Returns node.
    }
    /*
     * Returns true if word has already been added to the WordTree and false otherwise.
     */
    private boolean contains(Node node, String word) {
        if (node == null) { // Checks if the word is not word tree and returns false.
            return false;
        } else if (word.compareTo(node.word) < 0) { // Checks to see if the value is less than 0.
            return contains(node.left, word);
        } else if (word.compareTo(node.word) > 0) {  // Checks to see if the value is greater than 0.
            return contains(node.right, word);
        } else {
            return true; // This shows if the word is in the word tree.
        }
    }

    public boolean contains(String word) {
        return contains(root, word);
    }

    /*
     * Searches for word in the tree. If found, it will return the floating-point quotient of the corresponding Node's.
     * Score and count. If the word is not found, it will return 2.0, a neutral score.
     *
     */
    private static double getScore(Node node, String word) { // Returns value of the score.
        if (node == null) { // If it is null it will return 2.0.
            return 2.0;
        } else if (word.compareTo(node.word) < 0) { // Checks if the value of the word less than 0.
            return getScore(node.left, word);
        } else if (word.compareTo(node.word) > 0) { // Checks if the value of the word less than 0.
            return getScore(node.right, word);
        } else{
            return (double) node.score / node.count; // Averages the score and returns it.
        }
    }

    public double getScore(String word) {
        return getScore(root, word);
    }

    /*
     * This method prints out each word in the WordTree in alphabetical order, followed by a tab, followed by its
     * total score, followed by a tab, followed by its count, followed by a newline.
     * It makes sense to adapt an inorder traversal to this task.
     */
    private static void print(Node node, PrintWriter out) { // Prints out the data of the tree.
        if (node != null) {
            print(node.left, out);
            out.println(node.word + "\t" + node.score + "\t" + node.count);
            print(node.right, out);
        }
    }
    public void print(PrintWriter out) {
        print(root, out);
    }

    /*
     * Testing the red black to make sure it works.
     * @return the height of the tree.
     */
    public int height () {
        return heightOfTree(root);

    }
    private static int heightOfTree( Node node){
        if (node==null){
            return 0;
        }
        else{
         int left = heightOfTree(node.left) ;
         int right = heightOfTree(node.right);
         return 1+Math.max(left,right);
        }
    }
}