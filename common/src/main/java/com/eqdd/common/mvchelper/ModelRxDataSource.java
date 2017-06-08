package com.eqdd.common.mvchelper;


import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by lvzhihao on 17-3-30.
 */

public class ModelRxDataSource<T> extends MRxDataSource<List<T>> {
    private int pageSize=1;
    private boolean isMore = false;
    public int mPage = 1;

    OnLoadSource onLoadSource;
    public ModelRxDataSource(OnLoadSource onLoadSource) {
        this.onLoadSource=onLoadSource;
    }

    public interface OnLoadSource<T>{
         Observable<List<T>> loadSourcce(final int page, DoneActionRegister<List<T>> register);
    }
    private Observable<List<T>> load(final int page, DoneActionRegister<List<T>> register) throws Exception {

       return onLoadSource.loadSourcce(page,register)
               .flatMap(new Func1<List<T>, Observable<List<T>>>() {
                   @Override
                   public Observable<List<T>> call(List<T> s) {

                       if (s!=null&&s.size()==pageSize){
                           isMore=true;
                       }else {
                           isMore=false;
                       }
                       return Observable.just(s);
                   }
               });
    }

    @Override
    public boolean hasMore() {
        return isMore;
    }

    @Override
    public Observable<List<T>> refreshRXM(DoneActionRegister<List<T>> register) throws Exception {
        mPage = 1;
        return load(mPage, register);
    }

    @Override
    public Observable<List<T>> loadMoreRXM(DoneActionRegister<List<T>> register) throws Exception {
        return load(++mPage, register);
    }

}