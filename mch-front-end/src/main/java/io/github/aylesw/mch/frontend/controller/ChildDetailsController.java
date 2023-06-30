package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class ChildDetailsController implements Initializable {

    private long id;

    private Parent previous;

    private Object previousController;

    ChildDetailsController(long id, Parent previous, Object previousController) {
        this.id = id;
        this.previous = previous;
        this.previousController = previousController;
    }

    @FXML
    private Label lblName;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnParentRef;

    @FXML
    private ProgressIndicator spinner1;

    @FXML
    private FlowPane metricsContainer;

    @FXML
    private JFXButton btnAddMetrics;

    @FXML
    private JFXButton btnRequestUpdate;

    @FXML
    private VBox injectionsContainer;

    @FXML
    private JFXButton addInjection;

    @FXML
    private VBox examinationsContainer;

    @FXML
    private JFXButton btnAddExamination;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtSex;

    @FXML
    private JFXTextField txtDob;

    @FXML
    private JFXTextField txtEthnicity;

    @FXML
    private JFXTextField txtBirthplace;

    @FXML
    private JFXTextField txtInsuranceId;

    @FXML
    private JFXComboBox<String> cbxSex;

    @FXML
    private VBox root;

    private long parentId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        loadData();
        loadBodyMetrics();
        loadInjections();
        loadExaminations();
    }

    private void lockFields() {
        txtFullName.setEditable(false);
        cbxSex.setVisible(false);
        txtSex.setVisible(true);
        txtSex.setEditable(false);
        txtDob.setEditable(false);
        txtEthnicity.setEditable(false);
        txtBirthplace.setEditable(false);
        txtInsuranceId.setEditable(false);

        btnUpdate.setVisible(false);
        btnCancel.setVisible(false);
    }

    private void unlockFields() {
        txtFullName.setEditable(true);
        txtSex.setVisible(false);
        cbxSex.setVisible(true);
        txtDob.setEditable(true);
        txtEthnicity.setEditable(true);
        txtBirthplace.setEditable(true);
        txtInsuranceId.setEditable(true);

        btnUpdate.setVisible(true);
        btnCancel.setVisible(true);
    }

    void loadData() {
        spinner1.setVisible(true);
        lockFields();

        Service<Map<String, Object>> service = new Service<Map<String, Object>>() {
            @Override
            protected Task<Map<String, Object>> createTask() {
                return new Task<Map<String, Object>>() {
                    @Override
                    protected Map<String, Object> call() throws Exception {
                        try {
                            return new ApiRequest.Builder<Map<String, Object>>()
                                    .url(AppConstants.BASE_URL + "/children/" + id)
                                    .token(Utils.getToken())
                                    .method("GET")
                                    .build().request();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            var map = service.getValue();
            if (map != null) {
                txtFullName.setText(map.get("fullName").toString());
                txtSex.setText(map.get("sex").toString());
                cbxSex.getSelectionModel().selectFirst();
                while (!cbxSex.getSelectionModel().getSelectedItem().equals(map.get("sex").toString())) {
                    cbxSex.getSelectionModel().selectNext();
                }
                txtDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(map.get("dob").toString()));
                txtEthnicity.setText(Optional.ofNullable(map.get("ethnicity")).orElse("").toString());
                txtBirthplace.setText(Optional.ofNullable(map.get("birthplace")).orElse("").toString());
                txtInsuranceId.setText(Optional.ofNullable(map.get("insuranceId")).orElse("").toString());
                lblName.setText("Bé " + txtFullName.getText());
                parentId = ((Double) map.get("parentId")).longValue();
                btnParentRef.setText(map.get("parentName").toString());

                spinner1.setVisible(false);
            }
        });

        service.start();
    }

    void loadBodyMetrics() {
        try {
            List<Map<String, Object>> result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/children/" + id + "/body-metrics")
                    .token(Utils.getToken())
                    .method("GET")
                    .build().request();

            var items = result.stream().map(map -> ScreenManager.getBodyMetricsItem(map, this)).toList();

            metricsContainer.getChildren().setAll(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadInjections() {
        try {
            List<Map<String, Object>> result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/children/" + id + "/injections")
                    .token(Utils.getToken())
                    .method("GET")
                    .build().request();

            var items = result.stream().map(map -> ScreenManager.getInjectionItem(map, this)).toList();

            injectionsContainer.getChildren().setAll(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadExaminations() {
        try {
            List<Map<String, Object>> result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/children/" + id + "/examinations")
                    .token(Utils.getToken())
                    .method("GET")
                    .build().request();

            var items = result.stream().map(map -> ScreenManager.getExaminationItem(map, this)).toList();

            examinationsContainer.getChildren().setAll(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addExamination(ActionEvent event) {
        ScreenManager.getAddExaminationStage(id, this).show();
    }

    @FXML
    void addInjection(ActionEvent event) {
        ScreenManager.getAddInjectionStage(id, this).show();
    }

    @FXML
    void addMetrics(ActionEvent event) {
        ScreenManager.getAddBodyMetricsStage(id, this).show();
    }

    @FXML
    void cancel(ActionEvent event) {
        loadData();
    }

    @FXML
    void deleteProfile(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa hồ sơ trẻ này không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + id)
                    .token(Utils.getToken())
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa hồ sơ trẻ thành công!");
            back(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void editProfile(ActionEvent event) {
        unlockFields();
    }

    @FXML
    void requestUpdate(ActionEvent event) {
        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + id + "/body-metrics/request-update")
                    .token(Utils.getToken())
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Yêu cầu cập nhật chiều cao cân nặng thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void updateProfile(ActionEvent event) {
        String requestBody = new RequestBodyMap()
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getSelectionModel().getSelectedItem())
                .put("dob", Beans.DATE_FORMAT_CONVERTER.toISO(txtDob.getText()))
                .put("ethnicity", txtEthnicity.getText())
                .put("birthplace", txtBirthplace.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + id)
                    .token(Utils.getToken())
                    .method("PUT")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật hồ sơ trẻ thành công!");
            loadData();
            lockFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewParentProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getUserDetailsPanel(parentId, root, this));
    }

    @FXML
    void back(ActionEvent event) {

        switch (previousController) {
            case ChildRefItemController childRefItemController -> {
                childRefItemController.updateName(txtFullName.getText());
            }
            case ManageChildrenController manageChildrenController -> {
                manageChildrenController.loadChildrenData();
            }
            default -> throw new IllegalStateException("Unexpected value: " + previousController);
        }
        ScreenManager.setMainPanel(previous);
    }

}
