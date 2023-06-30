package io.github.aylesw.mch.frontend.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Map;

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

    public static void setHeaderBar() {
        mainWindowController.getBorderPane().setTop(getHeaderBar());
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

    public static Parent getHeaderBar() {
        return loadNode("header.fxml", null);
    }

    public static Parent getAdminNavBar() {
        return loadNode("nav-bar-admin.fxml", new AdminNavBarController());
    }

    public static Parent getManageUsersPanel() {
        return loadNode("manage-users.fxml", new ManageUsersController());
    }

    public static Stage getAddUserStage(ManageUsersController parent) {
        return loadStage("Thêm người dùng", "add-user.fxml", new AddUserController(parent));
    }

    public static Parent getUserDetailsPanel(long id, Parent previous, Object previousController) {
        return loadNode("user-details.fxml", new UserDetailsController(id, previous, previousController));
    }

    public static Stage getAddChildStage(ManageChildrenController parent) {
        return loadStage("Thêm hồ sơ trẻ", "add-child.fxml", new AddChildController(parent));
    }

    public static Stage getAddChildStage(long parentId, UserDetailsController parent) {
        return loadStage("Thêm hồ sơ trẻ", "add-child.fxml", new AddChildController(parentId, parent));
    }

    public static Parent getChildRefItem(long id, String name, Parent parent) {
        return loadNode("child-ref-item.fxml", new ChildRefItemController(id, name, parent));
    }

    public static Parent getNotificationItem(Map<String, Object> properties) {
        return loadNode("notification-item.fxml", new NotificationItemController(properties));
    }

    public static Parent getNotificationsPanel() {
        return loadNode("notifications.fxml", new NotificationsController());
    }

    public static Parent getManageChildrenPanel() {
        return loadNode("manage-children.fxml", new ManageChildrenController());
    }

    public static Parent getChildDetailsPanel(long id, Parent previous, Object previousController) {
        return loadNode("child-details.fxml", new ChildDetailsController(id, previous, previousController));
    }

    public static Parent getBodyMetricsItem(Map<String, Object> properties, ChildDetailsController parentController) {
        return loadNode("body-metrics-item.fxml", new BodyMetricsItemController(properties, parentController));
    }

    public static Stage getAddBodyMetricsStage(long childId, ChildDetailsController parentController) {
        return loadStage("Thêm chiều cao cân nặng", "add-body-metrics.fxml", new AddBodyMetricsController(childId, parentController));
    }

    public static Parent getInjectionItem(Map<String, Object> properties, ChildDetailsController parentController) {
        return loadNode("injection-item.fxml", new InjectionItemController(properties, parentController));
    }

    public static Stage getAddInjectionStage(long childId, ChildDetailsController parentController) {
        return loadStage("Thêm mũi tiêm", "add-injection.fxml", new AddInjectionController(childId, parentController));
    }

    public static Stage getAddInjectionStage(ManageInjectionsController parentController) {
        return loadStage("Thêm mũi tiêm", "add-injection.fxml", new AddInjectionController(parentController));
    }

    public static Stage getEditInjectionStage(Map<String, Object> properties, ChildDetailsController parentController) {
        return loadStage("Sửa mũi tiêm", "edit-injection.fxml", new EditInjectionController(properties, parentController));
    }

    public static Stage getAddExaminationStage(long childId, ChildDetailsController parentController) {
        return loadStage("Thêm thông tin khám chữa bệnh", "add-examination.fxml", new AddExaminationController(childId, parentController));
    }

    public static Parent getExaminationItem(Map<String, Object> properties, ChildDetailsController parentController) {
        return loadNode("examination-item.fxml", new ExaminationItemController(properties, parentController));
    }

    public static Stage getEditExaminationStage(Map<String, Object> properties, ChildDetailsController parentController) {
        return loadStage("Sửa thông tin khám chữa bệnh", "edit-examination.fxml", new EditExaminationController(properties, parentController));
    }

    public static Parent getManageInjectionsPanel() {
        return loadNode("manage-injections.fxml", new ManageInjectionsController());
    }

    public static Parent getManageEventsPanel() {
        return loadNode("manage-events.fxml", null);
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
