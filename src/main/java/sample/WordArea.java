package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class WordArea extends VBox {
    private Text wordBox;
    private TextField contextBox1, contextBox2;
    private Button editSave;
    private Control control;
    private Text info;
    public boolean editing = false;
    WordArea(Control control){
        super();
        this.control = control;

        wordBox=new Text();
        wordBox.setFont(new Font(Constants.wordFont,200));
        info=new Text("Weight: \nHistory: ");


        contextBox1=new TextField();
        contextBox1.setFont(new Font(Constants.wordFont,15));
        contextBox1.setAlignment(Pos.CENTER);
        contextBox1.setDisable(true);

        contextBox2=new TextField();
        contextBox2.setFont(new Font(Constants.wordFont,15));
        contextBox2.setDisable(true);
        contextBox2.setAlignment(Pos.CENTER);

        VBox contexts = new VBox(contextBox1,contextBox2);

        editSave=new Button("Edit");
        editSave.setVisible(false);
        editSave.setOnAction(e -> {
            if(!editing){
                contextBox1.setDisable(false);
                contextBox2.setDisable(false);
                editSave.setText("Save");
                editing=true;
            }
            else{
                contextBox1.setDisable(true);
                contextBox2.setDisable(true);
                try {
                    this.control.saveWord(
                            contextBox1.getText(),
                            contextBox2.getText());
                } catch (Exception ee){
                    ee.printStackTrace();
                }
                editSave.setText("Edit");
                editing=false;
            }
            control.update();
        });

        HBox contextPanel=new HBox(contexts,editSave);

        getChildren().addAll(wordBox,info,contextPanel);
        setAlignment(Pos.CENTER);
    }
    public void displayWord(Word w){
        if(w == null){
            wordBox.setText("");
            contextBox1.setText("");
            contextBox2.setText("");
            info.setText("Weight: \nHistory: ");
            editSave.setVisible(false);
            return;
        }
        wordBox.setText(w.getWord());
        contextBox1.setText(w.getContext());
        contextBox2.setText(w.getContext2());
        info.setText("Weight: "+w.getWeight()+"\nHistory: "+w.getHistoryString());
        editSave.setVisible(true);
    }
}
