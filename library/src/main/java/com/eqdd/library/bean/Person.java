package com.eqdd.library.bean;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.eqdd.common.adapter.recycleadapter.entity.MultiItemEntity;
import com.eqdd.common.utils.PinYinUtil;


/*
 * 文件名:     Person
 * 创建者:     阿钟
 * 创建时间:   2016/11/17 19:07
 * 描述:       封装联系人列表信息
 */
public class Person implements Parcelable, MultiItemEntity, ComparaEntity {

    private boolean isCheck = false;
    private boolean isEnable = true;
    //姓名
    private String name;
    //拼音
    private String pinyin;
    //拼音首字母
    private String headerWord;
    //手机号码
    private String phone;
    //邮箱
    private String email;
    //头像
    private Bitmap photo;

    private int itemType = 0;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
        this.pinyin = PinYinUtil.getPinyin(name);
        headerWord = pinyin.substring(0, 1);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", headerWord='" + headerWord + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", photo=" + photo +
                '}';
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeaderWord() {
        return headerWord;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.pinyin);
        dest.writeString(this.headerWord);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeParcelable(this.photo, flags);
    }

    protected Person(Parcel in) {
        this.name = in.readString();
        this.pinyin = in.readString();
        this.headerWord = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.photo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public String getComparaContent() {
        return headerWord;
    }
}