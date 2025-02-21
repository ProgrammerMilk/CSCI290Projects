/**
 * File: RaggedArrayList.java
 * ****************************************************************************
 *                           Revision History
 * ****************************************************************************
 * 
 * 
 * 2/19/2025 - Jonathan Peil - Started working on the add method
 * 02/08/2025 - junting Zhang - updated findEnd, added stop condition in the 
 *                              nested while loop instead of using "break".
 * 02/07/2025 - Junting Zhang - Implement both findFront and findEnd using 
 *                              binarySearch (commented out)
 *  02/07/2025 - Dylan Sherwood - implement findEnd()
 *  02/06/2025 - Junting Zhang - fixed bugs in findFront()
 * 2/5/2025 - Dylan Sherwood - finished findFront()
 * 2/3/2025 - Jonathan Peil - Started creating findFront()
 * 8/2015 - Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 * ****************************************************************************
 */
package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * *
 * The RaggedArrayList is a 2 level data structure that is an array of arrays.
 *
 * It keeps the items in sorted order according to the comparator. Duplicates
 * are allowed. New items are added after any equivalent items.
 *
 * NOTE: normally fields, internal nested classes and non API methods should all
 * be private, however they have been made public so that the tester code can
 * set them
 *
 * @author Bob Booth
 * @param <E> A generic data type so that this structure can be built with any
 * data type (object)
 */
public class RaggedArrayList<E> implements Iterable<E> {

    // must be even so when split get two equal pieces
    private static final int MINIMUM_SIZE = 4;
    /**
     * The total number of elements in the entire RaggedArrayList
     */
    public int size;
    /**
     * really is an array of L2Array, but compiler won't let me cast to that
     */
    public Object[] l1Array;
    /**
     * The number of elements in the l1Array that are used.
     */
    public int l1NumUsed;
    /**
     * a Comparator object so we can use compare for Song
     */
    private Comparator<E> comp;

    /**
     * create an empty list always have at least 1 second level array even if
     * empty, makes code easier (DONE - do not change)
     *
     * @param c a comparator object
     */
    public RaggedArrayList(Comparator<E> c) {
        size = 0;
        // you can't create an array of a generic type
        l1Array = new Object[MINIMUM_SIZE];
        // first 2nd level array
        l1Array[0] = new L2Array(MINIMUM_SIZE);
        l1NumUsed = 1;
        comp = c;
    }

    /**
     * ***********************************************************
     * nested class for 2nd level arrays read and understand it. (DONE - do not
     * change)
     */
    public class L2Array {

        /**
         * the array of items
         */
        public E[] items;
        /**
         * number of items in this L2Array with values
         */
        public int numUsed;

        /**
         * Constructor for the L2Array
         *
         * @param capacity the initial length of the array
         */
        public L2Array(int capacity) {
            // you can't create an array of a generic type
            items = (E[]) new Object[capacity];
            numUsed = 0;
        }
    }// end of nested class L2Array

    // ***********************************************************
    /**
     * total size (number of entries) in the entire data structure (DONE - do
     * not change)
     *
     * @return total size of the data structure
     */
    public int size() {
        return size;
    }

    /**
     * null out all references so garbage collector can grab them but keep
     * otherwise empty l1Array and 1st L2Array (DONE - Do not change)
     */
    public void clear() {
        size = 0;
        // clear all but first l2 array
        Arrays.fill(l1Array, 1, l1Array.length, null);
        l1NumUsed = 1;
        L2Array l2Array = (L2Array) l1Array[0];
        // clear out l2array
        Arrays.fill(l2Array.items, 0, l2Array.numUsed, null);
        l2Array.numUsed = 0;
    }

    /**
     * *********************************************************
     * nested class for a list position used only internally 2 parts: level 1
     * index and level 2 index
     */
    public class ListLoc {

        /**
         * Level 1 index
         */
        public int level1Index;

        /**
         * Level 2 index
         */
        public int level2Index;

        /**
         * Parameterized constructor DONE (Do Not Change)
         *
         * @param level1Index input value for property
         * @param level2Index input value for property
         */
        public ListLoc(int level1Index, int level2Index) {
            this.level1Index = level1Index;
            this.level2Index = level2Index;
        }

        /**
         * test if two ListLoc's are to the same location (done -- do not
         * change)
         *
         * @param otherObj the other listLoc
         * @return true if they are the same location and false otherwise
         */
        public boolean equals(Object otherObj) {
            // not really needed since it will be ListLoc
            if (getClass() != otherObj.getClass()) {
                return false;
            }
            ListLoc other = (ListLoc) otherObj;

            return level1Index == other.level1Index
                    && level2Index == other.level2Index;
        }

        /**
         * move ListLoc to next entry when it moves past the very last entry it
         * will be one index past the last value in the used level 2 array. Can
         * be used internally to scan through the array for sublist also can be
         * used to implement the iterator.
         */
        public void moveToNext() {
            // TO DO IN PART 5 and NOT BEFORE
        }
    }

    /**
     * find 1st matching entry
     *
     * @param item the thing we are searching for a place to put.
     * @return ListLoc of 1st matching item or of 1st item greater than the item
     * if no match this might be an unused slot at the end of a level 2 array
     * @Author Dylan,Johnathan,Junting
     */
    public ListLoc findFront(E item) {
        // @junting - initial l2Array
        L2Array l2Array = (L2Array) l1Array[0];
        // @junting - initial outer loop couner
        int i = 0;
        // Loops through the Level 1 array that contains all the level 2 arrays JP
        for (i = 0; i < l1NumUsed; i++) {
            //Grabs the level 2 array at index i JP
            l2Array = (L2Array) l1Array[i];
            // Search within the Level 2 array for the first occurrence of the item JP
            for (int j = 0; j < l2Array.numUsed; j++) {
                // If a match is found JP
                if (comp.compare(item, l2Array.items[j]) == 0) {
                    //return the items position JP
                    return new ListLoc(i, j);
                }
            }
            // if item is not found, check where it could be inserted DS
            for (int j = 0; j < l2Array.numUsed; j++) {
                if (comp.compare(item, l2Array.items[j]) < 0) {
                    return new ListLoc(i, j);
                }
            }
        }
        // @junting- if target item comparison is larger than rest of the data, 
        // set insertion point to the end of last sub array
        return new ListLoc(i - 1, l2Array.numUsed);
        
        
    }

    /**
     * 
     * find location after the last matching entry or if no match, it finds the
     * index of the next larger item this is the position to add a new entry
     * this might be an unused slot at the end of a level 2 array
     *
     * @param item the thing we are searching for a place to put.
     * @return the location where this item should go
     * @Author Dylan
     */
       public ListLoc findEnd(E item) {
    // call findFront to locate the first item or insertion point DS
    ListLoc loc = findFront(item);

    //Check for item if item is not there, then this is the insetion point DS
    if (loc.level1Index < 0 || loc.level1Index >= l1NumUsed) {
        return loc;
    }
    //find the level 2 array at the level 1 index DS
    L2Array currentArray = (L2Array) l1Array[loc.level1Index];
    //checks that level 2 index is in range and unused DS
    if (loc.level2Index >= currentArray.numUsed ||
    //check for a match DS 
        comp.compare(item, currentArray.items[loc.level2Index]) != 0) {
        return loc;
    }

    
    //create two ints to hold our current indexes DS
    int currentL1 = loc.level1Index;
    int currentL2 = loc.level2Index;

    //check thatthe next index is in bounds and that it is equal to the item DS
    //if both conditions are true increment our position DS 
    while (currentL2 + 1 < currentArray.numUsed &&
           comp.compare(item, currentArray.items[currentL2 + 1]) == 0) {
        currentL2++;
    }

    //check for other inner array duplicates DS
    boolean found = true;
    while (found && currentL1 + 1 < l1NumUsed) {
        L2Array nextArray = (L2Array) l1Array[currentL1 + 1];
    //if the first item is equal check for duplicates DS
        if (nextArray.numUsed > 0 && comp.compare(item, nextArray.items[0]) == 0) {
    //move to the next level-2 array DS
            currentL1++;  
    //reset the counter DS
            currentL2 = 0;  
    //check the next array DS
            while (currentL2 + 1 < nextArray.numUsed &&
                   comp.compare(item, nextArray.items[currentL2 + 1]) == 0) {
                currentL2++;
            }
    //set the next array DS            
            currentArray = nextArray;
        } else {
    //if the next inner array does not contrain the item stop looking DS        
            found = false;
        }
    }
    //after searching every array, the next position past the last search
    //will be the insertion point  DS   
    return new ListLoc(currentL1, currentL2 + 1);
}
    
    
//       /**
//     * Implementation using binary search
//     *  
//     * find 1st matching entry
//     * @param item the thing we are searching for a place to put.
//     * @return ListLoc of 1st matching item or of 1st item greater than 
//     * the item if no match this might be an unused slot at the end of a 
//     * level 2 array
//     * @Author Junting Zhang
//     */
//    public ListLoc findFront(E item) {
//        int i = 0; // outer loop couter
//        int index = -1;  // initial match index in L2 
//        ListLoc listLoc;  // declare listLoc reference
//        L2Array l2Array = (L2Array)l1Array[0];  // initial L2Array object
//        
//        // loop through each item in l1 array
//        for (i =0; i < l1NumUsed; i++){
//            l2Array = (L2Array)l1Array[i];  // get current l2 array object
//           // using try and catch to prevent program crash when null is checked.
//            try{
//                // get match index 
//                index = Arrays.binarySearch(l2Array.items, item, comp);
//                // if item not found (index < 0), set index to the insertion point 
//                if(index<0){
//                    index = -(index+1);
//                // if match found and more than one, return the earliest match in the sorted list
//                }else if(index > 0 ){
//                    int pre = index -1;
//                    while(index >= 0 && comp.compare(l2Array.items[pre], item) == 0){
//                        index = pre;
//                        pre--;
//                    }  
//                }
//                // else index  == 0, no left item can be checked, just return 0
//                // get loction of l1 and l2
//                listLoc = new ListLoc(i,index);
//                return listLoc;
//            }catch(Exception e){
//                System.out.println("Checking null!");
//            }         
//        }
//        // if item is larger than all the data , set insertion point to the end of the data set
//        index = ((L2Array)l2Array).numUsed;
//        listLoc = new ListLoc(i-1, index);
//        return listLoc; 
//    }
    
    
//    /**
//     * Implementation using binary search :
//     *  
//     * find location after the last matching entry or if no match, it finds
//     * the index of the next larger item this is the position to add a new 
//     * entry this might be an unused slot at the end of a level 2 array
//     *
//     * @param item the thing we are searching for a place to put.
//     * @return the location where this item should go
//     * @Author Junting Zhang
//     */
//    public ListLoc findEnd(E item) {
//        int i = 0; // outer loop couter
//        int index = -1;  // initial match index in L2 
//        ListLoc listLoc;  // declare listLoc reference
//        L2Array l2Array = (L2Array)l1Array[0];  // initial L2Array object
//        
//        // loop through each item in l1 array
//        for (i =0; i < l1NumUsed; i++){
//            l2Array = (L2Array)l1Array[i];  // get current l2 array object
//            
//            try{
//                // get match index or negative for item not found
//                index = Arrays.binarySearch(l2Array.items, item, comp);
//                // if item not found, get insertion point and return
//                if(index<0){
//                    index = -(index+1);
//                    listLoc = new ListLoc(i,index);
//                    return listLoc;  
//                // if match found and more than one, retrun last match in the sorted list
//                }else if(index > 0 ){
//                    int next = index +1;
//                    while(index >= 0 && comp.compare(l2Array.items[next], item) == 0){
//                        index = next;
//                        next++;
//                    } 
//                }
//                // get the next index if last match found in the sorted array and return
//                index = index+1;
//                listLoc = new ListLoc(i,index);
//                return listLoc;
//            }catch(Exception e){
//                System.out.println("Checking null!");
//            }         
//        }
//        // if item is larger than all the data , insert into end of the data set
//        index = ((L2Array)l2Array).numUsed;
//        listLoc = new ListLoc(i-1, index);
//        return listLoc; 
//    }

    
    /**
     * add object after any other matching values findEnd will give the
     * insertion position
     *
     * @param item the thing we are searching for a place to put.
     * @return
     */
    public boolean add(E item) {
            
            //Finds the correct insertion location JP
            ListLoc loc = findEnd(item); // Finds the last matching item or the correct position to insert JP
            L2Array targetArray = (L2Array) l1Array[loc.level1Index]; // Retrieves the Level 2 array at the determined L1 index JP

            //Checks to see if there is space in the Level 2 Array for shifting and inserting JP
            if (targetArray.numUsed < targetArray.items.length){
                // Shifts elements to the right to make space for the new item
                System.arraycopy(targetArray.items, loc.level2Index, targetArray.items, loc.level2Index + 1, targetArray.numUsed - loc.level2Index);
                
                // Insert the item at the correct position
                targetArray.items[loc.level2Index] = item;
                // Increase the number of used slots in this L2Array
                targetArray.numUsed++;
                // Increases the overall size of the data structure
                size++;
                //Returns true after the item has been successfully added
                return true;  
            }
            
            // If there is no space in the L2Array, the array is split into 2
            if (targetArray.items.length == targetArray.numUsed) {
                //Determines the midpoint to split at
                int mid = targetArray.numUsed / 2;
        
                //Creates a new L2Array with the same size
                L2Array newArray = new L2Array(targetArray.items.length);
                
                //Move the second half of elements into the new array
                System.arraycopy(targetArray.items, mid, newArray.items, 0, targetArray.numUsed - mid);
                
                //Set the new array's size
                newArray.numUsed = targetArray.numUsed - mid;
                
                //Adjusts the original array's size
                targetArray.numUsed = mid;


                // Shift the L1 Array to make space for the L2Array
                if (l1NumUsed + 1 >= l1Array.length){
                    //Double the size of the L1Array if necessary
                    l1Array = Arrays.copyOf(l1Array, l1Array.length * 2);
                }
                
                // Shift elements in L1Array to create space for the new L2Array
                System.arraycopy(l1Array, loc.level1Index + 1, l1Array, loc.level1Index + 2, l1NumUsed - loc.level1Index);
                
                //Inset the new L2Array at the correct position
                l1Array[loc.level1Index + 1] = newArray;
                //Increase the count of used L1 elements
                l1NumUsed++;
                
                return add(item);
            }
            
            return true;
               
    }
    

    /**
     * check if list contains a match
     *
     * @param item the thing we are looking for.
     * @return true if the item is already in the data structure
     */
    public boolean contains(E item) {
        // TO DO in part 5 and NOT BEFORE

        return false;
    }

    /**
     * copy the contents of the RaggedArrayList into the given array
     *
     * @param a - an array of the actual type and of the correct size
     * @return the filled in array
     */
    public E[] toArray(E[] a) {
        // TO DO in part 5 and NOT BEFORE

        return a;
    }

    /**
     * returns a new independent RaggedArrayList whose elements range from
     * fromElemnt, inclusive, to toElement, exclusive. The original list is
     * unaffected findStart and findEnd will be useful here
     *
     * @param fromElement the starting element
     * @param toElement the element after the last element we actually want
     * @return the sublist
     */
    public RaggedArrayList<E> subList(E fromElement, E toElement) {
        // TO DO in part 5 and NOT BEFORE

        RaggedArrayList<E> result = new RaggedArrayList<E>(comp);
        return result;
    }

    /**
     * returns an iterator for this list this method just creates an instance of
     * the inner Itr() class (DONE)
     *
     * @return an iterator
     */
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * Iterator is just a list loc. It starts at (0,0) and finishes with index2
     * 1 past the last item in the last block
     */
    private class Itr implements Iterator<E> {

        private ListLoc loc;

        /*
         * create iterator at start of list
         * (DONE)
         */
        Itr() {
            loc = new ListLoc(0, 0);
        }

        /**
         * check to see if there are more items
         */
        public boolean hasNext() {
            // TO DO in part 5 and NOT BEFORE

            return false;
        }

        /**
         * return item and move to next throws NoSuchElementException if off end
         * of list. An exception is thrown here because calling next() without
         * calling hasNext() shows a certain level or stupidity on the part of
         * the programmer, so it can blow up. They deserve it.
         */
        public E next() {
            // TO DO in part 5 and NOT BEFORE

            throw new IndexOutOfBoundsException();
        }

        /**
         * Remove is not implemented. Just use this code. (DONE)
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
