package com.github.financing.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.financing.R;
import com.github.financing.adapter.holder.BidInfoHolder;
import com.github.financing.bean.BidInfoBean;

import java.util.List;

/**
 * Created by user on 2016/10/11.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<BidInfoHolder> {
    private Activity activity;
    private List<BidInfoBean> bidList;
    public RecyclerAdapter(Activity activity,List<BidInfoBean> bidList) {
        this.activity = activity;
        this.bidList = bidList;
    }

    @Override
    public BidInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BidInfoHolder myViewHolder = new BidInfoHolder(LayoutInflater.from(activity).inflate(R.layout.item_recycle_bid, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(BidInfoHolder holder, int position) {
        BidInfoBean bidInfoBean = bidList.get(position);
        holder.tvBidName.setText(bidInfoBean.getBidName());
        holder.tvBidType.setText(bidInfoBean.getBidType());
        holder.tvYearRate.setText(bidInfoBean.getBidYearRate());
        holder.tvLoanTerm.setText(bidInfoBean.getBidLoanTerm());
        holder.tvMinimum.setText(bidInfoBean.getBidMinimum());
        holder.tvRepayment.setText(bidInfoBean.getBidRepayment());
    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }
}
