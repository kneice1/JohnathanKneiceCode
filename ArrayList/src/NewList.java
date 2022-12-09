public class NewList {
    private String[] array;
    private int size;


    public NewList(){
        size=0;
        array= new String[10];
    }

    /**
     * Add element to the last position in the list. This method may require your array to be resized.
     */
    void add(String element)
    {
        check();
        array[size]=element;
        size++;
    }

    /**
     *  Add element element to the list at position index, moving elements at index and
     *  higher to the following locations. This method may require your array to be resized.
     */

    void add(int index, String element) {
        if (index>size||index<0) {
            throw new IndexOutOfBoundsException();
        }
        check();
        if (array[index] != "") {
                String[] temp = array;
                array[index] = element;
                for (int i = index; i < array.length + 1; i++) {
                    check();
                    array[index + 1] = temp[index];
                }
            }
        else {
                array[index] = element;
            }

        }

    /**
     * Add all the elements from list to the end of your list.
     * This method may require your array to be resized.
     */

    void addAll(NewList list){
        check();
        for (int i=0;i<=list.size-1;i++){
            check();
            add(list.array[i]);
        }
    }

    /**
     * Remove all elements from your list.
     */

    void clear(){
        for (int i=0;i< size-1;i++){
            array[i]="";
        }
        size=0;
    }

    /**
     * Return true if your list contains element and false
     * otherwise. Remember to compare String values using the equals() method.
     * @param element
     * @return
     */


    boolean contains(String element){
        boolean isThere=false;

        for (int i=0;i< size;i++){
            if(array[i].equals(element)){
                isThere= true;
            }
        }
        return isThere;
    }

    /**
     * Return the element at position index.
     * @param index
     * @return
     */
    String get(int index){

        if (index>size||index<0) {
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    /**
     * Return the index of the first occurrence of element or -1 if your list does not contain it.
     * @param element
     * @return
     */
    int indexOf(String element){
        int position=-1;
        for (int i=0;i< size-1;i++){
            if (array[i].equals(element)){
                position=i;
            }
        }
        return position;
    }
    /**
     * Return true if your list contains no items and false otherwise.
     */

    boolean isEmpty() {
        boolean empty = true;
        for (int i = 0;size-1>i; i++) {
            if (!array[i].equals("")) {
                empty = false;
            }
        }
        return empty;
    }

    /**
     * Return the index of the last occurrence of element or -1 if your list does not contain it.
     */
    int lastIndexOf(String element){
        int lastIndex=-1;
        for (int i=size-1;i>=0;--i){
            if(array[i].equals(element)){
                lastIndex=i;
                i=-1;
            }
        }
        return lastIndex;
    }

    /**
     * Remove the element at position index from your list and return it.
     * All the elements after index will each move to the location before.
     * @param index
     * @return
     */

    String remove(int index){
        if (index>size||index<0) {
            throw new IndexOutOfBoundsException();
        }
        String[] temp=array;
        String removed=array[index];
        array[size]="";
        for (int i=index;i<size-1;i++){
            array[i]=temp[i+1];
        }
        size--;
        return removed;
    }

    /**
     * Change the element at position index to element.
     */
    void set(int index, String element){
        if (index>size||index<0) {
            throw new IndexOutOfBoundsException();
        }
        array[index]=element;
    }

    /**
     * Return the size of the list.
     */

    int size(){
        return size;
    }

    /**
     * checks to see if array needs to be resized.
     */

    private void check() {
        if (array.length==size) {
            int arraySize= array.length*2;
            String[] temp = array;
            array = new String[arraySize];
            for (int i = 0; i < size; i++) {
                array[i] = temp[i];

            }
        }
    }



}

