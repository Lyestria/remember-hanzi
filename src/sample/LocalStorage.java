package sample;

import java.io.*;

public class LocalStorage {

    private final String filepath = "saves/words.sav";
    private final String dir = "saves/";

    public void saveData(WordBank bank)throws IOException{
        try{
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(bank);
            objOut.close();
            System.out.println("File Saved!");

        } catch (IOException e){
            System.out.println("Save Failed!");
            throw e;
        }
    }

    public WordBank retrieveData() throws Exception{
        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            WordBank bank = (WordBank)objIn.readObject();
            objIn.close();
            System.out.println("File Loaded!");
            return bank;
        } catch (Exception e){
            File f = new File(System.getProperty("user.dir")+dir);
            System.out.println(f.mkdir());
            throw e;
        }
    }
}
