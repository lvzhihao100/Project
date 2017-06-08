package com.eqdd.library.ui.select;

import com.yalantis.ucrop.entity.LocalMedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvzhihao on 17-5-25.
 */

public class RichTextResult implements Serializable {
    private String content;
    private List<LocalMedia> selectMedia = new ArrayList();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<LocalMedia> getSelectMedia() {
        return selectMedia;
    }

    public void setSelectMedia(List<LocalMedia> selectMedia) {
        this.selectMedia = selectMedia;
    }
}
