package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddUserToEventController implements Initializable {

    private long eventId;

    private ManageEventsController parentController;

    public AddUserToEventController(long eventId, ManageEventsController parentController) {
        this.eventId = eventId;
        this.parentController = parentController;
    }

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private Label lblError;

    @FXML
    private JFXListView<Map<String, Object>> listUsers;

    private ObservableList<Map<String, Object>> users;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        users = FXCollections.observableArrayList();
        listUsers.setItems(users);

        listUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listUsers.setCellFactory(view -> new JFXListCell<>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText("%s (%s)".formatted(item.get("fullName").toString(), item.get("email").toString()));
                    setPrefWidth(220);
                    setWrapText(true);
                }
            }
        });

        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/users")
                    .method("GET")
                    .build().request();
            users.setAll(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void add(ActionEvent event) {
        lblError.setText("");
        if (listUsers.getSelectionModel().getSelectedItem()==null) {
            lblError.setText("Bạn chưa chọn người dùng nào!");
            return;
        }

        try {
            long userId = ((Double) listUsers.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId + "/register?user-id=" + userId)
                    .method("POST")
                    .build().request();

            ((Stage) txtSearch.getScene().getWindow()).close();
            parentController.loadParticipants(eventId);
        } catch (Exception e) {
            if (e.getMessage().contains("age condition")) {
                lblError.setText("Người dùng không thuộc độ tuổi của sự kiện này!");
            } else {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtSearch.getScene().getWindow()).close();
    }

    @FXML
    void search(KeyEvent event) {
        listUsers.setItems(users.filtered(u ->
                u.get("fullName").toString().toLowerCase().contains(txtSearch.getText().toLowerCase()) ||
                        u.get("email").toString().toLowerCase().contains(txtSearch.getText().toLowerCase())));
    }

}
