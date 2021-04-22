package controller.post;

import controller.utility.AlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.post.Job;
import model.post.Post;
import model.user.User;
import service.UniLinkConfig;
import service.UniLinkService;
import utility.CustomException;
import utility.InputValidator;

import java.io.File;

// Add Job view controller
public class AddJobViewController {

    private final UniLinkService uniLinkService = UniLinkService.getInstance();
    private final InputValidator inputValidator = new InputValidator();
    private User user;
    private String sourceImage = "";

    @FXML
    private Button btnBackHome, btnAddImage, btnAddPost;

    @FXML
    private ImageView postImage;
    @FXML
    private TextField txtTitle, txtDescription, txtProposedPrice;

    // Init data
    public void initData(User user) {
        this.user = user;
    }

    // Back button handler
    @FXML
    public void btnBackHomeHandler(ActionEvent actionEvent) {
        // System.out.println(this.user.toString());
        Stage stage = (Stage) btnBackHome.getScene().getWindow();
        stage.close();
    }

    // Add post handler
    @FXML
    public void btnAddPostHandler(ActionEvent actionEvent) {
        try {
            if (validateInput()) {
                Job newJob = new Job("TEMP-ID", txtTitle.getText(), txtTitle.getText(), null, user.getID(), Double.parseDouble(txtProposedPrice.getText()));
                Post post = uniLinkService.newJob(newJob);
                //String source = postImage.getImage().getUrl();
                if (post != null) {
                    if (sourceImage.length() > 0) {
                        String postImageName = post.getID() + sourceImage.substring(sourceImage.lastIndexOf("."));
                        String destination = UniLinkConfig.DEFAULT_IMAGE_PATH + postImageName;
                        if (uniLinkService.uploadImage(sourceImage, destination)) {
                            uniLinkService.updateImage(post.getID(), postImageName);
                        }
                    }
                    AlertController.Info("Info", null, "Success! Job has been successfully created with id " + post.getID());
                    Stage stage = (Stage) btnAddPost.getScene().getWindow();
                    stage.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            AlertController.Error("Error", null, ex.toString());
        }
    }

    // Add image handler
    @FXML
    public void btnAddImageHandler(ActionEvent actionEvent) {
        System.out.println("Update image handler");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
            File newFile = fileChooser.showOpenDialog(btnAddImage.getScene().getWindow());
            if (newFile != null && newFile.length() <= UniLinkConfig.MAX_IMAGE_SIZE) {
                Image uploadedImage = new Image(newFile.toURI().toString());
                postImage.setImage(uploadedImage);
                sourceImage = newFile.getAbsolutePath();
            } else {
                throw new CustomException("Please upload valid image (size < 250KB)");
            }
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
    }

    // Validate input handler
    public boolean validateInput() {
        boolean isValid = true;
        //System.out.println(txtDate.getValue().format(UniLinkConfig.DATE_FORMATTER));
        String errorMessage = "";

        try {
            inputValidator.isValidString(txtTitle.getText());
            try {
                inputValidator.isValidString(txtDescription.getText());
                try {
                    inputValidator.isValidNumber(txtProposedPrice.getText(), true);
                } catch (Exception ex) {
                    errorMessage = String.format("%s in %s", ex.toString(), "proposed price");
                }
            } catch (Exception ex) {
                errorMessage = String.format("%s in %s", ex.toString(), "description");
            }
        } catch (Exception ex) {
            errorMessage = String.format("%s in %s", ex.toString(), "Title");
        }
        if (errorMessage.length() > 0) {
            isValid = false;
            AlertController.Error("Error", null, errorMessage);
        }
        return isValid;
    }

}

