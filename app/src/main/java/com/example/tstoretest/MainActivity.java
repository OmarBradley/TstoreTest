package com.example.tstoretest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import com.example.tstoretest.data.TstoreSearchResult;
import com.example.tstoretest.itemview.TstoreItemViewAdapter;
import com.example.tstoretest.network.NetworkManager;
import com.example.tstoretest.network.url.GetMethodParameters;
import com.example.tstoretest.network.url.URLParameters;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btnSearch) Button btnSearch;
    @Bind(R.id.editKeyword) EditText editKeyword;
    @Bind(R.id.tstoreItemListView) RecyclerView tstoreItemListView;
    TstoreItemViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTstoreListView();
        setOnClickListener();
    }

    private void setTstoreListView() {
        adapter = new TstoreItemViewAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        tstoreItemListView.setAdapter(adapter);
        tstoreItemListView.setLayoutManager(layoutManager);
    }

    private void setOnClickListener() {
        btnSearch.setOnClickListener(view -> {
            String keyword = editKeyword.getText().toString();
            try {
                NetworkManager.getInstance().getResult(makeGetParameters.apply(keyword), TstoreSearchResult.class, (request, result) -> {
                    Optional<TstoreSearchResult> resultData = Optional.ofNullable((TstoreSearchResult) result);
                    resultData.ifPresent(e -> Stream.of(e.getTstore().getProducts().getProduct()).forEach(adapter::addTstoreItem));
                }, (request, code, cause) -> {
                    Toast.makeText(MainActivity.this, "fail...", Toast.LENGTH_SHORT).show();
                });
            } catch (UnsupportedEncodingException e) {
                Log.e("support", e.toString());
            }
        });
    }

    Function<String, URLParameters> makeGetParameters = (keyword) -> {
        Map<String, String> headerParameters = new HashMap<>();
        headerParameters.put("Accept", "application/json");
        headerParameters.put("appKey", "458a10f5-c07e-34b5-b2bd-4a891e024c2a");

        String URL_FORMAT = "http://apis.skplanetx.com/tstore/products";

        Map<String, String> queryParameter = new HashMap<>();
        queryParameter.put("version", "1");
        queryParameter.put("page", "1");
        queryParameter.put("count", "10");
        queryParameter.put("searchKeyword", keyword);
        queryParameter.put("order", "L");
        return new GetMethodParameters(this, URL_FORMAT, queryParameter, headerParameters);
    };

}
