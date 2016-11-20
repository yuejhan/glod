package com.github.financing.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.financing.R;
import com.github.financing.adapter.holder.BidInfoHolder;

/**
 * Created by user on 2016/10/11.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<BidInfoHolder> {
    private Activity activity;
    public RecyclerAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public BidInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BidInfoHolder myViewHolder = new BidInfoHolder(LayoutInflater.from(activity).inflate(R.layout.item_recycle_bid, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(BidInfoHolder holder, int position) {
        holder.tv.setText("冠军稳赢");
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
