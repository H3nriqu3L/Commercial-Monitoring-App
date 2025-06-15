package com.example.commercial_monitoring_app.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NavigationResponse {
    @SerializedName("dadosUsuarios")
    private UserDataWrapper userDataWrapper;

    @SerializedName("success")
    private boolean success;

    public UserDataWrapper getUserDataWrapper() {
        return userDataWrapper;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class UserDataWrapper {
        @SerializedName("success")
        private boolean success;

        @SerializedName("dados")
        private List<User> users;

        public List<User> getUsers() {
            return users;
        }
    }

    public static class User {
        @SerializedName("id")
        private String id;

        @SerializedName("nome")
        private String name;

        @SerializedName("email")
        private String email;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }
}