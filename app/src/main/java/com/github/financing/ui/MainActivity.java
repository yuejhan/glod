package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.github.financing.R;
import com.github.financing.base.BaseActivity;
import com.github.financing.fragment.IndexFragment;
import com.github.financing.fragment.PersonalFragment;
import com.github.financing.fragment.ProductsFragment;
import com.github.financing.fragment.ServerFragment;

public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener{

    private LayoutInflater layoutInflater;
    private FragmentTabHost mTabHost;
    private static final String TAG_1 = "1";
    private static final String TAG_2 = "2";
    private static final String TAG_3 = "3";
    private static final String TAG_4 = "4";
    // 定义数组存放fragment
    private final Class fragmentArray[] = {IndexFragment.class, ProductsFragment.class, ServerFragment.class, PersonalFragment.class};

    private int mImageViewArray[] = {R.drawable.tab_index_image,R.drawable.tab_product_image,R.drawable.tab_activity_image,R.drawable.tab_personal_image};
    private String mTextViewArray[] = {"首页","产品","服务","我"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        Log.i("mainActivity","--initView调用开始--");
        layoutInflater = LayoutInflater.from(this);
        Log.i("mainActivity","------layouutInflater-----"+layoutInflater.hashCode());
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        Log.i("mainActivity","------mTabHost------"+mTabHost.hashCode());
        mTabHost.setup(this, getSupportFragmentManager(), R.id.homecontent);
        for (int i = 0;i < fragmentArray.length;i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(String.valueOf(i + 1)).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);

        }
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setOnTabChangedListener(this);
        Log.i("mainActivity", "--initView调用结束--");
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        Log.i("mainActivity","--getTabItemView调用开始--index:"+index);
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        Log.i("mainActivity","--------view:tab_item_view-------"+view.hashCode());
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextViewArray[index]);
        Log.i("mainActivity", "--getTabItemView调用结束--index:"+index);
        return view;
    }


    @Override
    public void onTabChanged(String tabId) {
        Log.e(TAG_1,"============"+tabId);
    }

    public void refresh(){

        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(mTabHost.getCurrentTabTag());
        getSupportFragmentManager().beginTransaction().detach(fragmentByTag).commit();
        getSupportFragmentManager().beginTransaction().attach(fragmentByTag).commit();
    }
}
