/**
 * File: Song.java
 * **********************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 * 02/02/2025 - Junting Zhang - create CmpArtist class
 *1.20.2025 - Dylan Sherwood  - Updated classes and methods
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing  
 ************************************************************************
 */
package student;

import java.util.Comparator;


/**
 * Song class to hold strings for a song's artist, title, and lyrics
 * Do not add any methods for part 1, just implement the ones that are
 * here.
 * 
 * @author boothe
 *         Made generic so compare to method would work without typecasting
 *         or checking DS
 */
public class Song implements Comparable<Song> {
        // Private string fields for (artist, title, and lyrics) added by DS
        private String artist;
        private String title;
        private String lyrics;
        
        
        /**
         * a nested Comparator class that compares songs by artist.
         * 
         * @author Junting Zhang
         */
        public static class CmpArtist extends CmpCnt implements Comparator<Song>  {
            /**
             * override Comparator compare() method, to compare Song object s1 ,
             * s2 by artist
             * @param s1 Song object 1
             * @param s2 Song object 2
             * @return 0 s1=s2, negative for s1< s2, position for s1>s2
             * @author Junting Zhang
             */
            @Override
            public int compare(Song s1, Song s2) {
                cmpCnt++;
                return s1.getArtist().compareToIgnoreCase(s2.getArtist());
                
            }
        }

        /**
         * Parameterized constructor
         * 
         * @param artist the author of the song
         * @param title  the title of the song
         * @param lyrics the lyrics as a string with linefeeds embedded
         *               Private field constructors added by DS
         * @Author Dylan
         */
        public Song(String artist, String title, String lyrics) {

                this.artist = artist;
                this.title = title;
                this.lyrics = lyrics;

        }

        /**
         *
         * @return artist the author of the song
         *         Return statement added by DS
         * @Author Dylan
         */
        public String getArtist() {
                return artist;
        }

        /**
         *
         * @return lyrics the lyrics as a string with linefeeds embedded
         *         Return statement added by DS
         * @Author Dylan
         */
        public String getLyrics() {
                return lyrics;
        }

        /**
         *
         * @return title the title of the song
         *         Return statement added by DS
         *  @Author Dylan
         */
        public String getTitle() {
                return title;
        }

        /**
         * returns name and title ONLY on one line in the form:
         * artist, "title"
         * 
         * @return a formatted string with comma and quotes added
         *         Return statement added with desired formatting by DS
         * @Author Dylan
         */
        @Override
        public String toString() {
                return artist + ",   \"" + title + "\"";
        }

        /**
         * the default comparison of songs
         * primary key: artist, secondary key: title
         * used for sorting and searching the song array
         * if two songs have the same artist and title they are considered the same
         * 
         * @param song2
         * @return a negative number, positive number or 0 depending on whether
         *         this song should be before, after or is the same. Used for a
         *         "natural" sorting order. In this case first by author then by
         *         title so that the all of an artist's songs are together,
         *         but in alpha order. Follow the given example.
         * 
         * 
         * 
         */
        @Override
        // Initial logic added for testing purposes only DS
        /**
         * Creates an int variable with the value of comparing current artist and the
         * artist of song 2, if artist is less than result is negative, if greater than
         * result is positive, if 0 they are equal. DS
         * @Author Dylan
         * 
         */

        public int compareTo(Song song2) {
                int thisComparison = artist.compareToIgnoreCase(song2.artist);
                // If they are not equal, returns the value DS
                if (thisComparison != 0) {
                        return thisComparison;
                }
                // if they were equal, compares titles DS
                // Return negative if its less, 0 if equal, Positive if greater
                return title.compareToIgnoreCase(song2.title);

        }

        /**
         * testing method to unit test this class
         * 
         * @param args
         */
        public static void main(String[] args) {
                Song s1 = new Song("Professor B",
                                "Small Steps",
                                "Write your programs in small steps\n"
                                                + "small steps, small steps\n"
                                                + "Write your programs in small steps\n"
                                                + "Test and debug every step of the way.\n");

                Song s2 = new Song("Brian Dill",
                                "Ode to Bobby B",
                                "Professor Bobby B., can't you see,\n"
                                                + "sometimes your data structures mystify me,\n"
                                                + "the biggest algorithm pro since Donald Knuth,\n"
                                                + "here he is, he's Robert Boothe!\n");

                Song s3 = new Song("Professor B",
                                "Debugger Love",
                                "I didn't used to like her\n"
                                                + "I stuck with what I knew\n"
                                                + "She was waiting there to help me,\n"
                                                + "but I always thought print would do\n\n"
                                                + "Debugger love .........\n"
                                                + "Now I'm so in love with you\n");

                System.out.println("testing getArtist: " + s1.getArtist());
                System.out.println("testing getTitle: " + s1.getTitle());
                System.out.println("testing getLyrics:\n" + s1.getLyrics());

                System.out.println("*******  testing toString: ******* \n");
                System.out.println("Song 1: " + s1);
                System.out.println("Song 2: " + s2);
                System.out.println("Song 3: " + s3);
                System.out.println();

                System.out.println("*******  testing compareTo:*******  \n");
                System.out.println("Song1 vs Song2 = " + s1.compareTo(s2));
                System.out.println("Song2 vs Song1 = " + s2.compareTo(s1));
                System.out.println("Song1 vs Song3 = " + s1.compareTo(s3));
                System.out.println("Song3 vs Song1 = " + s3.compareTo(s1));
                System.out.println("Song1 vs Song1 = " + s1.compareTo(s1));
                
                System.out.println();
                
                // testing CmpArtist compare() method
                // create CmpArtist object
                CmpArtist comArtist = new CmpArtist();
                System.out.println("*******  testing compare: ********* \n");
                System.out.println("Song1 vs Song2 = " + comArtist.compare(s1, s2));
                
                System.out.println("Song2 vs Song1 = " + comArtist.compare(s2, s1));
                System.out.println("expected result : if s1 < s2 , then s2 > s1, or s1 > s2 , then s2 < s1 \n");
                System.out.println("Song1 vs Song3 = " + comArtist.compare(s1, s3));
                System.out.println("expected result : 0 \n");
                System.out.println("Song3 vs Song1 = " + comArtist.compare(s3, s1));
                System.out.println("expected result : 0 \n");
                System.out.println("Song2 vs Song3 = " + comArtist.compare(s2, s3));
                System.out.println("Song3 vs Song2 = " + comArtist.compare(s3, s2));
                System.out.println("expected result : if s2 < s3 , then s3 > s2, or s2 > s3 , then s3 < s2 ");

        }
}