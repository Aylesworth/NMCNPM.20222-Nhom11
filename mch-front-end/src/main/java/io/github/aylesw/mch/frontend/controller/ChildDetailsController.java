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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
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
    private VBox root;

    @FXML
    private Label lblName;

    @FXML
    private JFXButton btnEditProfile;

    @FXML
    private JFXButton btnDeleteProfile;

    @FXML
    private JFXTextField txtFullName;

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
    private JFXTextField txtSex;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnParentRef;

    @FXML
    private ProgressIndicator spinner1;

    @FXML
    private JFXButton btnAddMetrics;

    @FXML
    private JFXButton btnRequestUpdate;

    @FXML
    private FlowPane metricsContainer;

    @FXML
    private JFXButton btnAddInjection;

    @FXML
    private VBox injectionsContainer;

    @FXML
    private JFXButton btnAddExamination;

    @FXML
    private VBox examinationsContainer;

    @FXML
    private Label lblError;

    @FXML
    private DatePicker dpDob;

    private long parentId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!UserIdentity.getRoles().contains("ADMIN")) {
            btnDeleteProfile.setManaged(false);
            btnEditProfile.setText("Cập nhật hồ sơ");
            btnRequestUpdate.setManaged(false);
            btnAddMetrics.setText("Cập nhật");
            btnAddInjection.setText("Đăng ký tiêm");
            btnAddExamination.setManaged(false);
        }
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        dpDob.setConverter(Beans.DATE_STRING_CONVERTER);
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
        dpDob.setVisible(false);
        txtDob.setVisible(true);
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
        txtDob.setVisible(false);
        dpDob.setVisible(true);
        txtEthnicity.setEditable(true);
        txtBirthplace.setEditable(true);
        txtInsuranceId.setEditable(true);

        btnUpdate.setVisible(true);
        btnCancel.setVisible(true);
    }

    public void loadData() {
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
                txtDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(map.get("dob").toString()));
                txtEthnicity.setText(Optional.ofNullable(map.get("ethnicity")).orElse("").toString());
                txtBirthplace.setText(Optional.ofNullable(map.get("birthplace")).orElse("").toString());
                txtInsuranceId.setText(Optional.ofNullable(map.get("insuranceId")).orElse("").toString());
                lblName.setText("Bé " + txtFullName.getText().toUpperCase());
                parentId = ((Double) map.get("parentId")).longValue();
                btnParentRef.setText(map.get("parentName").toString());

                spinner1.setVisible(false);
            }
        });

        service.start();
    }

    private List<Map<String, Object>> bodyMetricsData;

    void loadBodyMetrics() {
        try {
            bodyMetricsData = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/children/" + id + "/body-metrics")
                    .token(Utils.getToken())
                    .method("GET")
                    .build().request();

            var items = bodyMetricsData.stream().map(map -> ScreenManager.getBodyMetricsItem(map, this)).toList();

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
        ScreenManager.getAddInjectionStage(1, id, this).show();
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
        cbxSex.getSelectionModel().selectFirst();
        while (!cbxSex.getSelectionModel().getSelectedItem().equals(txtSex.getText())) {
            cbxSex.getSelectionModel().selectNext();
        }
        dpDob.setValue(LocalDate.parse(txtDob.getText(), Beans.DATE_FORMATTER));
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
    void showGrowthCharts(ActionEvent event) {
        ScreenManager.getGrowthChartsStage(bodyMetricsData).show();
    }

    @FXML
    void updateProfile(ActionEvent event) {
        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String requestBody = new RequestBodyMap()
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getSelectionModel().getSelectedItem())
                .put("dob", Beans.DATE_FORMAT_CONVERTER.toISO(txtDob.getText()))
                .put("ethnicity", txtEthnicity.getText())
                .put("birthplace", txtBirthplace.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();

        try {
            if (UserIdentity.isAdmin()) {
                new ApiRequest.Builder<String>()
                        .url(AppConstants.BASE_URL + "/children/" + id)
                        .method("PUT")
                        .requestBody(requestBody)
                        .build().request();

                Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật hồ sơ trẻ thành công!");
            } else {
                new ApiRequest.Builder<String>()
                        .url(AppConstants.BASE_URL + "/children/" + id + "/request-change")
                        .method("POST")
                        .requestBody(requestBody)
                        .build().request();

                Utils.showAlert(Alert.AlertType.INFORMATION, "Yêu cầu thay đổi đang chờ được phê duyệt.");
            }
            loadData();
            lockFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtFullName.setStyle("");
        cbxSex.setStyle("");
        dpDob.setStyle("");
        txtEthnicity.setStyle("");
        txtBirthplace.setStyle("");
        txtInsuranceId.setStyle("");

        if (txtFullName.getText().isBlank()) {
            txtFullName.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền họ tên!");
        }
        if (cbxSex.getSelectionModel().getSelectedItem() == null) {
            cbxSex.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn giới tính!");
        }
        try {
            LocalDate.parse(dpDob.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpDob.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày sinh không hợp lệ!");
        }
        if (!txtInsuranceId.getText().isBlank() && !txtInsuranceId.getText().matches("[A-Z]{2}[0-9]{13}")) {
            txtInsuranceId.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số BHYT khỗng hợp lệ!");
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
            case ManageInjectionsController manageInjectionsController -> {
                manageInjectionsController.loadScheduleData();
            }
            case ChildrenListController childrenListController -> {
                childrenListController.loadChildren();
            }
            default -> throw new IllegalStateException("Unexpected value: " + previousController);
        }
        ScreenManager.setMainPanel(previous);
    }

}
