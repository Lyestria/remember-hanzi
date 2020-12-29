package sample;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

public class WordBank implements Serializable {
    private HashMap<String,Word> bank;
    private ArrayList<String> words;
    private ArrayList<String> bucket;
    private int it;

    //CSV Headers and Display Names
    final static private String
        WORD="Character",
        CONTEXT="Word Example",
        CONTEXT2="Pinyin",
        HISTORY="History",
        WEIGHT="Weight",
        PROB="Probability of Appearance in Next Bucket";


    public WordBank(){
        bank=new HashMap<>();
        words=new ArrayList<>();
        bucket=new ArrayList<>();
        it=-1;
    }

    WordBank(File file) throws IOException{
        this();
        String fileName=file.getName();
        int lastIndexOf = fileName.lastIndexOf(".");
        String extension=fileName.substring(lastIndexOf+1).toLowerCase();
        if(!extension.equals("csv"))throw new IOException();

        BufferedReader br=new BufferedReader(new FileReader(file));
        String[] header=br.readLine().split(",");

        String cur;
        int row=1;
        while((cur=br.readLine()) != null){
            String[] cells=cur.split(",");
            String word="";
            Word info=new Word(word);
            for(int i=0;i<Math.min(6,cells.length);i++){
                try{
                    switch(i){
                        case 0:
                            word=cells[i];
                            info.setWord(word);
                            break;
                        case 1:
                            info.setContext(cells[i]);
                            break;
                        case 2:
                            info.setContext2(cells[i]);
                            break;
                        case 3:
                            char[] his=cells[i].toCharArray();
                            ArrayList<Boolean> boolHis = info.getHistory();
                            for(char c:his) {
                                if(c=='0')boolHis.add(false);
                                else if(c=='1') boolHis.add(true);
                                else throw new Exception();
                            }
                            break;
                        case 4:
                            info.setWeight(Double.parseDouble(cells[i]));
                        case 5:
                            info.setProb(Double.parseDouble(cells[i]));
                    }
                } catch(Exception e){
                    System.out.printf("Error Reading Cell at Row %d Cell %d",row,i);
                }
            }
            if(info.getNumCor() <= info.getTotal() && word!=null)
                add(word,info);
            row++;
        }
        br.close();
    }

    public Word add(String w){
        return add(w, new Word(w));
    }

    public void clear(){
        bank.clear();
        words.clear();
        bucket.clear();
        it=-1;
    }

    private void nextBucket(){
        bucket=(ArrayList<String>)words.stream()
            .filter(str -> bank.get(str).inNextBucket())
            .collect(Collectors.toList());
        Collections.shuffle(bucket);
        it=-1;
    }

    Word curWord(){
        if(it<0)return null;
        return bank.get(bucket.get(it));
    }

    Word nextWord(){
        if(words.size()==0) throw new RuntimeException("Wordbank is empty");
        while(it+1==bucket.size()) nextBucket();
        return bank.get(bucket.get(++it));
    }

    Word pureRandomWord(){
        if(words.size()==0) throw new RuntimeException("Wodbank is empty");
        int r = (int)(Math.random()*words.size());
        return bank.get(words.get(r));
    }

    void toCsv(File file)throws IOException{
        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
        PrintWriter pw=new PrintWriter(bw);
        String stupidExcelBeggining = new String(new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF});
        pw.write(stupidExcelBeggining);
        pw.printf("%s,%s,%s,%s,%s,%s\n",WORD,CONTEXT,CONTEXT2,HISTORY,WEIGHT,PROB);
        for(String word : words){
            Word info = bank.get(word);
            pw.printf("%s,%s,%s,%s,%f,%f\n",
                word,
                info.getContext(),
                info.getContext2(),
                info.getHistoryString(),
                info.getWeight(),
                info.getProb());
        }
        pw.flush();
        pw.close();
    }



    public Word add(String w,String context, String context2){
        Word info=add(w);
        info.setContext(context);
        info.setContext2(context2);
        return info;
    }

    public Word add(String w, Word newInfo){
        Word info = bank.get(w);
        if(info == null){
            bank.put(w, newInfo);
            words.add(w);
        }
        else{
            info.update(newInfo);
            //This needs to be better implemented
            //A new update function is necessary
        }
        return info;
    }

    public void addAll(WordBank wb){
        for(String w: wb.words){
            add(w, wb.bank.get(w));
        }
    }

    public void editWord(String str, String context1, String context2) {
        Word word = bank.get(str);
        word.setContext(context1);
        word.setContext2(context2);
    }
}
