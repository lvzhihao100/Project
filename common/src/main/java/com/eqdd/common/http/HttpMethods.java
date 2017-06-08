package com.eqdd.common.http;


import com.eqdd.common.base.App;
import com.eqdd.common.base.BaseActivity;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.utils.GsonUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by vzhihao on 2016/11/1.
 */
public class HttpMethods {


    private Retrofit retrofit;
    public HttpService httpService;

    private static class SingletonHolder {
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private HttpMethods() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(new LogInterceptor())
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new CookiesManager())
                .addNetworkInterceptor(new CacheInterceptor())
                .cache(new CacheProvide().provideCache())
                .build();


        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(App.BASE_URL)
                .build();

        httpService = retrofit.create(HttpService.class);
    }


    public Observable<String> getStringObserver(BaseActivity life, String url,
                                                Map<String, String> headMaps,
                                                Map<String, String> map, int mode) {

        Observable<String> listObservable = (mode == 0 ? httpService.getDataFromServer(url, headMaps, map)
                : httpService.getDataFromServerPost(url, headMaps, map))
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {

                        return Observable.just(s);
                    }
                });
        if (life != null) {
            listObservable = listObservable.compose(life.<String>bindToLifecycle());
        }
        return listObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public <T> Observable<List<T>> getListObserver(BaseActivity life,
                                                   final Class<T> clazz, String url,
                                                   Map<String, String> headMaps,
                                                   Map<String, String> map, int mode) {

        Observable<List<T>> listObservable = (mode == 0 ? httpService.getDataFromServer(url, headMaps, map)
                : httpService.getDataFromServerPost(url, headMaps, map))
                .flatMap(new Func1<String, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(String s) {
                        List<T> list = GsonUtils.changeGsonToList(s, clazz);
                        return Observable.just(list);
                    }
                });
        if (life != null) {
            listObservable = listObservable.compose(life.<List<T>>bindToLifecycle());
        }
        return listObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public <T> Observable<BaseBean<T>> getBaseBeanObserver(BaseActivity life,
                                                           final Class<T> clazz, String url,
                                                           Map<String, String> headMaps,
                                                           Map<String, String> map, int mode) {

        Observable<BaseBean<T>> listObservable = (mode == 0 ? httpService.getDataFromServer(url, headMaps, map)
                : httpService.getDataFromServerPost(url, headMaps, map))
                .flatMap(new Func1<String, Observable<BaseBean<T>>>() {
                    @Override
                    public Observable<BaseBean<T>> call(String s) {
                        BaseBean<T> list = GsonUtils.changeGsonToBaseBean(s, clazz);
                        return Observable.just(list);
                    }
                });
        if (life != null) {
            listObservable = listObservable.compose(life.<BaseBean<T>>bindToLifecycle());
        }
        return listObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public <T> Observable<T> getBeanObserver(BaseActivity life,
                                             final Class<T> clazz, String url,
                                             Map<String, String> headMaps,
                                             Map<String, String> map, int mode) {

        Observable<T> listObservable = (mode == 0 ? httpService.getDataFromServer(url, headMaps, map)
                : httpService.getDataFromServerPost(url, headMaps, map))
                .flatMap(new Func1<String, Observable<T>>() {
                    @Override
                    public Observable<T> call(String s) {
                        T t = GsonUtils.changeGsonToBean(s, clazz);
                        return Observable.just(t);
                    }
                });
        if (life != null) {
            listObservable = listObservable.compose(life.<T>bindToLifecycle());
        }
        return listObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public <T> Observable<List<T>> getBaseBeanListObserver(BaseActivity life,
                                                           final Class<T> clazz, String url,
                                                           Map<String, String> headMaps,
                                                           Map<String, String> map, int mode) {

        Observable<List<T>> listObservable = (mode == 0 ? httpService.getDataFromServer(url, headMaps, map)
                : httpService.getDataFromServerPost(url, headMaps, map))
                .flatMap(new Func1<String, Observable<List<T>>>() {
                    @Override
                    public Observable<List<T>> call(String s) {
                        BaseBean<T> list = GsonUtils.changeGsonToBaseBean(s, clazz);

                        return Observable.just( list.getItems());
                    }
                });
        if (life != null) {
            listObservable = listObservable.compose(life.<List<T>>bindToLifecycle());
        }
        return listObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}