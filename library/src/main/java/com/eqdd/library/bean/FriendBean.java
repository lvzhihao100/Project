package com.eqdd.library.bean;


import com.eqdd.common.utils.PinYinUtil;
import com.eqdd.library.http.HttpConfig;

/**
 * Created by lvzhihao on 17-4-7.
 */

public class FriendBean {
    /**
     * iphoto : 0
     * uname : 18281611382
     * pname : 游客
     */

    private String iphoto;
    private String uname;
    private String pname;


    private String pinyin= PinYinUtil.getPinyin("游客");
    private String headerWord=pinyin.substring(0, 1);

    @Override
    public String toString() {
        return "FriendBean{" +
                "iphoto='" + iphoto + '\'' +
                ", uname='" + uname + '\'' +
                ", pname='" + pname + '\'' +
                ", headerWord='" + headerWord + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }

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
        this.uname = uname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
