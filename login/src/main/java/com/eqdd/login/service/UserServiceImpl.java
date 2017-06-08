package com.eqdd.login.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.User;
import com.eqdd.library.service.UserService;
import com.eqdd.library.utils.GreenDaoUtil;


/**
 * Created by lvzhihao on 17-5-30.
 */
@Route(path = Config.LOGIN_SERVICE_USER)
public class UserServiceImpl implements UserService {
    @Override
    public void init(Context context) {
        System.out.println("进123入");
    }

    @Override
    public User getUser() {
        return GreenDaoUtil.getUser();
    }

    @Override
    public int insert(User user) {
        if (getUser() != null && getUser().getUname().equals(user.getUname())) {
            user.setId(getUser().getId());
            GreenDaoUtil.getUserDao().delete(user);
        }
        SPUtil.saveAccount("username", user.getUname());
        return (int) GreenDaoUtil.getUserDao().insert(user);
    }
}
