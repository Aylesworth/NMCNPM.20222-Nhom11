package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.Main;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class NotificationsController implements Initializable {

    @FXML
    private VBox notificationList;

    @FXML
    private ProgressIndicator spinner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spinner.setVisible(true);

        Service<List<Parent>> service = new Service<>() {
            @Override
            protected Task<List<Parent>> createTask() {
                return new Task<>() {
                    @Override
                    protected List<Parent> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(AppConstants.BASE_URL + "/users/" + Main.getUserId() + "/notifications")
                                    .token(Utils.getToken())
                                    .method("GET")
                                    .build().request();

                            return result.stream().map(ScreenManager::getNotificationItem).toList();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            if (service.getValue() != null) {
                spinner.setVisible(false);
                notificationList.getChildren().setAll(service.getValue());
            }
        });

        service.start();
    }
}
