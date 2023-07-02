package io.github.aylesw.mch.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ChildCardController implements Initializable {

    private final Map<String, Object> properties;

    private final Parent parent;

    private ChildrenListController parentController;

    public ChildCardController(Map<String, Object> properties, Parent parent, ChildrenListController parentController) {
        this.properties = properties;
        this.parent = parent;
        this.parentController = parentController;
    }

    @FXML
    private VBox vBox;

    @FXML
    private Label lblName;

    @FXML
    private Label lblSex;

    @FXML
    private Label lblAge;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblName.setText("Bé " + properties.get("fullName").toString());
        lblSex.setText("Giới tính: " + properties.get("sex").toString());
        lblAge.setText("Tuổi: " + ((Double) properties.get("ageInMonths")).longValue() + " tháng");
        vBox.setStyle("-fx-background-color: "
                + (properties.get("sex").toString().equals("Nam") ? "aqua" : "gainsboro") +
                "; -fx-background-radius: 20");
    }

    @FXML
    void viewProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(
                ((Double) properties.get("id")).longValue(),
                parent,
                parentController
        ));
    }

}
