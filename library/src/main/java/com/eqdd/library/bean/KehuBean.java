package com.eqdd.library.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lvzhihao on 17-5-24.
 */

public class KehuBean implements Parcelable {
    String name;
    String type;
    String area;
    String adress;
    String tel;
    String httpUrl;
    String remark;
    List<String> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.area);
        dest.writeString(this.adress);
        dest.writeString(this.tel);
        dest.writeString(this.httpUrl);
        dest.writeString(this.remark);
        dest.writeStringList(this.images);
    }

    public KehuBean() {
    }

    protected KehuBean(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.area = in.readString();
        this.adress = in.readString();
        this.tel = in.readString();
        this.httpUrl = in.readString();
        this.remark = in.readString();
        this.images = in.createStringArrayList();
    }

    public static final Creator<KehuBean> CREATOR = new Creator<KehuBean>() {
        @Override
        public KehuBean createFromParcel(Parcel source) {
            return new KehuBean(source);
        }

        @Override
        public KehuBean[] newArray(int size) {
            return new KehuBean[size];
        }
    };
}
