package controller.post;

import controller.utility.AlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.post.*;
import model.user.User;
import service.UniLinkConfig;
import service.UniLinkService;
import utility.CustomException;
import utility.InputValidator;
import utility.Status;

import java.io.File;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

// Post detail view controller
public class PostDetailViewController {
    private final UniLinkService uniLinkService = UniLinkService.getInstance();
    private final InputValidator inputValidator = new InputValidator();
    private User user;
    private Post post;
    private String responseUnit = UniLinkConfig.RESPONSE_UNIT;

    @FXML
    private Text logoHeaderTitle;
    @FXML
    private Button btnBackHome, btnUpdateImage, btnClosePost, btnDeletePost, btnSavePost;
    @FXML
    private ImageView postImage;
    @FXML
    private GridPane postDetailGrid, replyDetailGrid;
    private int postDetailGridCount = 0, replyDetailGridCount = 0;

    @FXML
    private TextField txtTitle, txtDescription, txtVenue, txtCapacity, txtAskingPrice, txtMinimumRaise, txtProposedPrice;
    @FXML
    private DatePicker txtDate;

    private boolean isCreatorLoggedIn = false; // user = creator?
    private boolean isUpdateImageDisabled = false; // Disable update image?
    private boolean isInputDisabled = false; // input disable?
    private boolean isCloseBtnDisabled = false; // close button disable?
    private boolean isDeleteBtnDisabled = false; // delete button disable?
    private boolean isSaveBtnDisabled = false; // save button disable?

    // Init data
    public void initData(User user, Post post) {
        System.out.println("Post detail init");
        if (post != null) {
            this.user = user;
            this.post = post;
            this.isCreatorLoggedIn = post.getCreatorID().equalsIgnoreCase(user.getID());
            this.isInputDisabled = (post.getStatus() == Status.CLOSED || !this.isCreatorLoggedIn || post.getReplies().size() > 0);
            this.isCloseBtnDisabled = post.getStatus() == Status.CLOSED || !this.isCreatorLoggedIn;
            this.isDeleteBtnDisabled = !this.isCreatorLoggedIn;
            this.isUpdateImageDisabled = isInputDisabled;
            this.isSaveBtnDisabled = isInputDisabled;

            postDetailGrid.getChildren().clear(); // clear detail grod
            postDetailGridCount = 0;

            this.displayImage(); // display post image
            this.displayPostDetail(); // display post detail
            this.displayReplyDetail(); // display reply detail
            // Disable close/save/delete button
            this.disableInput(); // disable input
        } else {
            AlertController.Error("Error", "", "There some issue while displaying details");
        }
    }

    // Display image handler
    private void displayImage() {
        try {
            String imageURL = String.format("file:%s%s", UniLinkConfig.DEFAULT_IMAGE_PATH, (post.getImageName() == null ? UniLinkConfig.DEFAULT_SUMMARY_IMAGE : post.getImageName()));
//            System.out.println(imageURL);
            System.out.println("Image found in detail");
            postImage.setImage(new Image(imageURL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Disable input(s)
    private void disableInput() {
        //if (post.getStatus() == Status.CLOSED || !this.isCreatorLoggedIn) {
        btnClosePost.setDisable(isCloseBtnDisabled);
        btnDeletePost.setDisable(isDeleteBtnDisabled);
        btnSavePost.setDisable(isSaveBtnDisabled);
        btnUpdateImage.setDisable(isUpdateImageDisabled);
        //}
    }

    // Display reply detail handler
    private void displayReplyDetail() {
        replyDetailGrid.getChildren().clear();
        replyDetailGridCount = 0;
        Label no = new Label("No.");
        Label responder = new Label("Responder");
        Label response = new Label("Offer");
        replyDetailGrid.addRow(replyDetailGridCount++, no, responder, response);
        List<Reply> replies = post.getReplies();

        if (post instanceof Sale)
            Collections.sort(replies, new SortReplyDesc());
        else if (post instanceof Job)
            Collections.sort(replies, new SortReply());
        else {
            response.setText("Response");
            responseUnit = "";
            Collections.sort(replies, new SortReply());
        }
        for (Reply tempReply : replies) {
            Text no_value = new Text(replyDetailGridCount + "");
            Text responder_value = new Text(tempReply.getResponderID());
            Text response_value = new Text(responseUnit + tempReply.getValue());
            replyDetailGrid.addRow(replyDetailGridCount++, no_value, responder_value, response_value);
        }
    }

    // Display post detail handler
    private void displayPostDetail() {
        Label title = new Label("Title");
        txtTitle = new TextField(post.getTitle());
        Label description = new Label("Description");
        txtDescription = new TextField(post.getDescription());
        Label creator = new Label("Created By");
        Text txtCreator = new Text(post.getCreatorID());
        Label status = new Label("Status");
        Text txtStatus = new Text(post.getStatus().toString());

        //if (post.getStatus() == Status.CLOSED || !this.isCreatorLoggedIn) {
        txtTitle.setDisable(isInputDisabled);
        txtDescription.setDisable(isInputDisabled);
        txtStatus.setDisable(isInputDisabled);
        // }

        postDetailGrid.addRow(postDetailGridCount++, title, txtTitle);
        postDetailGrid.addRow(postDetailGridCount++, description, txtDescription);
        postDetailGrid.addRow(postDetailGridCount++, creator, txtCreator);
        postDetailGrid.addRow(postDetailGridCount++, status, txtStatus);
        // Check for post type
        if (post instanceof Event)
            this.displayPostDetail((Event) post);
        else if (post instanceof Sale)
            this.displayPostDetail((Sale) post);
        else
            this.displayPostDetail((Job) post);
    }

    // Display event post detail handler
    private void displayPostDetail(Event obj) {
        //  System.out.println(obj.toString());
        logoHeaderTitle.setText("Event Detail");
        Label date = new Label("Date");
        txtDate = new DatePicker(LocalDate.parse(obj.getDate(), UniLinkConfig.DATE_FORMATTER));
        Label venue = new Label("Venue");
        txtVenue = new TextField(obj.getVenue());
        Label capacity = new Label("Capacity");
        txtCapacity = new TextField(String.valueOf(obj.getCapacity()));
        //  if (obj.getStatus() == Status.CLOSED || !obj.getCreatorID().equalsIgnoreCase(user.getID())) {
        txtDate.setDisable(isInputDisabled);
        txtVenue.setDisable(isInputDisabled);
        txtCapacity.setDisable(isInputDisabled);
        //  }
        postDetailGrid.addRow(postDetailGridCount++, date, txtDate);
        postDetailGrid.addRow(postDetailGridCount++, venue, txtVenue);
        postDetailGrid.addRow(postDetailGridCount++, capacity, txtCapacity);
    }

    // Display sale post detail handler
    private void displayPostDetail(Sale obj) {
        //  System.out.println(obj.toString());
        logoHeaderTitle.setText("Sale Detail");
        Label askingPrice = new Label("Asking Price");
        txtAskingPrice = new TextField(responseUnit + obj.getAskingPrice());
        Label minimumRaise = new Label("Minimum Raise");
        txtMinimumRaise = new TextField(responseUnit + obj.getMinimumRaise());
        Label highestOffer = new Label("Highest Offer");
        Text txtHighestOffer = new Text((obj.getReplies().size() == 0 ? "No Offer" : (responseUnit + obj.getHighestOffer())));

        //if (obj.getStatus() == Status.CLOSED || !obj.getCreatorID().equalsIgnoreCase(user.getID())) {
        txtAskingPrice.setDisable(isInputDisabled);
        txtMinimumRaise.setDisable(isInputDisabled);
        //}

        postDetailGrid.addRow(postDetailGridCount++, askingPrice, txtAskingPrice);
        postDetailGrid.addRow(postDetailGridCount++, minimumRaise, txtMinimumRaise);
        postDetailGrid.addRow(postDetailGridCount++, highestOffer, txtHighestOffer);
    }

    // Display job post detail handler
    private void displayPostDetail(Job obj) {
        //  System.out.println(obj.toString());
        logoHeaderTitle.setText("Job Detail");
        Label proposedPrice = new Label("Proposed Price");
        txtProposedPrice = new TextField(responseUnit + obj.getProposedPrice());
        Label lowestOffer = new Label("Lowest Offer");
        Text txtLowestOffer = new Text((obj.getReplies().size() == 0 ? "No Offer" : (responseUnit + obj.getLowestOffer())));

        //if (obj.getStatus() == Status.CLOSED || !obj.getCreatorID().equalsIgnoreCase(user.getID())) {
        txtProposedPrice.setDisable(isInputDisabled);
        //}

        postDetailGrid.addRow(postDetailGridCount++, proposedPrice, txtProposedPrice);
        postDetailGrid.addRow(postDetailGridCount++, lowestOffer, txtLowestOffer);
    }

    // Back button handler
    @FXML
    private void btnBackHomeHandler(ActionEvent actionEvent) {
        //Main.showHome(Main.getUser());
        // System.out.println(this.user.toString());
        Stage stage = (Stage) btnBackHome.getScene().getWindow();
        stage.close();
        //Main.showHome(user);
    }

    // Post close button handler
    @FXML
    private void btnClosePostHandler(ActionEvent actionEvent) {
        System.out.println("Close Post");
        try {
            if (AlertController.Confirm("Are you sure to close post " + post.getID() + " ?")) {
                uniLinkService.closePost(user, post.getID());
                Stage stage = (Stage) btnDeletePost.getScene().getWindow();
                stage.close();
            }
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
    }

    // Post delete button handler
    @FXML
    private void btnDeletePostHandler(ActionEvent actionEvent) {
        System.out.println("Delete Post");
        try {
            if (AlertController.Confirm("Are you sure to delete post " + post.getID() + " ?")) {
                uniLinkService.deletePost(user, post.getID());
                Stage stage = (Stage) btnDeletePost.getScene().getWindow();
                stage.close();
            }
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
    }

    // Post save button handler
    @FXML
    private void btnSavePostHandler(ActionEvent actionEvent) {
        System.out.println("Save Post");
        System.out.println(postDetailGrid.getChildren().get(2).getAccessibleText());
        if (validateInput()) {
            Post updatedPost = post;
            updatedPost.setTitle(txtTitle.getText());
            updatedPost.setImageName(post.getImageName());
            updatedPost.setDescription(txtDescription.getText());
            if (updatedPost instanceof Event) {
                //updatedPost = (Event) updatedPost;
                ((Event) updatedPost).setDate(txtVenue.getText());
                ((Event) updatedPost).setDate(txtDate.getValue().format(UniLinkConfig.DATE_FORMATTER));
                ((Event) updatedPost).setCapacity(Integer.parseInt(txtCapacity.getText()));
                //txtTitle, txtDescription, txtVenue, txtCapacity, txtAskingPrice, txtMinimumRaise, txtProposedPrice;
            } else if (updatedPost instanceof Sale) {
                ((Sale) updatedPost).setAskingPrice(Double.parseDouble(txtAskingPrice.getText().replace(responseUnit,"")));
                ((Sale) updatedPost).setMinimumRaise(Double.parseDouble(txtMinimumRaise.getText().replace(responseUnit,"")));
            } else {
                ((Job) updatedPost).setProposedPrice(Double.parseDouble(txtProposedPrice.getText().replace(responseUnit,"")));
            }
            try {
                uniLinkService.updatePost(updatedPost);
                this.post = updatedPost;
                AlertController.Info("Info", null, "Post detail has been updated:" + this.post.getID());
                Stage stage = (Stage) btnSavePost.getScene().getWindow();
                stage.close();
            } catch (Exception ex) {
                AlertController.Error("Error", null, ex.toString());
            }
        }
    }

    // Post update image button handler
    @FXML
    private void btnUpdateImageHandler(ActionEvent actionEvent) {
        System.out.println("Update image handler");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
            File newFile = fileChooser.showOpenDialog(btnUpdateImage.getScene().getWindow());
            if (newFile != null && newFile.length() <= UniLinkConfig.MAX_IMAGE_SIZE) {
                Image uploadedImage = new Image(newFile.toURI().toString());
                String source = newFile.getAbsolutePath();
                String postImageName = post.getID() + source.substring(source.lastIndexOf("."));
                String destination = UniLinkConfig.DEFAULT_IMAGE_PATH + postImageName;
                if (uniLinkService.uploadImage(source, destination)) {
                    postImage.setImage(uploadedImage);
                    post.setImageName(postImageName);
                    uniLinkService.updateImage(post.getID(), postImageName);
                }
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
                    switch (post.getClass().toString()) {
                        case "Event":
                            validateEventInput();
                            break;
                        case "Sale":
                            validateSaleInput();
                            break;
                        case "Job":
                            validateJobInput();
                            break;
                    }
                } catch (Exception ex) {
                    errorMessage = String.format("%s", ex.toString());
                }
            } catch (Exception ex) {
                errorMessage = String.format("%s in %s", ex.toString(), "description");
            }
        } catch (Exception ex) {
            errorMessage = String.format("%s in %s", ex.toString(), "Title");
        }
        if (errorMessage.length() > 0) {
            AlertController.Error("Error", null, errorMessage);
            isValid = false;
        }
        // isValid = true;
        return isValid;
    }

    // Validate event related input
    private void validateEventInput() throws CustomException {
        try {
            inputValidator.isValidString(txtVenue.getText());
            try {
                inputValidator.isValidDate(txtDate.getValue().format(UniLinkConfig.DATE_FORMATTER), true);
                try {
                    inputValidator.isValidNumber(txtCapacity.getText(), true);
                } catch (Exception ex) {
                    throw new CustomException(String.format("%s in %s", ex.toString(), "capacity"));
                }
            } catch (Exception ex) {
                throw new CustomException(String.format("%s in %s", ex.toString(), "date"));
            }
        } catch (Exception ex) {
            throw new CustomException(String.format("%s in %s", ex.toString(), "venue"));
        }
    }

    // Validate sale related input
    private void validateSaleInput() throws CustomException {
        try {
            inputValidator.isValidNumber(txtAskingPrice.getText(), true);
            try {
                inputValidator.isValidNumber(txtMinimumRaise.getText().replace(responseUnit,""), true);
                Double askingPrice = Double.parseDouble(txtAskingPrice.getText().replace(responseUnit,""));
                Double minimumRaise = Double.parseDouble(txtMinimumRaise.getText().replace(responseUnit,""));
                if (minimumRaise >= askingPrice) {
                    System.out.println("Minimum raise needs to be less than asking price");
                    throw new CustomException("Minimum raise needs to be less than asking price");
                }
            } catch (Exception ex) {
                throw new CustomException(String.format("%s in %s", ex.toString(), "minimum raise"));
            }
        } catch (Exception ex) {
            throw new CustomException(String.format("%s in %s", ex.toString(), "asking price"));
        }
    }

    // Validate job related input
    private void validateJobInput() throws CustomException {
        try {
            inputValidator.isValidNumber(txtProposedPrice.getText().replace(responseUnit,""), true);
        } catch (Exception ex) {
            throw new CustomException(String.format("%s in %s", ex.toString(), "proposed price"));
        }
    }

}
