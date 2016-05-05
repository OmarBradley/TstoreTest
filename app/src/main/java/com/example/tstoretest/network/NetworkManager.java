package com.example.tstoretest.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Supplier;
import com.example.tstoretest.data.Product;
import com.example.tstoretest.data.Tstore;
import com.example.tstoretest.data.TstoreSearchResult;
import com.example.tstoretest.network.cancel.RequestCancel;
import com.example.tstoretest.network.okhttpclient.OKHttpClientFactory;
import com.example.tstoretest.network.okhttpclient.TstoreOkHttpClient;
import com.example.tstoretest.util.functionalinterface.TriConsumer;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.net.URLEncoder;
import java.util.Map;

import lombok.Getter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 재화 on 2016-05-04.
 */
public class NetworkManager<T> {

    private static final class SingletonHolder {
        private static final NetworkManager INSTANCE = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Getter
    private OkHttpClient client;
    @Getter
    private RequestCancel requestCancel;

    private NetworkManager() {
        OKHttpClientFactory factory = new TstoreOkHttpClient();
        client = factory.buildOkHttpClient();
        requestCancel = new RequestCancel(client);
    }

    private static final String URL_FORMAT = "http://apis.skplanetx.com/tstore/products";

    public <T> Request getTstoreProduct(Context tag, String url, Map<String, String> headerParameters, Map<String, String> queryParameters, Class<T> jsonDataClass, BiConsumer<Request, T> onSuccess, TriConsumer<Request, Integer, Throwable> onFailure) throws UnsupportedEncodingException {
        Handler handler = new Handler(Looper.getMainLooper());
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        Stream.of(queryParameters).forEach(e -> urlBuilder.addQueryParameter(e.getKey(), e.getValue()));
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .headers(Headers.of(headerParameters))
                .tag(tag)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(() -> {
                    onFailure.accept(request, -1, e);
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                T searchResult = gson.fromJson(response.body().charStream(), jsonDataClass);
                handler.post(() -> {
                    onSuccess.accept(request, searchResult);
                });
            }
        });
        return request;
    }
}
