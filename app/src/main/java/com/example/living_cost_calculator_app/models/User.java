package com.example.living_cost_calculator_app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public User() {}

    public User(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }
    public static class login_user implements Serializable{
        @SerializedName("usernameOrEmail")
        private String usernameOrEmail;

        @SerializedName("password")
        private String password;

        public login_user(String usernameOrEmail, String password) {
            this.usernameOrEmail = usernameOrEmail;
            this.password = password;
        }
    }

    public static class token implements Serializable{
        @SerializedName("token")
        private String token;

        public token(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class register_user implements Serializable{
        @SerializedName("email")
        private String email;

        @SerializedName("username")
        private String username;

        @SerializedName("password")
        private String password;

        public register_user(String email, String username, String password) {
            this.email = email;
            this.username = username;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class search_user implements Serializable{
        @SerializedName("q")
        private String q;

        public search_user(String q) {
            this.q = q;
        }

        public String getQ() {
            return q;
        }

        public void setQ(String q) {
            this.q = q;
        }
    }
}
