package com.bwie.Haorongji20180825.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpUtils {

    private OkHttpClient okHttpClient;

    private static OKHttpUtils okHttpUtils;

    public static OKHttpUtils getInstance() {

        if (okHttpUtils==null){

            synchronized (OKHttpUtils.class){

                if (okHttpUtils==null){

                    okHttpUtils=new OKHttpUtils();

                }

            }

        }

        return okHttpUtils;
    }

    private OKHttpUtils() {


        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient=new OkHttpClient.Builder()
                .readTimeout(5,TimeUnit.SECONDS)
                .writeTimeout(5,TimeUnit.SECONDS)
                .connectTimeout(5,TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();

    }

    public void postData(String url, HashMap<String,String> params, final RequestCallBack requestCallBack){

        FormBody.Builder formbody=new FormBody.Builder();

        if (params!=null&&params.size()>0){

            for (Map.Entry<String, String> stringStringEntry : params.entrySet()) {
                formbody.add(stringStringEntry.getKey(),stringStringEntry.getValue());
            }

        }

        Request request = new Request.Builder()
                .url(url)
                .post(formbody.build())
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestCallBack.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestCallBack.onResponse(call, response);
            }
        });


    }

    private void getData(String url, HashMap<String,String> hashMap, final RequestCallBack requestCallBack){

        StringBuilder builder=new StringBuilder();

        String addUrl="";

        for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {

            builder.append("?").append(stringStringEntry.getKey()).append("=").append(stringStringEntry.getValue()).append("&");

        }

        addUrl=url+builder.toString().substring(0,builder.length()-1);


        Request request= new Request.Builder()
                .url(url)
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestCallBack.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestCallBack.onResponse(call, response);
            }
        });
    }
}
