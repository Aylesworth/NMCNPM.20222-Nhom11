package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddChildController implements Initializable {

    private Object parentController;

    public AddChildController(Object parentController) {
        this(-1, parentController);
    }

    public AddChildController(long parentId, Object parentController) {
        this.parentId = parentId;
        this.parentController = parentController;
    }

    private long parentId;

    @FXML
    private JFXComboBox<Map<String, Object>> cbxParent;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXComboBox<String> cbxSex;

    @FXML
    private DatePicker dpDob;

    @FXML
    private JFXTextField txtEthnicity;

    @FXML
    private JFXTextField txtBirthplace;

    @FXML
    private JFXTextField txtInsuranceId;

    @FXML
    private Label lblError;

    private ObservableList<Map<String, Object>> parents;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        dpDob.setConverter(Beans.DATE_STRING_CONVERTER);
        setUpParentComboBox();
        if (parentController instanceof ChildrenListController) {
            cbxParent.setVisible(false);
            cbxParent.setManaged(false);
        }
    }

    private void setUpParentComboBox() {
        parents = FXCollections.observableArrayList();
        cbxParent.setItems(parents);
        cbxParent.setCellFactory(view -> new JFXListCell<>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText("%s (%s)".formatted(item.get("fullName").toString(), item.get("email").toString()));
                }
            }
        });
        cbxParent.setConverter(new StringConverter<Map<String, Object>>() {
            @Override
            public String toString(Map<String, Object> map) {
                if (map != null)
                    return map.get("fullName").toString();
                return null;
            }

            @Override
            public Map<String, Object> fromString(String s) {
                return null;
            }
        });
        cbxParent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                parentId = ((Double) newValue.get("id")).longValue();
            }
        });

        String apiUrl = AppConstants.BASE_URL + "/users";
        String token = Utils.getToken();
        String method = "GET";

        Service<ObservableList<Map<String, Object>>> service = new Service<>() {
            @Override
            protected Task<ObservableList<Map<String, Object>>> createTask() {
                return new Task<>() {
                    @Override
                    protected ObservableList<Map<String, Object>> call() throws Exception {
                        try {
                            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                                    .url(apiUrl).token(token).method(method)
                                    .build().request();
                            return FXCollections.observableArrayList(result)
                                    .sorted(Comparator.comparing(map -> map.get("fullName").toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(e -> {
            if (service.getValue() != null) {
                parents.setAll(service.getValue());
                if (parentId != -1) {
                    var parentController = parents.filtered(p ->
                            ((Double) p.get("id")).longValue() == parentId).get(0);
                    cbxParent.getSelectionModel().select(parentController);
                }
            }
        });

        service.start();
    }

    @FXML
    void addChild(ActionEvent event) {

        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String method = "POST";
        String requestBody = new RequestBodyMap()
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getValue())
                .put("dob", dpDob.getValue().toString())
                .put("ethnicity", txtEthnicity.getText())
                .put("birthplace", txtBirthplace.getText())
                .put("insuranceId", txtInsuranceId.getText())
                .toJson();

        Service<String> service = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        try {
                            if (parentController instanceof ChildrenListController)
                                return new ApiRequest.Builder<String>()
                                        .url(AppConstants.BASE_URL + "/children/register?parent-id=" + parentId)
                                        .method(method)
                                        .requestBody(requestBody)
                                        .build().request();

                            return new ApiRequest.Builder<String>()
                                    .url(AppConstants.BASE_URL + "/children?parent-id=" + parentId)
                                    .method(method)
                                    .requestBody(requestBody)
                                    .build().request();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }
        };

        service.setOnSucceeded(e -> {
            if (service.getValue() != null) {
//                Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Thêm hồ sơ trẻ thành công!");
                if (parentController instanceof ChildrenListController) {
                    Utils.showAlert(Alert.AlertType.INFORMATION, "Hồ sơ trẻ đang chờ được phê duyệt.");
                }
                ((Stage) txtFullName.getScene().getWindow()).close();

                if (parentController instanceof UserDetailsController userDetailsController) {
                    userDetailsController.loadChildrenList();
                    return;
                }

                if (parentController instanceof ManageChildrenController manageChildrenController) {
                    manageChildrenController.loadChildrenData();
                    return;
                }

                if (parentController instanceof ChildrenListController childrenListController) {
                    childrenListController.loadChildren();
                    return;
                }
            }
        });

        service.start();
    }

    private void validateFields() throws Exception {
        lblError.setText("");
        cbxParent.setStyle("");
        txtFullName.setStyle("");
        cbxSex.setStyle("");
        dpDob.setStyle("");
        txtEthnicity.setStyle("");
        txtBirthplace.setStyle("");
        txtInsuranceId.setStyle("");

        if (cbxParent.getSelectionModel().getSelectedItem() == null) {
            cbxParent.setStyle(AppConstants.ERROR_BACKGROUND);
            throw new Exception("Vui lòng chọn cha/mẹ!");
        }
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
    void cancel(ActionEvent event) {
        ((Stage) txtFullName.getScene().getWindow()).close();
    }

}
