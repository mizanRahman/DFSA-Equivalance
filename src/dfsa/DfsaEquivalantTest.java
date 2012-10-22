/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author mizan
 */
public class DfsaEquivalantTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            Scanner input = new Scanner(new File("input.txt"));
            int numdfa = input.nextInt();
            
            //in the next lines -1 is occured to transform 1 indexd to 0 index. 
            //output  will also be transformed
            DFA[] dfa=new DFA[numdfa];
            DFA[] mindfa=new DFA[numdfa];
            System.out.println("\nall given dfsa:\n");

            for(int n=0;n<numdfa;n++){
                int numStates=input.nextInt();
                int numAlpha = input.nextInt();
                int startState = input.nextInt()-1;
                input.nextLine(); //go to next line
                StringTokenizer tokenizer = new StringTokenizer(input.nextLine());//go to next line returning current line
                HashSet<Integer> aStates = new HashSet<Integer>();
                while(tokenizer.hasMoreTokens()){
                    int val = Integer.parseInt(tokenizer.nextToken());
                    aStates.add(val-1);
                }



                int[][] transtable = new int[numStates][numAlpha];            
                //take input of transition table
                for(int i=0;i<numStates;i++){
                    for(int j=0;j<numAlpha;j++){
                        int row=input.nextInt()-1;
                        int col=input.next().charAt(0)-'a';
                        int nextState=input.nextInt()-1;
                        transtable[row][col]=nextState;

                    }
                }
                /*input taking is done now construct dfa*/
                dfa[n] = new DFA(numStates,numAlpha,startState,aStates,transtable);
                System.out.println(dfa[n]);

            
            }
            
            
            
            
            /*minimize all dfa*/
            System.out.println("\nall minized dfsa :\n");
            for(int i=0;i<numdfa;i++){                    
                    mindfa[i] = HofcroftMinimizer.minimize(dfa[i]);
                    System.out.println(mindfa[i]);

            }
            
            
            //check for equivalance
            System.out.println("\nequivalant dfa pair : ");
            boolean result=false;
            for(int i=0;i<numdfa;i++){
                for(int j=i+1;j<numdfa;j++){
                    result = mindfa[i].isEquivalant(mindfa[j]);
                    if(result==true)
                        System.out.println("("+(i+1)+","+(j+1)+")");
                }

            }
            
            
            
            
            
            
            
            
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        catch(IOException e){
            System.out.println("parse error: "+e.getMessage());
        }
        catch(Exception e){
            System.out.println(e);
        }
        
        
        
        
        
        
        
        
        
    }
    
}
