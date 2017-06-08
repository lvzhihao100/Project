package com.eqdd.common.http;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by vzhihao on 2016/11/9.
 */
public interface HttpService {


    @GET
    Observable<String> getDataFromServer(@Url String url, @HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @POST
    @Multipart
    Observable<String> getDataFromServerMultipart(@Url String url, @HeaderMap Map<String, String> headers, @PartMap Map<String, RequestBody> part,
                                                  @Part() List<MultipartBody.Part> parts);
    @POST
    @FormUrlEncoded
    Observable<String> getDataFromServerPost(@Url String url, @HeaderMap Map<String, String> headers, @FieldMap Map<String, String> query);
}
