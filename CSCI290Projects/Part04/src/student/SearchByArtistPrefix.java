/**
 * File: SearchByArtistPrefix.java 
 *****************************************************************************
 *                       Revision History
 *****************************************************************************
 * 02/02/2025 - Junting Zhang(Sarah) - Instrument Code , test GUI. 
 *                  count number of comparisons, print statistics 
 * 01/30/2025 Mustafa Qahtan - Added search detail to the terminal and fixed some bugs.
 * 8/2015 Anne Applin - Added formatting and JavaDoc
 * 2015 - Bob Boothe - starting code
 * 
 
 *****************************************************************************
 */

package student;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;
/**
 * Search by Artist Prefix searches the artists in the song database
 * for artists that begin with the input String
 * @author Bob Booth
 */

public class SearchByArtistPrefix {
    // keep a local direct reference to the song array
    private Song[] songs;

    /**
     * constructor initializes the property. [Done]
     * @param sc a SongCollection object
     */
    public SearchByArtistPrefix(SongCollection sc) {
        songs = sc.getAllSongs();
    }

    /**
     * find all songs matching artist prefix uses binary search should operate
     * in time log n + k (# matches)
     * converts artistPrefix to lowercase and creates a Song object with
     * artist prefix as the artist in order to have a Song to compare.
     * walks back to find the first "beginsWith" match, then walks forward
     * adding to the arrayList until it finds the last match.
     *
     * @param artistPrefix all or part of the artist's name, assumes no tailing space for input string
     * @return an array of songs by artists with sub-strings that match the prefix
     * @author Mustafa Qahtan, Junting Zhang
     */
    public Song[] search(String artistPrefix) {
        // create a new arrayList to store search result
        List<Song> songResult = new ArrayList<>();
        // @Junting - create a new CmpArtist object
        Song.CmpArtist cmp = new Song.CmpArtist();
        // @Junting- convert artistPrefix to lower case 
        String newArtistPrefix = artistPrefix.toLowerCase();
        // A temporary song obj with artistPrefix to use for binarySearch
        Song tempSong = new Song(newArtistPrefix, "", "");
        
        //@Junting - try catch block to catch exceptions that binarySearch() threws
        try{
            // get index of first match or (-insersion point)-1  if match not found
            int index = Arrays.binarySearch(songs, tempSong, cmp);
            System.out.println("Index from binary search is : " + index);

            // @junting - get comparison count in binary search 
            int binarySearchComparisons= ((CmpCnt)cmp).getCmpCnt();
            System.out.println("Binary search comparisons: " + binarySearchComparisons);

            // If no match is found, we return a negative number, so we adjust the index.
            if (index < 0) {
                // Adjust the index position to where the song should be inserted.
                index = -index - 1;
            }
            
            /* @Junting - additional comparison counter. 
            The first additional comparison starts from the first found index, 
            which has already been counted in the binarySearch comparison. Subtract one 
            to determine the actual number of additional comparisons needed.
            */
            int additonalComparison = -1; // @Junting - keep track of additional comparison in while loops
            int frontFoundIndex = index; // @Junting - initial first match index as insertion point if no match found.  
            int endFound = index; // @Junting - keep track of the last match in sorted songs 
            int count = 0; // @Junting - counter for number of match found
            // Looking for songs that match from the right side if we have a match.
            int rightIndex = index;
            // @junting - convert song in list to lowercase to compare with target string
            while (rightIndex < songs.length && songs[rightIndex].getArtist().toLowerCase().startsWith(newArtistPrefix)) {    
                songResult.add(songs[rightIndex]);
                endFound = rightIndex; 
                count++;
                additonalComparison++; 
                rightIndex++; 
            }
            
            //@Junting - if there is match found, calculate the first found match location
            if(count > 0){
                frontFoundIndex = endFound-count + 1; 
            }
            
            additonalComparison++;   // @Junting - extra comparison after right last match is found. 

            // Now we check on the left side as well.
            int leftIndex = index - 1; // reset the counter.
            // @junting - convert song in the allSongs to lowercase to compare with target string
            while (leftIndex >= 0 && songs[leftIndex].getArtist().toLowerCase().startsWith(newArtistPrefix)) {
                songResult.add(0, songs[leftIndex]);
                frontFoundIndex = leftIndex;  
                additonalComparison++;   
                leftIndex--;
            }
            additonalComparison++;   // @Junting - extra comparison after left last match is found. 
            
            //int frontFoundIndex = songs.indexOf(songResult[0]);
            // print statistics
            System.out.println("Front found at " + frontFoundIndex);
            System.out.println("Comparisons to build the list: " + additonalComparison);
            System.out.println("Actual complexity is: " + (binarySearchComparisons+ additonalComparison));
            System.out.println();
            int k = songResult.size();  
            int log = (int)(Math.log(songs.length) / Math.log(2));  
            System.out.println("k(total matches) is " + k);
            System.out.println("log_{2}(n) = " + log);
            System.out.println("Theoretical complexity k + log_{2}(n) is: " + (k+ log) + "\n");

        
        }catch(Exception e){
            System.err.println(e.getMessage());
            System.exit(0);
        }
        // Convert the ArrayList to an array and return it.
        return songResult.toArray(new Song[0]);
    }

    /**
     * testing method for this unit
     * @param args  command line arguments set in Project Properties -
     * the first argument is the data file name and the second is the partial
     * artist name, e.g. be which should return beatles, beach boys, bee gees,
     * etc.
     */

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog song file [search string]");
            return;
        }
        
        //crete a new SongCollection object
        SongCollection sc = new SongCollection(args[0]);
        // @Junting - Gets all the songs
        Song[] allSongs = sc.getAllSongs();
        // @Junting - Print length of the songs
        System.out.println("Total songs: " + allSongs.length);
        
        //  create a new SearchByArtistPrefix object
        SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);
       
        // @Junting - update sc to byArtistResult to print first 10 or less songs from search results
        if (args.length > 1) {
            System.out.println("searching for: '" + args[1] + "'");
            Song[] byArtistResult = sbap.search(args[1]);
            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
        }
    }
}