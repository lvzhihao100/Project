package com.eqdd.common.http;



import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 耿 on 2016/8/12.
 */
public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String cache = request.header("Cache-Time");
        if (!TextUtils.isEmpty(cache)) {
            Response response1 = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    //cache for 30 days
                    .header("Cache-Control", "max-age="+cache)
                    .build();
            return response1;
        } else {
            return response;
        }
    }
}
