package main;

import controller.HomeViewController;
import controller.utility.AlertController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.user.User;

import java.io.IOException;

public class Main extends Application {
    private static Stage currentWindow = null;
    private static String fxmlPath = "../view/";
    private static String styleSheet = Main.class.getResource("../resource/css/style.css").toExternalForm();
    private static BorderPane mainLayout;
    private User user;
    @Override
    public void start(Stage stage) {
        currentWindow = stage;
        currentWindow.setTitle("UniLink 1.0");
        this.show();
    }

    public static void show() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath + "main_view.fxml"));
            mainLayout = loader.load();
            Scene mainScene = new Scene(mainLayout);
            mainScene.getStylesheets().add(styleSheet);
            currentWindow.setScene(mainScene);
            currentWindow.show();
        } catch (Exception ex) {
            AlertController.Error("Error",null, ex.toString());
        }
    }

    public static void showHome(User user) {
        try {
            //System.out.println(user.getID());
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath + "home_view.fxml"));
            BorderPane homeLayout = loader.load();
            HomeViewController controller = loader.getController();
            Scene homeScene = new Scene(homeLayout);
            homeScene.getStylesheets().add(styleSheet);
            controller.initData(user);
            currentWindow.setScene(homeScene);
        } catch (Exception ex) {
            AlertController.Error("Error",null, ex.toString());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
