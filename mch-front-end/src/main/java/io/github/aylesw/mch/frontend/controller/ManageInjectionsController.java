package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

public class ManageInjectionsController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> tblSchedule;

    @FXML
    private JFXButton btnChangeDate;

    @FXML
    private JFXButton btnDeleteInjection;

    @FXML
    private JFXButton btnViewProfile;

    @FXML
    private TableView<Map<String, Object>> tblRegistrations;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtDob;

    @FXML
    private JFXTextField txtAge;

    @FXML
    private JFXTextField txtVaccine;

    @FXML
    private JFXTextField txtDoseNo;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtNote;

    @FXML
    private JFXButton btnConfirm;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnViewProfile2;

    @FXML
    private BarChart<String, Number> chartStats;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private JFXComboBox<String> cbxMonth;

    @FXML
    private JFXComboBox<String> cbxYear;

    @FXML
    private TableView<Map<String, Object>> tblStats;

    private ObservableList<Map<String, Object>> schedule;
    private ObservableList<Map<String, Object>> registrations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadScheduleData();
        loadRegistrations();

        chartStats.setManaged(false);

        List<String> months = new ArrayList<>(IntStream.rangeClosed(1, 12).mapToObj(String::valueOf).toList());
        months.add(0, "Tất cả");
        List<String> years = new ArrayList<>(IntStream.rangeClosed(2020, 2023).mapToObj(String::valueOf).toList());
        Collections.reverse(years);
        years.add(0, "Tất cả");

        cbxMonth.setItems(FXCollections.observableArrayList(months));
        cbxMonth.getSelectionModel().selectFirst();
        cbxYear.setItems(FXCollections.observableArrayList(years));
        cbxYear.getSelectionModel().selectFirst();

        cbxMonth.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (cbxYear.getSelectionModel().getSelectedItem().equals("Tất cả"))
                cbxMonth.getSelectionModel().selectFirst();
            loadStats();
        });
        cbxYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Tất cả"))
                cbxMonth.getSelectionModel().selectFirst();
            loadStats();
        });
        loadStats();


        // Set the tick unit to 1 to display integer values
        yAxis.setTickUnit(1);
        yAxis.setTickLength(1);

        // Set the tick label formatter to display integer values
        yAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object.intValue());
            }

            @Override
            public Number fromString(String string) {
                // Not needed for displaying integer values
                return null;
            }
        });

        xAxis.setPrefWidth(10);

        // Add the data series to the bar chart
        chartStats.getData().add(dataSeries);
    }

    private void hideButtons() {
        btnChangeDate.setVisible(false);
        btnDeleteInjection.setVisible(false);
        btnViewProfile.setVisible(false);
    }

    private void showButtons() {
        btnChangeDate.setVisible(true);
        btnDeleteInjection.setVisible(true);
        btnViewProfile.setVisible(true);
    }

    void loadScheduleData() {
        hideButtons();
        schedule = FXCollections.observableArrayList();
        tblSchedule.setItems(schedule);

        var childNameCol = new TableColumn<Map<String, Object>, String>("Tên trẻ");
        childNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("childName").toString()));

        var vaccineCol = new TableColumn<Map<String, Object>, String>("Vaccine");
        vaccineCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("vaccineName").toString()));

        var doseNoCol = new TableColumn<Map<String, Object>, String>("Mũi số");
        doseNoCol.setCellValueFactory(data -> new SimpleStringProperty(((Double) data.getValue().get("vaccineDoseNo")).longValue() + ""));

        var dateCol = new TableColumn<Map<String, Object>, String>("Ngày tiêm");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(Beans.DATE_FORMAT_CONVERTER.toCustom(data.getValue().get("date").toString())));

        tblSchedule.getColumns().setAll(childNameCol, vaccineCol, doseNoCol, dateCol);

        tblSchedule.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                hideButtons();
            } else {
                showButtons();
            }
        });

        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/injections/schedule")
                    .method("GET")
                    .build().request();

            schedule.setAll(result);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void hideInfoFields() {
        txtName.setVisible(false);
        txtDob.setVisible(false);
        txtAge.setVisible(false);
        txtVaccine.setVisible(false);
        txtDoseNo.setVisible(false);
        txtDate.setVisible(false);
        txtNote.setVisible(false);
        btnConfirm.setVisible(false);
        btnCancel.setVisible(false);
        btnViewProfile2.setVisible(false);
    }

    private void showInfoFields() {
        txtName.setVisible(true);
        txtDob.setVisible(true);
        txtAge.setVisible(true);
        txtVaccine.setVisible(true);
        txtDoseNo.setVisible(true);
        txtDate.setVisible(true);
        txtNote.setVisible(true);
        btnConfirm.setVisible(true);
        btnCancel.setVisible(true);
        btnViewProfile2.setVisible(true);
    }

    void loadRegistrations() {
        hideInfoFields();
        registrations = FXCollections.observableArrayList();
        tblRegistrations.setItems(registrations);

        var childNameCol = new TableColumn<Map<String, Object>, String>("Tên trẻ");
        childNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("childName").toString()));

        var vaccineCol = new TableColumn<Map<String, Object>, String>("Vaccine");
        vaccineCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("vaccineName").toString()));

        var doseNoCol = new TableColumn<Map<String, Object>, String>("Mũi số");
        doseNoCol.setCellValueFactory(data -> new SimpleStringProperty(((Double) data.getValue().get("vaccineDoseNo")).longValue() + ""));

        var dateCol = new TableColumn<Map<String, Object>, String>("Ngày tiêm");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(Beans.DATE_FORMAT_CONVERTER.toCustom(data.getValue().get("date").toString())));

        tblRegistrations.getColumns().setAll(childNameCol, vaccineCol, doseNoCol, dateCol);

        tblRegistrations.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                hideInfoFields();
            } else {
                txtName.setText(newValue.get("childName").toString());
                txtDob.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("childDob").toString()));
                txtAge.setText(((Double) newValue.get("childAgeInMonths")).longValue() + "");
                txtVaccine.setText(newValue.get("vaccineName").toString());
                txtDoseNo.setText(((Double) newValue.get("vaccineDoseNo")).longValue() + "");
                txtDate.setText(Beans.DATE_FORMAT_CONVERTER.toCustom(newValue.get("date").toString()));
                txtNote.setText(Optional.ofNullable(newValue.get("note")).orElse("").toString());
                showInfoFields();
            }
        });

        try {
            var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/injections/pending-registrations")
                    .method("GET")
                    .build().request();

            registrations.setAll(result);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    // Create a data series
    private XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();

    void loadStats() {
        List<Map<String, Object>> stats;
        try {
            String month = cbxMonth.getSelectionModel().getSelectedItem();
            String year = cbxYear.getSelectionModel().getSelectedItem();

            String requestParams = (month.equals("Tất cả") || year.equals("Tất cả")) ? "" : "?month=" + month + "&year=" + year;

            stats = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/injections/vaccine-stats" + requestParams)
                    .method("GET")
                    .build().request();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
//
//        dataSeries.getData().clear();
//        stats.stream()
//                .filter(data -> ((Double) data.get("doseNo")).longValue() == 0
//                        && ((Double) data.get("quantity")).longValue() > 0)
//                .forEach(data -> {
//                    dataSeries.getData().add(new XYChart.Data<>(
//                            data.get("vaccine").toString(),
//                            ((Double) data.get("quantity")).intValue()
//                    ));
//                });
//        chartStats.getData().clear();
//        chartStats.getData().add(dataSeries);

        var vaccineCol = new TableColumn<Map<String, Object>, String>("Vaccine");
        vaccineCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().get("vaccine").toString()));

        var doseNoCol = new TableColumn<Map<String, Object>, String>("Mũi");
        doseNoCol.setCellValueFactory(data ->
                new SimpleStringProperty(((Double)data.getValue().get("doseNo")).intValue()==0 ? "Tất cả" : ((Double) data.getValue().get("doseNo")).longValue() + ""));
        doseNoCol.setCellFactory(TextFieldTableCell.forTableColumn());

        var quantityCol = new TableColumn<Map<String, Object>, String>("Số lượng");
        quantityCol.setCellValueFactory(data ->
                new SimpleStringProperty(((Double) data.getValue().get("quantity")).longValue() + ""));
        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn());

        tblStats.getColumns().setAll(vaccineCol, doseNoCol, quantityCol);
//        tblStats.getItems().addAll(stats);

        var sortedData = new SortedList<Map<String, Object>>(FXCollections.observableArrayList(stats));
        sortedData.setComparator(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (!o1.get("vaccine").equals(o2.get("vaccine")))
                    return o1.get("vaccine").toString().compareTo(o2.get("vaccine").toString());
                return ((Double) o1.get("doseNo")).intValue() - ((Double) o2.get("doseNo")).intValue();
            }
        });

        tblStats.getItems().setAll(sortedData);
    }

    @FXML
    void addInjection(ActionEvent event) {
        ScreenManager.getAddInjectionStage(this).show();
    }

    @FXML
    void cancel(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Lý do");
            dialog.setHeaderText("Nhập lý do hủy đăng ký mũi tiêm:");
            dialog.setContentText(null);
            var input = dialog.showAndWait();

            while (input.isEmpty() || input.get().isBlank()) {
                if (input.isEmpty()) return;
                Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập gì đó!");
                input = dialog.showAndWait();
            }

            String reason = input.get();
            long id = ((Double) tblRegistrations.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/injections/reject-registration?id=" + id + "&reason=" + reason)
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Hủy đăng ký tiêm chủng thành công!");
            loadRegistrations();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changeDate(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Thay đổi ngày");
        dialog.setHeaderText("Vui lòng nhập ngày mới (DD/MM/YYYY):");
        dialog.setContentText("");
        var input = dialog.showAndWait();

        while (true) {
            if (input.isEmpty()) return;

            try {
                LocalDate.parse(input.get(), Beans.DATE_FORMATTER);
                break;
            } catch (Exception e) {
                Utils.showAlert(Alert.AlertType.ERROR, "Ngày không hợp lệ!");
                input = dialog.showAndWait();
            }
        }

        var selectedItem = tblSchedule.getSelectionModel().getSelectedItem();

        String requestBody = new RequestBodyMap(selectedItem)
                .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(input.get()))
                .toJson();

        long childId = ((Double) selectedItem.get("childId")).longValue();
        long id = ((Double) selectedItem.get("id")).longValue();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id)
                    .method("PUT")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật ngày tiêm thành công!");
            loadScheduleData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void confirm(ActionEvent event) {
        try {
            long id = ((Double) tblRegistrations.getSelectionModel().getSelectedItem().get("id")).longValue();
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/injections/approve-registration?id=" + id)
                    .method("POST")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xác nhận đăng ký tiêm chủng thành công!");
            loadScheduleData();
            loadRegistrations();
        } catch (Exception e) {
            if (e.getMessage().contains("expired")) {
                Utils.showAlert(Alert.AlertType.ERROR, "Không thể xác nhận do đã qua ngày đăng ký tiêm!");
            } else {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void deleteInjection(ActionEvent event) {
        ButtonType buttonType = Utils.showAlert(Alert.AlertType.CONFIRMATION, "Bạn có chắc muốn xóa mũi tiêm này không?");
        if (!buttonType.equals(ButtonType.OK)) return;

        long id = ((Double) tblSchedule.getSelectionModel().getSelectedItem().get("id")).longValue();
        long childId = ((Double) tblSchedule.getSelectionModel().getSelectedItem().get("childId")).longValue();

        try {
            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections/" + id)
                    .method("DELETE")
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Xóa mũi tiêm thành công!");
            loadScheduleData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void viewProfile(ActionEvent event) {
        long childId = ((Double) tblSchedule.getSelectionModel().getSelectedItem().get("childId")).longValue();
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(
                childId,
                tblSchedule.getParent().getParent().getParent().getParent().getParent(),
                this));
    }

    @FXML
    void viewProfile2(ActionEvent event) {
        long childId = ((Double) tblRegistrations.getSelectionModel().getSelectedItem().get("childId")).longValue();
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(
                childId,
                tblSchedule.getParent().getParent().getParent().getParent().getParent(),
                this));
    }

    @FXML
    void exportSchedule(ActionEvent actionEvent) {
//TODO:
    }

}
