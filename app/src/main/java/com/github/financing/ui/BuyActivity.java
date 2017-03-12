package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.financing.R;
import com.github.financing.bean.BidInfoBean;
import com.github.financing.bean.BonusBean;
import com.github.financing.requester.DataRequester;
import com.github.financing.requester.RequestUtil;
import com.github.financing.utils.Constants;
import com.github.financing.views.dialog.SVProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2017/3/11
 * 描述：
 *******************************************/
public class BuyActivity extends AppCompatActivity {

    private TextView payComment,actualAmount,selectBonus,bonusCount,payAmount;
    private RelativeLayout backBuy;
    private BidInfoBean bidInfoBean;
    private String amount;
    private ArrayList<BonusBean> bonusList = new ArrayList<>();
    private SVProgressHUD mSVProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Intent intent = this.getIntent();
        bidInfoBean = (BidInfoBean) intent.getSerializableExtra("bidDetail");
        amount = intent.getStringExtra("amount");

        mSVProgressHUD = new SVProgressHUD(this);
        initData();
        initView();
    }

    private void initView(){
        payComment = (TextView) this.findViewById(R.id.pay_comment);
        payComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buyComment();
            }
        });
        backBuy = (RelativeLayout) this.findViewById(R.id.buy_back);
        backBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyActivity.this.finish();
            }
        });
        bonusCount = (TextView) this.findViewById(R.id.bonus_count);
        payAmount = (TextView) this.findViewById(R.id.pay_amount);
        payAmount.setText(amount);
        actualAmount = (TextView) this.findViewById(R.id.actual_amount);
        actualAmount.setText(amount);
        selectBonus = (TextView) this.findViewById(R.id.select_bonus);
        selectBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bonusList",bonusList);
                intent.putExtras(bundle);
                intent.setClass(BuyActivity.this,BonusActivity.class);
                startActivityForResult(intent,5001);
            }
        });
    }
    private void initData(){
//        DataRequester
//                .withHttp(getApplicationContext())
//                .setUrl(Constants.APP_BASE_URL+"/Account/Pay")
//                .setMethod(DataRequester.Method.POST)
//                .setBody(body)
//                .setStringResponseListener(new DataRequester.StringResponseListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        Map map = RequestUtil.parseResponse(response);
//                        if(map != null && "0000".equals(map.get("code"))){
//                            Toast.makeText(getApplicationContext(),"购买产品成功!",Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(getApplicationContext(),"购买产品失败!",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                })
//                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(),"网络异常!",Toast.LENGTH_SHORT).show();
//                    }
//                }).requestString();
    }

    private void buyComment(){
        mSVProgressHUD.showWithStatus("正在购买中...",SVProgressHUD.SVProgressHUDMaskType.Black);
        Map<String,String> body = new HashMap<String, String>();
        body.put("amount",amount);
        body.put("bidId",bidInfoBean.getBidId()+"");
        body.put("token","");
        body.put("userId","");
        body.put("bonusId","");
        body.put("mobilePhone","");
        DataRequester
                .withHttp(getApplicationContext())
                .setUrl(Constants.APP_BASE_URL+"/Account/Pay")
                .setMethod(DataRequester.Method.POST)
                .setBody(body)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Map map = RequestUtil.parseResponse(response);
                        if(map != null && "0000".equals(map.get("code"))){
                            mSVProgressHUD.cancelWithSuccessContext("购买产品成功!");

//                            Toast.makeText(getApplicationContext(),"购买产品成功!",Toast.LENGTH_SHORT).show();
                        }else{
                            mSVProgressHUD.cancelWithErrorContext("购买产品失败");
//                            Toast.makeText(getApplicationContext(),"购买产品失败!",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(mSVProgressHUD.isShowing()){
                            mSVProgressHUD.cancelWithErrorContext("购买产品失败,网络异常!");
                        }
//                        Toast.makeText(getApplicationContext(),"网络异常!",Toast.LENGTH_SHORT).show();
                    }
                }).requestString();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            if(mSVProgressHUD.isShowing()){
                mSVProgressHUD.dismiss();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);

    }
}
