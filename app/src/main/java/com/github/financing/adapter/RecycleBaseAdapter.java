package com.github.financing.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.financing.R;
import com.github.financing.adapter.holder.BidInfoHolder;
import com.github.financing.adapter.holder.RecordHolder;
import com.github.financing.bean.RecycleCommonBean;
import com.github.financing.listener.OnItemClickListener;

import java.util.List;

/********************************************
 * 作者：Administrator
 * 时间：2017/3/18
 * 描述：
 *******************************************/
public class RecycleBaseAdapter extends RecyclerView.Adapter<RecordHolder> implements View.OnClickListener {

    private Activity activity;
    private List<RecycleCommonBean> recordList;
    private OnItemClickListener onItemClickListener;
    public RecycleBaseAdapter(Activity activity, List<RecycleCommonBean> recordList){
        this.activity = activity;
        this.recordList = recordList;
    }
    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_recycle_common, parent, false);
        RecordHolder myViewHolder = new RecordHolder(view);
        view.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        RecycleCommonBean bean = recordList.get(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvAmount.setText(bean.getAmount()+"元");
        holder.tvTime.setText(bean.getTime());
        holder.tvState.setText(bean.getState());
        holder.itemView.setTag(bean);
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener != null){
            onItemClickListener.OnItemClick(view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
