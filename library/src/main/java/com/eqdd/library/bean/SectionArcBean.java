package com.eqdd.library.bean;

import com.eqdd.common.adapter.recycleadapter.entity.MultiItemEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lvzhihao on 17-4-20.
 */

public class SectionArcBean implements Serializable, MultiItemEntity {
//    {"id":8,"type":"未定","careename":"哦哦哦","postname":"ygg","postnum":"0919000013"}
    public static final int SECTION = 0;
    public static final int CAREER = 1;
    public static final int TITLE = 1;
    public static final int CONTENT = 0;
    /**
     * id : 0919000010
     * dname : 自己部门
     * fid : 0
     * childs : []
     */
    private String title;
    private int layoutType;
    private int dataType;
    private String type;
    private String careename;


    @Expose
    @SerializedName("postname")
    private String careepostname;
    private String id;
    private String dname;
    private String postnum;
    private String fid;
    private List<SectionArcBean> childs;


    public String getPostnum() {
        return postnum;
    }

    public void setPostnum(String postnum) {
        this.postnum = postnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCareename() {
        return careename;
    }

    public void setCareename(String careename) {
        this.careename = careename;
    }

    public String getCareepostname() {
        return careepostname;
    }

    public void setCareepostname(String careepostname) {
        this.careepostname = careepostname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public List<SectionArcBean> getChilds() {
        return childs;
    }

    public void setChilds(List<SectionArcBean> childs) {
        this.childs = childs;
    }

    @Override
    public int getItemType() {
        return layoutType;
    }
}
