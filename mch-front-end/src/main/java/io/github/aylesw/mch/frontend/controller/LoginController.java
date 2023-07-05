package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.RequestBodyMap;
import io.github.aylesw.mch.frontend.common.UserIdentity;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Map;

import static io.github.aylesw.mch.frontend.common.AppConstants.BASE_URL;
import static io.github.aylesw.mch.frontend.common.AppConstants.ERROR_BACKGROUND;

public class LoginController {

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnForgotPassword;

    @FXML
    private JFXButton btnSignUp;

    @FXML
    private Label lblError;

    @FXML
    void login() {
        Utils.saveToken("");

        try {
            validateFields();
        } catch (Exception e) {
            lblError.setText(e.getMessage());
            return;
        }

        String url = BASE_URL + "/auth/login";
        String method = "POST";
        String requestBody = new RequestBodyMap()
                .put("email", txtEmail.getText())
                .put("password", txtPassword.getText())
                .toJson();

        Map<String, Object> response = null;
        try {
            response = new ApiRequest.Builder<Map<String, Object>>()
                    .url(url)
                    .method(method)
                    .requestBody(requestBody)
                    .build().request();

            String token = response.get("token").toString();
            Utils.saveToken(token);
            UserIdentity.updateUserIdentity();

            ((Stage) txtEmail.getScene().getWindow()).close();

            ScreenManager.getMainStage().show();
            ScreenManager.setMainPanel(null);
            ScreenManager.setNavBar();
            ScreenManager.setHeaderBar();
        } catch (Exception e) {
            if (e.getMessage().contains("No value present") || e.getMessage().contains("Bad credentials") || e instanceof IllegalStateException) {
                lblError.setText("Email hoặc mật khẩu không đúng!");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void validateFields() throws Exception {
        txtEmail.setStyle("");
        txtPassword.setStyle("");
        lblError.setText("");
        if (txtEmail.getText().isBlank()) {
            txtEmail.setStyle(ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập email!");
        }
        if (txtPassword.getText().isEmpty()) {
            txtPassword.setStyle(ERROR_BACKGROUND);
            throw new Exception("Vui lòng nhập mật khẩu!");
        }
    }

    @FXML
    void resetPassword() {
        ScreenManager.getResetPasswordStage().show();
    }

    @FXML
    void signUp() {
        ScreenManager.getSignUpStage().show();
    }
}

