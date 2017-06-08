package com.eqdd.library.bean.multiitem;


import com.eqdd.common.adapter.recycleadapter.entity.MultiItemEntity;

/**
 * Created by lvzhihao on 17-4-24.
 */

public class EditMultiBean implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int EDIT = 2;
    private int itemType;
    String title;
    String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public EditMultiBean(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
