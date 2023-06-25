package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Beans;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {

    private Long userId;

    private Stage container;

    private ManageUserController parent;

    public EditUserController(Long userId, Stage container, ManageUserController parent) {
        this.userId = userId;
        this.container = container;
        this.parent = parent;
    }

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXComboBox<String> boxSex;

    @FXML
    private JFXTextField txtDob;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCitizenId;

    @FXML
    private JFXTextField txtInsuranceId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        fetchData();
    }

    private void fetchData() {
        String url = AppConstants.BASE_URL + "/user/%d".formatted(userId);
        String token = Utils.getToken();
        String method = "GET";

        Map<String, Object> result = null;
        try {
            result = new ApiRequest.Builder<Map<String, Object>>()
                    .url(url)
                    .token(token)
                    .method(method)
                    .build().request();

            txtEmail.setText(result.get("email").toString());
            txtFullName.setText(result.get("fullName").toString());
            boxSex.getSelectionModel().selectFirst();
            while (!boxSex.getSelectionModel().getSelectedItem().equals(result.get("sex").toString())) {
                boxSex.getSelectionModel().selectNext();
            }
            txtDob.setText((LocalDate.parse(result.get("dob").toString(), DateTimeFormatter.ISO_DATE)).format(Beans.DATE_TIME_FORMATTER));
            txtPhoneNumber.setText(result.get("phoneNumber").toString());
            txtAddress.setText(result.get("address").toString());
            txtCitizenId.setText(result.get("citizenId") == null ? "" : result.get("citizenId").toString());
            txtInsuranceId.setText(result.get("insuranceId") == null ? "" : result.get("insuranceId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Lỗi", null, "Đã có lỗi xảy ra!");
        }

    }

    @FXML
    void cancel(ActionEvent event) {
        container.close();
    }

    @FXML
    void editUser(ActionEvent event) {

    }

}

