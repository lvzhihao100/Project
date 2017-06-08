package com.eqdd.common.http;


import android.support.annotation.NonNull;

import com.eqdd.common.base.BaseActivity;
import com.eqdd.common.base.loading.waitdialog.TDevice;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.utils.ToastUtil;
import com.google.gson.JsonParseException;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by lvzhihao on 17-3-28.
 */

public class HttpPresneter {
    Class clazz;
    Map<String, String> params = new HashMap();
    Map<String, String> headers = new HashMap();
    Map<String, RequestBody> partMap = new HashMap<>();
    Map<String, String> fileMap = new HashMap();
    String url;
    OnHttpListener onHttpListener;
    ResponseType responseType;
    boolean isShowProgress;
    BaseActivity baseActivity;

    public interface OnHttpListener<T> {
        void onError(Throwable e);

        void onNext(T t);
    }


    private HttpPresneter(Builder builder) {
        this.url = builder.url;
        this.params = builder.params;
        this.headers = builder.headers;
        this.onHttpListener = builder.onHttpListener;
        this.clazz = builder.clazz;
        this.partMap = builder.partMap;
        this.fileMap = builder.fileMap;
        this.responseType = builder.responseType;
        this.isShowProgress = builder.isShowProgress;
        this.baseActivity = builder.baseActivity;

    }

    private Observable<String> httpStringObserver(int mode) {

        return HttpMethods
                .getInstance()
                .getStringObserver(baseActivity, url, headers, params, mode);
    }

    private Observable<List> httpListObserver(int mode) {

        return HttpMethods
                .getInstance()
                .getListObserver(baseActivity, clazz, url, headers, params, mode);

    }

    private Observable<BaseBean> httpBaseBeanObserver(int mode) {

        return HttpMethods
                .getInstance()
                .getBaseBeanObserver(baseActivity, clazz, url, headers, params, mode);
    }

    private Observable httpBeanObserver(int mode) {

        return HttpMethods
                .getInstance()
                .getBeanObserver(baseActivity, clazz, url, headers, params, mode);
    }

    private Observable<List> httpBaseBeanListObserver(int mode) {
        return HttpMethods
                .getInstance()
                .getBaseBeanListObserver(baseActivity, clazz, url, headers, params, mode);
    }


    private void httpString(int mode) {
        Subscriber subscriber = getSubscriber();
        HttpMethods
                .getInstance()
                .getStringObserver(baseActivity, url, headers, params, mode)
                .subscribe(subscriber);
    }

    @NonNull
    private Subscriber getSubscriber() {
        return new Subscriber() {

            @Override
            public void onStart() {
                super.onStart();
                if (!TDevice.hasInternet()) {
                    onError(new ConnectException());
                    return;
                }
                if (isShowProgress && baseActivity != null) {
                    baseActivity.showLoading();
                }
            }

            @Override
            public void onCompleted() {
                if (isShowProgress && baseActivity != null) {
                    baseActivity.hideLoading();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (isShowProgress && baseActivity != null) {
                    baseActivity.hideLoading();
                }
                System.out.println(e);
                if (e instanceof ConnectException){
                    ToastUtil.showShort("网络错误，请检查网络设置");
                }else if (e instanceof JsonParseException){
                    ToastUtil.showShort("数据解析错误");
                }else {
                    ToastUtil.showShort("服务器错误");
                }
                onHttpListener.onError(e);

            }

            @Override
            public void onNext(Object t) {
                onHttpListener.onNext(t);
            }
        };
    }

    private void httpList(int mode) {
        Subscriber subscriber = getSubscriber();
        HttpMethods
                .getInstance()
                .getListObserver(null, clazz, url, headers, params, mode)
                .subscribe(subscriber);
    }

    private void httpBaseBean(int mode) {
        Subscriber subscriber = new Subscriber() {

            @Override
            public void onStart() {
                super.onStart();


            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println("错误" + e.toString());
                onHttpListener.onError(e);

            }

            @Override
            public void onNext(Object t) {
                onHttpListener.onNext(t);
            }
        };
        HttpMethods
                .getInstance()
                .getBaseBeanObserver(null, clazz, url, headers, params, mode)
                .subscribe(subscriber);
    }

    private void httpBean(int mode) {
        Subscriber subscriber = getSubscriber();
        HttpMethods
                .getInstance()
                .getBeanObserver(null, clazz, url, headers, params, mode)
                .subscribe(subscriber);
    }

    private void httpBaseBeanList(int mode) {
        Subscriber subscriber = getSubscriber();
        HttpMethods
                .getInstance()
                .getBaseBeanListObserver(null, clazz, url, headers, params, mode)
                .subscribe(subscriber);
    }


    public void postBaseBeanList() {
        httpBaseBeanList(1);
    }

    public void getBaseBeanList() {
        httpBaseBeanList(0);
    }

    public void postBean() {
        httpBean(1);
    }

    public void getBean() {
        httpBean(0);
    }

    public void postBasebean() {
        httpBaseBean(1);
    }

    public void getBasebean() {
        httpBaseBean(0);
    }

    public void postList() {
        httpList(1);
    }

    public void getList() {
        httpList(0);
    }

    public void postString() {
        httpString(1);
    }

    public void getString() {
        httpString(0);
    }


    public Observable<List> postBaseBeanListObserver() {
        return httpBaseBeanListObserver(1);
    }

    public Observable<List> getBaseBeanListObserver() {
        return httpBaseBeanListObserver(0);
    }

    public Observable postBeanObserver() {
        return httpBeanObserver(1);
    }

    public Observable getBeanObserver() {
        return httpBeanObserver(0);
    }

    public Observable<BaseBean> postBasebeanObserver() {
        return httpBaseBeanObserver(1);
    }

    public Observable<BaseBean> getBasebeanObserver() {
        return httpBaseBeanObserver(0);
    }

    public Observable<List> postListObserver() {
        return httpListObserver(1);
    }

    public Observable<List> getListObserver() {
        return httpListObserver(0);
    }

    public Observable<String> postStringObserver() {
        return httpStringObserver(1);
    }

    public Observable<String> getStringObserver() {
        return httpStringObserver(0);
    }


    public static enum ResponseType {
        STRING,
        BASEBEAN,
        LIST,
        BEAN
    }

    public static class Builder {
        Map<String, String> params = new HashMap();
        Map<String, RequestBody> partMap = new HashMap();
        Map<String, String> fileMap = new HashMap();

        Map<String, String> headers = new HashMap();
        String url;
        OnHttpListener onHttpListener;
        Class clazz;
        ResponseType responseType;
        boolean isShowProgress = false;
        BaseActivity baseActivity;

        public Builder CacheTime(String time) {
            this.headers.put("Cache-Time", time);
            return this;
        }

        public Builder Url(String url) {
            this.url = url;
            return this;
        }

        public Builder Params(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        public Builder Params(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        public Builder PartMap(String key, String value) {
            this.partMap.put(key, HttpMultiUtil.createPartFromString(value));
            return this;
        }

        public Builder fileMap(String param, String filePath) {
            this.fileMap.put(param, filePath);
            return this;
        }

        public Builder Headers(Map<String, String> headers) {
            this.headers.putAll(headers);
            return this;
        }

        public Builder Headers(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder classType(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder showProgress(BaseActivity baseActivity) {
            return showProgress(baseActivity, true);
        }

        public Builder showProgress(BaseActivity baseActivity, boolean isShowProgress) {
            this.baseActivity = baseActivity;
            this.isShowProgress = isShowProgress;
            return this;
        }

        public Builder setActivity(BaseActivity baseActivity) {
            this.baseActivity = baseActivity;
            return this;
        }

        public Builder setOnHttpListener(OnHttpListener onHttpListener) {
            this.onHttpListener = onHttpListener;
            return this;
        }

        public Builder() {
            this.params = new HashMap();
            this.headers = new HashMap();
            this.partMap = new HashMap();
        }

        private String checkUrl(String url) {
            if (checkNULL(url)) {
                throw new NullPointerException("absolute url can not be empty");
            }
            return url;
        }

        public static boolean checkNULL(String str) {
            return str == null || "null".equals(str) || "".equals(str);
        }

        public HttpPresneter build() {
            this.url = this.checkUrl(this.url);
            this.params = this.checkParams(this.params);
            this.headers = this.checkHeaders(this.headers);
            return new HttpPresneter(this);
        }


        public static Map<String, String> checkParams(Map<String, String> params) {
            if (params == null) {
                params = new HashMap();
            }
            Iterator var1 = ((Map) params).entrySet().iterator();

            while (var1.hasNext()) {
                Map.Entry entry = (Map.Entry) var1.next();
                if (entry.getValue() == null) {
                    ((Map) params).put(entry.getKey(), "");
                }
            }

            return (Map) params;
        }

        public static Map<String, String> checkHeaders(Map<String, String> headers) {
            if (headers == null) {
                headers = new HashMap();
            }
            Iterator var1 = ((Map) headers).entrySet().iterator();

            while (var1.hasNext()) {
                Map.Entry entry = (Map.Entry) var1.next();
                if (entry.getValue() == null) {
                    ((Map) headers).put(entry.getKey(), "");
                }
            }
            return (Map) headers;
        }
    }
}
