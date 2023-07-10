package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class InjectionsController implements Initializable {

    private long parentId;

    public InjectionsController(long parentId) {
        this.parentId = parentId;
    }

    @FXML
    private VBox schedulePanel;

    @FXML
    private FlowPane suggestionPanel;

    List<Long> childIds;

    List<Map<String, Object>> injections;
    List<Map<String, Object>> suggestions;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            childIds = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/children/find-by-parent?id=" + parentId)
                    .method("GET")
                    .build().request()
                    .stream()
                    .map(data -> Utils.toLongValue(data.get("id")))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        loadScheduleData();
        loadSuggestions();
    }

    private void loadScheduleData() {
        injections = new ArrayList<>();

        childIds.forEach(id -> {
            try {
                var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                        .url(AppConstants.BASE_URL + "/children/" + id + "/injections")
                        .method("GET")
                        .build().request();
                injections.addAll(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        injections = injections.stream()
                .filter(i -> !i.get("status").toString().equals("Chờ xác nhận")
                        && !LocalDate.parse(i.get("date").toString()).isBefore(LocalDate.now()))
                .sorted((Comparator.comparing(o -> o.get("date").toString())))
                .toList();

        var items = injections.stream().map(ScreenManager::getScheduleItem).toList();
        schedulePanel.getChildren().setAll(items);
    }

    private void loadSuggestions() {
        suggestions = new ArrayList<>();

        childIds.forEach(id -> {
            try {
                var result = new ApiRequest.Builder<List<Map<String, Object>>>()
                        .url(AppConstants.BASE_URL + "/children/" + id + "/injections/suggestions")
                        .method("GET")
                        .build().request();
                suggestions.addAll(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        var items = suggestions.stream().map(ScreenManager::getInjectionSuggestionItem).toList();
        suggestionPanel.getChildren().setAll(items);
    }

    @FXML
    void registerForInjection(ActionEvent event) {
        ScreenManager.getAddInjectionStage(2, parentId, null).show();
    }

}
