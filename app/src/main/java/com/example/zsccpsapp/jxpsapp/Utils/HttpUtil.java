package com.example.zsccpsapp.jxpsapp.Utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 99zan on 2018/1/17.
 */

public class HttpUtil {

    private static HttpUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    public static String BASE_URL = "http://jxptw.99zan.vip/";

    public static HttpUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new HttpUtil();
        }
        return downloadUtil;
    }

    private HttpUtil() {
        okHttpClient = new OkHttpClient();
    }

    public void get(String url, Callback callback){
        Request.Builder requestBuilder = new Request.Builder().url(BASE_URL+url);
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call mcall= okHttpClient.newCall(request);
        mcall.enqueue(callback);
    }

    public void post(String url, RequestBody requestBody, Callback callback){
        Request request = new Request.Builder()
                .url(BASE_URL+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
