package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserDetailsController implements Initializable {

    private long id;

    private Parent previous;

    private Object previousController;

    public UserDetailsController(long id, Parent previous, Object previousController) {
        this.id = id;
        this.previous = previous;
        this.previousController = previousController;
    }

    @FXML
    private VBox vBox;

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
    private JFXButton btnBack;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnAddChild;

    @FXML
    private ProgressIndicator spinner1;

    @FXML
    private ProgressIndicator spinner2;

    @FXML
    private DatePicker dpDob;

    @FXML
    private Label lblError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (previous==null) {
            btnBack.setManaged(false);
        }
        if (!UserIdentity.getRoles().contains("ADMIN")) {
            btnDelete.setManaged(false);
            btnAddChild.setManaged(false);
            btnEdit.setText("Cập nhật hồ sơ");
        }
        dpDob.setConverter(Beans.DATE_STRING_CONVERTER);
        loadData();
        loadChildrenList();
        loadEventsList();
    }

    private void lockFields() {
        txtEmail.setEditable(false);
        txtFullName.setEditable(false);
        txtSex.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtAddress.setEditable(false);
        txtCitizenId.setEditable(false);
        txtInsuranceId.setEditable(false);

        cbxSex.setVisible(false);
        txtSex.setVisible(true);

        dpDob.setVisible(false);
        txtDob.setVisible(true);

        btnSave.setVisible(false);
        btnCancel.setVisible(false);
    }

    private void unlockFields() {
        txtEmail.setEditable(true);
        txtFullName.setEditable(true);
        txtPhoneNumber.setEditable(true);
        txtAddress.setEditable(true);
        txtCitizenId.setEditable(true);
        txtInsuranceId.setEditable(true);

        txtSex.setVisible(false);
        cbxSex.setVisible(true);

        txtDob.setVisible(false);
        dpDob.setVisible(true);

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
            txtCitizenId.setText(Optional.ofNullable(map.get("citizenId")).orElse("").toString());
            txtInsuranceId.setText(Optional.ofNullable(map.get("insuranceId")).orElse("").toString());
            lblName.setText(txtFullName.getText().toUpperCase());

            spinner1.setVisible(false);
        });
        service.start();
    }

    void loadChildrenList() {
        String url = AppConstants.BASE_URL + "/children/find-by-parent?id=" + id;
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
                        m -> ScreenManager.getChildRefItem(
                                ((Double) m.get("id")).longValue(),
                                m.get("fullName").toString(),
                                m.get("sex").toString(),
                                lblName.getParent().getParent())
                ).toList();

                childrenPane.getChildren().setAll(childRefItems);
                childrenPane.getChildren().add(addChildButton);
            }
        });

        service.start();
    }

    void loadEventsList() {
        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/events/find-by-user?id=" + id)
                    .method("GET")
                    .build().request();
            var items = result.stream().map(event -> {
                Label label = new Label(event.get("name").toString());
                label.setStyle("""
                            -fx-text-fill: #212121;
                            -fx-font-family: "Roboto", sans-serif;
                            -fx-font-size: 14px;
                        """);
                MaterialIconView icon = new MaterialIconView();
                icon.setGlyphName("EVENT");
                icon.setFill(Color.BLACK);
                icon.setGlyphSize(20);
                label.setGraphic(icon);
                return label;
            }).toList();
            eventsPane.getChildren().setAll(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void back(ActionEvent event) {
        switch (previousController) {
            case ManageUsersController manageUsersController -> {
                manageUsersController.loadUsersData();
            }
            case ChildDetailsController childDetailsController -> {
                childDetailsController.loadData();
            }
            default -> {
            }
        }
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

            back(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void editProfile(ActionEvent event) {
        cbxSex.getSelectionModel().selectFirst();
        while (!cbxSex.getSelectionModel().getSelectedItem().equals(txtSex.getText()))
            cbxSex.getSelectionModel().selectNext();
        dpDob.setValue(LocalDate.parse(txtDob.getText(), Beans.DATE_FORMATTER));
        unlockFields();
    }

    @FXML
    void saveProfile(ActionEvent event) {
        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getValue())
                .put("dob", dpDob.getValue().toString())
                .put("phoneNumber", txtPhoneNumber.getText())
                .put("address", txtAddress.getText())
                .put("citizenId", txtCitizenId.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();

        try {
            if (UserIdentity.getRoles().contains("ADMIN")) {
                new ApiRequest.Builder<String>().url(AppConstants.BASE_URL + "/users/" + id).method("PUT").requestBody(requestBody).build().request();
                Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật hồ sơ người dùng thành công!");
            } else {
                new ApiRequest.Builder<String>().url(AppConstants.BASE_URL + "/users/" + id + "/request-change").method("POST").requestBody(requestBody).build().request();
                Utils.showAlert(Alert.AlertType.INFORMATION, "Vui lòng chờ quản trị viên phê duyệt thay đổi.");
            }

            loadData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        txtEmail.setStyle("");
        txtFullName.setStyle("");
        cbxSex.setStyle("");
        dpDob.setStyle("");
        txtPhoneNumber.setStyle("");
        txtAddress.setStyle("");
        txtCitizenId.setStyle("");
        txtInsuranceId.setStyle("");

        if (txtFullName.getText().isBlank()) {
            txtFullName.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền họ tên!");
        }
        if (txtEmail.getText().isBlank()) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập email!");
        }
        if (!txtEmail.getText().matches("[A-Za-z0-9.]+@[A-Za-z0-9]+([.][A-Za-z0-9]+)+")) {
            txtEmail.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Email không hợp lệ!");
        }
        if (cbxSex.getSelectionModel().getSelectedItem() == null) {
            cbxSex.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn giới tính!");
        }
        LocalDate dob;
        try {
            dob = LocalDate.parse(dpDob.getEditor().getText(), Beans.DATE_FORMATTER);
        } catch (Exception e) {
            dpDob.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày sinh không hợp lệ!");
        }
        if (dob.isAfter(LocalDate.now())) {
            dpDob.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Ngày sinh không hợp lệ!");
        }
        if (txtPhoneNumber.getText().isBlank()) {
            txtPhoneNumber.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập số điện thoại!");
        }
        if (!txtPhoneNumber.getText().matches("[0-9]{10,12}")) {
            txtPhoneNumber.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số điện thoại không hợp lệ!");
        }
        if (txtAddress.getText().isBlank()) {
            txtAddress.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng điền địa chỉ!");
        }
        if (!txtCitizenId.getText().isBlank() && !txtCitizenId.getText().matches("[0-9]{12}")) {
            txtCitizenId.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số CCCD không hợp lệ!");
        }
        if (!txtInsuranceId.getText().isBlank() && !txtInsuranceId.getText().matches("[A-Z]{2}[0-9]{13}")) {
            txtInsuranceId.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Số BHYT khỗng hợp lệ!");
        }
    }

    @FXML
    void addChild(ActionEvent event) {
        ScreenManager.getAddChildStage(id, this).show();
    }
}
