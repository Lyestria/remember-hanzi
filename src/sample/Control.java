package sample;


import javafx.scene.control.TextField;

import java.io.File;

public class Control {
    private UserInterface ui;

    private WordBank bank;

    private LocalStorage storage;

    Control(LocalStorage storage) throws Exception{
        this.ui = ui;
        this.storage = storage;
        try{
            bank = storage.retrieveData();
        } catch(Exception e){
            bank = new WordBank();
        }
    }

    public void setUI(UserInterface ui) {
        this.ui = ui;
    }

    public void getWord() throws Exception{
        try {
            Word word = bank.nextWord();
        }
        catch (Exception e){
            throw new Exception();
        }
        update();
    }

    public void addWord(String str)throws Exception{
        if(str.length()!=1)throw new Exception("Invalid Input");
        char c=str.toCharArray()[0];
        if(Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS){
            bank.add(Character.toString(c));
            try{
                storage.saveData(bank);
            } catch(Exception e){
            }
        }

    }

    public void clear(){
        bank.clear();
        try{
            storage.saveData(bank);
        } catch(Exception e){

        }
        update();
    }

    public void saveWord(String context1, String context2) {
        try {
            Word word=bank.curWord();
            word.setContext(context1);
            word.setContext2(context2);
            storage.saveData(bank);
        } catch (Exception e){
            System.out.println("Update Failed");
        }

    }

    public void loadCSV(File file) {
        try {
            bank = new WordBank(file);
            storage.saveData(bank);
        } catch (Exception e){
            System.out.println("Load Failed");
        }
    }

    public void saveCSV(File file){
        try{
            bank.toCsv(file);
        } catch (Exception e){
            System.out.println("Save Failed");
        }
    }

    public void reportResult(boolean b) {
        try {
            Word word = bank.curWord();
            word.report(b);
            storage.saveData(bank);

        } catch (Exception e){
            System.out.println("Report Failed");
        }
    }

    public void update(){
        ui.checkNextWordAllowance();
        ui.checkAnswerButtonAllowance();
        ui.display(bank.curWord());
    }

}
