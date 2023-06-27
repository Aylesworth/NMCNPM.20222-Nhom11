package io.github.aylesw.mch.frontend.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {

    private static MainWindowController mainWindowController;

    private static Stage mainStage;

    static {
        mainWindowController = new MainWindowController();
        mainStage = loadStage("Quản lý Sức khỏe Mẹ và Bé", "main-window.fxml", mainWindowController);
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setNavBar(boolean adminView) {
        if (adminView) {
            mainWindowController.getBorderPane().setLeft(getAdminNavBar());
        } else {

        }
    }

    public static void setMainPanel(Parent root) {
        mainWindowController.getBorderPane().setCenter(root);
    }

    public static Stage getLoginStage() {
        return loadStage("Đăng nhập", "login.fxml", new LoginController());
    }

    public static Stage getSignUpStage() {
        return loadStage("Đăng ký", "sign-up.fxml", new SignUpController());
    }

    public static Parent getAdminNavBar() {
        return loadNode("nav-bar-admin.fxml", null);
    }

    public static Parent getManageUsersPanel() {
        return loadNode("manage-users.fxml", new ManageUsersController());
    }

    public static Stage getAddUserStage(ManageUsersController parent) {
        return loadStage("Thêm người dùng", "add-user.fxml", new AddUserController(parent));
    }

    public static Parent getUserDetailsPanel(long id, Parent previous, ManageUsersController previousController) {
        return loadNode("user-details.fxml", new UserDetailsController(id, previous, previousController));
    }

    public static Stage getAddChildStage(Object parent) {
        return loadStage("Thêm hồ sơ trẻ", "add-child.fxml", new AddChildController(parent));
    }

    public static Stage getAddChildStage(long parentId, UserDetailsController parent) {
        return loadStage("Thêm hồ sơ trẻ", "add-child.fxml", new AddChildController(parentId, parent));
    }

    public static Parent getChildRefItem(long id, String name) {
        return loadNode("child-ref-item.fxml", new ChildRefItemController(id, name));
    }

    public static Stage loadStage(String title, String resourceName, Object controller) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(loadScene(resourceName, controller));
        return stage;
    }

    public static Scene loadScene(String resourceName, Object controller) {
        Scene scene = new Scene(loadNode(resourceName, controller));
        return scene;
    }

    public static Parent loadNode(String resourceName, Object controller) {
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
