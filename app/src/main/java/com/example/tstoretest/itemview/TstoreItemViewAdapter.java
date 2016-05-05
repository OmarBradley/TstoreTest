package com.example.tstoretest.itemview;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tstoretest.R;
import com.example.tstoretest.data.Product;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 재화 on 2016-05-04.
 */
public class TstoreItemViewAdapter extends RecyclerView.Adapter<TstoreItemViewHolder>{

    @LayoutRes private static final int VIEW_HOLDER_LAYOUT = R.layout.view_tstore_item;
    @Getter @Setter private List<Product> tstoreItems = new ArrayList<>();

    public void addTstoreItem(Product element){
        tstoreItems.add(element);
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount() {
        return tstoreItems.size();
    }

    @Override
    public TstoreItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(VIEW_HOLDER_LAYOUT, viewGroup, false);
        return new TstoreItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TstoreItemViewHolder holder, int position) {
        holder.setTstoreItemView(tstoreItems.get(position));
    }
}
