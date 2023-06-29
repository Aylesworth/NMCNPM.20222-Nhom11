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
    private Parent parent;

    @FXML
    JFXButton btnChildRef;

    public ChildRefItemController(long id, String name, Parent parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnChildRef.setText(name);
    }

    @FXML
    void viewChildProfile(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getChildDetailsPanel(id, parent, this));
    }

    void updateName(String name) {
        this.name = name;
        btnChildRef.setText(name);
    }

    Parent getParent() {
        return parent;
    }

}

