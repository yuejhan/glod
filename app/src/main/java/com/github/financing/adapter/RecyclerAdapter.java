package com.github.financing.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.financing.R;
import com.github.financing.adapter.holder.BidInfoHolder;
import com.github.financing.bean.BidInfoBean;
import com.github.financing.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by user on 2016/10/11.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<BidInfoHolder> implements View.OnClickListener{
    private Activity activity;
    private List<BidInfoBean> bidList;
    private OnItemClickListener onItemClickListener;
    public RecyclerAdapter(Activity activity,List<BidInfoBean> bidList) {
        this.activity = activity;
        this.bidList = bidList;
    }

    @Override
    public BidInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_recycle_bid, parent, false);
        BidInfoHolder myViewHolder = new BidInfoHolder(view);
        view.setOnClickListener(this);
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
        holder.itemView.setTag(bidInfoBean);
    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener != null){
            onItemClickListener.OnItemClick(view);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
