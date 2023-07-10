package io.github.aylesw.mch.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class AboutController implements Initializable {
    @FXML
    private AnchorPane homeView;

    public AboutController(){
    }

    private String readFile(){
        StringBuilder strBuilder = new StringBuilder();

        try {
            File myObj = new File("GioiThieu.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                strBuilder.append(data);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return strBuilder.toString();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebView browser = new WebView();
        browser.setPrefSize(1030,800);
        WebEngine webEngine = browser.getEngine();
        webEngine.loadContent(this.readFile());
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(1030,800);
        scrollPane.setContent(browser);
        this.homeView.getChildren().add(scrollPane);
    }
}
