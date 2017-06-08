package com.eqdd.library.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lvzhihao on 17-5-2.
 */

public class SelectBean implements Parcelable ,ComparaEntity{

    public static final int PRIVITE=1;
    public static final int GROUP=2;
    int layoutType;
    String head;
    String content;
    int type;
    String id;
    String letter;
    private boolean isCheck=false;


    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.layoutType);
        dest.writeString(this.head);
        dest.writeString(this.content);
        dest.writeInt(this.type);
        dest.writeString(this.id);
        dest.writeString(this.letter);
    }

    public SelectBean() {
    }

    protected SelectBean(Parcel in) {
        this.layoutType = in.readInt();
        this.head = in.readString();
        this.content = in.readString();
        this.type = in.readInt();
        this.id = in.readString();
        this.letter = in.readString();
    }

    public static final Creator<SelectBean> CREATOR = new Creator<SelectBean>() {
        @Override
        public SelectBean createFromParcel(Parcel source) {
            return new SelectBean(source);
        }

        @Override
        public SelectBean[] newArray(int size) {
            return new SelectBean[size];
        }
    };

    @Override
    public String getComparaContent() {
        return letter;
    }
}
