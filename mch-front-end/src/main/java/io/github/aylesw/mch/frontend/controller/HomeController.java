package io.github.aylesw.mch.frontend.controller;

import io.github.aylesw.mch.frontend.common.UserIdentity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        text1.setText("Ứng dụng Quản Lý Sức Khỏe Mẹ Và Bé là người bạn đồng hành đáng tin cậy cho tất cả các bà mẹ và bé yêu. " +
                "Với sứ mệnh đem lại trải nghiệm tốt nhất cho gia đình của bạn, " +
                "chúng tôi đã phát triển một ứng dụng đa chức năng và độc đáo.");
        text2.setText("Với ứng dụng Quản Lý Sức Khỏe Mẹ Và Bé, bạn không còn lo lắng về việc quản lý thông tin và theo dõi sức khỏe của bé. " +
                "Hãy để chúng tôi đồng hành cùng bạn trong hành trình chăm sóc và nuôi dưỡng bé yêu, " +
                "mang đến sự yên tâm và tiện ích hàng ngày cho gia đình của bạn.");
    }

    @FXML
    void children(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getChildrenListPanel(UserIdentity.getUserId()));
    }

    @FXML
    void events(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getEventsListPanel(UserIdentity.getUserId()));
    }

    @FXML
    void injections(ActionEvent event) {
        ScreenManager.setMainPanel(ScreenManager.getInjectionsPanel(UserIdentity.getUserId()));
    }

}

