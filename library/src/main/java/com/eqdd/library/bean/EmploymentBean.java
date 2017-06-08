package com.eqdd.library.bean;


import com.eqdd.common.adapter.recycleadapter.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by lvzhihao on 17-5-11.
 */

public class EmploymentBean implements Serializable,MultiItemEntity {

    /**
     * uid : 15286837836
     * photo : /upload/2017/4-29/15286837836/20170429113130.png
     * name : 爱新觉罗·玄烨
     * sex : 0
     * content : 15286837836
     * careername : Ceshi
     * postname : d125
     * entertimes : 2017/5/11 0:00:00
     */

    private String uid;
    private String photo;
    private String name;
    private int sex;
    private String content;
    private String careername;
    private String postname;
    private String entertimes;

    @Override
    public int getItemType() {
        return MultiItemEntity.EMPLOYMENT;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCareername() {
        return careername;
    }

    public void setCareername(String careername) {
        this.careername = careername;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public String getEntertimes() {
        return entertimes;
    }

    public void setEntertimes(String entertimes) {
        this.entertimes = entertimes;
    }
}
