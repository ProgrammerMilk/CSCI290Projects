/**
 * File: SongCollection.java
 ************************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 * 2/3/2025 - Jonathan Peil - Removed the quotation marks around the outputted lyrics 
 * 02/02/2025 - Junting - GUI set up and test
 * 1/21/2025 - Jonathan Peil - Finished creating the SongCollection class
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 */

package student;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * SongCollection.java
 * Reads the specified data file and build an array of songs.
 * 
 * @author boothe
 */
public class SongCollection {

    private Song[] songs; // The array of Song objects

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore
     * not portable. Java runs on a virtual machine that IS portable. So this
     * is permissable because we are programming in Java and Java runs on a
     * virtual machine not directly on the hardware.
     *
     * @param filename The path and filename to the datafile that we are using
     *                 must be set in the Project Properties as an argument.
     */
    public SongCollection(String filename) {
        try {
            // Array list that holds song objects while reading the file JP
            ArrayList<Song> songList = new ArrayList<>();

            // BufferedReader created to open the file up for reading JP
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            // Variables that will temporarily store song data JP
            String line;// Holds the current line being read from the file
            String artist = null, title = null; // initilze variables to null JP
            StringBuilder lyricsBuilder = new StringBuilder(); // StringBuilder called lyricsBuilder that will collect
                                                               // song lyrics JP

            // BufferReader will read the line in the file as long as there is something to
            // read JP
            while ((line = reader.readLine()) != null) {
                // Checks to see if line starts with ARTIST JP
                if (line.startsWith("ARTIST=")) {
                    // Checks to see if we are already processing a song JP
                    if (artist != null && title != null && lyricsBuilder.length() > 0) {
                        // Adds the song to the array list JP
                        songList.add(new Song(artist, title, lyricsBuilder.toString()));
                    }
                    // Extracts the Artist's name by removine the "ARTIST=" from the string JP
                    artist = line.substring(7).trim();
                    // Removes quotations DS
                    artist = artist.replace("\"", "");
                    // Resets the title
                    title = null;
                    // Resets the lyrics
                    lyricsBuilder.setLength(0);

                    // Checks to see if the line starts with "TITLE=" JP
                } else if (line.startsWith("TITLE=")) {
                    // Extracts the title name by trimming off "TITLE=" JP
                    title = line.substring(6).trim();
                    // Removes quotations DS
                    title = title.replace("\"", "");
                    // Checks to see if the line starts with "LYRICS=" JP
                } else if (line.startsWith("LYRICS=")) {
                    // Starts reading the lyrics trimming out "LYRICS=" and removing the first
                    // quotation mark JP
                    lyricsBuilder.append(line.substring(7).trim().replace("\"", "")).append("\n");
                } else {
                    // Appends the lyrics and moves the last quotation mark JP
                    lyricsBuilder.append(line.replace("\"", "")).append("\n");
                }

            }

            // Adds the last song if necessary
            if (artist != null && title != null && lyricsBuilder.length() > 0) {
                songList.add(new Song(artist, title, lyricsBuilder.toString()));
            }

            // Closed the file reader
            reader.close();

            // Converts the ArrayList to a static array for sorting JP
            songs = songList.toArray(new Song[0]);
            Arrays.sort(songs);

            // Catchs any errors to prevent crashing
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        // use a try catch block
        // read in the song file and build the songs array
        // there are several ways to read in the lyrics correctly.
        // the line feeds between lines and the blank lines between verses
        // must be retained.

        // sort the songs array using Arrays.sort (see the Java API)
        // this will use the compareTo() in Song to do the job.
    }

    /**
     * this is used as the data source for building other data structures
     * 
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }

    /**
     * unit testing method
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);

        // Gets all the songs from the collection
        Song[] list = sc.getAllSongs();

        // Prints out the amount of songs by outputting the list length JP
        System.out.println("Total songs: " + list.length);

        // Prints out a title for the first 10 songs JP
        System.out.println("First 10 songs (or fewer):");
        Stream.of(list).limit(10).forEach(System.out::println);
    }
}