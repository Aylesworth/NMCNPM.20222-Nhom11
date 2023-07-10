package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.UserIdentity;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
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

    public static void setNavBar() {
        mainWindowController.getBorderPane().setLeft(getNavBar());
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
        return loadNode("header.fxml", new HeaderController());
    }

    public static Parent getNavBar() {
        if (UserIdentity.getRoles().contains("ADMIN"))
            return loadNode("nav-bar-admin.fxml", new NavBarAdminController(UserIdentity.getUserId()));
        return loadNode("nav-bar.fxml", new NavBarController(UserIdentity.getUserId()));
    }

    public static Parent getAboutPage() {
        return loadNode("about.fxml", new AboutController());
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

    public static Stage getAddChildStage(ManageChildrenController parentController) {
        return loadStage("Thêm hồ sơ trẻ", "add-child.fxml", new AddChildController(parentController));
    }

    public static Stage getAddChildStage(long parentId, UserDetailsController parentController) {
        return loadStage("Thêm hồ sơ trẻ", "add-child.fxml", new AddChildController(parentId, parentController));
    }

    public static Stage getAddChildStage(long parentId, ChildrenListController parentController) {
        return loadStage("Thêm hồ sơ trẻ", "add-child.fxml", new AddChildController(parentId, parentController));
    }

    public static Parent getChildRefItem(long id, String name, String sex, Parent parent, Object parentController) {
        return loadNode("child-ref-item.fxml", new ChildRefItemController(id, name, sex, parent, parentController));
    }

    public static Parent getNotificationItem(Map<String, Object> properties) {
        return loadNode("notification-item.fxml", new NotificationItemController(properties));
    }

    public static Parent getNotificationsPanel(long userId) {
        return loadNode("notifications.fxml", new NotificationsController(userId));
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

    public static Stage getAddInjectionStage(int mode, long id, ChildDetailsController parentController) {
        return loadStage("Thêm mũi tiêm", "add-injection.fxml", new AddInjectionController(mode, id, parentController));
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
        return loadNode("manage-events.fxml", new ManageEventsController());
    }

    public static Stage getAddEventStage(ManageEventsController parentController) {
        return loadStage("Thêm sự kiện", "add-event.fxml", new AddEventController(parentController));
    }

    public static Stage getAddUserToEventStage(long eventId, ManageEventsController parentController) {
        return loadStage("Thêm người tham dự", "add-user-to-event.fxml", new AddUserToEventController(eventId, parentController));
    }

    public static Parent getChildCard(Map<String, Object> properties, Parent parent, ChildrenListController parentController) {
        return loadNode("child-card.fxml", new ChildCardController(properties, parent, parentController));
    }

    public static Parent getChildrenListPanel(long userId) {
        return loadNode("children-list.fxml", new ChildrenListController(userId));
    }

    public static Parent getEventCard(Map<String, Object> properties, int state, EventsListController parentController) {
        return loadNode("event-card.fxml", new EventCardController(properties, state, parentController));
    }

    public static Parent getEventsListPanel(long userId) {
        return loadNode("events-list.fxml", new EventsListController(userId));
    }

    public static Stage getChangePasswordStage() {
        return loadStage("Đổi mật khẩu", "change-password.fxml", new ChangePasswordController());
    }

    public static Stage getResetPasswordStage() {
        return loadStage("Quên mật khẩu", "reset-password.fxml", new ResetPasswordController());
    }

    public static Parent getDashboardAdminPanel() {
        return loadNode("dashboard-admin.fxml", new DashboardAdminController());
    }

    public static Stage getGrowthChartsStage(List<Map<String, Object>> bodyMetricsData) {
        return loadStage("Biểu đồ phát triển", "growth-charts.fxml", new GrowthChartsController(bodyMetricsData));
    }

    public static Parent getInjectionsPanel(long parentId) {
        return loadNode("injections.fxml", new InjectionsController(parentId));
    }

    public static Parent getScheduleItem(Map<String, Object> properties) {
        return loadNode("schedule-item.fxml", new ScheduleItemController(properties));
    }

    public static Parent getInjectionSuggestionItem(Map<String,Object> properties) {
        return loadNode("injection-suggestion-item.fxml", new InjectionSuggestionItemController(properties));
    }

    public static Stage loadStage(String title, String resourceName, Object controller) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(loadScene(resourceName, controller));
        try {
            stage.getIcons().add(new Image(ScreenManager.class.getResource("/image/logo.png").toURI().toURL().toString()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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
