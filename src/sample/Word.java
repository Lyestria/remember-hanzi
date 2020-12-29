package sample;

import java.io.Serializable;
import java.util.ArrayList;

//TODO: 
//Probably remove numCor and total. They don't seem neccesary

public class Word implements Serializable {

    private String word, context, context2;

    private ArrayList<Boolean> history;

    private int numCor,total;

    private double weight, prob;

    final static private double scaleFactor=0.8, minWeight=0.3;
    
    public Word(String word){
        this.word=word;
        clearAll();
    }

    public void clearAll(){
        clearData();
        context=context2="";
    }

    public void clearData(){
        history=new ArrayList<>();
        numCor = total = 0;
        weight = 1;
        prob = 0;
    }

    /**
     * Update the state of this word depending on whether 
     * or not user's answer is correct.
     * @param correct
     */
    public void report(Boolean correct){
        history.add(correct);
        if(correct){
            weight = Math.max(weight*scaleFactor,minWeight);
            numCor++;
        }
        else{
            weight = 1;
        }
        total++;
    }
    
    /**
     * Check if this word is going inside the next bucket
     */
    public boolean inNextBucket(){
        prob+=weight;
        if(Math.random()<prob){
            prob-=1;
            return true;
        }
        return false;
    }
    
    //This needs to be changed
    public void update(Word newInfo){
        this.numCor += newInfo.numCor;
        this.total += newInfo.total;
        history.addAll(newInfo.history);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setContext2(String context2) {
        this.context2 = context2;
    }

    public String getContext2() {
        return context2;
    }

    public ArrayList<Boolean> getHistory() {
        return history;
    }

    public int getNumCor() {
        return numCor;
    }

    public int getTotal() {
        return total;
    }

    public void setNumCor(int numCor) {
        this.numCor = numCor;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public double getWeight() {
        return weight;
    }

    public double getProb() {
        return prob;
    }

    public String getHistoryString() {
        char[] his = new char[history.size()];
        for (int i = 0; i < his.length; i++) {
            his[i] = history.get(i) ? '1' : '0';
        }
        return String.valueOf(his);
    }

    public String toString(){
        return word;
    }
}
