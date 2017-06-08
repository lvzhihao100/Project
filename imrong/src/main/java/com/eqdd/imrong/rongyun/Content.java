package com.eqdd.imrong.rongyun;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvzhihao on 17-5-1.
 */

public class Content implements Parcelable {
    private String name="";
    private String imgurl="";
    private String bumen="";
    private String gangwei = "";
    private String company = "";
    private String uid = "";
    private String comid = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getBumen() {
        return bumen;
    }

    public void setBumen(String bumen) {
        this.bumen = bumen;
    }

    public String getGangwei() {
        return gangwei;
    }

    public void setGangwei(String gangwei) {
        this.gangwei = gangwei;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.imgurl);
        dest.writeString(this.bumen);
        dest.writeString(this.gangwei);
        dest.writeString(this.company);
        dest.writeString(this.uid);
        dest.writeString(this.comid);
    }

    public Content(String name, String imgurl, String bumen, String gangwei, String company, String uid, String comid) {
        this.name = name;
        this.imgurl = imgurl;
        this.bumen = bumen;
        this.gangwei = gangwei;
        this.company = company;
        this.uid = uid;
        this.comid = comid;
    }

    public Content() {
    }

    protected Content(Parcel in) {
        this.name = in.readString();
        this.imgurl = in.readString();
        this.bumen = in.readString();
        this.gangwei = in.readString();
        this.company = in.readString();
        this.uid = in.readString();
        this.comid = in.readString();
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel source) {
            return new Content(source);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
