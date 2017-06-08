package com.eqdd.imrong.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.imrong.R;
import com.eqdd.library.base.Config;


/**
 * Created by vzhihao on 2017/2/25.
 */
@Route(path = Config.IMRONG_SUBCONVERSITION)
public class SubConversationListActivtiy extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imrong_subconversationlist);
    }
}
