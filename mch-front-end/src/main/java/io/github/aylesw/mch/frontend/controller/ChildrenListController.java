package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ChildrenListController implements Initializable {

    private long userId;

    public ChildrenListController(long userId) {
        this.userId = userId;
    }

    @FXML
    private FlowPane root;

    @FXML
    private FlowPane flowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadChildren();
    }

    void loadChildren() {
        try {
            var result = new ApiRequest.Builder<List<Map<String,Object>>>()
                    .url(AppConstants.BASE_URL+"/children/find-by-parent?id="+userId)
                    .method("GET")
                    .build().request();

            var items = result.stream().map(child -> ScreenManager.getChildCard(child, root, this)).toList();
            flowPane.getChildren().setAll(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addChild(ActionEvent event) {
        ScreenManager.getAddChildStage(userId, this).show();
    }
}
