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

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ExaminationItemController implements Initializable {

    private Map<String, Object> properties;

    private ChildDetailsController parentController;

    public ExaminationItemController(Map<String, Object> properties, ChildDetailsController parentController) {
        this.properties = properties;
        this.parentController = parentController;
    }

    @FXML
    private Label lblDate;

    @FXML
    private Label lblReason;

    @FXML
    private Label lblNote;

    @FXML
    private Label lblFacility;

    @FXML
    private Label lblResult;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXListView<String> listMedicines;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!UserIdentity.isAdmin()) {
            btnEdit.setVisible(false);
            btnEdit.setManaged(false);
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
        }
        lblDate.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(properties.get("date").toString()));
        lblFacility.setText(properties.get("facility").toString());
        lblReason.setText(properties.get("reason").toString());
        lblResult.setText(properties.get("result").toString());
        lblNote.setText(Optional.ofNullable(properties.get("note")).orElse("").toString());

        var medicines = FXCollections.observableArrayList((List<String>) properties.get("medicines"));
        listMedicines.setItems(medicines);
    }

    @FXML
    void delete(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa thông tin này không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        long childId = ((Double) properties.get("childId")).longValue();
        long id = ((Double) properties.get("id")).longValue();
        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/examinations/" + id)
                    .token(Utils.getToken())
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa thông tin khám chữa bệnh thành công!");
            parentController.loadExaminations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void edit(ActionEvent event) {
        ScreenManager.getEditExaminationStage(properties, parentController).show();
    }

}
