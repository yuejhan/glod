package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.financing.R;
import com.github.financing.base.BaseActivity;
import com.github.financing.requester.DataRequester;
import com.github.financing.requester.RequestUtil;
import com.github.financing.utils.CommonUtil;
import com.github.financing.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2016/11/27
 * 描述：
 *******************************************/
public class ForgetActivity extends BaseActivity {
    private TextView tvNext,tvGetValid;
    private RelativeLayout rlBack;
    private EditText etPhone,etValidCode;
    private static final String TAG="ForgetActivity";
   private TimeCount time = new TimeCount(60000,1000);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView(){
        rlBack = (RelativeLayout) this.findViewById(R.id.forget_back);
        tvNext = (TextView) this.findViewById(R.id.forget_next);
        etPhone = (EditText) this.findViewById(R.id.forget_phone);
        tvGetValid = (TextView) this.findViewById(R.id.forget_get_valid);
        etValidCode = (EditText) this.findViewById(R.id.forget_validCode);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetActivity.this.finish();
            }
        });

        tvGetValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = etPhone.getText().toString();
                if("".equals(phoneNumber)){
                    Toast.makeText(getApplicationContext(),"手机号不能为空",Toast.LENGTH_LONG).show();
                }
                Map<String,String> body = new HashMap<String, String>();
                body.put("mobilephone",phoneNumber);
                DataRequester
                        .withHttp(getApplicationContext())
                        .setUrl(Constants.APP_BASE_URL+"/MsgAuthCode")
                        .setMethod(DataRequester.Method.POST)
                        .setBody(body)
                        .setStringResponseListener(new DataRequester.StringResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG,response);
                                Map map = RequestUtil.parseResponse(response);
                                if(map != null && !"0000".equals(map.get("code")))
                                    Toast.makeText(getApplicationContext(),"验证码获取失败",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG,"===================");
                                Toast.makeText(getApplicationContext(),"服务器异常!",Toast.LENGTH_LONG).show();
                            }
                        }).requestString();
                time.start();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhone.getText().toString();
                String validCode = etValidCode.getText().toString();
//                if(!CommonUtil.checkPhoneNumber(phone)){
//                    Toast.makeText(getApplicationContext(),"手机号格式有误",Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if("".equals(validCode)){
//                    Toast.makeText(getApplicationContext(),"请输入验证码",Toast.LENGTH_LONG).show();
//                    return;
//                }
                Intent intent = new Intent();
                intent.putExtra(Constants.MOBILEPHONE,phone);
                intent.putExtra(Constants.VALIDCODE,validCode);
                intent.setClass(ForgetActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    class TimeCount extends CountDownTimer{
        public TimeCount(long millisInFuture,long countDeonInterval){
            super(millisInFuture,countDeonInterval);
        }

        @Override
        public void onTick(long l) {
            tvGetValid.setClickable(false);
            tvGetValid.setText(l/1000+"s");
        }

        @Override
        public void onFinish() {
            tvGetValid.setText(R.string.forget_get_valid);
            tvGetValid.setClickable(true);
        }
    }
}
