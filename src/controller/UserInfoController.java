package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.user.User;
import service.UniLinkConfig;

// User info controller
public class UserInfoController {
    @FXML
    private ImageView userImage;
    @FXML
    private GridPane userDetailGrid;
    private int userDetailGridCount = 0;

    // Init data of user info
    public void initData(User user) {
        System.out.println(user.toString());
        try {
            String imageURL = String.format("file:%s%s", UniLinkConfig.DEFAULT_IMAGE_PATH, "user.png");
            // System.out.println(imageURL);
            System.out.println("Image found in detail");
            userImage.setImage(new Image(imageURL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        userDetailGrid.getChildren().clear(); // clear user detail grid
        Label title = new Label("StudentID");
        Label title_value = new Label("balar025");
        Label description = new Label("Name");
        Label description_value = new Label("Ravikumar Balar");
        userDetailGrid.addRow(userDetailGridCount++, title, title_value);
        userDetailGrid.addRow(userDetailGridCount++, description, description_value);
    }
}
