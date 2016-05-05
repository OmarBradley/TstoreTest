package com.example.tstoretest.network.request;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.tstoretest.data.TstoreSearchResult;
import com.example.tstoretest.network.cancel.RequestCancel;
import com.example.tstoretest.network.okhttpclient.OKHttpClientFactory;
import com.example.tstoretest.network.okhttpclient.TstoreOkHttpClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 재화 on 2016-05-05.
 */
public class NetworkRequest {
    private OkHttpClient client;

    public NetworkRequest(OkHttpClient client){
        this.client = client;
    }

    public static interface OnResultListener<T> {
        public void onSuccess(Request request, T result);
        public void onFailure(Request request, int code, Throwable cause);
    }

    private static final int MESSAGE_SUCCESS = 0;
    private static final int MESSAGE_FAILURE = 1;

    static class CallbackObject<T> {
        Request request;
        T result;
        IOException exception;
        OnResultListener<T> listener;
    }

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CallbackObject object = (CallbackObject) msg.obj;
            Request request = object.request;
            OnResultListener listener = object.listener;
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    listener.onSuccess(request, object.result);
                    break;
                case MESSAGE_FAILURE:
                    listener.onFailure(request, -1, object.exception);
                    break;
            }
        }
    };

    private static final String URL_FORMAT = "http://apis.skplanetx.com/tstore/products?version=1&page=1&count=20&searchKeyword=%s&order=L";

    public Request getTstoreProduct(Context context, String keyword, int start, int display,
                                    final OnResultListener<TstoreSearchResult> listener) throws UnsupportedEncodingException {
        String url = String.format(URL_FORMAT, URLEncoder.encode(keyword, "utf-8"));

        final CallbackObject<TstoreSearchResult> callbackObject = new CallbackObject<TstoreSearchResult>();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("appKey", "458a10f5-c07e-34b5-b2bd-4a891e024c2a")
                .tag(context)
                .build();
        callbackObject.request = request;
        callbackObject.listener = listener;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackObject.exception = e;
                Message msg = handler.obtainMessage(MESSAGE_FAILURE, callbackObject);
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                TstoreSearchResult searchResult = gson.fromJson(response.body().charStream(), TstoreSearchResult.class);
                callbackObject.result = searchResult;
                Message msg = handler.obtainMessage(MESSAGE_SUCCESS, callbackObject);
                handler.sendMessage(msg);
            }
        });
        return request;
    }


}
