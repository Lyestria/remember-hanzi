package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;


public class PastWordsArea extends VBox {
    private Button startButton, pauseButton, resetButton;
    private Control control;
    private Label countArea;
    private TextFlow pastArea;
    private Text header;

    PastWordsArea(Control control){
        super();
        this.control = control;

        startButton = new Button("Start");
        startButton.setOnAction( event -> {
            control.startCounting();
            startButton.setDisable(true);
            pauseButton.setDisable(false);
        });

        pauseButton = new Button("Pause");
        pauseButton.setOnAction( event -> {
            control.stopCounting();
            startButton.setDisable(false);
            pauseButton.setDisable(true);
        });
        pauseButton.setDisable(true);

        resetButton = new Button("Reset");
        resetButton.setOnAction( event -> {
            control.resetCount();
        });

        HBox buttonArea = new HBox();
        buttonArea.setPadding(new Insets(20));
        buttonArea.getChildren().addAll(startButton,pauseButton,resetButton);

        countArea = new Label();
        countArea.setPadding(new Insets(15));

        header = new Text("Tested Words\n");
        header.setFont(new Font(Constants.wordFont,20));
        header.setTextAlignment(TextAlignment.CENTER);

        pastArea = new TextFlow();
        pastArea.setTextAlignment(TextAlignment.CENTER);
        reset();
        setCount(0);

        getChildren().addAll(buttonArea,countArea,pastArea);
    }

    public void setCount(int count){
        System.out.println(count);
        countArea.setText("Number of Words Tested: "+count);
    }


    public void reset() {
        pastArea.getChildren().setAll(header);
    }

    public void addWord(Word word, boolean b) {
        Text body=new Text(word.toString()+"\n");
        body.setFont(new Font(Constants.wordFont,18));
        body.setTextAlignment(TextAlignment.CENTER);
        body.setFill(b? Color.GREEN : Color.RED);
        pastArea.getChildren().add(body);
    }
}
