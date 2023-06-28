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
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddChildController implements Initializable {

    private Object parent;

    public AddChildController(Object parent) {
        this(-1, parent);
    }

    public AddChildController(long parentId, Object parent) {
        this.parentId = parentId;
        this.parent = parent;
    }

    private long parentId;

    @FXML
    private JFXComboBox<Map<String, Object>> cbxParent;

    @FXML
    private JFXTextField txtFullName;

    @FXML
    private JFXComboBox<String> cbxSex;

    @FXML
    private JFXTextField txtDob;

    @FXML
    private JFXTextField txtEthnicity;

    @FXML
    private JFXTextField txtBirthplace;

    @FXML
    private JFXTextField txtInsuranceId;

    @FXML
    private ProgressIndicator spinner;

    private ObservableList<Map<String, Object>> parents;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spinner.setVisible(true);
        cbxSex.setItems(FXCollections.observableArrayList("Nam", "Nữ"));
        setUpParentComboBox();
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
                spinner.setVisible(false);
                parents.setAll(service.getValue());
                if (parentId != -1) {
                    var parent = parents.filtered(p ->
                            ((Double) p.get("id")).longValue() == parentId).get(0);
                    cbxParent.getSelectionModel().select(parent);
                }
            }
        });

        service.start();
    }

    @FXML
    void addChild(ActionEvent event) {
        spinner.setVisible(true);

        String url = AppConstants.BASE_URL + "/children?parent-id=" + parentId;
        String token = Utils.getToken();
        String method = "POST";
        String requestBody = new RequestBodyMap()
                .put("fullName", txtFullName.getText())
                .put("sex", cbxSex.getValue())
                .put("dob", Beans.DATE_FORMAT_CONVERTER.toISO(txtDob.getText()))
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
                            return new ApiRequest.Builder<String>()
                                    .url(url).token(token).method(method).requestBody(requestBody)
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
                spinner.setVisible(false);
                Utils.showAlert(Alert.AlertType.INFORMATION, "Thông báo", null, "Thêm hồ sơ trẻ thành công!");
                ((Stage) txtFullName.getScene().getWindow()).close();

                if (parent instanceof UserDetailsController userDetailsController) {
                    userDetailsController.loadChildrenList();
                    return;
                }

                if (parent instanceof ManageChildrenController manageChildrenController) {
                    manageChildrenController.loadChildrenData();
                    return;
                }
            }
        });

        service.start();
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtFullName.getScene().getWindow()).close();
    }

}
