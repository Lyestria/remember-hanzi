package sample;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalStorage {


    private final Path curDir = Paths.get(System.getProperty("user.home"));
    private final Path dir = Paths.get(curDir.toString(), "saves");
    private final Path filepath = Paths.get(dir.toString(),"words.sav");

    public void saveData(WordBank bank)throws IOException{
        if(Files.exists(dir)){
            if(new File(dir.toString()).mkdirs()){
                System.out.printf("Directory %s Created\n",dir.toString());
            }
        }
        try{
            FileOutputStream fileOut = new FileOutputStream(filepath.toString());
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
            System.out.println(filepath.toString());
            FileInputStream fileIn = new FileInputStream(filepath.toString());
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            WordBank bank = (WordBank)objIn.readObject();
            objIn.close();
            System.out.println("File Loaded!");
            return bank;
        } catch (Exception e){
            File f = new File(dir.toString());
            System.out.println(dir.toString());
            System.out.println(f.mkdir());
            throw e;
        }
    }
}
