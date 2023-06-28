package io.github.aylesw.mch.frontend.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AdminNavBarController {

    @FXML
    void children(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getManageChildrenPanel());
    }

    @FXML
    void events(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getManageEventsPanel());
    }

    @FXML
    void home(ActionEvent event) {
        ScreenManager.setMainPanel(null);
    }

    @FXML
    void injections(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getManageInjectionsPanel());
    }

    @FXML
    void notifications(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getNotificationsPanel());
    }

    @FXML
    void users(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getManageUsersPanel());
    }

}
