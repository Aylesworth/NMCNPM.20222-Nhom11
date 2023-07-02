package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class NavBarController implements Initializable {

    private long userId;

    public NavBarController(long userId) {
        this.userId = userId;
    }

    @FXML
    private Label lblNewNotiCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateNotiCount();
    }

    private void updateNotiCount() {
        try {
            var result = new ApiRequest.Builder<Map<String, Object>>()
                    .url(AppConstants.BASE_URL + "/users/" + userId + "/notifications/count-new")
                    .method("GET")
                    .build().request();
            long count = ((Double) result.get("count")).longValue();

            if (count > 0) {
                lblNewNotiCount.setVisible(true);
                lblNewNotiCount.setText(" " + count + " ");
            } else {
                lblNewNotiCount.setVisible(false);
                lblNewNotiCount.setManaged(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void children(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getChildrenListPanel(userId));
        updateNotiCount();
    }

    @FXML
    void events(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getEventsListPanel(userId));
        updateNotiCount();
    }

    @FXML
    void home(ActionEvent event) {
        updateNotiCount();
    }

    @FXML
    void myProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getUserDetailsPanel(userId, null, null));
        updateNotiCount();
    }

    @FXML
    void notifications(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getNotificationsPanel(userId));
        updateNotiCount();
    }

}
