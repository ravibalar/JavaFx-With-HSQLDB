package controller;

import controller.post.AddEventViewController;
import controller.post.AddJobViewController;
import controller.post.AddSaleViewController;
import controller.utility.AlertController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import model.post.Post;
import model.user.User;
import service.UniLinkConfig;
import service.UniLinkService;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

// Home tab controller view
public class HomeTabController implements Initializable {
    private final UniLinkService uniLinkService = UniLinkService.getInstance();
    private final String styleSheet = Main.class.getResource("../resource/css/style.css").toExternalForm();
    @FXML
    ChoiceBox postTypeFilter, statusFilter, creatorFilter;
    private User user;
    @FXML
    private HomeViewController homeViewController;
    private String postTypeSelect = "All";
    private String statusSelect = "All";
    private String creatorSelect = "All";
    @FXML
    private BorderPane tabContent;
    @FXML
    private ListView postListView;

    // Set parent controller
    public void setParent(HomeViewController homeViewController) {
        this.homeViewController = homeViewController;
    }

    // Init data
    public void initData(User user) {
        System.out.println("Hometab init");
        //System.out.println(user.getID());
        this.user = user;
        postTypeFilterHandler(); // post type filter handler
        statusFilterHandler(); // status filter handler
        creatorFilterHandler(); // creator filter handler
        refresh();
    }

    // post type filter handler
    public void postTypeFilterHandler() {
        postTypeFilter.setItems(FXCollections.observableArrayList(UniLinkConfig.POST_TYPE_FILTER));
        // Post Type filter set
        postTypeFilter.getSelectionModel().selectFirst();
        postTypeFilter.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String selectedPostType = (String) postTypeFilter.getItems().get((Integer) number2);
                postTypeSelect = selectedPostType;
                refresh();
            }
        });
    }

    // status filter handler
    public void statusFilterHandler() {
        statusFilter.setItems(FXCollections.observableArrayList(UniLinkConfig.STATUS_FILTER));
        // Status filter set
        statusFilter.getSelectionModel().selectFirst();
        statusFilter.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String selectedStatus = (String) statusFilter.getItems().get((Integer) number2);
                statusSelect = selectedStatus;
                refresh();
            }
        });
    }

    // creator filter handler
    public void creatorFilterHandler() {
        creatorFilter.setItems(FXCollections.observableArrayList(UniLinkConfig.USER_FILTER));
        // Creator filter set
        creatorFilter.getSelectionModel().selectFirst();
        creatorFilter.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                String selectedCreator = (String) creatorFilter.getItems().get((Integer) number2);
                creatorSelect = selectedCreator;
                refresh();
            }
        });
    }

    // refresh list view
    public void refresh() {
        displayPost(true);
    }

    // Display post handler depend on filter
    public void displayPost(boolean isFilter) {
        System.out.println("Display type" + postTypeSelect + " " + statusSelect + " " + creatorSelect);
        Map<String, Post> postListFilter = null;
        switch (creatorSelect.toUpperCase()) {
            case "ALL":
                postListFilter = uniLinkService.getAllPost();
                break;
            case "MY":
                postListFilter = uniLinkService.getPostByUser(user);
                break;
        }

        postListView.getItems().clear(); // clear post list view
        //  System.out.println(postList.isEmpty());
        if (postListFilter == null || postListFilter.size() == 0) {
            AlertController.Info("Info", null, "No post to display");
            // postListView.getItems().add(new Label("No post to display"));
        } else {
            //  Map<String, Post> postListFilter = postList;
            if (!postTypeSelect.equalsIgnoreCase("All"))
                postListFilter = postListFilter.entrySet().stream().filter(map -> map.getValue().getClass().toString().contains(postTypeSelect))
                        .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
            if (!statusSelect.equalsIgnoreCase("All"))
                postListFilter = postListFilter.entrySet().stream().filter(map -> map.getValue().getStatus().toString().equalsIgnoreCase(statusSelect))
                        .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue()));
            if (postListFilter == null || postListFilter.size() == 0) {
                AlertController.Error("Error", null, "No post to display");
            } else {
                for (Map.Entry<String, Post> postSet : postListFilter.entrySet()) {
                    Post tempPost = postSet.getValue();
                    //  System.out.println(tempPost.toString());
                    displayPost(tempPost);
                }
            }
        }
    }

    // display by post summary
    private void displayPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/postsummary_view.fxml"));
            AnchorPane postSummary = loader.load();
            PostSummaryViewController postSummaryViewController = loader.getController();
            postSummaryViewController.setParent(this);
            postSummaryViewController.initData(user, post);
            //postViewGrid.addRow(postViewGrid.getRowCount(), postSummary);
            postListView.getItems().add(postSummary);
        } catch (IOException ex) {
            AlertController.Error("Error", null, "There is some issue while displaying post");
        }
    }

    // Add event handler
    @FXML
    private void addEventHandler(ActionEvent actionEvent) {
        System.out.println("Add New event");
        try {
            FXMLLoader loader = new FXMLLoader();
            System.out.println(getClass().getName());
            loader.setLocation(Main.class.getResource("../view/addevent_view.fxml"));
            Parent detailView = loader.load();
            AddEventViewController addEventController = loader.getController();
            addEventController.initData(user);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.addPostViewHandler(window, new Scene(detailView));
        } catch (Exception ex) {
            AlertController.Error("Error", null, "There is some issue while add Event post");
            ex.printStackTrace();
        }
    }

    // Add Sale handler
    @FXML
    private void addSaleHandler(ActionEvent actionEvent) {
        System.out.println("Add New Sale");
        try {
            FXMLLoader loader = new FXMLLoader();
            System.out.println(getClass().getName());
            loader.setLocation(Main.class.getResource("../view/addsale_view.fxml"));
            Parent detailView = loader.load();
            AddSaleViewController addSaleController = loader.getController();
            addSaleController.initData(user);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.addPostViewHandler(window, new Scene(detailView));
        } catch (IOException ex) {
            AlertController.Error("Error", null, "There is some issue while add Sale post");
            ex.printStackTrace();
        }
    }

    // Add job handler
    @FXML
    private void addJobHandler(ActionEvent actionEvent) {
        System.out.println("Add New job");
        // System.out.println(getClass().getName());
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/addjob_view.fxml"));
            Parent detailView = loader.load();
            AddJobViewController addJobController = loader.getController();
            addJobController.initData(user);
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.addPostViewHandler(window, new Scene(detailView));
        } catch (Exception ex) {
            AlertController.Error("Error", null, "There is some issue while add Job post");
            ex.printStackTrace();
        }
    }

    // Add post handler
    public void addPostViewHandler(Stage window, Scene detailView) {
        try {
            //  Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Stage viewStage = new Stage();
            viewStage.setScene(detailView);
            viewStage.getScene().getStylesheets().add(this.styleSheet);
            viewStage.initOwner(window);
            viewStage.initModality(Modality.APPLICATION_MODAL);
            viewStage.showAndWait();
            refresh();
        } catch (Exception ex) {
            // AlertController.Error("Error", null, "There is some issue while add Job post");
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Hometab initialize");
    }
}
