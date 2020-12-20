package sample;


import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        try {
            Constants.initialize();
            LocalStorage storage = new LocalStorage();
            Control control = new Control(storage);

            UserInterface ui = new UserInterface(stage,control);
            control.setUI(ui);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
