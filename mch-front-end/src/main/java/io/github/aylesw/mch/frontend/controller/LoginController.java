package io.github.aylesw.mch.frontend.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.github.aylesw.mch.frontend.Main;
import io.github.aylesw.mch.frontend.common.ApiRequest;
import io.github.aylesw.mch.frontend.common.RequestBodyMap;
import io.github.aylesw.mch.frontend.common.Utils;
import javafx.fxml.FXML;

import java.util.Map;

import static io.github.aylesw.mch.frontend.common.AppConstants.BASE_URL;

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
    void login() {
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
            Main.updateUserIdentity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateFields() throws Exception {
    }

    @FXML
    void goToForgotPassword() {

    }

    @FXML
    void goToSignUp() {
        ScreenManager.getSignUpStage().show();
    }
}

