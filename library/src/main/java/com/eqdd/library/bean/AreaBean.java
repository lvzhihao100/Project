package com.eqdd.library.bean;

import java.util.List;

/**
 * Created by lvzhihao on 17-4-12.
 */

public class AreaBean {

    /**
     * name : 市辖区
     * code : 010
     * sub : []
     */

    private String name;
    private String code;
    private String qucode;
    private List<AreaBean> sub;

    @Override
    public String toString() {
        return "AreaBean{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", qucode='" + qucode + '\'' +
                ", sub=" + sub +
                '}';
    }

    public String getQucode() {
        return qucode;
    }

    public void setQucode(String qucode) {
        this.qucode = qucode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AreaBean> getSub() {
        return sub;
    }

    public void setSub(List<AreaBean> sub) {
        this.sub = sub;
    }
}
