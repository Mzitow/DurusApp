package com.durusappIslamicLecturesApp.models;

public class LoginRequest {

    private String user_name;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
