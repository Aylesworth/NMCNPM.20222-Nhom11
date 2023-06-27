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
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class UserDetailsController implements Initializable {

    private long id;

    private Parent previous;

    private ManageUsersController previousController;

    public UserDetailsController(long id, Parent previous, ManageUsersController previousController) {
        this.id = id;
        this.previous = previous;
        this.previousController = previousController;
    }

    @FXML
    private Label lblName;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtDob;

    @FXML
    private JFXComboBox<String> cbxSex;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCitizenId;

    @FXML
    private JFXTextField txtInsuranceId;

    @FXML
    private JFXTextField txtSex;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private FlowPane childrenPane;

    @FXML
    private VBox eventsPane;

    @FXML
    private ProgressIndicator spinner1;

    @FXML
    private ProgressIndicator spinner2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        loadChildrenList();
    }

    private void lockFields() {
        txtEmail.setEditable(false);
        txtFullName.setEditable(false);
        txtSex.setEditable(false);
        txtDob.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtAddress.setEditable(false);
        txtCitizenId.setEditable(false);
        txtInsuranceId.setEditable(false);

        cbxSex.setVisible(false);
        txtSex.setVisible(true);

        btnSave.setVisible(false);
        btnCancel.setVisible(false);
    }

    private void unlockFields() {
        txtEmail.setEditable(true);
        txtFullName.setEditable(true);
        txtDob.setEditable(true);
        txtPhoneNumber.setEditable(true);
        txtAddress.setEditable(true);
        txtCitizenId.setEditable(true);
        txtInsuranceId.setEditable(true);

        txtSex.setVisible(false);
        cbxSex.setVisible(true);

        btnSave.setVisible(true);
        btnCancel.setVisible(true);
    }

    private void loadData() {
        String url = AppConstants.BASE_URL + "/users/" + id;
        String token = Utils.getToken();
        String method = "GET";

        cbxSex.setEditable(false);
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        lockFields();

        Service<Map<String, Object>> service = new Service<Map<String, Object>>() {
            @Override
            protected Task<Map<String, Object>> createTask() {
                return new Task<>() {
                    @Override
                    protected Map<String, Object> call() throws Exception {
                        return new ApiRequest.Builder<Map<String, Object>>()
                                .url(url).token(token).method(method)
                                .build().request();
                    }
                };
            }
        };

        service.setOnSucceeded(event -> {
            var map = service.getValue();

            txtEmail.setText(map.get("email").toString());
            txtFullName.setText(map.get("fullName").toString());
            txtSex.setText(map.get("sex").toString());
            txtDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(map.get("dob").toString()));
            txtPhoneNumber.setText(map.get("phoneNumber").toString());
            txtAddress.setText(map.get("address").toString());
            txtCitizenId.setText(map.get("citizenId").toString());
            txtInsuranceId.setText(map.get("insuranceId").toString());
            lblName.setText(txtFullName.getText());

            cbxSex.getSelectionModel().selectFirst();
            while (!cbxSex.getSelectionModel().getSelectedItem().equals(map.get("sex").toString()))
                cbxSex.getSelectionModel().selectNext();

            spinner1.setVisible(false);
        });
        service.start();
    }

    void loadChildrenList() {
        String url = AppConstants.BASE_URL + "/children?parent-id=" + id;
        String token = Utils.getToken();
        String method = "GET";

        Service<List<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<List<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected List<Map<String, Object>> call() throws Exception {
                        try {
                            return new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(url).token(token).method(method)
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
            if (service.getValue() != null) {
                spinner2.setVisible(false);

                Node addChildButton = childrenPane.getChildren().get(childrenPane.getChildren().size() - 1);
                List<Parent> childRefItems = service.getValue().stream().map(
                        m -> ScreenManager.getChildRefItem(((Double) m.get("id")).longValue(), m.get("fullName").toString())
                ).toList();

                childrenPane.getChildren().setAll(childRefItems);
                childrenPane.getChildren().add(addChildButton);
            }
        });

        service.start();
    }

    @FXML
    void back(ActionEvent event) {
        ScreenManager.setMainPanel(previous);
    }

    @FXML
    void cancelEdit(ActionEvent event) {
        lockFields();
        loadData();
    }

    @FXML
    void deleteProfile(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", null, "Bạn có chắc muốn xóa người dùng này không?");
        if (!buttonType.equals(ButtonType.OK))
            return;

        String url = AppConstants.BASE_URL + "/users/" + id;
        String token = Utils.getToken();
        String method = "DELETE";

        try {
            new ApiRequest.Builder<String>().url(url).token(token).method(method).build().request();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Xóa người dùng thành công!");

            previousController.loadUsersData();
            back(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void editProfile(ActionEvent event) {
        unlockFields();
    }

    @FXML
    void saveProfile(ActionEvent event) {
        String url = AppConstants.BASE_URL + "/users/" + id;
        String token = Utils.getToken();
        String method = "PUT";
        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getValue())
                .put("dob", Beans.DATE_FORMAT_CONVERTER.toISO(txtDob.getText()))
                .put("phoneNumber", txtPhoneNumber.getText())
                .put("address", txtAddress.getText())
                .put("citizenId", txtCitizenId.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();
        System.out.println(requestBody);
        try {
            new ApiRequest.Builder<String>().url(url).token(token).method(method).requestBody(requestBody).build().request();
            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Cập nhật hồ sơ người dùng thành công!");

            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void addChild(ActionEvent event) {
        ScreenManager.getAddChildStage(id, this).show();
    }
}
