package com.eqdd.common.http;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by lvzhihao on 17-3-28.
 */

public class HttpMultiUtil {
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), descriptionString);
    }

    public static List<MultipartBody.Part> prepareFilePart(Map<String,String> fileMap) {
        ArrayList<MultipartBody.Part> parts = new ArrayList<>();
        Iterator<Map.Entry<String, String>> iterator = fileMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            File file = new File(next.getValue());
            // 为file建立RequestBody实例
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
            parts.add(MultipartBody.Part.createFormData(next.getKey(), file.getName(), requestFile));
        }

        return parts;
    }
}
