/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfsa;

/**
 *
 * @author mizan
 */
public class Alphabet {

    private char startLetter ;

    public char getStartLetter() {
        return startLetter;
    }
    private int size;
    private char[] letters;
    
    public Alphabet(int numLetters){
        this('a',numLetters);        
    }  
    
    
    public Alphabet(char letter, int n) {
        startLetter = letter;
	size = n;
	letters = new char[size];

        for(short  i = 0; i < size; i++) {
	    letters[i] = (char) (startLetter + i);
	}
    }

    public char[] getLetters() {
        return letters;
    }

    public int size() {
        return size;
    }
    
    

    
}
