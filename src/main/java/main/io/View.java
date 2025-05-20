package main.io;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class View {
    public String prompt(String msg) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ввід");
        dialog.setHeaderText(msg);
        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    public void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    public void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
