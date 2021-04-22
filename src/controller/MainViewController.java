package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import main.Main;
import model.user.User;
import service.UniLinkService;

// Main view handler
public class MainViewController {
    private Main main;
    private final UniLinkService uniLinkService = UniLinkService.getInstance();

    @FXML
    private Label successLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField txtUsername;
    @FXML
    private Button btnStart;

    @FXML
    private void initialize() {
        successLabel.setText("");
        errorLabel.setText("");
        txtUsername.setText("");
        Tooltip tooltip = new Tooltip();
        tooltip.setText("User name must be SXXXXXX");
        txtUsername.setTooltip(tooltip);
    }

    // Login button handler
    @FXML
    private void btnLoginHandler(ActionEvent actionEvent) {
        errorLabel.setText("");
        successLabel.setText("");
        if (txtUsername.getText().equals("")) {
            errorLabel.setText("Please enter username");
        } else if (!uniLinkService.userLogin(txtUsername.getText())) {
            errorLabel.setText("Please enter valid username(S123456)");
        } else {
            successLabel.setText("Success");
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            User user = new User(txtUsername.getText());
            Main.showHome(user);
        }
    }
}
