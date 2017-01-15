package com.github.financing.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.financing.R;
import com.github.financing.adapter.LoopPagerAdapter;
import com.github.financing.adapter.RecyclerAdapter;
import com.github.financing.base.BaseFragment;
import com.github.financing.views.loopPage.RollPagerView;
import com.github.financing.views.scrollText.AutoVerticalScrollTextView;

import java.lang.ref.WeakReference;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/9git
 * 描述：轮播图、公告、产品初始化
 *******************************************/
public class IndexFragment extends BaseFragment{
    private RecyclerAdapter recyclerAdapter;
    private AutoVerticalScrollTextView scrollText;
    private String[] strings = {};
    private boolean isRunning=true;
    private int number =0;

    private RollPagerView rollPager;
    private RecyclerView recyclerView;
    private View view;
    private WeakReference<Thread> noticThread;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 199) {
                scrollText.next();
                number++;
                scrollText.setText(strings[number%strings.length]);
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
            Log.i("indexFargment","-------init:view:fragment_index-----"+view.getClass().getName()+"----------"+view.hashCode());
            // 图片轮播
            rollPager = (RollPagerView) view.findViewById(R.id.roll_pager);
            rollPager.setAnimationDurtion(3000);
//            rollPager.setAdapter(new MyRollPagerAdapter(rollPager));
            // 文字公告
            scrollText = (AutoVerticalScrollTextView) view.findViewById(R.id.autoscroll_text);
//            scrollText.setText(strings[0]);
            // 产品列表
            recyclerView = (RecyclerView) view.findViewById(R.id.index_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//            recyclerAdapter = new RecyclerAdapter(this.getActivity());
//            recyclerView.setAdapter(recyclerAdapter);
        }else{
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }

//        if(noticThread == null){
//            Thread t = new Thread(){
//                @Override
//                public void run() {
//                    while (isRunning){
//                        SystemClock.sleep(5000);
//                        handler.sendEmptyMessage(199);
//                    }
//                }
//            };
//            noticThread = new WeakReference<Thread>(t);
//            noticThread.get().start();
//            Log.i("indexFargment", "-------noticThread-----" + noticThread.getClass().getName() + "----------" + noticThread.hashCode());
//        }

//        Log.i("indexFargment","-------thread-------"+noticThread.getClass().getName()+"-----"+noticThread.hashCode());
//        Log.i("indexFargment","-------view------"+view.getClass().getName()+"----------"+view.hashCode());
//        Log.i("indexFargment","-------handler------"+handler.getClass().getName()+"----------"+handler.hashCode());
        Log.i("indexFargment","--index:onCreateView结束--");
        return view;
    }

    private class MyRollPagerAdapter extends LoopPagerAdapter{

        public MyRollPagerAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

//        private int[] img = {R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5};
        private int[] img = {};
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView imageView = new ImageView(container.getContext());
            imageView.setImageResource(img[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            return imageView;
        }

        @Override
        public int getRealCount() {
            return img.length;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
    }
}
