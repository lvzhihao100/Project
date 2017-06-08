package com.eqdd.library.base;

import android.os.Bundle;

import com.eqdd.common.base.BaseActivity;
import com.eqdd.library.bean.User;
import com.eqdd.library.utils.GreenDaoUtil;

/**
 * Created by lvzhihao on 17-5-30.
 */

public abstract class CommonActivity extends BaseActivity {
    public User user;

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = GreenDaoUtil.getUser();
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

}
