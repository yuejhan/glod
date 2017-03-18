package com.github.financing.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.financing.R;

/********************************************
 * 作者：Administrator
 * 时间：2017/3/18
 * 描述：
 *******************************************/
public class RecordHolder  extends RecyclerView.ViewHolder{
    public TextView tvTitle,tvAmount,tvTime,tvState;

    public RecordHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.common_title);
        tvAmount = (TextView) itemView.findViewById(R.id.common_amount);
        tvTime = (TextView) itemView.findViewById(R.id.common_time);
        tvState = (TextView) itemView.findViewById(R.id.common_state);

    }
}
