package com.eqdd.common.http;

/**
 * Created by vzhihao on 2016/11/2.
 */
public class ApiException  extends Exception{
    public int code;
    public String message;

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;

    }
}
