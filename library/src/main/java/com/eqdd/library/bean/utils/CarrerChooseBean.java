package com.eqdd.library.bean.utils;

import com.eqdd.common.bean.MultiChooseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvzhihao on 17-5-11.
 */

public class CarrerChooseBean extends MultiChooseBean {
    /**
     * name : 计算机硬件类
     * code : 01
     * sub : [{"name":"高级硬件工程师","code":"0101","english":"Senior Hardware Engineer"},{"name":"硬件工程师","code":"0102","english":"Hardware Engineer"},{"name":"其他","code":"0103","english":"Others"}]
     */

    private String name;
    private String code;
    private List<CarrerChooseBean> sub;
    private String english;

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
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

    public List<CarrerChooseBean> getSub() {
        return sub;
    }

    public void setSub(List<CarrerChooseBean> sub) {
        this.sub = sub;
    }

    @Override
    public List<MultiChooseBean> getNextDeep() {
        List<MultiChooseBean> multiChooseBeen = new ArrayList<>();
        multiChooseBeen.addAll(sub);
        return multiChooseBeen;
    }

    @Override
    public String getContent() {
        return name;
    }
}
