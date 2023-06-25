package io.github.aylesw.mch.frontend.common;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.prefs.Preferences;

public class Utils {
    public static String getToken() {
        Preferences preferences = Preferences.userRoot().node("io.github.aylesw.mch.frontend");
        return preferences.get("TOKEN", null);
    }

    public static void saveToken(String token) {
        Preferences preferences = Preferences.userRoot().node("io.github.aylesw.mch.frontend");
        preferences.put("TOKEN", token);
    }

    public static ButtonType showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult();
    }

}
