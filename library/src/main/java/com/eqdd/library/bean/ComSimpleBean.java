package com.eqdd.library.bean;

import java.io.Serializable;

/**
 * Created by lvzhihao on 17-5-10.
 */

public class ComSimpleBean implements Serializable{

    /**
     * comid : 043400000
     * comname : 第一家企业
     * comdutyman : 段宏梅
     * comdutytel : 15286837836
     * comtype : 私营企业
     * combusi : 出租车客运-5330
     * comadres : 吉林省四平市伊通满族自治县-0434
     * comcontact : 2314312344321
     * comemai : 3214242@1321
     * authen : 1
     */

    private String comid;
    private String comname;
    private String comdutyman;
    private String comdutytel;
    private String comtype;
    private String combusi;
    private String comadres;
    private String comcontact;
    private String comemai;
    private int authen;

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getComname() {
        return comname;
    }

    public void setComname(String comname) {
        this.comname = comname;
    }

    public String getComdutyman() {
        return comdutyman;
    }

    public void setComdutyman(String comdutyman) {
        this.comdutyman = comdutyman;
    }

    public String getComdutytel() {
        return comdutytel;
    }

    public void setComdutytel(String comdutytel) {
        this.comdutytel = comdutytel;
    }

    public String getComtype() {
        return comtype;
    }

    public void setComtype(String comtype) {
        this.comtype = comtype;
    }

    public String getCombusi() {
        return combusi;
    }

    public void setCombusi(String combusi) {
        this.combusi = combusi;
    }

    public String getComadres() {
        return comadres;
    }

    public void setComadres(String comadres) {
        this.comadres = comadres;
    }

    public String getComcontact() {
        return comcontact;
    }

    public void setComcontact(String comcontact) {
        this.comcontact = comcontact;
    }

    public String getComemai() {
        return comemai;
    }

    public void setComemai(String comemai) {
        this.comemai = comemai;
    }

    public int getAuthen() {
        return authen;
    }

    public void setAuthen(int authen) {
        this.authen = authen;
    }
}
