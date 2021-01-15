package sample;

import javafx.scene.text.Font;
import java.io.File;

public class Constants{
    public static Font chineseFont;
    public static void initialize(){
        try {
            chineseFont = Font.font ("FZHei-B01S");
            System.out.println(chineseFont);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
