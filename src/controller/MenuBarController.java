package controller;

import controller.utility.AlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.user.User;
import service.UniLinkService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Menu bar controller
public class MenuBarController implements Initializable {
    private final UniLinkService uniLinkService = UniLinkService.getInstance();
    private final String styleSheet = Main.class.getResource("../resource/css/style.css").toExternalForm();
    private Main main;
    private User user = null;
    @FXML
    private HomeViewController homeViewController;

    @FXML
    private MenuItem menuUserInfo, menuQuit, menuExport, menuImport;

    // Set parent controller
    public void setParent(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }

    // Init data
    public void initData(User user) {
        System.out.println("Menu bar init");
        //  System.out.println(user.getID());
        this.user = user;
    }

    @FXML
    private void menuUserInfoHandler(ActionEvent actionEvent) {
        System.out.println(user.getID());
        if (user != null) {
            // System.out.println(postTableView.getSelectionModel().getSelectedItem().toString());
            FXMLLoader loader = new FXMLLoader();
            System.out.println(getClass().getName());
            loader.setLocation(getClass().getResource("../view/userinfo_view.fxml"));
            try {
                Parent detailView = loader.load();
                UserInfoController userInfoController = loader.getController();
                userInfoController.initData(user);
                //Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Stage viewStage = new Stage();
                viewStage.setScene(new Scene(detailView));
                viewStage.getScene().getStylesheets().add(this.styleSheet);
                //viewStage.initOwner();
                viewStage.initModality(Modality.APPLICATION_MODAL);
                viewStage.showAndWait();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            AlertController.Error("Error", null, "There is issue while rendering user detail");
        }

    }

    // Menu quit handler
    @FXML
    private void menuQuitHandler(ActionEvent actionEvent) {
        // db.closeConnection();
        this.homeViewController.quit();
    }

    // Menu export handler
    @FXML
    private void menuExportHandler(ActionEvent actionEvent) {
        System.out.println("Menu Export");
        try {
            // directory chooser
            DirectoryChooser dirChooser = new DirectoryChooser();
            //Show save file dialog
            File selectedDirPath = dirChooser.showDialog(menuExport.getParentPopup().getScene().getWindow());//.getAbsolutePath();
            if (selectedDirPath != null) {
                // Create file export_data file in selected directory
                File file = new File(selectedDirPath.getAbsolutePath() + "/export_data.txt");
                System.out.println(file.toURI());
                try {
                    FileWriter fileWriter = null;
                    fileWriter = new FileWriter(file);
                    // uniLinkService.exportData();
                    fileWriter.write(uniLinkService.exportData()); // Write export data into file
                    fileWriter.close();
                    AlertController.Info("Info", null, "Data exported successfully");
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                    throw new Exception(ex.toString());
                }
            } else {
                throw new Exception("Issue while exporting data");
            }
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
    }

    // Menu import handler
    @FXML
    private void menuImportHandler(ActionEvent actionEvent) {
        System.out.println("Menu Import");
        try {
            // File chooser txt files
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            //Show save file dialog
            File file = fileChooser.showOpenDialog(menuExport.getParentPopup().getScene().getWindow());//.getAbsolutePath();

            if (file != null) {
                System.out.println(file.toURI());
                try {
                    uniLinkService.importData(user, file); // import data from choose file
                    AlertController.Info("Info", null, "Data imported successfully");
                    homeViewController.refresh();
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    throw new Exception(ex.toString());
                    //AlertController.Error("Error", null, "There is some error while importing data");
                }
            } else {
                throw new Exception("Issue while importing file");
            }
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Menubar Initialize");
    }

}
