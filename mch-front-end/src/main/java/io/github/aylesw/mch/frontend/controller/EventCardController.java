package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import io.github.aylesw.mch.frontend.common.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class EventCardController implements Initializable {

    private Map<String, Object> properties;
    private int state;
    private EventsListController parentController;

    public EventCardController(Map<String, Object> properties, int state, EventsListController parentController) {
        this.properties = properties;
        this.state = state;
        this.parentController = parentController;
    }

    @FXML
    private VBox vBox;

    @FXML
    private Label lblName;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblAge;

    @FXML
    private JFXButton btnRegister;

    @FXML
    private Label lblRegistered;

    @FXML
    private JFXButton btnUnregister;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblName.setText(properties.get("name").toString());
        if (properties.get("description") == null || properties.get("description").toString().isBlank())
            lblDescription.setManaged(false);
        else
            lblDescription.setText("Mô tả: " + properties.get("description").toString());
        lblDate.setText("Ngày diễn ra: %s - %s"
                .formatted(Beans.DATE_FORMAT_CONVERTER.toCustom(properties.get("fromDate").toString()),
                        Beans.DATE_FORMAT_CONVERTER.toCustom(properties.get("toDate").toString())));
        lblAge.setText("Dành cho độ tuổi: %d - %d"
                .formatted(((Double) properties.get("minAge")).longValue(),
                        ((Double) properties.get("maxAge")).longValue()));

        btnRegister.setVisible(false);
        lblRegistered.setVisible(false);
        btnUnregister.setVisible(false);
        switch (state) {
            case 1 -> lblRegistered.setVisible(true);
            case 2 -> btnUnregister.setVisible(true);
            default -> btnRegister.setVisible(true);
        }
    }

    @FXML
    void register(ActionEvent event) {
        try {
            long eventId = ((Double) properties.get("id")).longValue();
            long userId = UserIdentity.getUserId();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId + "/register?user-id=" + userId)
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Đăng ký tham gia sự kiện thành công!");
            parentController.loadEvents();
        } catch (Exception e) {
            if (e.getMessage().contains("age condition")) {
                Utils.showAlert(Alert.AlertType.ERROR, "Bạn không thuộc độ tuổi của sự kiện này!");
            } else {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void unregister(ActionEvent event) {
        try {
            long eventId = ((Double) properties.get("id")).longValue();
            long userId = UserIdentity.getUserId();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId + "/unregister?user-id=" + userId)
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Hủy đăng ký sự kiện thành công.");
            parentController.loadEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
