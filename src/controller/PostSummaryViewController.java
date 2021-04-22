package controller;

import controller.post.PostDetailViewController;
import controller.utility.AlertController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.post.*;
import model.user.User;
import service.UniLinkConfig;
import service.UniLinkService;
import utility.CapacityException;
import utility.CustomException;
import utility.PostClosedException;
import utility.Status;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

// Post summary view controller
public class PostSummaryViewController implements Initializable {
    private final UniLinkService uniLinkService = UniLinkService.getInstance();
    private final String styleSheet = Main.class.getResource("../resource/css/style.css").toExternalForm();
    private String responseUnit = UniLinkConfig.RESPONSE_UNIT;
    private User user;
    private Post post;
    private boolean isCreatorLoggedIn = false;
    @FXML
    private HBox postSummary;
    @FXML
    private ImageView postImage;

    @FXML
    private GridPane postSummaryGrid;
    private int postSummaryGridCount = 0;

    @FXML
    private Button btnReplyPost, btnMoreDetail;
    @FXML
    private HomeTabController homeTabController;

    // Set parent controller
    public void setParent(HomeTabController homeTabController) {
        this.homeTabController = homeTabController;
    }

    // Init data
    public void initData(User user, Post post) {
        System.out.println("PostSummary init");
//        System.out.println(user.getID());
        // Check post
        if (post != null && user != null) {
            //  System.out.println(post.toString());
            try {
                this.user = user;
                this.post = post;
                this.isCreatorLoggedIn = post.getCreatorID().equalsIgnoreCase(user.getID());
                postSummaryGrid.getChildren().clear();
                postSummaryGridCount = 0;

                displayImage(); // Display image
                displayPostSummary(); // display post summary

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            if (post == null)
                System.out.println("Post is Not set");
            if (user == null)
                System.out.println("User is Not set");
            postSummary.getChildren().clear();
        }
    }

    // Display image
    private void displayImage() {
        try {
            String imageName = (post.getImageName() == null || post.getImageName().equalsIgnoreCase("") ? UniLinkConfig.DEFAULT_SUMMARY_IMAGE : post.getImageName());
            String imageURL = String.format("file:%s%s", UniLinkConfig.DEFAULT_IMAGE_PATH, imageName);
            // System.out.println(imageURL);
            postImage.setImage(new Image(imageURL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Display post summary
    private void displayPostSummary() {
        Label id = new Label("PostId");
        Text id_value = new Text(post.getID());
        Label title = new Label("Title");
        Text title_value = new Text(post.getTitle());
        Label description = new Label("Description");
        Text description_value = new Text(post.getDescription());
        Label creator = new Label("Created By");
        Text creator_value = new Text(post.getCreatorID());
        Label status = new Label("Status");
        Text status_value = new Text(post.getStatus().toString());

        postSummaryGrid.addRow(postSummaryGridCount++, id, id_value);
        postSummaryGrid.addRow(postSummaryGridCount++, title, title_value);
        postSummaryGrid.addRow(postSummaryGridCount++, description, description_value);
        postSummaryGrid.addRow(postSummaryGridCount++, creator, creator_value);
        postSummaryGrid.addRow(postSummaryGridCount++, status, status_value);

        // Check for post type
        if (post instanceof Event) {
            this.displayPostSummary((Event) post);
            postSummary.setBackground(new Background(new BackgroundFill(UniLinkConfig.EVENT_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        } else if (post instanceof Sale) {
            this.displayPostSummary((Sale) post);
            postSummary.setBackground(new Background(new BackgroundFill(UniLinkConfig.SALE_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            this.displayPostSummary((Job) post);
            postSummary.setBackground(new Background(new BackgroundFill(UniLinkConfig.JOB_BG, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        if (post.getStatus() == Status.CLOSED || this.isCreatorLoggedIn) {
            btnReplyPost.setDisable(true);
        }
        if (!this.isCreatorLoggedIn) {
            btnMoreDetail.setDisable(true);
        }
    }

    // Display Event summary
    private void displayPostSummary(Event obj) {
        //  System.out.println(obj.toString());
        Label date = new Label("Date");
        Text date_value = new Text(obj.getDate());
        Label venue = new Label("Venue");
        Text venue_value = new Text(obj.getVenue());
        Label capacity = new Label("Capacity");
        Text capacity_value = new Text(String.valueOf(obj.getCapacity()));
        Label noOfAttendee = new Label("Attendee Count");
        Text noOfAttendee_value = new Text(String.valueOf(post.getReplies().size()));

        btnReplyPost.setText("Join");

        postSummaryGrid.addRow(postSummaryGridCount++, date, date_value);
        postSummaryGrid.addRow(postSummaryGridCount++, venue, venue_value);
        postSummaryGrid.addRow(postSummaryGridCount++, capacity, capacity_value);
        postSummaryGrid.addRow(postSummaryGridCount++, noOfAttendee, noOfAttendee_value);
    }

    // Display Sale summary
    private void displayPostSummary(Sale obj) {
        //  System.out.println(obj.toString());
        Label askingPrice = new Label("Asking Price");
        Text askingPrice_value = new Text(responseUnit + obj.getAskingPrice());
        Label minimumRaise = new Label("Minimum Raise");
        Text minimumRaise_value = new Text(responseUnit + obj.getMinimumRaise());
        Label highestOffer = new Label("Highest Offer");
        Text highestOffer_value = new Text((obj.getReplies().size() == 0 ? "No Offer" : (responseUnit + obj.getHighestOffer())));


        if (this.isCreatorLoggedIn)
            postSummaryGrid.addRow(postSummaryGridCount++, askingPrice, askingPrice_value);
        postSummaryGrid.addRow(postSummaryGridCount++, minimumRaise, minimumRaise_value);
        postSummaryGrid.addRow(postSummaryGridCount++, highestOffer, highestOffer_value);
    }

    // Display Job summary
    private void displayPostSummary(Job obj) {
        //  System.out.println(obj.toString());
        Label proposedPrice = new Label("Proposed Price");
        Text proposedPrice_value = new Text(responseUnit + obj.getProposedPrice());
        Label lowestOffer = new Label("Lowest Offer");
        Text lowestOffer_value = new Text((obj.getReplies().size() == 0 ? "No Offer" : (responseUnit + obj.getLowestOffer())));

        postSummaryGrid.addRow(postSummaryGridCount++, proposedPrice, proposedPrice_value);
        postSummaryGrid.addRow(postSummaryGridCount++, lowestOffer, lowestOffer_value);
    }


    // Reply button handler
    @FXML
    private void btnReplyPostHandler(ActionEvent actionEvent) {
        try {
            // Check for post close
            if (post.getStatus() == Status.CLOSED) {
                throw new PostClosedException("Post is closed");
            }
            boolean isValid = false;
            double replyValue = -1;
            // Check for post type
            if (post instanceof Event) { // Check for replying post type is event?
                if (post.getReplies().size() == ((Event) post).getCapacity()) { // Check if event is full
                    throw new CapacityException("Event is full");
                }
                isValid = AlertController.Confirm("Do you want to join event?");
                replyValue = isValid ? 1 : -1;
            } else { // for other than event
                TextInputDialog td = new TextInputDialog();
                td.setTitle("Reply to post");
                td.setHeaderText(post.getPostMiniDetails() + "\n" + "Enter your offer");
                final Button okButton = (Button) td.getDialogPane().lookupButton(ButtonType.OK);
                okButton.addEventFilter(ActionEvent.ACTION, ae -> {
                    if (!isValidReply(td.getEditor().getText())) { // Check for valid input
                        //System.out.println(td.getEditor().getText());
                        ae.consume(); //not valid
                    }
                });
                Optional<String> result = td.showAndWait();
                if (result.isPresent()) {
                    isValid = true;
                    replyValue = Double.parseDouble(td.getEditor().getText());
                }
            }

            if (isValid) {
                //double replyValue = Double.parseDouble(td.getEditor().getText());
                if (uniLinkService.addReply(user, new Reply(post.getID(), replyValue, user.getID()))) {
                    AlertController.Info("Info", null, "Offer accepted for post " + post.getID());
                    System.out.println("After successful reply, Reinitiliaze summary");
                    initData(user, uniLinkService.getPostById(post.getID()));
                } else {
                    throw new CustomException("There is some issue with replying to post");
                }
            }
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
    }

    // Check reply valid?
    private boolean isValidReply(String replyValueStr) {
        // Check for reply value or Q
        try {
            double replyValue = -1;
            if (replyValueStr != null && replyValueStr.length() > 0
                    && !replyValueStr.equalsIgnoreCase("Q")) {
                try { // Convert string to double
                    replyValue = Double.parseDouble(replyValueStr);
                } catch (Exception ex) { // Default exception throw
                    replyValue = -1;
                }

                if (replyValue < 0) { // Check positive value
                    System.out.println("Enter only positive number");
                    throw new CustomException("Enter only positive number");
                } else if (replyValue + 1 != 2 && post instanceof Event) {
                    // Check if reply value is 1 and post type is event
                    System.out.println("Please Enter event reply with 1");
                    throw new CustomException("Please Enter event reply with 1");
                } else { // Create reply object
                    return true;
                }
            } else {
                if (post instanceof Event) { // Check for replying post type is event?
                    throw new CustomException("Enter your reply 1");
                } else { // for other than event
                    throw new CustomException("Enter your valid offer");
                }
            }
        } catch (Exception ex) {
            AlertController.Error("Error", null, ex.toString());
        }
        return false;
    }

    // More detail button handler
    @FXML
    private void btnMoreDetailHandler(ActionEvent actionEvent) {
        if (post != null) {
            // System.out.println(postTableView.getSelectionModel().getSelectedItem().toString());
            FXMLLoader loader = new FXMLLoader();
            // System.out.println(getClass().getName());
            loader.setLocation(Main.class.getResource("../view/postdetail_view.fxml"));
            try {
                Parent detailView = loader.load();
                PostDetailViewController eventDetailViewController = loader.getController();
                eventDetailViewController.initData(user, post);
                Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Stage viewStage = new Stage();
                viewStage.setScene(new Scene(detailView));
                viewStage.getScene().getStylesheets().add(this.styleSheet);
                viewStage.initOwner(window);
                viewStage.initModality(Modality.APPLICATION_MODAL);
                viewStage.showAndWait();
                homeTabController.refresh(); // refresh list view after post detail close
                //initData(user, uniLinkService.getPostById(post.getID()));
            } catch (Exception ex) {
                AlertController.Error("Error", null, ex.toString());
            }
        } else {
            AlertController.Info("Info", null, "At least select one row from table");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
