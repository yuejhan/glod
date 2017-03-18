package com.github.financing.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.financing.R;
import com.github.financing.adapter.RecycleBaseAdapter;
import com.github.financing.bean.RecycleCommonBean;
import com.github.financing.requester.DataRequester;
import com.github.financing.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2017/3/11
 * 描述：
 *******************************************/
public class BonusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private  RecycleBaseAdapter baseAdapter;
    private View emptyView,errorView;
    private TextView commonTitle;
    private RelativeLayout  backView;
    private boolean running = false;

    private List<RecycleCommonBean> bonusList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        String title = getIntent().getStringExtra("title");
        initDate();

        errorView = this.findViewById(R.id.bonus_error);
        emptyView = this.findViewById(R.id.common_null);

        commonTitle = (TextView) this.findViewById(R.id.common_re_title);
        commonTitle.setText(title);
        backView = (RelativeLayout) this.findViewById(R.id.common_re_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = (RecyclerView) this.findViewById(R.id.recycle_common);
        // 初始化布局管理器
        linearLayoutManager = new LinearLayoutManager(this);
        // 给recycleView设置布局管理器
        recyclerView.setLayoutManager(linearLayoutManager);
        baseAdapter = new RecycleBaseAdapter(this, bonusList);
        recyclerView.setAdapter(baseAdapter);
    }

    private void initDate(){
        for (int i=0;i<10;i++){
            RecycleCommonBean b = new RecycleCommonBean();
            b.setTitle("20元红包");
            b.setAmount("20");
            b.setTime("2017-1-1");
            b.setState("未使用");
            bonusList.add(b);
        }

        if(!running) return;
        bonusList.clear();
        Map<String,String> body = new HashMap<String, String>();
        DataRequester
                .withHttp(this)
                .setUrl(Constants.APP_BASE_URL+"/Common/BonusList")
                .setMethod(DataRequester.Method.POST)
                .setBody(body).setJsonArrayResponseListener(new DataRequester.JsonArrayResponseListener() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i =0;i<response.length();i++){
                    RecycleCommonBean infoBean = new RecycleCommonBean();
                    try {
                        JSONObject o = response.getJSONObject(i);
                        infoBean.setTitle(o.optString("title"));
                        infoBean.setAmount(o.optString("amount"));
                        infoBean.setTime(o.optString("time"));
                        infoBean.setState(o.optString("state"));
                        bonusList.add(infoBean);
                    } catch (JSONException e) {
                    }
                }
                running = false;
                if(bonusList != null && bonusList.size() >0){
                    baseAdapter.notifyDataSetChanged();
                }else{
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        }).setResponseErrorListener(new DataRequester.ResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                running = false;
                errorView.setVisibility(View.VISIBLE);
            }
        }).requestJsonArray();
    }
}
