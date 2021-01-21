package com.durusappIslamicLecturesApp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public   class ScholarsModel {

    @SerializedName("data")
    @Expose
    private List<Scholar> data = null;

    public List<Scholar> getData() {
        return data;
    }

    public void setData(List<Scholar> data) {
        this.data = data;
    }

    public static class Scholar  implements Parcelable {

        public Scholar() {
        }

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("scholar_name")
        @Expose
        private String scholarName;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("images")
        @Expose
        private List<String> images = null;
        @SerializedName("dars")
        @Expose
        private List<Dar> dars = null;

        public Scholar(Parcel in) {
            if (in.readByte() == 0) {
                id = null;
            } else {
                id = in.readInt();
            }
            userId = in.readString();
            title = in.readString();
            scholarName = in.readString();
            countryId = in.readString();
            address = in.readString();
            images = in.createStringArrayList();
        }

        public  static  final Creator<Scholar> CREATOR = new Creator<Scholar>() {
            @Override
            public Scholar createFromParcel(Parcel in) {
                return new Scholar(in);
            }

            @Override
            public Scholar[] newArray(int size) {
                return new Scholar[size];
            }
        };

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getScholarName() {
            return scholarName;
        }

        public void setScholarName(String scholarName) {
            this.scholarName = scholarName;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public List<Dar> getDars() {
            return dars;
        }

        public void setDars(List<Dar> dars) {
            this.dars = dars;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (id == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(id);
            }
            dest.writeString(userId);
            dest.writeString(title);
            dest.writeString(scholarName);
            dest.writeString(countryId);
            dest.writeString(address);
            dest.writeStringList(images);
        }
    }

    public static  class Dar {

        @SerializedName("darsType_name")
        @Expose
        private String darsType_name;

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("scholar_id")
        @Expose
        private String scholarId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("dars_type_id")
        @Expose
        private String darsTypeId;
        @SerializedName("duration")
        @Expose
        private String duration;
        @SerializedName("dars_title")
        @Expose
        private String darsTitle;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("scholar_name")
        @Expose
        private String scholarName;
        @SerializedName("category_name")
        @Expose
        private String categoryName;

        @SerializedName("dar_audio")
        @Expose
        private List<DarAudio> darAudio = null;

        public List<DarAudio> getDarAudio() {
            return darAudio;
        }

        public void setDarAudio(List<DarAudio> darAudio) {
            this.darAudio = darAudio;
        }

        public String getDarsType_name() {
            return darsType_name;
        }

        public void setDarsType_name(String darsType_name) {
            this.darsType_name = darsType_name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getScholarId() {
            return scholarId;
        }

        public void setScholarId(String scholarId) {
            this.scholarId = scholarId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getDarsTypeId() {
            return darsTypeId;
        }

        public void setDarsTypeId(String darsTypeId) {
            this.darsTypeId = darsTypeId;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getDarsTitle() {
            return darsTitle;
        }

        public void setDarsTitle(String darsTitle) {
            this.darsTitle = darsTitle;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getScholarName() {
            return scholarName;
        }

        public void setScholarName(String scholarName) {
            this.scholarName = scholarName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

    }

    public static class DarAudio {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("dars_id")
        @Expose
        private String darsId;
        @SerializedName("images")
        @Expose
        private List<String> images = null;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDarsId() {
            return darsId;
        }

        public void setDarsId(String darsId) {
            this.darsId = darsId;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }


    }
}









