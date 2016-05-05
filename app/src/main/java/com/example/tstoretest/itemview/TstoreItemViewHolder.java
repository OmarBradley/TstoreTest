package com.example.tstoretest.itemview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Consumer;
import com.bumptech.glide.Glide;
import com.example.tstoretest.R;
import com.example.tstoretest.data.Product;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by 재화 on 2016-05-04.
 */
public class TstoreItemViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.thumbnailIView)  CircleImageView thumbnailIView;
    @Bind(R.id.nameView)  TextView nameView;
    @Bind(R.id.scoreView)  TextView scoreView;
    private Context context;

    public TstoreItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    public void setTstoreItemView(Product element) {
        nameView.setText(element.getName());
        scoreView.setText(Float.toString(element.getScore()));
        setThumbnailIImage(element);
    }

    private void setThumbnailIImage(Product element) {
        if (!TextUtils.isEmpty(element.getThumbnailUrl())) {
            Glide.with(context).load(element.getThumbnailUrl()).into(thumbnailIView);
        } else {
            thumbnailIView.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
