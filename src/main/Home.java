package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {
    private Scene homeScene;
    public Home(){

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../view/home_view.fxml"));
            homeScene = new Scene(root, 600, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene(){
        return homeScene;
    }
}
