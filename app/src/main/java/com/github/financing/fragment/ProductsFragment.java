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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.financing.R;
import com.github.financing.adapter.ConstellationAdapter;
import com.github.financing.adapter.RecyclerAdapter;
import com.github.financing.base.BaseFragment;
import com.github.financing.bean.BidInfoBean;
import com.github.financing.listener.OnItemClickListener;
import com.github.financing.requester.DataRequester;
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
    private static final int pageSize = 3;
    private int curretnPage = 1,orderPosition =0,typePosition = 0;
    private RecyclerAdapter recyclerAdapter;
    private SamplePtrFrameLayout prtFrameLayout;
    private String headers[] = {"类型","排序"};
    private String typeArrays[] = {"不限","金计划"};
    private String orderArrays[] = {"不限","日期"};
    private List<BidInfoBean> bidList = new ArrayList<>();
    private View view,maskView,orderContentView,typeContentView;
    private RecyclerView recyclerView;
    private GridView orderGridView,typeGridView;
    private TextView tabOrderView,tabTypeView,orderOk,typeOk;
    private View emptyView;
    private FrameLayout drawContent;
    private LinearLayoutManager linearLayoutManager;
    private DropEnum currentTab = DropEnum.empty;
    private ConstellationAdapter orderAdapter,typeAdapter;
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()){
                case R.id.type_constellation:
                    if(typePosition == position){
                        break;
                    }
                    typeAdapter.setCheckItem(position);
                    typePosition = position;

                    break;
                case R.id.order_constellation:
                    if(orderPosition == position){
                        break;
                    }
                    orderAdapter.setCheckItem(position);
                    orderPosition = position;
                    break;
            }

        }
    };
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.mask_content:
                    closeMenu(currentTab);
                    currentTab = DropEnum.empty;
                    break;
                case R.id.tab_order:
                    switchMenu(DropEnum.order);
                    break;
                case R.id.tab_type:
                    switchMenu(DropEnum.type);
                    break;
                case R.id.type_ok:
                    tabTypeView.setText(typePosition == 0 ? headers[0] : typeArrays[typePosition]);
                    closeMenu(DropEnum.type);
                    autoRefresh();
                    break;
                case R.id.order_ok:
                    tabOrderView.setText(orderPosition == 0 ? headers[1] : orderArrays[orderPosition]);
                    closeMenu(DropEnum.order);
                    autoRefresh();
                    break;
            }
        }
    };

    private static boolean running = false;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){

            // 初始化fragment
            view = inflater.inflate(R.layout.fragment_prodcuts, null);
            // 下拉刷新控件
            prtFrameLayout = (SamplePtrFrameLayout) view.findViewById(R.id.product_frame_layout);

            /**
             * 初始化recycleView对象
             */
            recyclerView = (RecyclerView) view.findViewById(R.id.product_recycler);
            // 初始化布局管理器
            linearLayoutManager = new LinearLayoutManager(this.getActivity());
            // 给recycleView设置布局管理器
            recyclerView.setLayoutManager(linearLayoutManager);
            // 初始化recycleView的适配器
            recyclerAdapter = new RecyclerAdapter(this.getActivity(),bidList);
            recyclerView.setAdapter(recyclerAdapter);
            // 给recycleView 的item设置点击事件 和监听滑动到底部事件
            initRecycleClickListener();

            // 网络异常显示视图
            emptyView = view.findViewById(R.id.product_comment_error);

            /**
             *  初始化draw布局的参数
              */
            tabOrderView = (TextView)view.findViewById(R.id.tab_order);
            tabTypeView = (TextView)view.findViewById(R.id.tab_type);
            tabOrderView.setOnClickListener(clickListener);
            tabTypeView.setOnClickListener(clickListener);
            // 最外层的主布局
//            drawLayout = (FrameLayout) view.findViewById(R.id.draw_frameLayout);
            // 遮盖层
            maskView = view.findViewById(R.id.mask_content);
            maskView.setOnClickListener(clickListener);
            // 内容
            drawContent = (FrameLayout) view.findViewById(R.id.draw_content);
            // 初始化下拉内容
            initGridContent(inflater);
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

    private void initGridContent(LayoutInflater inflater){
        orderContentView = inflater.inflate(R.layout.tab_order_layout, null);
        orderGridView = (GridView) orderContentView.findViewById(R.id.order_constellation);
        orderOk = (TextView) orderContentView.findViewById(R.id.order_ok);
        orderOk.setOnClickListener(clickListener);
        orderAdapter = new ConstellationAdapter(getActivity(), Arrays.asList(orderArrays));
        orderGridView.setAdapter(orderAdapter);
        orderGridView.setOnItemClickListener(itemClickListener);
        orderContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        drawContent.addView(orderContentView,0);

        typeContentView = inflater.inflate(R.layout.tab_type_layout, null);
        typeGridView = (GridView) typeContentView.findViewById(R.id.type_constellation);
        typeOk = (TextView) typeContentView.findViewById(R.id.type_ok);
        typeOk.setOnClickListener(clickListener);
        typeAdapter = new ConstellationAdapter(getActivity(), Arrays.asList(typeArrays));
        typeGridView.setAdapter(typeAdapter);
        typeGridView.setOnItemClickListener(itemClickListener);
        typeContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        drawContent.addView(typeContentView,1);
    }

    public void closeMenu(DropEnum dropEnum) {
        if(dropEnum == DropEnum.order){
            tabOrderView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
            tabOrderView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.drop_down_unselected_icon),null);
            orderContentView.setVisibility(View.GONE);
        }else{
            tabTypeView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
            tabTypeView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.drop_down_unselected_icon),null);
            typeContentView.setVisibility(View.GONE);
        }
        drawContent.setVisibility(View.GONE);
        drawContent.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
        maskView.setVisibility(View.GONE);
        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));

    }
    private void switchMenu(DropEnum dropEnum) {
        if(dropEnum == DropEnum.order){
            if(dropEnum != currentTab){
                closeMenu(DropEnum.type);
            }
            tabOrderView.setTextColor(getResources().getColor(R.color.drop_down_selected));
            tabOrderView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.drop_down_selected_icon),null);
            orderContentView.setVisibility(View.VISIBLE);
        }else{
            if(dropEnum != currentTab){
                closeMenu(DropEnum.order);
            }
            tabTypeView.setTextColor(getResources().getColor(R.color.drop_down_selected));
            tabTypeView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.drop_down_selected_icon),null);
            typeContentView.setVisibility(View.VISIBLE);
        }
        drawContent.setVisibility(View.VISIBLE);
        drawContent.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
        maskView.setVisibility(View.VISIBLE);
        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
        currentTab = dropEnum;
    }
    private void addRefresh(int pageSize,int pageNum){
        if(running) return;
        running = true;
        Map<String,String> body = new HashMap<String, String>();
        body.put("pageSize",pageSize+"");
        body.put("pageNum",pageNum+"");
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
                        curretnPage++;
                    } catch (JSONException e) {
                    }
                }
                running = false;
                if(recyclerAdapter != null){
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        }).setResponseErrorListener(new DataRequester.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                running = false;
            }
        }).requestJsonArray();
    }

    private void autoRefresh(){
        if(running) return;
        running = true;
        bidList.clear();
        prtFrameLayout.autoRefresh();
        Map<String,String> body = new HashMap<String, String>();
        body.put("pageSize",pageSize+"");
        body.put("pageNum","1");
        body.put("order",orderPosition+"");
        body.put("type",typePosition+"");
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
                emptyView.setVisibility(View.VISIBLE);
                stopRefresh();
            }
        }).requestJsonArray();
    }

    private void initRecycleClickListener(){
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.i(TAG,"-------------------------------------"+newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    Log.i(TAG,"---------------------------------entry----"+lastVisibleItemPosition);
                    if(lastVisibleItemPosition >= (linearLayoutManager.getItemCount()-1)){
                        Log.i(TAG,"------------------------------start-------");
                        addRefresh(pageSize,curretnPage+1);
                    }
                }
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
