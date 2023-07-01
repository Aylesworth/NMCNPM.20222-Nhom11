package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.RequestBodyMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class AddEventController {

    private ManageEventsController parentController;

    public AddEventController(ManageEventsController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private DatePicker dpFromDate;

    @FXML
    private DatePicker dpToDate;

    @FXML
    private JFXTextField txtMinAge;

    @FXML
    private JFXTextField txtMaxAge;

    @FXML
    void add(ActionEvent event) {
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
                    .url(AppConstants.BASE_URL+"/events")
                    .method("POST")
                    .requestBody(requestBody)
                    .build().request();

            parentController.setUpEventList();
            ((Stage) txtName.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        ((Stage) txtName.getScene().getWindow()).close();
    }

}
