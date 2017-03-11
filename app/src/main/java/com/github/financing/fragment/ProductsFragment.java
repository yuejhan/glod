package com.github.financing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.financing.R;
import com.github.financing.adapter.DropDownAdapter;
import com.github.financing.adapter.RecyclerAdapter;
import com.github.financing.base.BaseFragment;
import com.github.financing.bean.BidInfoBean;
import com.github.financing.listener.OnItemClickListener;
import com.github.financing.listener.TabChangeListener;
import com.github.financing.requester.DataRequester;
import com.github.financing.requester.RequestUtil;
import com.github.financing.ui.DetailActivity;
import com.github.financing.utils.Constants;
import com.github.financing.utils.DropEnum;
import com.github.financing.views.loadingView.SamplePtrFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/9
 * 描述：
 *******************************************/
public class ProductsFragment extends BaseFragment {

    private static final String TAG = "ProductsFragment";

    private RecyclerAdapter recyclerAdapter;
    private SamplePtrFrameLayout prtFrameLayout;
    private String headers[] = {"类型", "期限"};
    private String typeArrays[] = {"不限"};
    private String orderArrays[] = {"不限"};
    private List<BidInfoBean> bidList = new ArrayList<>();


    private TabChangeListener tabChangeListener;
    private DropDownAdapter orderAdapter;
    private DropDownAdapter typeAdapter;

    private View view;
    private RecyclerView recyclerView;
    private GridView gridView;
    private TextView orderView;
    private TextView typeView;
    private View containt;
    private FrameLayout pullView,maskView;

    private static boolean running = false;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        initData();
        Log.i("ProductsFragment", "---product:onCreateView开始--");
        if(view == null){
            view = inflater.inflate(R.layout.fragment_prodcuts, null);
            prtFrameLayout = (SamplePtrFrameLayout) view.findViewById(R.id.product_frame_layout);
            Log.i("ProductsFragment", "--------product:view:fragment_prodcuts-------"+view.hashCode());
            pullView = (FrameLayout) view.findViewById(R.id.product_pull_view);
            recyclerView = (RecyclerView) view.findViewById(R.id.product_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recyclerAdapter = new RecyclerAdapter(this.getActivity(),bidList);
            recyclerView.setAdapter(recyclerAdapter);
            initItemClick();

            orderView = (TextView)view.findViewById(R.id.tab_order);
            orderView.setTag("order");
            typeView = (TextView)view.findViewById(R.id.tab_type);
            typeView.setTag("type");

            orderAdapter = new DropDownAdapter(this.getActivity(), Arrays.asList(orderArrays), DropEnum.order) ;
            typeAdapter = new DropDownAdapter(this.getActivity(),Arrays.asList(typeArrays), DropEnum.type) ;

            containt =  view.findViewById(R.id.containt_frame);
            gridView = (GridView)view.findViewById(R.id.grid_contain);
            maskView = (FrameLayout) view.findViewById(R.id.mask_containt);
            maskView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    maskView.setVisibility(View.GONE);
                    containt.setVisibility(View.GONE);
                }
            });
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
                    maskView.setVisibility(View.GONE);
                    containt.setVisibility(View.GONE);
                }
            });

            tabChangeListener = new TabChangeListener(this,containt);
            orderView.setOnClickListener(tabChangeListener);
            typeView.setOnClickListener(tabChangeListener);


            // 刷新列表
            prtFrameLayout.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(prtFrameLayout, recyclerView, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    autoRefresh();
                }
            });

            autoRefresh();
        }else{
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        Log.i("ProductsFragment", "--product:onCreateView结束--");
        return view;
    }

    public void checkMenu(DropEnum param){
        Log.i("ProductsFragment","--product:checkMenu开始--");
        maskView.setVisibility(View.VISIBLE);
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



    private void autoRefresh(){
        if(running) return;
        running = true;
        bidList.clear();
        prtFrameLayout.autoRefresh();
        Map<String,String> body = new HashMap<String, String>();
        body.put("pageSize","8");
        body.put("pageNum","0");
        DataRequester
        .withHttp(getActivity())
        .setUrl(Constants.APP_BASE_URL+"/Common/ProductList")
        .setMethod(DataRequester.Method.POST)
        .setBody(body).setJsonArrayResponseListener(new DataRequester.JsonArrayResponseListener() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG,response.toString());
                for (int i =0;i<response.length();i++){
                    BidInfoBean infoBean = new BidInfoBean();
                    try {
                        JSONObject o = response.getJSONObject(i);
                        infoBean.setBidName(o.optString("Name"));
                        infoBean.setBidType(o.getString("TypeCode"));
                        infoBean.setBidYearRate(o.optString("YearRate"));
                        infoBean.setBidLoanTerm(o.optString("LoanTerm"));
                        infoBean.setBidMinimum(o.optString("Minimum"));
                        infoBean.setBidRepayment(o.optString("RepaymentMethod"));
                        bidList.add(infoBean);
                    } catch (JSONException e) {
                    }
                }
                running = false;
                if(recyclerAdapter != null){
                    recyclerAdapter.notifyDataSetChanged();
                }
                stopRefresh();
            }
        }).setResponseErrorListener(new DataRequester.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                running = false;
                stopRefresh();
            }
        }).requestJsonArray();
    }

    private void initItemClick(){
        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(View view) {
                BidInfoBean bidInfoBean = (BidInfoBean) view.getTag();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bidDetail",bidInfoBean);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    // 停止刷新
    private void stopRefresh(){
        prtFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                prtFrameLayout.refreshComplete();
            }
        }, 200);
    }
}
