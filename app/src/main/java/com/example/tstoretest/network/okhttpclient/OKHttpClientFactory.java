package com.example.tstoretest.network.okhttpclient;

import okhttp3.OkHttpClient;

/**
 * Created by 재화 on 2016-05-04.
 */
// use template method pattern
public abstract class OKHttpClientFactory {
    OkHttpClient.Builder builder;

    public final OkHttpClient buildOkHttpClient() {
        builder = new OkHttpClient().newBuilder();
        setClientCacheOfOKHttpClientBuilder();
        setTimeOutOfOKHttpClientBuilder();
        return builder.build();
    }

    abstract protected void setClientCacheOfOKHttpClientBuilder();

    abstract protected void setTimeOutOfOKHttpClientBuilder();
}
