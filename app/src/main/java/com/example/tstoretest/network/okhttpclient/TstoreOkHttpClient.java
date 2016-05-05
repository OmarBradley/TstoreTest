package com.example.tstoretest.network.okhttpclient;

import android.content.Context;

import com.example.tstoretest.application.TstoreTestApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;

/**
 * Created by 재화 on 2016-05-04.
 */
public class TstoreOkHttpClient extends OKHttpClientFactory {

    private static final int MAX_CACHE_SIZE = 10 * 1024 * 1024;
    private static final String DEFAULT_CACHE_DIRECTORY = "mycache";
    private static final long TIME_OUT = 10000;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    @Override
    protected void setClientCacheOfOKHttpClientBuilder() {
        Context context = TstoreTestApplication.getContext();
        File dir = new File(context.getCacheDir(), DEFAULT_CACHE_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Cache cache = new Cache(dir, MAX_CACHE_SIZE);
        builder.cache(cache);
    }

    @Override
    protected void setTimeOutOfOKHttpClientBuilder() {
        builder.connectTimeout(TIME_OUT, TIME_UNIT);
        builder.readTimeout(TIME_OUT, TIME_UNIT);
        builder.writeTimeout(TIME_OUT, TIME_UNIT);
    }
}
