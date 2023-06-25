package io.github.aylesw.mch.frontend;

import io.github.aylesw.mch.frontend.controller.MainFrameController;
import io.github.aylesw.mch.frontend.controller.ManageUserController;
import io.github.aylesw.mch.frontend.controller.ScreenManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

//        ScreenManager.getLoginWindow().show();

        ScreenManager.getMainFrame().show();
        ScreenManager.getMainFrameController().getBorderPane().setLeft(ScreenManager.getAdminNavigationBar());
        ScreenManager.getMainFrameController().getBorderPane().setCenter(ScreenManager.getManageUsersPanel());
    }
}
