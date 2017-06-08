package com.eqdd.library.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by lvzhihao on 17-4-6.
 */
@Entity
public class User {
    @Id(autoincrement = true)
    Long id;
    private String uname;
    private String iphoto;
    private String pname;
    private String name;
    private int sex;
    private String nation;
    private String birth;
    private String bplace;
    private String email;
    private String comid;
    private int authen;
    @Generated(hash = 1627484910)
    public User(Long id, String uname, String iphoto, String pname, String name,
                int sex, String nation, String birth, String bplace, String email,
                String comid, int authen) {
        this.id = id;
        this.uname = uname;
        this.iphoto = iphoto;
        this.pname = pname;
        this.name = name;
        this.sex = sex;
        this.nation = nation;
        this.birth = birth;
        this.bplace = bplace;
        this.email = email;
        this.comid = comid;
        this.authen = authen;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUname() {
        return this.uname;
    }
    public void setUname(String uname) {
        this.uname = uname;
    }
    public String getIphoto() {
        return this.iphoto;
    }
    public void setIphoto(String iphoto) {
        this.iphoto = iphoto;
    }
    public String getPname() {
        return this.pname;
    }
    public void setPname(String pname) {
        this.pname = pname;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getNation() {
        return this.nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }
    public String getBirth() {
        return this.birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }
    public String getBplace() {
        return this.bplace;
    }
    public void setBplace(String bplace) {
        this.bplace = bplace;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getComid() {
        return this.comid;
    }
    public void setComid(String comid) {
        this.comid = comid;
    }
    public int getAuthen() {
        return this.authen;
    }
    public void setAuthen(int authen) {
        this.authen = authen;
    }

}