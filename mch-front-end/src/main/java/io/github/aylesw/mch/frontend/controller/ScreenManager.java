package io.github.aylesw.mch.frontend.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {

    private static MainFrameController mainFrameController;

    private static Stage mainFrame;

    static {
        mainFrameController = new MainFrameController();
        mainFrame = new Stage();
        mainFrame.setTitle("Quản lý Sức khỏe Mẹ và Bé");
        mainFrame.setScene(loadScene("main-frame.fxml", mainFrameController));
    }

    public static Stage getMainFrame() {
        return mainFrame;
    }

    public static MainFrameController getMainFrameController() {
        return mainFrameController;
    }

    public static Stage loginStage() {
        return loadStage("Đăng nhập", "login.fxml", new LoginController());
    }

    public static Stage signUpStage() {
        return loadStage("Đăng ký", "sign-up.fxml", new SignUpController());
    }

    public static Parent adminNavBar() {
        return loadRoot("nav-bar-admin.fxml", null);
    }

    public static Parent manageUsersPanel() {
        return loadRoot("manage-users.fxml", new ManageUsersController());
    }

    public static Stage addUserStage() {
        return loadStage("Thêm người dùng", "add-user.fxml", new AddUserController());
    }


    public static Stage loadStage(String title, String resourceName, Object controller) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(loadScene(resourceName, controller));
        return stage;
    }

    public static Scene loadScene(String resourceName, Object controller) {
        Scene scene = new Scene(loadRoot(resourceName, controller));
        return scene;
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
