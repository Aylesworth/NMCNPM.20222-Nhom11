package io.github.aylesw.mch.frontend.common;

import java.util.List;
import java.util.Map;

public class UserIdentity {

    private static long userId;

    private static String userFullName;

    private static List<String> roles;

    public static long getUserId() {
        return userId;
    }

    public static String getUserFullName() {
        return userFullName;
    }

    public static List<String> getRoles() {
        return roles;
    }

    public static boolean isAdmin() {
        return getRoles().contains("ADMIN");
    }

    public static void updateUserIdentity() throws Exception {
        var result = new ApiRequest.Builder<Map<String, Object>>()
                .url(AppConstants.BASE_URL + "/users/my-identity")
                .token(Utils.getToken())
                .method("GET")
                .build().request();
//        if (result == null) return;
        userId = ((Double) result.get("id")).longValue();
        userFullName = result.get("name").toString();
        roles = (List<String>) result.get("roles");
    }
}
