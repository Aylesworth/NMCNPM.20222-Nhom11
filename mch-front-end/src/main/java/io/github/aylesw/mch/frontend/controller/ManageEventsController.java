package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.*;
import io.github.aylesw.mch.frontend.common.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.LocalDateStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageEventsController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> tblParticipants;

    @FXML
    private JFXListView<Map<String, Object>> listEvents;

    @FXML
    private JFXButton btnAddUser;

    @FXML
    private JFXButton btnViewProfile;

    @FXML
    private JFXButton btnRemoveUser;

    @FXML
    private JFXTextField txtName;

    @FXML
    private DatePicker dpFromDate;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private DatePicker dpToDate;

    @FXML
    private JFXTextField txtMinAge;

    @FXML
    private JFXTextField txtMaxAge;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private ToggleGroup filter;

    @FXML
    private JFXRadioButton rbtnAll;

    @FXML
    private JFXRadioButton rbtnCurrent;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSendNotification;

    private ObservableList<Map<String, Object>> events;
    private ObservableList<Map<String, Object>> participants;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpEventList();
        setUpDatePickers();
        setUpParticipantsTable();
    }

    void setUpEventList() {
        events = FXCollections.observableArrayList();

        filter.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (rbtnCurrent.isSelected()) {
                loadEventsFromUrl("/events/current");
            } else {
                loadEventsFromUrl("/events");
            }
        });

        loadEventsFromUrl("/events");

        listEvents.setItems(events);
        listEvents.setCellFactory(view -> new JFXListCell<>() {
            @Override
            protected void updateItem(Map<String, Object> item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.get("name").toString());
                    setTooltip(new Tooltip(getText()));
                    setWrapText(true);
                    setPrefWidth(210);
                }
            }
        });

        btnSendNotification.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        btnAddUser.setVisible(false);

        listEvents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                long eventId = ((Double) newValue.get("id")).longValue();
                loadParticipants(eventId);

                txtName.setText(newValue.get("name").toString());
                txtDescription.setText(Optional.ofNullable(newValue.get("description")).orElse("").toString());
                txtMinAge.setText(((Double) newValue.get("minAge")).longValue() + "");
                txtMaxAge.setText(((Double) newValue.get("maxAge")).longValue() + "");
                dpFromDate.setValue(LocalDate.parse(newValue.get("fromDate").toString()));
                dpToDate.setValue(LocalDate.parse(newValue.get("toDate").toString()));

                btnSendNotification.setVisible(true);
                btnUpdate.setVisible(true);
                btnDelete.setVisible(true);
                btnAddUser.setVisible(true);
            } else {
                clearInfo();
                tblParticipants.setItems(FXCollections.observableArrayList());

                btnSendNotification.setVisible(false);
                btnUpdate.setVisible(false);
                btnDelete.setVisible(false);
                btnAddUser.setVisible(false);
            }
        });

    }

    private void clearInfo() {
        txtName.setText("");
        txtDescription.setText("");
        txtMaxAge.setText("");
        txtMinAge.setText("");
        dpFromDate.setValue(null);
        dpToDate.setValue(null);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }

    void setUpDatePickers() {
        var converter = new LocalDateStringConverter(Beans.DATE_FORMATTER, null);
        dpFromDate.setConverter(converter);
        dpToDate.setConverter(converter);
    }

    private void setUpParticipantsTable() {
        participants = FXCollections.observableArrayList();
        tblParticipants.setItems(participants);

        var nameCol = new TableColumn<Map<String, Object>, String>("Họ tên");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("fullName").toString()));

        var emailCol = new TableColumn<Map<String, Object>, String>("Email");
        emailCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("email").toString()));

        var ageCol = new TableColumn<Map<String, Object>, String>("Tuổi");
        ageCol.setCellValueFactory(data -> new SimpleStringProperty(((Double) data.getValue().get("age")).longValue() + ""));

        tblParticipants.getColumns().setAll(nameCol, emailCol, ageCol);

        btnAddUser.setVisible(false);
        btnViewProfile.setVisible(false);
        btnRemoveUser.setVisible(false);

        tblParticipants.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                btnViewProfile.setVisible(true);
                btnRemoveUser.setVisible(true);
            } else {
                btnViewProfile.setVisible(false);
                btnRemoveUser.setVisible(false);
            }
        });
    }

    private void loadEventsFromUrl(String url) {
        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + url)
                    .method("GET")
                    .build().request();

            events.setAll(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadParticipants(long eventId) {
        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId + "/participants")
                    .method("GET")
                    .build().request();

            participants.setAll(result);
            tblParticipants.setItems(participants);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addEvent(ActionEvent event) {
        ScreenManager.getAddEventStage(this).show();
    }

    @FXML
    void addUser(ActionEvent event) {
        long eventId = ((Double) listEvents.getSelectionModel().getSelectedItem().get("id")).longValue();
        ScreenManager.getAddUserToEventStage(eventId, this).show();
    }

    @FXML
    void sendNotification(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Thông báo mới");
        dialog.setHeaderText("Vui lòng nhập thông báo cho sự kiện:");
        dialog.setContentText(null);
        var input = dialog.showAndWait();

        if (input.isEmpty()) return;

        try {
            long eventId = ((Double) listEvents.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId + "/send-notification?content=" + input.get())
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Gửi thông báo thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteEvent(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa sự kiện này không?");
        if (!buttonType.equals(ButtonType.OK))
            return;

        try {
            long eventId = ((Double) listEvents.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId)
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa sự kiện thành công!");
            setUpEventList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void removeUser(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa người dùng này khỏi sự kiện không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        try {
            long eventId = ((Double) listEvents.getSelectionModel().getSelectedItem().get("id")).longValue();
            long userId = ((Double) tblParticipants.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId + "/participants/" + userId)
                    .method("DELETE")
                    .build().request();

            loadParticipants(eventId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search(KeyEvent event) {
        listEvents.setItems(events.filtered(e ->
                e.get("name").toString().toLowerCase()
                        .contains(txtSearch.getText().toLowerCase())));
    }

    @FXML
    void updateEvent(ActionEvent event) {
        long eventId = ((Double) listEvents.getSelectionModel().getSelectedItem().get("id")).longValue();
        String requestBody = new RequestBodyMap()
                .put("name", txtName.getText())
                .put("description", txtDescription.getText())
                .put("minAge", Integer.parseInt(txtMinAge.getText()))
                .put("maxAge", Integer.parseInt(txtMaxAge.getText()))
                .put("fromDate", dpFromDate.getValue().toString())
                .put("toDate", dpToDate.getValue().toString())
                .toJson();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/events/" + eventId)
                    .method("PUT")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật sự kiện thành công!");
            setUpEventList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewProfile(ActionEvent event) {
        long id = ((Double) tblParticipants.getSelectionModel().getSelectedItem().get("id")).longValue();
        ScreenManager.setMainPanel(ScreenManager.getUserDetailsPanel(id, listEvents.getParent().getParent().getParent(), this));
    }

}
