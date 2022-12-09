public class HashTable {
    private static class Node {
        public String key;
        public String value;
    }

    private Node[] table = new Node[101];
    private int size = 0;

    /*
    Private helper method that that finds the hash value of a particular String for the current table.
    First use the hashCode() method on the String to find its hash value.
    Then, use the trick described in the book to make sure that it's non-negative.
    Finally, take this value modulus the length of the table and return the result.
    Note that this approach differs from the mid-square approach used in class.
     */
    private int hash(String key) {

        int hash=(key.hashCode() & 0x7fffffff)%table.length;
        return hash;
    }
    /*
    Private helper method that finds the step size used when searching for an unused bucket with the double-hashing
    approach. Like the previous method, you first call hashCode() on the String and then make the value non-negative.
    Next take this value modulus 97 and then add 1 to the result and return it.
     */
    private int step(String key) {
        int hash=((key.hashCode() & 0x7fffffff)%97)+1;
        return hash;
    }
    /*
    Private helper method that takes the current length of the table, multiplies it by 2,
    and then finds the next prime number larger than that value. This method aids the resizing process for the table.
    The double-hashing scheme we are using depends on having a table whose length is prime.
     */
    private int nextPrime(int length) {
        int prime=length*2+1;
        while (!isPrime(prime)){
            prime+=2;
        }
        return prime;

    }
    //checks if number is prime
    private boolean isPrime(int number) {
        if (number==1){
            return false;
        }
        for (int i=2; i*i<=number;i++){
            if (number%i==0){
                return false;
            }
        }
        return true;
    }
    //Public method that returns true if the hash table contains the given key and false otherwise.
    public boolean containsKey(String key) {
        int hash=hash(key);
        int step= step(key);
        while ( table[hash]!=null){
            if ( table[hash].key.equals(key)){
                return true;
            }
            hash=(hash+step)% table.length;
        }
        return false;
    }
    /*
    Public method that adds the given key-value pair to the hash table, using a double-hashing approach to
     handle collisions. This method returns true if the key was not present in the hash table beforehand and false if
     an update is being done to an existing key. When adding a key, resize the table if the load factor is 0.75 or
     higher. Use the helper methods above to find the next prime number larger than twice the current table size, and
     resize the table to that prime number.
     */
    public boolean put( String key, String value ) {
        //checking if load factor is greter than 0.75.
        if ((size/ (double)table.length)>=.75){
            int tempSize=nextPrime(table.length);
            Node[] temp=table;
            table = new Node[tempSize];
            size=0;
            for (int i=0;i<temp.length;i++){
                if(temp[i]!=null){
                    put(temp[i].key, temp[i].value);
                }
            }
        }
        //puts node into table
        int hash = hash(key);
        int step = step(key);
        while (table[hash] != null) {
            if (table[hash].key.equals(key)) {
                table[hash].value=value;
                return false;
            }
            hash = (hash + step) % table.length;
        }
        Node node = new Node();
        node.value=value;
        node.key=key;
        table[hash]=node;
        size++;
        return true;
    }
    /*
    Public method that finds the value associated with the given key inside the hash table. The value is returned if
    the key is present in the hash table. Otherwise, this method returns null.
     */
    public String get( String key ) {
        int hash = hash(key);
        int step = step(key);
        while (table[hash] != null) {
            if (table[hash].key.equals(key)) {
                return table[hash].value;
            }
            hash = (hash + step) % table.length;
        }
        return null;
    }
}
