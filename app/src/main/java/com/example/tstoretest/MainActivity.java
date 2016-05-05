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
import com.example.tstoretest.data.Product;
import com.example.tstoretest.data.Products;
import com.example.tstoretest.data.Tstore;
import com.example.tstoretest.data.TstoreSearchResult;
import com.example.tstoretest.itemview.TstoreItemViewAdapter;
import com.example.tstoretest.network.NetworkManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

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
                Map<String, String> headerParameters = new HashMap<>();
                headerParameters.put("Accept", "application/json");
                headerParameters.put("appKey", "458a10f5-c07e-34b5-b2bd-4a891e024c2a");

                String URL_FORMAT = "http://apis.skplanetx.com/tstore/productsasas";
                String url = String.format(URL_FORMAT);

                Map<String, String> queryParameter = new HashMap<>();
                queryParameter.put("version", "1");
                queryParameter.put("page", "1");
                queryParameter.put("count", "10");
                queryParameter.put("searchKeyword", keyword);
                queryParameter.put("order", "L");

                NetworkManager.getInstance().getTstoreProduct(this,URL_FORMAT, headerParameters, queryParameter, TstoreSearchResult.class, (request, result) -> {
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

}
