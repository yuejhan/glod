package com.github.financing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.financing.R;
import com.github.financing.adapter.LoopPagerAdapter;
import com.github.financing.adapter.RecyclerAdapter;
import com.github.financing.application.DefaultApplication;
import com.github.financing.base.BaseFragment;
import com.github.financing.bean.BidInfoBean;
import com.github.financing.listener.OnItemClickListener;
import com.github.financing.requester.DataRequester;
import com.github.financing.ui.DetailActivity;
import com.github.financing.utils.Constants;
import com.github.financing.utils.MyBitmapCache;
import com.github.financing.views.ReboundView.ReboundScrollView;
import com.github.financing.views.loadingView.SamplePtrFrameLayout;
import com.github.financing.views.loadingView.SmileyLoadingView;
import com.github.financing.views.loopPage.RollPagerView;
import com.github.financing.views.recycleView.EmptyRecyclerView;
import com.github.financing.views.recycleView.ListViewForScrollView;
import com.github.financing.views.recycleView.MyLinearLayoutManager;
import com.github.financing.views.scrollText.AutoVerticalScrollTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/9git
 * 描述：轮播图、公告、产品初始化
 *******************************************/
public class IndexFragment extends BaseFragment{
    private RecyclerAdapter recyclerAdapter;
    private AutoVerticalScrollTextView scrollText;
    private SamplePtrFrameLayout mPtrFrameLayout;
    private List<BidInfoBean> mDataList = new ArrayList<>();
    private List<String> urlLists = new ArrayList<>();
    private String[] strings = {};
    private boolean isRunning=false;
    private boolean running=true;
    private int number =0;

    private RollPagerView rollPager;
    private RecyclerView recyclerView;
    private View view,emptyView,indexListView;
    private NestedScrollView pullLayout;

    private ImageLoader mImageLoader;
    private WeakReference<Thread> noticThread;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 199:
                    if(strings.length>0){
                        scrollText.next();
                        number++;
                        scrollText.setText(strings[number%strings.length]);
                    }

                    break;
                default:break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("indexFargment","---index:onCreateView开始--");
        if(view == null){
            Log.i("indexFargment","------init:handler------"+handler.getClass().getName()+"----------"+handler.hashCode());
            view = inflater.inflate(R.layout.new_index, null);
            // 下拉刷新的顶级视图
            mPtrFrameLayout = (SamplePtrFrameLayout) view.findViewById(R.id.ptr_frame_layout);
            // 下拉刷新的子集视图
            pullLayout = (NestedScrollView) view.findViewById(R.id.index_pull_view);
            Log.i("indexFargment","-------init:view:fragment_index-----"+view.getClass().getName()+"----------"+view.hashCode());
            // 图片轮播
            rollPager = (RollPagerView) view.findViewById(R.id.new_roll_pager);
            // 文字公告
            scrollText = (AutoVerticalScrollTextView) view.findViewById(R.id.new_autoscroll_text);
            scrollText.setText("暂时没有数据!");
            // 公告一下的视图布局
            indexListView = view.findViewById(R.id.index_list);
            // 网络错误页面
            emptyView = view.findViewById(R.id.index_comment_error);
            // 产品列表
            recyclerView = (RecyclerView) view.findViewById(R.id.new_index_recycler);
//            recyclerView.setNestedScrollingEnabled(false);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recyclerView.setLayoutManager(new MyLinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,true));
            recyclerAdapter = new RecyclerAdapter(this.getActivity(),mDataList);
            recyclerView.setAdapter(recyclerAdapter);
            initItemClick();
//          // 刷新列表
            mPtrFrameLayout.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(mPtrFrameLayout, pullLayout, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    refresh();
                }
            });
            initData();
        }else{
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }

        if(noticThread == null){
            Thread t = new Thread(){
                @Override
                public void run() {
                    while (running){
                        SystemClock.sleep(5000);
                        handler.sendEmptyMessage(199);
                    }
                }
            };
            noticThread = new WeakReference<Thread>(t);
            noticThread.get().start();
            Log.i("indexFargment", "-------noticThread-----" + noticThread.getClass().getName() + "----------" + noticThread.hashCode());
        }
        Log.i("indexFargment","--index:onCreateView结束--");
        return view;
    }

    private void initData(){
        initUrlImage();
        autoRefresh();
    }
    private class MyRollPagerAdapter extends LoopPagerAdapter{
        public MyRollPagerAdapter(RollPagerView viewPager) {
            super(viewPager);
            mImageLoader = new ImageLoader(DefaultApplication.getInstance().getDefaultQueue(), new MyBitmapCache());
        }
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
            mImageLoader.get(urlLists.get(position), listener);
            return imageView;
        }

        @Override
        public int getRealCount() {
            return urlLists.size();
        }
    }


    private void autoRefresh() {
        mPtrFrameLayout.autoRefresh();
        if(mDataList.size() > 0) return;
        if(isRunning) return;
        isRunning = true;
        Map<String,String> body = new HashMap<String, String>();
        body.put("pageSize","3");
        body.put("pageNum","0");
        DataRequester
                .withHttp(getActivity())
                .setUrl(Constants.APP_BASE_URL+"/Common/ProductListForHomePage")
                .setMethod(DataRequester.Method.POST)
                .setBody(body).setJsonArrayResponseListener(new DataRequester.JsonArrayResponseListener() {
            @Override
            public void onResponse(JSONArray response) {
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
                        mDataList.add(infoBean);
                        mDataList.add(infoBean);
                    } catch (JSONException e) {
                    }
                }
                isRunning = false;
                indexListView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                if(recyclerAdapter != null){
                    recyclerAdapter.notifyDataSetChanged();
                }
                stopRefresh();
            }
        }).setResponseErrorListener(new DataRequester.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                indexListView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                stopRefresh();
                isRunning = false;
            }
        }).requestJsonArray();
    }

    // 下拉刷新
    private void refresh() {
        mDataList.clear();
        autoRefresh();
        if(urlLists.size() == 0 ){
            initUrlImage();
        }
    }

    // 初始化url路径数据
    private void initUrlImage(){
        urlLists.clear();
        DataRequester
                .withHttp(getActivity())
                .setUrl(Constants.APP_BASE_URL+"/Account/Advertisement")
                .setMethod(DataRequester.Method.POST)
               .setJsonArrayResponseListener(new DataRequester.JsonArrayResponseListener() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i =0;i<response.length();i++){
                    try {
                        JSONObject o = response.getJSONObject(i);
                        urlLists.add(o.getString("ImgUrl"));
                    } catch (JSONException e) {
                    }
                }
                isRunning = false;
                stopRefresh();
                rollPager.setPlayDelay(8000);
                rollPager.setAnimationDurtion(3000);
                rollPager.setAdapter(new MyRollPagerAdapter(rollPager));
            }
        }).setResponseErrorListener(new DataRequester.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopRefresh();
                isRunning = false;
            }
        }).requestJsonArray();
//        urlLists.add("http://www.thjcf.com.cn/Content/UI/images/index_banner03.jpg");
//        urlLists.add("http://www.thjcf.com.cn/Content/UI/images/index_banner01.png");
//        urlLists.add("http://www.thjcf.com.cn/Content/UI/images/index_banner02.png");

    }
    // 停止刷新
    private void stopRefresh(){
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.refreshComplete();
            }
        }, 200);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
        running = false;
    }
}
