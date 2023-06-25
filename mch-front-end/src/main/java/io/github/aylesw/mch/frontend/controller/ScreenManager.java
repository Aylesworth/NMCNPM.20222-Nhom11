package io.github.aylesw.mch.frontend.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {

    private static MainFrameController mainFrameController = new MainFrameController();

    private static Stage mainFrame;

    public static Stage getMainFrame() {
        if (mainFrame == null) {
            mainFrame = new Stage();
            mainFrame.setTitle("Quản lý Sức khỏe Mẹ và Bé");
            mainFrame.setScene(loadScene("main-frame.fxml", mainFrameController));
        }
        return mainFrame;
    }

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static Stage getLoginWindow() {
        Stage stage = new Stage();
        stage.setTitle("Đăng nhập");
        stage.setScene(loadScene("login.fxml", new LoginController()));
        return stage;
    }

    public static Stage getSignUpWindow() {
        Stage stage = new Stage();
        stage.setTitle("Đăng ký");
        stage.setScene(loadScene("sign-up.fxml", new SignUpController()));
        return stage;
    }

    public static Stage getAddUserWindow(ManageUserController parent) {
        Stage stage = new Stage();
        stage.setTitle("Thêm người dùng");
        stage.setScene(loadScene("add-user.fxml", new AddUserController(stage, parent)));
        return stage;
    }

    public static Stage getEditUserWindow(Long userId, ManageUserController parent) {
        Stage stage = new Stage();
        stage.setTitle("Sửa người dùng");
        stage.setScene(loadScene("edit-user.fxml", new EditUserController(userId, stage, parent)));
        return stage;
    }

    public static Scene loadScene(String resourceName, Object controller) {
        return new Scene(loadRoot(resourceName, controller));
    }

    public static Parent loadRoot(String resourceName, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(ScreenManager.class.getResource("/view/" + resourceName).toURI().toURL());
            loader.setController(controller);
            return loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
