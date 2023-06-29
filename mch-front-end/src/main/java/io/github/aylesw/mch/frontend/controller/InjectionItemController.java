package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class InjectionItemController implements Initializable {
    private Map<String, Object> properties;
    private ChildDetailsController parentController;

    public InjectionItemController(Map<String, Object> properties, ChildDetailsController parentController) {
        this.properties = properties;
        this.parentController = parentController;
    }

    @FXML
    private Label lblDate;

    @FXML
    private Label lblVaccineName;

    @FXML
    private Label lblDoseNo;

    @FXML
    private Label lblNote;

    @FXML
    private Label lblStatus;

    @FXML
    private JFXButton btnHandleReaction;

    @FXML
    private JFXListView<String> listReactions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnHandleReaction.setVisible(false);
        lblDate.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(properties.get("date").toString()));
        lblVaccineName.setText(properties.get("vaccineName").toString());
        lblDoseNo.setText(((Double) properties.get("vaccineDoseNo")).longValue() + "");
        lblNote.setText(Optional.ofNullable(properties.get("note")).orElse("").toString());
        lblStatus.setText(properties.get("status").toString());
        var reactions = (List<String>) properties.get("reactions");
        listReactions.setItems(FXCollections.observableArrayList(reactions));
        listReactions.getSelectionModel().selectedItemProperty().addListener((observale, oldValue, newValue) -> {
            if (newValue != null) {
                btnHandleReaction.setVisible(true);
            }
        });
    }

    @FXML
    void delete(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa thông tin này?");
        if (!buttonType.equals(ButtonType.OK)) return;

        try {
            long childId = ((Double) properties.get("childId")).longValue();
            long id = ((Double) properties.get("id")).longValue();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id)
                    .token(Utils.getToken())
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa thông tin mũi tiêm thành công!");
            parentController.loadInjections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void edit(ActionEvent event) {
        ScreenManager.getEditInjectionStage(properties, parentController).show();
    }

    @FXML
    void handleReaction(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Xử lý triệu chứng");
        dialog.setHeaderText("Vui lòng điền hướng dẫn xử lý triệu chứng:");
        dialog.setContentText("");
        var input = dialog.showAndWait();
        if (input.isEmpty()) return;

        long childId = ((Double) properties.get("childId")).longValue();
        long id = ((Double) properties.get("id")).longValue();
        String reaction = listReactions.getSelectionModel().getSelectedItem();
        String advice = input.get();
        String requestBody = new RequestBodyMap()
                .put("reaction", reaction)
                .put("advice", advice)
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id + "/handle-reaction")
                    .token(Utils.getToken())
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Gửi hướng dẫn xử lý triệu chứng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
