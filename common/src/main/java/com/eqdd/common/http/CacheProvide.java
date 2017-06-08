package com.eqdd.common.http;


import com.eqdd.common.base.App;

import okhttp3.Cache;

/**
 * Created by 耿 on 2016/8/12.
 */
public class CacheProvide {


    public Cache provideCache() {
        return new Cache(App.INSTANCE.getCacheDir(), 50*1024 * 1024);
    }
}
