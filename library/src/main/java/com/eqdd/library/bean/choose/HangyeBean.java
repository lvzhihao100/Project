package com.eqdd.library.bean.choose;


import com.eqdd.common.bean.MultiChooseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvzhihao on 17-4-17.
 */

public  class HangyeBean extends MultiChooseBean {

    /**
     * name : 农、林、牧、渔业
     * code : A
     * children : []
     */

    private String name;
    private String code;
    private List<HangyeBean> children;

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

    public List<HangyeBean> getChildren() {
        return children;
    }

    public void setChildren(List<HangyeBean> children) {
        this.children = children;
    }


    @Override
    public List<MultiChooseBean> getNextDeep() {
        List<MultiChooseBean> multiChooseBeen = new ArrayList<>();
        multiChooseBeen.addAll(children);
        return multiChooseBeen;
    }

    @Override
    public String getContent() {
        return name;
    }
}
