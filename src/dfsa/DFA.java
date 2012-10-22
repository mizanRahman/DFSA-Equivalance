/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfsa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author mizan
 */
public class DFA {
    
   
    private int nStates; 
    private int nLetters;
    private Alphabet alphabet;
    private HashSet<Integer> states;
    private HashSet<Integer> acceptingStates;
    private int startState;
   
    int[][] next;

     
    public   DFA(int nStates, int nLetters,int startState, HashSet accepingStates,int[][] transTable) {
	this(nStates, new Alphabet(nLetters),startState,accepingStates,transTable);
    }

    
    public   DFA(int nStates, Alphabet alphabet,int startState, HashSet acceptingStates,int[][] transTable) {
	this.nStates = nStates;
        this.nLetters = alphabet.size();
        this.alphabet = alphabet;
        this.startState = startState;        
        this.acceptingStates = acceptingStates;	
        next = transTable;

    }
    
    
    public   DFA(int nStates, int nLetters,int startState, HashSet accepingStates) {
	this(nStates, new Alphabet(nLetters),startState,accepingStates);
    }
    public   DFA(int nStates, Alphabet alphabet,int startState, HashSet acceptingStates) {
	this.nStates = nStates;
        this.nLetters = alphabet.size();
        this.alphabet = alphabet;
        this.startState = startState;        
        this.acceptingStates = acceptingStates;
	next = new int[nStates][nLetters];

    }
    
    

    public int getnStates() {
        return nStates;
    }
    
    

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public int getnLetters() {
        return nLetters;
    }
    
    

    public int getStartState() {
        return startState;
    }
    
    
    public int getNext(int state, char symbol){
        short symbolIndex = (short)(symbol-alphabet.getStartLetter());
        return next[state][symbolIndex];
    }
    
    
    public HashSet getStates(){
        if(states==null){
            states = new HashSet<Integer>();
            for(int i=0;i<nStates;i++){
                states.add(i);
            }
        }   
        return states;
    }
    
    
    public HashSet getAcceptingStates(){
        return acceptingStates;
    }
    
    /*
     * this method will remove unreachable states from DFA 
     * to make it standard formatted
     */
    synchronized public void standardize(){
        LinkedList<Integer> processQueue = new LinkedList();
        HashSet<Integer> rechableStates = new HashSet<Integer>();
        
        rechableStates.add(startState);
        for(int i = 0;i<nLetters;i++){
            processQueue.add(next[startState][i]);
            
        }
        while (processQueue.isEmpty()==false) {
            int state = processQueue.remove();
            rechableStates.add(state);
            
            for(int i = 0;i<nLetters;i++){
                int siblingState = next[state][i];
                if(processQueue.contains(siblingState)==false)
                    processQueue.add(siblingState);
           
            }
            

        }
        
        //now reconstruct new DFA
       
         int numStates= rechableStates.size();
        int[][] transtable = new int[numStates][nLetters];
        
        for (int state : rechableStates) {
            for(int i=0;i<nLetters;i++){
                transtable[state][i] = next[state][i];
            }

        }
        new DFA(nStates, alphabet, startState, acceptingStates, transtable);
        //new DFA(nStates,alphabet,startState,acceptingStates,rechableStates);
        

        
        
        
    }
    
    
    
    
    public boolean isEquivalant(DFA dfa){
        
        if(this.nStates!=dfa.getnStates())return false;
        if(this.acceptingStates.size()!=dfa.getAcceptingStates().size())return false;
        //if #total states and #accepting states are equal then #nastates must be equal
        Permutation p = new Permutation(dfa.getnStates()); 
        boolean result = false;
        while(p.hasNext()){
            result=true;
            for(int i=0;i<this.next.length;i++){
                for(int j=0;j<this.next[i].length;j++){
                    int permutedStatus = p.get(this.next[i][j]);
                    int index = p.get(i);
                    if(dfa.next[index][j]!=permutedStatus){
                        result=false;
                        break;
                    }
                        
                }
                if(result==false)break;
            }   
            if(result==true)
                break;
            else{
                p.next();
            }
        }
        
        return result;
        /*
        if(array2dEquals(this.next, dfa.next))
            return true;
        else 
            return false;*/
        
        
        
    }
    
    
    
    
    /*
     * this method  is just for debug purpose      
     */
    static DFA makeit(int choix) {
	Alphabet al = new Alphabet(2);
        int startState = 1;
        
        
	if (choix == 0) {
            HashSet<Integer> acceptStates = new HashSet<Integer>();
            acceptStates.add(3);
	    DFA a = new DFA(6,al,startState,acceptStates);
            
	    a.next[0][0]= 1;a.next[0][1]= 2;
	    a.next[1][0]= 1;a.next[1][1]= 3;
	    a.next[2][0]= 4;a.next[2][1]= 2;
	    a.next[3][0]= 2;a.next[3][1]= 3;
	    a.next[4][0]= 2;a.next[4][1]= 4;
	    a.next[5][0]= 3;a.next[5][1]= 5;
	    return a;
	}
        else if(choix==1) {
	    HashSet<Integer> acceptStates = new HashSet<Integer>();
            acceptStates.add(4); 
            DFA a = new DFA(6,al,startState,acceptStates);

	    a.next[0][0]= 1;a.next[0][1]= 2;
	    a.next[1][0]= 1;a.next[1][1]= 4;
	    a.next[2][0]= 3;a.next[2][1]= 2;
	    a.next[3][0]= 2;a.next[3][1]= 3;
	    a.next[4][0]= 2;a.next[4][1]= 4;
	    a.next[5][0]= 5;a.next[5][1]= 4;
	    return a;
	}
        else if(choix==2){
            HashSet<Integer> acceptStates = new HashSet<Integer>();
            acceptStates.add(1); acceptStates.add(2);
            DFA a = new DFA(6,al,startState,acceptStates);

            a.next[0][0]= 3;a.next[0][1]= 1;
	    a.next[1][0]= 4;a.next[1][1]= 1;
	    a.next[2][0]= 0;a.next[2][1]= 2;
	    a.next[3][0]= 5;a.next[3][1]= 2;
	    a.next[4][0]= 3;a.next[4][1]= 1;
	    a.next[5][0]= 5;a.next[5][1]= 5;
	    return a;
            
        }
        else if(choix==3){
            HashSet<Integer> acceptStates = new HashSet<Integer>();
            acceptStates.add(3); 
            DFA a = new DFA(3,al,startState,acceptStates);

            a.next[0][0]= 2;a.next[0][1]= 1;
	    a.next[1][0]= 3;a.next[1][1]= 2;
	    a.next[2][0]= 3;a.next[2][1]= 2;
	    return a;
        }
        else if(choix==4){
            HashSet<Integer> acceptStates = new HashSet<Integer>();
            acceptStates.add(2);
            DFA a = new DFA(3,al,startState,acceptStates);

            a.next[0][0]= 2;a.next[0][1]= 1;
	    a.next[1][0]= 2;a.next[1][1]= 1;
	    a.next[2][0]= 1;a.next[2][1]= 3;
	    return a;
        }
        else{
            return null;
        }
    }
            
       
    @Override
    public String toString(){
	
        StringBuilder u = new StringBuilder();
	for(int state=0;state<nStates;state++){
	    
	    for(int letter=0;letter<nLetters;letter++){
                char alpha = (char)(letter+'a');
                u.append(state+1).append(" ");
		u.append(alpha).append(" ");
                u.append(next[state][letter]+1).append("\n");
            }
	    
	}
        
        u.append("accepting state(s): ");
        for(int i =0;i<nStates;i++){
            if(acceptingStates.contains(i))
                u.append(i+1).append(" ");
        }
	return u.toString();
    }

    
    
}
