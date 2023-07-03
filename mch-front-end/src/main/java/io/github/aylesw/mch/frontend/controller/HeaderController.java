package io.github.aylesw.mch.frontend.controller;


import io.github.aylesw.mch.frontend.common.UserIdentity;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HeaderController implements Initializable {

    @FXML
    private MenuButton userMenu;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userMenu.setText(UserIdentity.getUserFullName());
    }

    @FXML
    void changePassword(ActionEvent event) {
        ScreenManager.getChangePasswordStage().show();
    }

    @FXML
    void logout(ActionEvent event) {
        Utils.saveToken("");
        ((Stage) userMenu.getScene().getWindow()).close();
        ScreenManager.getLoginStage().show();
    }

    @FXML
    void viewProfile(ActionEvent event) {

    }

}
