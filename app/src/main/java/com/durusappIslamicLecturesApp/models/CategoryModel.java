package com.durusappIslamicLecturesApp.models;

import java.util.List;

public class CategoryModel {

    private boolean success;
    private List<ScholarsModel.Dar> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ScholarsModel.Dar> getData() {
        return data;
    }

    public void setData(List<ScholarsModel.Dar> data) {
        this.data = data;
    }
}
