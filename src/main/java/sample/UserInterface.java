package sample;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class UserInterface{

    private Stage stage;
    private Group root;
    private Control control;

    private WordArea wordArea;
    private TextField newWord;

    private Button getWordButton;
    private Button answerCorrect,answerIncorrect;
    private Button importCSVButton, exportCSVButton;
    private Button clearButton;
    private PastWordsArea pastWordsArea;

    private boolean answering = false;


    UserInterface(Stage stage,Control control){
        this.stage = stage;
        stage.setTitle("Remember Hanzi");
        this.control = control;
        root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        initialize();
        stage.show();
    }

    public void initialize(){

        VBox maincontent = new VBox();
        pastWordsArea = new PastWordsArea(control);

        HBox content = new HBox();
        content.getChildren().addAll(maincontent,pastWordsArea);
        content.setPadding(new Insets(10));
        root.getChildren().add(content);

        wordArea = new WordArea(control);
        HBox addWordArea = new HBox();
        getWordButton = new Button("Next Word");
        Button addWordButton = new Button("Add A Word");
        newWord = new TextField();
        addWordArea.getChildren().addAll(addWordButton,newWord);
        addWordArea.setSpacing(20);

        clearButton = new Button("Reset Word List");

        importCSVButton = new Button("Import CSV");

        exportCSVButton = new Button("Export CSV");

        HBox importExport = new HBox();
        importExport.getChildren().addAll(importCSVButton,exportCSVButton);
        importExport.setSpacing(20);

        answerCorrect = new Button("Correct");
        answerCorrect.setVisible(answering);

        answerIncorrect = new Button("Incorrect");
        answerIncorrect.setVisible(answering);

        HBox answerGroup=new HBox();
        answerGroup.getChildren().addAll(answerCorrect,answerIncorrect);

        EventHandler<ActionEvent> answerResponse= e-> {
                control.reportResult(e.getSource()==answerCorrect);
                answering=false;
                control.update();
        };
        answerCorrect.setOnAction(answerResponse);
        answerIncorrect.setOnAction(answerResponse);

        maincontent.getChildren().addAll(wordArea,answerGroup, getWordButton,addWordArea, clearButton, importExport);
        maincontent.setSpacing(10);
        maincontent.setAlignment(Pos.CENTER);


        getWordButton.setOnAction(e -> {
            try {
                answering = true;
                control.getWord();
                answerCorrect.setFocusTraversable(true);
            } catch(Exception ex){
                System.out.println("Failed to get next word");
            }
        });
        addWordButton.setOnAction(e -> {
            enterWord();
        });
        newWord.addEventHandler(KeyEvent.KEY_PRESSED,e ->{
            if (e.getCode().equals(KeyCode.ENTER)) {
                enterWord();
            }
        });
        clearButton.setOnAction(e -> {
            control.clear();
        });
        importCSVButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Load File");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = chooser.showOpenDialog(new Stage());
            if(file == null){
                System.out.println("Load Failed");
                return;
            }
            control.loadCSV(file);
        });
        exportCSVButton.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save File");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = chooser.showSaveDialog(new Stage());
            if(file == null){
                System.out.println("Save Failed");
            }
            control.saveCSV(file);
        });
    }

    public void checkAnswerButtonAllowance() {
        answerCorrect.setVisible(answering);
        answerIncorrect.setVisible(answering);
    }

    public void enterWord(){
        try {
            control.addWord(newWord.getText());
            newWord.setText("");
        } catch(Exception e){
            System.out.println("Invalid Input");
        }
    }

    public void display(Word w){
        wordArea.displayWord(w);
    }


    public void checkNextWordAllowance() {
        boolean disable = answering || wordArea.editing;
        getWordButton.setDisable(disable);
        importCSVButton.setDisable(disable);
        clearButton.setDisable(disable);
    }

    public void setCounter(int numWords) {
        pastWordsArea.setCount(numWords);
    }

    public void resetPastWords() {
        pastWordsArea.reset();
    }

    public void addPastWord(Word word, boolean b) {
        pastWordsArea.addWord(word,b);
    }
}