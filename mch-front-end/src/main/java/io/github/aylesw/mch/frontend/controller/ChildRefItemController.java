package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChildRefItemController implements Initializable {

    private long id;
    private String name;
    private String sex;
    private Parent parent;
    private Object parentController;

    @FXML
    JFXButton btnChildRef;

    public ChildRefItemController(long id, String name, String sex, Parent parent, Object parentController) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.parent = parent;
        this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnChildRef.setText(name);
        btnChildRef.setStyle("-fx-background-color: " + (sex.equals("Nam") ? "AQUA" : "GAINSBORO"));
    }

    @FXML
    void viewChildProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(id, parent, parentController));
    }

    void updateName(String name) {
        this.name = name;
        btnChildRef.setText(name);
    }

    Parent getParent() {
        return parent;
    }

}

