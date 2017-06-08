package com.eqdd.common.mvchelper;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by LuckyJayce on 2016/7/22.
 */
public abstract class MRxDataSource<DATA> extends RxDataSource<DATA> {

    @Override
    public Observable<DATA> refreshRX(DoneActionRegister<DATA> register) throws Exception {
        return load(refreshRXM(register));
    }

    @Override
    public Observable<DATA> loadMoreRX(DoneActionRegister<DATA> register) throws Exception {
        return load(loadMoreRXM(register));
    }

    private Observable<DATA> load(Observable<DATA> observableAction) {
        return observableAction.flatMap(new Func1<DATA, Observable<DATA>>() {
            @Override
            public Observable<DATA> call(DATA response) {

                return Observable.just(response);
            }
        });
    }

    public abstract Observable<DATA> refreshRXM(DoneActionRegister<DATA> register) throws Exception;

    public abstract Observable<DATA> loadMoreRXM(DoneActionRegister<DATA> register) throws Exception;
}
