package com.durusappIslamicLecturesApp.models;
public class SignUpRequest {

    private final int usergroup_id;
    private final String user_name;
    private final String password;
    private final String phone_number;

    public SignUpRequest(int usergroup_id, String user_name, String password, String phone_number) {
        this.usergroup_id = usergroup_id;
        this.user_name = user_name;
        this.password = password;
        this.phone_number = phone_number;
    }
}
