package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class InjectionSuggestionItemController implements Initializable {

    private Map<String, Object> properties;

    public InjectionSuggestionItemController(Map<String, Object> properties) {
        this.properties = properties;
    }

    @FXML
    private Label lblChild;

    @FXML
    private Label lblVaccine;

    @FXML
    private Label lblDoseNo;

    @FXML
    private Label lblAgeGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblChild.setText("Bé " + properties.get("childName").toString().toUpperCase());
        lblVaccine.setText("Vaccine: " + properties.get("vaccineName").toString());
        lblDoseNo.setText("Mũi số: " + Utils.toLongValue(properties.get("doseNo")) + "");
        lblAgeGroup.setText("Độ tuổi: " + properties.get("ageGroupName").toString());
    }

    @FXML
    void register(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Chọn ngày");
        dialog.setHeaderText("Vui lòng nhập ngày bạn muốn đăng ký tiêm (DD/MM/YYYY):");
        dialog.setContentText(null);
        var input = dialog.showAndWait();

        while (true) {
            if (input.isEmpty()) return;

            try {
                var date = LocalDate.parse(input.get(), Beans.DATE_FORMATTER);
                if (date.isBefore(LocalDate.now())) {
                    Utils.showAlert(Alert.AlertType.ERROR, "Vui lòng nhập ngày trong tương lai!");
                    input = dialog.showAndWait();
                    continue;
                }
                break;
            } catch (Exception e) {
                Utils.showAlert(Alert.AlertType.ERROR, "Ngày không hợp lệ!");
                input = dialog.showAndWait();
            }
        }

        try {
            Long childId = Utils.toLongValue(properties.get("childId"));
            String requestBody = new RequestBodyMap()
                    .put("vaccineName", properties.get("vaccineName").toString())
                    .put("vaccineDoseNo", Utils.toLongValue(properties.get("doseNo")))
                    .put("date", Beans.DATE_FORMAT_CONVERTER.toISO(input.get()))
                    .put("status", "Chờ xác nhận")
                    .toJson();

            new ApiRequest.Builder<String>()
                    .url(AppConstants.BASE_URL + "/children/" + childId + "/injections")
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            Utils.showAlert(Alert.AlertType.INFORMATION, "Đăng ký tiêm chủng sẽ được quản trị viên xem xét và xác nhận");
        } catch (Exception e) {
            if (e.getMessage().contains("already injected")) {
                Utils.showAlert(Alert.AlertType.ERROR, "Trẻ đã tiêm hoặc đã đăng ký tiêm mũi tiêm này!");
            } else {
                e.printStackTrace();
            }
        }
    }
}

