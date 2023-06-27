package io.github.aylesw.mch.frontend;

import io.github.aylesw.mch.frontend.controller.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;

//        ScreenManager.getLoginStage().show();
//        ScreenManager.getSignUpStage().show();

        ScreenManager.getMainStage().show();
        ScreenManager.setNavBar(true);
        ScreenManager.setMainPanel(ScreenManager.getManageUsersPanel());
    }
}
