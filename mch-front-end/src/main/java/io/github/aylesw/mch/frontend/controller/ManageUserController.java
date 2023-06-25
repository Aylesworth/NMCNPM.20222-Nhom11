package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Beans;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ManageUserController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> tblUser;

    @FXML
    private JFXButton btnViewUser;

    @FXML
    private JFXButton btnEditUser;

    @FXML
    private JFXButton btnDeleteUser;
    @FXML
    private TableView<?> tblUserRegistration;

    @FXML
    private JFXButton btnApproveRegistration;

    @FXML
    private JFXButton btnDeclineRegistration;

    @FXML
    private TableView<?> tblUserRegistration1;

    @FXML
    private JFXButton btnApproveChange;

    @FXML
    private JFXButton btnDeclineChange;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnViewUser.setVisible(false);
        btnEditUser.setVisible(false);
        btnDeleteUser.setVisible(false);

        tblUser.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        btnViewUser.setVisible(true);
                        btnEditUser.setVisible(true);
                        btnDeleteUser.setVisible(true);
                    } else {
                        btnViewUser.setVisible(false);
                        btnEditUser.setVisible(false);
                        btnDeleteUser.setVisible(false);
                    }
                }
        );
        loadData();
    }

    public void loadData() {
        String url = AppConstants.BASE_URL + "/user/search?q=";
        String token = Utils.getToken();
        String method = "GET";

        try {
            List<Map<String, Object>> result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(url).token(token).method(method)
                    .build().request();

            ObservableList<Map<String, Object>> items = FXCollections.observableArrayList(result);

            tblUser.setItems(items);

            TableColumn<Map<String, Object>, String> idCol = (TableColumn<Map<String, Object>, String>) tblUser.getColumns().get(0);
            idCol.setCellValueFactory(data ->
                    new SimpleStringProperty(((Double) data.getValue().get("id")).longValue() + ""));
            idCol.setStyle("-fx-alignment: center;");

            TableColumn<Map<String, Object>, String> emailCol = (TableColumn<Map<String, Object>, String>) tblUser.getColumns().get(1);
            emailCol.setCellValueFactory(data ->
                    new SimpleStringProperty(data.getValue().get("email").toString()));
            emailCol.setStyle("-fx-alignment: center-left;");

            TableColumn<Map<String, Object>, String> fullNameCol = (TableColumn<Map<String, Object>, String>) tblUser.getColumns().get(2);
            fullNameCol.setCellValueFactory(data ->
                    new SimpleStringProperty((String) data.getValue().get("fullName").toString()));
            fullNameCol.setStyle("-fx-alignment: center-left;");

            TableColumn<Map<String, Object>, String> sexCol = (TableColumn<Map<String, Object>, String>) tblUser.getColumns().get(3);
            sexCol.setCellValueFactory(data ->
                    new SimpleStringProperty((String) data.getValue().get("sex").toString()));
            sexCol.setStyle("-fx-alignment: center;");

            TableColumn<Map<String, Object>, String> dobCol = (TableColumn<Map<String, Object>, String>) tblUser.getColumns().get(4);
            dobCol.setCellValueFactory(data -> {
                LocalDate dob = LocalDate.parse(data.getValue().get("dob").toString(), DateTimeFormatter.ISO_DATE);
                return new SimpleStringProperty(dob.format(Beans.DATE_TIME_FORMATTER));
            });
            dobCol.setStyle("-fx-alignment: center;");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void addUser(ActionEvent event) {
        ScreenManager.getAddUserWindow(this).show();
    }

    @FXML
    void deleteUser(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Xác nhận", null, "Bạn có chắc muốn xóa người dùng này không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        Long userId = ((Double) tblUser.getSelectionModel().getSelectedItem().get("id")).longValue();
        String url = AppConstants.BASE_URL + "/user/%d".formatted(userId);
        String token = Utils.getToken();
        String method = "DELETE";

        try {
            new ApiRequest.Builder<String>()
                    .url(url).token(token).method(method)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Xóa người dùng thành công!");
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showAlert(Alert.AlertType.ERROR, "Lỗi", null, "Đã có lỗi xảy ra!");
        }
    }

    @FXML
    void editUser(ActionEvent event) {
        Long userId = ((Double) tblUser.getSelectionModel().getSelectedItem().get("id")).longValue();
        ScreenManager.getEditUserWindow(userId, this).show();
    }

    @FXML
    void viewUser(ActionEvent event) {

    }

    @FXML
    void approveChange(ActionEvent event) {

    }

    @FXML
    void approveRegistration(ActionEvent event) {

    }

    @FXML
    void declineChange(ActionEvent event) {

    }

    @FXML
    void declineRegistration(ActionEvent event) {

    }
}
