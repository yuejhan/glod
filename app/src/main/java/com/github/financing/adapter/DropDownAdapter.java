package com.github.financing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.financing.R;
import com.github.financing.utils.DropEnum;

import java.util.List;



public class DropDownAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;

    private int checkItemPosition = 0;
    private DropEnum type;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public DropDownAdapter(Context context,List<String> list,DropEnum type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public int getCount() {
        if(list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dropdown_type, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    public DropEnum getMenuType(){
        return type;
    }
    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.mText.setText(list.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                viewHolder.mText.setBackgroundResource(R.drawable.check_bg);
            } else {
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.mText.setBackgroundResource(R.drawable.uncheck_bg);
            }
        }
    }

    static class ViewHolder {
        TextView mText;

        ViewHolder(View view) {
            mText = (TextView)view.findViewById(R.id.text);
        }
    }
}
