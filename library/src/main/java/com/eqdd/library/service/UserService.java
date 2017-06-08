package com.eqdd.library.service;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.eqdd.library.bean.User;

/**
 * Created by lvzhihao on 17-5-30.
 */

public interface UserService extends IProvider {

     User getUser();

     int insert(User user);
}
