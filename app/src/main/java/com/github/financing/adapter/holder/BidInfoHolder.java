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
    public TextView tvBidName,tvBidType,tvYearRate,tvLoanTerm,tvMinimum,tvRepayment;

    public BidInfoHolder(View itemView) {
        super(itemView);
        tvBidName = (TextView) itemView.findViewById(R.id.bid_name);
        tvBidType = (TextView) itemView.findViewById(R.id.bid_typeCode);
        tvYearRate = (TextView) itemView.findViewById(R.id.bid_yearRate);
        tvLoanTerm = (TextView) itemView.findViewById(R.id.bid_loanTerm);
        tvMinimum = (TextView) itemView.findViewById(R.id.bid_minimum);
        tvRepayment = (TextView) itemView.findViewById(R.id.bid_repayment);
    }
}
