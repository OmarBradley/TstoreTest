package com.example.tstoretest.network.url;

import android.content.Context;

import com.annimon.stream.Stream;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Created by 재화 on 2016-05-06.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetMethodParameters implements URLParameters{
    private Context tag;
    private String url;
    private Map<String, String> queryParameters;
    private Map<String, String> headerParameters;

    @Override
    public Request makeRequest() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        Stream.of(queryParameters).forEach(e -> urlBuilder.addQueryParameter(e.getKey(), e.getValue()));
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .headers(Headers.of(headerParameters))
                .tag(tag)
                .build();
        return request;
    }
}
