package io.github.aylesw.mch.frontend.common;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.github.aylesw.mch.frontend.controller.ScreenManager;
import javafx.scene.control.Alert;
import lombok.AllArgsConstructor;
import okhttp3.*;

import java.lang.reflect.Type;
import java.net.ConnectException;

@AllArgsConstructor
public class ApiRequest<T> {

    private String url;

    private String token = Utils.getToken();

    private String method;

    private String requestBody;

    private ApiRequest() {
    }

    public static class Builder<T> {

        private ApiRequest<T> apiRequest;

        public Builder() {
            apiRequest = new ApiRequest<T>();
        }

        public Builder<T> url(String url) {
            apiRequest.url = url;
            return this;
        }

        public Builder<T> token(String token) {
            apiRequest.token = token;
            return this;
        }

        public Builder<T> method(String method) {
            apiRequest.method = method;
            return this;
        }

        public Builder<T> requestBody(String requestBody) {
            apiRequest.requestBody = requestBody;
            return this;
        }

        public ApiRequest<T> build() {
            return apiRequest;
        }
    }

    public T request() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token != null ? "Bearer " + token : "")
                .method(method,
                        method.matches("GET|DELETE") ? null :
                                RequestBody.create(requestBody == null ? "" : requestBody, MediaType.parse("application/json")))
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (ConnectException e) {
            Utils.showAlert(Alert.AlertType.ERROR, "Lỗi: Không thể kết nối tới máy chủ. Vui lòng thử lại sau.");
            throw e;
        }
        String responseBody = response.body().string();
        if (responseBody.contains("Token has expired")) {
            Utils.showAlert(Alert.AlertType.WARNING, "Phiên đăng nhập của bạn đã hết hạn!");
            ScreenManager.getLoginStage().show();
            throw new Exception(responseBody);
        }
        if (!response.isSuccessful()) {
            if (response.code() == 403)
                ScreenManager.getLoginStage().show();
            throw new ApiRequestException(responseBody);
        }

        Type type = new TypeToken<T>() {
        }.getType();
        try {
            return Beans.GSON.fromJson(responseBody, type);
        } catch (JsonSyntaxException e) {
            return (T) responseBody;
        }
    }
}
