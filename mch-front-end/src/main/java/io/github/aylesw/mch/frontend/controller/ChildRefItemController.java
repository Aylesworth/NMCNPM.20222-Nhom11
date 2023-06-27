package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ChildRefItemController implements Initializable {

    private long id;
    private String name;

    @FXML
    JFXButton btnChildRef;

    public ChildRefItemController(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnChildRef.setText(name);
    }

    @FXML
    void viewChildProfile(ActionEvent event) {

    }

}

