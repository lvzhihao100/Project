package com.eqdd.library.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.eqdd.common.utils.PinYinUtil;
import com.eqdd.library.http.HttpConfig;


/**
 * Created by lvzhihao on 17-4-7.
 */

public class FriendApplyBean implements Parcelable {

    /**
     * iphoto : 0
     * uname : 15286837836
     * name : 0
     * pname : 游客00003
     * sex : 0
     * nation : 0
     * birth : 1900-01-01T00:00:00
     * bplace : 0
     * email : 0
     * authen : 0
     */

    private String iphoto;
    private String uname;
    private String name;
    private String pname;
    private int sex;
    private String nation;
    private String birth;
    private String bplace;
    private String email;
    private int authen;


    private String headerWord;
    private String pinyin;

    public String getHeaderWord() {
        return headerWord;
    }

    public void setHeaderWord(String headerWord) {
        this.headerWord = headerWord;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getIphoto() {
        return HttpConfig.BASE_URL_NO+iphoto;
    }

    public void setIphoto(String iphoto) {
        this.iphoto = iphoto;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        pinyin = PinYinUtil.getPinyin("游客");
        headerWord = pinyin.substring(0, 1);
        this.uname = uname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        pinyin = PinYinUtil.getPinyin(name);
        headerWord = pinyin.substring(0, 1);
        this.name = name;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBplace() {
        return bplace;
    }

    public void setBplace(String bplace) {
        this.bplace = bplace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAuthen() {
        return authen;
    }

    public void setAuthen(int authen) {
        this.authen = authen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iphoto);
        dest.writeString(this.uname);
        dest.writeString(this.name);
        dest.writeString(this.pname);
        dest.writeInt(this.sex);
        dest.writeString(this.nation);
        dest.writeString(this.birth);
        dest.writeString(this.bplace);
        dest.writeString(this.email);
        dest.writeInt(this.authen);
        dest.writeString(this.headerWord);
        dest.writeString(this.pinyin);
    }

    public FriendApplyBean() {
    }

    protected FriendApplyBean(Parcel in) {
        this.iphoto = in.readString();
        this.uname = in.readString();
        this.name = in.readString();
        this.pname = in.readString();
        this.sex = in.readInt();
        this.nation = in.readString();
        this.birth = in.readString();
        this.bplace = in.readString();
        this.email = in.readString();
        this.authen = in.readInt();
        this.headerWord = in.readString();
        this.pinyin = in.readString();
    }

    public static final Creator<FriendApplyBean> CREATOR = new Creator<FriendApplyBean>() {
        @Override
        public FriendApplyBean createFromParcel(Parcel source) {
            return new FriendApplyBean(source);
        }

        @Override
        public FriendApplyBean[] newArray(int size) {
            return new FriendApplyBean[size];
        }
    };
}
