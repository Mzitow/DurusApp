package com.durusappIslamicLecturesApp.models;

public class LoginResponse {

    private boolean success;
    private Datam data;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Datam getData() {
        return data;
    }

    public void setData(Datam data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Datam{

        private  int id;
        private  String usergroup_id;
        private  String user_name;
        private String phone_number;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsergroup_id() {
            return usergroup_id;
        }

        public void setUsergroup_id(String usergroup_id) {
            this.usergroup_id = usergroup_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }
    }

}
