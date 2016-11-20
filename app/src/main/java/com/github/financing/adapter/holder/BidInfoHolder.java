package com.github.financing.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.financing.R;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/30
 * 描述：
 *******************************************/
public class BidInfoHolder extends RecyclerView.ViewHolder{
    public TextView tv;

    public BidInfoHolder(View itemView) {
        super(itemView);
        tv = (TextView)itemView.findViewById(R.id.bid_title);
    }
}
