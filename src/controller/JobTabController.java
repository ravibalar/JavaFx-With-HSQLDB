package controller;

import controller.post.PostDetailViewController;
import controller.utility.AlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.post.Event;
import model.post.Job;
import model.user.User;

import java.io.IOException;

public class JobTabController {
    private static User currentUser;
    @FXML
    private BorderPane tabContent;
    @FXML
    private TableView postTableView;

    public static void initData(User user) {
        currentUser = user;
    }

    @FXML
    private void addJobHandler(ActionEvent actionEvent) {
    }

}
