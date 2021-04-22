package controller;

import controller.utility.AlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;
import model.user.User;
import service.UniLinkService;

import java.net.URL;
import java.util.ResourceBundle;

// Home view controller
public class HomeViewController implements Initializable {
    private final UniLinkService uniLinkService = UniLinkService.getInstance();
    private Main main;
    private User currentUser = null;
    private Stage stage;

    @FXML
    private AnchorPane menuBar;
    @FXML
    private MenuBarController menuBarController;
    @FXML
    private HomeTabController homeTabController;
    @FXML
    private Label txtWelcome;

    // init data
    public void initData(User user) {
        System.out.println("Home view init");
        System.out.println(user.getID());
        this.currentUser = user;
        menuBarController.setParent(this);
        menuBarController.initData(currentUser);
        homeTabController.setParent(this);
        homeTabController.initData(currentUser);
        txtWelcome.setText("Welcome " + currentUser.getID());
        // selectionModel.select(homeTab);
    }

    // refresh the listview
    public void refresh() {
        homeTabController.refresh();
    }

    // Logout button handler
    @FXML
    private void btnLogoutHandler(ActionEvent actionEvent) {
        // db.closeConnection();
        this.quit();
    }

    // Quit the application
    public void quit() {
        // db.closeConnection();
        try {
            // Save data to db
            uniLinkService.saveData();
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
        Main.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Home view initialize");
    }
}
