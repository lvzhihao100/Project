package com.eqdd.library.bean;

/**
 * Created by lvzhihao on 17-4-7.
 */
public class EnterBean {
    int icon;
    String content;
    boolean isEnter;
    boolean isTop;
    boolean isBottom;
    boolean isUnderLine;
    boolean isSwitch;
    boolean isIcon;

    public boolean isIcon() {
        return isIcon;
    }

    public void setIcon(boolean icon) {
        isIcon = icon;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }

    public boolean isUnderLine() {
        return isUnderLine;
    }

    public void setUnderLine(boolean underLine) {
        isUnderLine = underLine;
    }

    public EnterBean() {
        this.icon = 0;
        this.content = "";
        this.isEnter = false;
        this.isTop = false;
        this.isBottom = false;
        this.isUnderLine=true;
        this.isSwitch=false;
        this.isIcon=true;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEnter() {
        return isEnter;
    }

    public void setEnter(boolean enter) {
        isEnter = enter;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean bottom) {
        isBottom = bottom;
    }
}
