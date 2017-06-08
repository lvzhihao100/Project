package com.eqdd.library.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvzhihao on 17-5-9.
 */

public class ComInviteBean implements Parcelable {


    /**
     * icom : 啦咯啦咯啦咯
     * icomid : 091900001
     * ipeople : 吕志豪
     * isec : 创始人
     * ipost : 创始人
     * itel : 18281611381
     * iesec : 部门
     * iepost : 拒绝了
     * iepostnum : 11
     */

    private String icom;
    private String icomid;
    private String ipeople;
    private String isec;
    private String ipost;
    private String itel;
    private String iesec;
    private String iepost;
    private int iepostnum;

    public String getIcom() {
        return icom;
    }

    public void setIcom(String icom) {
        this.icom = icom;
    }

    public String getIcomid() {
        return icomid;
    }

    public void setIcomid(String icomid) {
        this.icomid = icomid;
    }

    public String getIpeople() {
        return ipeople;
    }

    public void setIpeople(String ipeople) {
        this.ipeople = ipeople;
    }

    public String getIsec() {
        return isec;
    }

    public void setIsec(String isec) {
        this.isec = isec;
    }

    public String getIpost() {
        return ipost;
    }

    public void setIpost(String ipost) {
        this.ipost = ipost;
    }

    public String getItel() {
        return itel;
    }

    public void setItel(String itel) {
        this.itel = itel;
    }

    public String getIesec() {
        return iesec;
    }

    public void setIesec(String iesec) {
        this.iesec = iesec;
    }

    public String getIepost() {
        return iepost;
    }

    public void setIepost(String iepost) {
        this.iepost = iepost;
    }

    public int getIepostnum() {
        return iepostnum;
    }

    public void setIepostnum(int iepostnum) {
        this.iepostnum = iepostnum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.icom);
        dest.writeString(this.icomid);
        dest.writeString(this.ipeople);
        dest.writeString(this.isec);
        dest.writeString(this.ipost);
        dest.writeString(this.itel);
        dest.writeString(this.iesec);
        dest.writeString(this.iepost);
        dest.writeInt(this.iepostnum);
    }

    public ComInviteBean() {
    }

    protected ComInviteBean(Parcel in) {
        this.icom = in.readString();
        this.icomid = in.readString();
        this.ipeople = in.readString();
        this.isec = in.readString();
        this.ipost = in.readString();
        this.itel = in.readString();
        this.iesec = in.readString();
        this.iepost = in.readString();
        this.iepostnum = in.readInt();
    }

    public static final Creator<ComInviteBean> CREATOR = new Creator<ComInviteBean>() {
        @Override
        public ComInviteBean createFromParcel(Parcel source) {
            return new ComInviteBean(source);
        }

        @Override
        public ComInviteBean[] newArray(int size) {
            return new ComInviteBean[size];
        }
    };
}
