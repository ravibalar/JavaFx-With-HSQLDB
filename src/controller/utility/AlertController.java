package controller.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertController {

    public static void Info(String title, String header, String body){
        Common(Alert.AlertType.INFORMATION, title, header, body);
    }

    public static void Warning(String title, String header, String body){
        Common(Alert.AlertType.WARNING, title, header, body);
    }

    public static void Error(String title, String header, String body){
        Common(Alert.AlertType.ERROR, title, header, body);
    }
    public static boolean Confirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            return true;
        }
        return false;
    }

    private static void Common(Alert.AlertType type, String title, String header, String body){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    }
}
