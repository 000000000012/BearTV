package com.fongmi.android.tv.net;

import android.content.ContentValues;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OKHttp {

    private final OkHttpClient mOk;

    private static class Loader {
        static volatile OKHttp INSTANCE = new OKHttp();
    }

    public static OKHttp get() {
        return Loader.INSTANCE;
    }

    public OKHttp() {
        mOk = getBuilder().build();
    }

    private OkHttpClient.Builder getBuilder() {
        return new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).connectTimeout(5, TimeUnit.SECONDS).sslSocketFactory(new SSLSocketFactoryCompat(SSLSocketFactoryCompat.trustAllCert), SSLSocketFactoryCompat.trustAllCert);
    }

    private OkHttpClient client() {
        return mOk;
    }

    public static Call newCall(String url) {
        return get().client().newCall(new Request.Builder().url(url).build());
    }

    public static Call newCall(String url, Headers headers) {
        return get().client().newCall(new Request.Builder().url(url).headers(headers).build());
    }

    public static Call newCall(String url, ContentValues param) {
        return get().client().newCall(new Request.Builder().url(buildUrl(url, param)).build());
    }

    private static HttpUrl buildUrl(String url, ContentValues param) {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        for (Map.Entry<String, Object> entry : param.valueSet()) builder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        return builder.build();
    }
}
