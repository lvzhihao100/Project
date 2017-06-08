package com.eqdd.library.bean;


import com.eqdd.common.adapter.recycleadapter.entity.MultiItemEntity;
import com.github.yoojia.inputs.Scheme;

import java.util.List;

public class EditBean implements MultiItemEntity {
	public static final int TITLE=-1;
	public static final int CONTENT=1;
	String title;
	String content;
	String hint;
	int type;//0文本，1,选择，2 日期 日，3 日期 妙,4,自己处理
	String[] chooseItem;
	String textTitle;
	String textTip;
	String topTitle;
	List<Scheme> schemes;
	boolean isShowTopTitle=false;
	boolean isEnter=true;
	boolean isTop=false;
	boolean isBottom=false;
	boolean isUnderLine=true;


	public List<Scheme> getSchemes() {
		return schemes;
	}

	public void setSchemes(List<Scheme> schemes) {
		this.schemes = schemes;
	}

	public boolean isUnderLine() {
		return isUnderLine;
	}

	public void setUnderLine(boolean underLine) {
		isUnderLine = underLine;
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

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getTopTitle() {
		return topTitle;
	}

	public void setTopTitle(String topTitle) {
		this.topTitle = topTitle;
	}

	public boolean isShowTopTitle() {
		return isShowTopTitle;
	}

	public void setShowTopTitle(boolean showTopTitle) {
		isShowTopTitle = showTopTitle;
	}

	public String getTextTitle() {
		return textTitle;
	}

	public void setTextTitle(String textTitle) {
		this.textTitle = textTitle;
	}

	public String getTextTip() {
		return textTip;
	}

	public void setTextTip(String textTip) {
		this.textTip = textTip;
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String[] getChooseItem() {
		return chooseItem;
	}

	public void setChooseItem(String[] chooseItem) {
		this.chooseItem = chooseItem;
	}

	@Override
	public int getItemType() {
		return type==TITLE?TITLE:CONTENT;
	}
}
