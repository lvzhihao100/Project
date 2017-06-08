package com.eqdd.library.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.eqdd.library.bean.SelectBean;

import java.util.List;

/**
 * Created by lvzhihao on 17-6-5.
 */

public interface ConversitionListService extends IProvider {
     void getConversitionListAsyn(OnDataGet<List<SelectBean>> onDataGet);

}
