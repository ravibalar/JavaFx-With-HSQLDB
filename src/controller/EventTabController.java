package controller;

import controller.post.AddEventViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.user.User;
import service.post.EventService;

import java.io.IOException;

public class EventTabController {
    private static User currentUser;
    private final EventService eventService = new EventService();
    @FXML
    private BorderPane tabContent;
    @FXML
    private GridPane postViewGrid;

    public static void initData(User user) {
        currentUser = user;
    }

    @FXML
    private void initialize() {
        // DB db= new DB();
        displayPost();
    }

    public void displayPost() {
        // DB db= new DB();
        // String[] title = eventService.getField();
        // for(int i=0;i<10;i++) {
        // FXMLLoader loader = new FXMLLoader();
        // System.out.println(getClass().getName());
        // loader.setLocation(getClass().getResource("../view/postsummary_view.fxml"));
        // try {
        // AnchorPane postSummary = loader.load();
        // PostSummaryViewController postSummaryViewController = loader.getController();
        // postSummaryViewController.initData(currentUser, new Event("TEMP-EVE",
        // "EVE001"+i, "EVE001 description", "balar025", "Cinema", "12/04/2020",
        // 4));
        // postViewGrid.addRow(i, postSummary);
        // } catch (IOException ex) {
        // e.printStackTrace();
        // }
        // }
    }

    @FXML
    private void addEventHandler(ActionEvent actionEvent) {
        System.out.println("Add New event");
        FXMLLoader loader = new FXMLLoader();
        System.out.println(getClass().getName());
        loader.setLocation(getClass().getResource("../view/addevent_view.fxml"));
        try {
            Parent detailView = loader.load();
            AddEventViewController addEventController = loader.getController();
            addEventController.initData(currentUser);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Stage viewStage = new Stage();
            viewStage.setScene(new Scene(detailView));
            viewStage.getScene().getStylesheets()
                    .add(Main.class.getResource("../resource/css/style.css").toExternalForm());
            viewStage.initOwner(window);
            viewStage.initModality(Modality.APPLICATION_MODAL);
            viewStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
