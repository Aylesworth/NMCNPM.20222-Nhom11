package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EventsListController implements Initializable {
    private long userId;

    public EventsListController(long userId) {
        this.userId = userId;
    }

    @FXML
    private FlowPane currentEventsList;

    @FXML
    private FlowPane myEventsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEvents();
    }

    void loadEvents() {
        try {
            var myEvents = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/events/find-by-user?id=" + userId)
                    .method("GET")
                    .build().request()
                    .stream().filter(event -> !LocalDate.parse(event.get("toDate").toString()).isBefore(LocalDate.now())).toList();

            var currentEvents = new ApiRequest.Builder<List<Map<String, Object>>>()
                    .url(AppConstants.BASE_URL + "/events/current")
                    .method("GET")
                    .build().request();

            var items = myEvents.stream().map(event -> ScreenManager.getEventCard(event, 2, this)).toList();
            myEventsList.getChildren().setAll(items);

            items = currentEvents.stream().map(event -> ScreenManager.getEventCard(event, myEvents.contains(event) ? 1 : 0, this)).toList();
            currentEventsList.getChildren().setAll(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
