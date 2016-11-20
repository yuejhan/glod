package com.github.financing.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.github.financing.R;
import com.github.financing.adapter.DropDownAdapter;
import com.github.financing.adapter.RecyclerAdapter;
import com.github.financing.base.BaseFragment;
import com.github.financing.listener.TabChangeListener;
import com.github.financing.utils.DropEnum;
import java.util.Arrays;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/9
 * 描述：
 *******************************************/
public class ProductsFragment extends BaseFragment {
    private RecyclerAdapter recyclerAdapter;
    private String headers[] = {"类型", "期限"};
    private String typeArrays[] = {"不限"};
    private String orderArrays[] = {"不限"};


    private TabChangeListener tabChangeListener;
    private DropDownAdapter orderAdapter;
    private DropDownAdapter typeAdapter;

    private View view;
    private RecyclerView recyclerView;
    private GridView gridView;
    private TextView orderView;
    private TextView typeView;
    private View containt;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("ProductsFragment", "---product:onCreateView开始--");
        if(view == null){
            view = inflater.inflate(R.layout.fragment_prodcuts, null);
            Log.i("ProductsFragment", "--------product:view:fragment_prodcuts-------"+view.hashCode());
            recyclerView = (RecyclerView) view.findViewById(R.id.product_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recyclerAdapter = new RecyclerAdapter(this.getActivity());
            recyclerView.setAdapter(recyclerAdapter);

            orderView = (TextView)view.findViewById(R.id.tab_order);
            orderView.setTag("order");
            typeView = (TextView)view.findViewById(R.id.tab_type);
            typeView.setTag("type");

            orderAdapter = new DropDownAdapter(this.getActivity(), Arrays.asList(orderArrays), DropEnum.order) ;
            typeAdapter = new DropDownAdapter(this.getActivity(),Arrays.asList(typeArrays), DropEnum.type) ;

            containt =  view.findViewById(R.id.containt_frame);
            gridView = (GridView)view.findViewById(R.id.grid_contain);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DropDownAdapter tempAdapter = (DropDownAdapter)gridView.getAdapter();
                    tempAdapter.setCheckItem(position);
                    if(tempAdapter.getMenuType() == DropEnum.order){
                        orderView.setText(orderArrays[position]);
                    }else {
                        typeView.setText(typeArrays[position]);
                    }
                    containt.setVisibility(View.GONE);
                }
            });


            tabChangeListener = new TabChangeListener(this,containt);
            orderView.setOnClickListener(tabChangeListener);
            typeView.setOnClickListener(tabChangeListener);
        }

        ViewGroup parent = (ViewGroup)view.getParent();
        if(parent != null){
            parent.removeView(view);
        }
        Log.i("ProductsFragment", "--product:onCreateView结束--");
        return view;
    }

    public void checkMenu(DropEnum param){
        Log.i("ProductsFragment","--product:checkMenu开始--");
        if(orderAdapter == null){
            Log.i("ProductsFragment","------创建orderAdapter-----"+orderAdapter.hashCode());
            orderAdapter = new DropDownAdapter(this.getActivity(), Arrays.asList(orderArrays), DropEnum.order) ;
        }
        if(typeAdapter == null){
            Log.i("ProductsFragment","------创建typeAdapter-----"+typeAdapter.hashCode());
            typeAdapter = new DropDownAdapter(this.getActivity(),Arrays.asList(typeArrays), DropEnum.type) ;
        }

        if(gridView != null){
            if(DropEnum.order == param){
                gridView.setAdapter(orderAdapter);
            }else if(DropEnum.type == param){
                gridView.setAdapter(typeAdapter);
            }
        }
        Log.i("ProductsFragment","------gridview-----"+gridView.hashCode());
        Log.i("ProductsFragment","------orderAdapter-----"+orderAdapter.hashCode());
        Log.i("ProductsFragment","------typeAdapter-----"+typeAdapter.hashCode());
        Log.i("ProductsFragment","--product:checkMenu结束--");
    }


}
