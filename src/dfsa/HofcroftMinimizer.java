/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dfsa;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 *
 * @author mizan
 */
public class HofcroftMinimizer {
    
    
    
    public static DFA minimize(DFA dfa)
    {
        //seperate out accepting state and no-accepting states
        
        HashSet aStates = dfa.getAcceptingStates();
        HashSet<Integer> allStates = dfa.getStates();
        HashSet<Integer> naStates = difference(allStates,aStates);
        //Set partition = new Set();
        LinkedList<HashSet<Integer>> partition=new LinkedList<HashSet<Integer>>();

        LinkedList<HashSet> refineQueue = new LinkedList<HashSet>(); 
        partition.add(aStates);
        partition.add(naStates);
        
        refineQueue.add(aStates);
        /*hofcroft minimization algorithm*/
        while(refineQueue.isEmpty()==false){
               HashSet setToRefine = refineQueue.remove();
               for(char alpha : dfa.getAlphabet().getLetters()){
                     LinkedHashSet<HashSet<Integer>> partElements = 
                               new LinkedHashSet<HashSet<Integer>>(partition);
                    for(HashSet<Integer> s: partElements){

                       HashSet x = new HashSet<Integer>(); 
                       for(int source: s ){
                           int dest = dfa.getNext(source, alpha);    
                           if(setToRefine.contains(dest)){
                               x.add(source);
                           }

                        }
                       LinkedHashSet<HashSet<Integer>> oldElements = 
                               new LinkedHashSet<HashSet<Integer>>(partition);


                       for(HashSet<Integer> y : oldElements ){
                           HashSet common = intersect(x, y);
                           HashSet diff = difference(y, x);
                           if(common.isEmpty()==false){
                               partition.remove(y);
                               partition.add(common);
                               if(!diff.isEmpty()){
                                   partition.add(diff);
                               }
                               if(refineQueue.contains(y)){                           
                                   refineQueue.remove(y);
                                   refineQueue.add(common);
                                   if(!diff.isEmpty()){
                                       refineQueue.add(diff);
                                   }
                               }
                               else{
                                   if(common.size()<=diff.size()){
                                       refineQueue.add(common);                                                              
                                   }
                                   else{
                                       refineQueue.add(diff);

                                   }
                               }

                           }



                   }


                   
               }
               }
        }
        
        int newStartState = -1;
        HashSet<Integer> newAstates=new HashSet<Integer>();
        //combine groups first one
        
        
        //now states are grouped 
        //change states which are not distinguishable to one state (first state of group)
        
        int[][] interTransTable = new int[dfa.next.length][dfa.getnLetters()];
        //duplicate dfa trans table
        for(int i=0;i<dfa.next.length;i++){
            System.arraycopy(dfa.next[i], 0, interTransTable[i], 0,dfa.getnLetters() );
        }
        
        HashSet<Integer> removeList = new HashSet <Integer>();
        for(HashSet<Integer> set: partition){
            LinkedList<Integer> list = new LinkedList<Integer>(set);
            int item1 = list.getFirst();
            if(dfa.getAcceptingStates().contains(item1)){
                    newAstates.add(item1);
                }
            if(dfa.getStartState()==item1){newStartState=item1;}
            
            for(int i =1;i<list.size();i++){//replace other set values with first value
                int value = list.get(i);
                replaceTable(interTransTable, value, item1);
                removeList.add(value);
                if(dfa.getAcceptingStates().contains(value)){
                    newAstates.add(item1);
                }
                if(dfa.getStartState()==value){newStartState=item1;}
                
                
                                
            }
        }
        
        
        //now remove unnecesary rows from transition table
        //merge 
        int[][] finalTransTable = new int[partition.size()][dfa.getnLetters()];
        int index=0;
        boolean revisit=false;
        for(int count=0;count<dfa.getnStates();count++){
            if(!removeList.contains(count)){
                if(revisit==true){
                    replaceTable(interTransTable, count, index);
                    replaceTable(finalTransTable, count, index);
                    if(newAstates.contains(count)){
                        newAstates.remove(count);
                        newAstates.add(index);
                    }
                    if(newStartState==count){newStartState=index;}
                    
                    revisit=false;
                }
                System.arraycopy(interTransTable[count], 0, finalTransTable[index], 0, dfa.getnLetters());
                index++;
            }
            else{
                revisit=true;
            }
        }
        
        
        
        
        //now transition table is ready 
        //construct new dfa with new parameters
        
        DFA minDfa = new DFA(partition.size(), dfa.getAlphabet(), newStartState,newAstates);
        minDfa.next = finalTransTable;
        return minDfa;
    }
    
    
    private static void replaceTable(int[][] table,int value, int replace){
        for(int i=0;i<table.length;i++){
            for(int j=0;j<table[i].length;j++){
                if(table[i][j]==value){
                    table[i][j]=replace;
                }
            }
        }
    }
    
    
    /*
     * returns A\B
     */
    private static HashSet<Integer> difference(HashSet<Integer> a,HashSet<Integer> b){
        HashSet<Integer> result =  new HashSet<Integer>();
        for(int e1: a){
            boolean addthis=true;
            for(int e2:b){
                if(e1==e2){
                    addthis = false;
                    break;
                }
            }
            if(addthis) result.add(e1);
        }
        return result;
    }
    
    
    /*
     * returns common elements of A and B
     */
    private static HashSet<Integer> intersect(HashSet<Integer> a,HashSet<Integer> b){
        HashSet<Integer> result =  new HashSet<Integer>();
        for(int e1: a){
            for(int e2:b){
                if(e1==e2){
                    result.add(e1);
                }
            }
        }
        return result;
    }
    
    
    
    
    
    
    
}
