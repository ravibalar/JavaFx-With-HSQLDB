package controller.post;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.Main;
import model.post.Event;

import java.io.IOException;

public class EventDetailViewController {
    @FXML
    private GridPane detailGrid;
    public void initData(Event event){
        System.out.println(event.toString());
        Label title = new Label("Title");
        Label title_value=new Label(event.getTitle());
        Label description = new Label("Description");
        Label description_value=new Label(event.getDescription());
        Label creator = new Label("Created By");
        Label creator_value=new Label(event.getCreatorID());
        detailGrid.addRow(0,title,title_value);
        detailGrid.addRow(1,description,description_value);
        detailGrid.addRow(2,creator,creator_value);
    }

    @FXML
    private void backHome(ActionEvent actionEvent) {
        //Main.showHome(Main.getUser());
    }
}
