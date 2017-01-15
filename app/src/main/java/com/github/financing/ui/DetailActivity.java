package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.financing.R;
import com.github.financing.bean.BidInfoBean;
import com.github.financing.requester.DataRequester;
import com.github.financing.requester.RequestUtil;
import com.github.financing.utils.CommonUtil;
import com.github.financing.utils.Constants;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2017/1/14
 * 描述：
 *******************************************/
public class DetailActivity extends AppCompatActivity {

    private BidInfoBean bidInfoBean;

    private RelativeLayout rlBack;
    private TextView tvTitle,tvYearIrr,tvPeriod,tvMini,tvRemain,tvDescription,tvRecord,tvRate,tvCommit;
    private EditText etAmount;
    private static final String TAG = "DetailActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = this.getIntent();
        bidInfoBean = (BidInfoBean) intent.getSerializableExtra("bidDetail");
        initView();
    }

    private void initView(){
        rlBack = (RelativeLayout) this.findViewById(R.id.detail_back);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity.this.finish();
            }
        });
        tvTitle = (TextView) this.findViewById(R.id.detail_title);
        tvTitle.setText(bidInfoBean.getBidName());
        tvYearIrr = (TextView) this.findViewById(R.id.detail_year_irr);
        tvYearIrr.setText(bidInfoBean.getBidYearRate()+"%");
        tvPeriod = (TextView) this.findViewById(R.id.detail_period);
        tvPeriod.setText(bidInfoBean.getBidLoanTerm()+"个月");
        tvMini = (TextView) this.findViewById(R.id.detail_minimum);
        tvMini.setText(bidInfoBean.getBidMinimum()+"元");
        tvRemain = (TextView) this.findViewById(R.id.detail_remain_amount);
        tvRemain.setText(bidInfoBean.getTotalAmount()+"元");
        tvDescription = (TextView) this.findViewById(R.id.detail_description);
        tvDescription.setOnClickListener(null);
        tvRecord = (TextView) this.findViewById(R.id.detail_record);
        tvRecord.setOnClickListener(null);
        tvRate = (TextView) this.findViewById(R.id.detail_investment_rate);
        tvCommit = (TextView) this.findViewById(R.id.detail_commit);
        etAmount = (EditText) this.findViewById(R.id.detail_investment);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputAmount = etAmount.getText().toString();
                Log.e(TAG,inputAmount);
                if(inputAmount != null && !"".equals(inputAmount)){
                    Log.e(TAG,inputAmount);
                    float result = Integer.parseInt(inputAmount) * Float.parseFloat(bidInfoBean.getBidYearRate())/12;
                    Log.e(TAG,result+"");
                    DecimalFormat fnum  =   new DecimalFormat("##0.00");
                    String format = fnum.format(result);
                    tvRate.setText(format);
                }else{
                    tvRate.setText("0.00");
                }
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputAmount = etAmount.getText().toString();
                if(inputAmount == null) {
                    Toast.makeText(getApplicationContext(),"请输入购买金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(CommonUtil.checkNumber(inputAmount)){
                    if(bidInfoBean == null){
                        Toast.makeText(getApplicationContext(),"网络异常!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    requestAndBuy(inputAmount);
                }else{
                    Toast.makeText(getApplicationContext(),"请输入正确的金额",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestAndBuy(String inputAmount){
        Map<String,String> body = new HashMap<String, String>();
        DataRequester
                .withHttp(getApplicationContext())
                .setUrl(Constants.APP_BASE_URL+"/ProductBuy")
                .setMethod(DataRequester.Method.POST)
                .setBody(body)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,response);
                        Map map = RequestUtil.parseResponse(response);
                        if(map != null && "0000".equals(map.get("code"))){
                            Toast.makeText(getApplicationContext(),"购买产品成功!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"购买产品失败!",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,"===================");
                        Toast.makeText(getApplicationContext(),"网络异常!",Toast.LENGTH_SHORT).show();
                    }
                }).requestString();
    }

}
