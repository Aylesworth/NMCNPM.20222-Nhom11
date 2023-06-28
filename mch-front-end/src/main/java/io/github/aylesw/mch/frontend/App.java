package io.github.aylesw.mch.frontend;

import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.AppConstants;
import io.github.aylesw.mch.frontend.common.Utils;
import io.github.aylesw.mch.frontend.controller.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;

public class App extends Application {

    private static long userId;

    private static String userFullName;

    public static long getUserId() {
        return userId;
    }

    public static String getUserFullName() {
        return userFullName;
    }

    public static void updateUserIdentity() throws Exception {
        var result = new ApiRequest.Builder<Map<String, Object>>()
                .url(AppConstants.BASE_URL + "/users/my-identity")
                .token(Utils.getToken())
                .method("GET")
                .build().request();
        userId = ((Double) result.get("id")).longValue();
        userFullName = result.get("name").toString();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            updateUserIdentity();
        } catch (Exception e) {
            ScreenManager.getLoginStage().show();
            return;
        }

//        ScreenManager.getLoginStage().show();

        ScreenManager.getMainStage().show();
        ScreenManager.setHeaderBar();
        ScreenManager.setNavBar(true);
//        ScreenManager.setMainPanel(ScreenManager.getManageUsersPanel());
    }
}
