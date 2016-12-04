package com.github.financing.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.financing.R;
import com.github.financing.base.BaseActivity;
import com.github.financing.requester.DataRequester;
import com.github.financing.requester.RequestUtil;
import com.github.financing.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2016/12/4
 * 描述：
 *******************************************/
public class RegisterActivity extends BaseActivity {

    private RelativeLayout rlBack;
    private TextView tvRegister,tvGetCode;
    private EditText etPhone,etPasswd,etValidCode;
    private TimeCount time = new TimeCount(60000,1000);
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    private void initView(){
        rlBack = (RelativeLayout) this.findViewById(R.id.register_back);
        tvRegister = (TextView) this.findViewById(R.id.register_btn);
        tvGetCode = (TextView) this.findViewById(R.id.register_get_valid);
        etPhone = (EditText) this.findViewById(R.id.register_phone);
        etPasswd = (EditText) this.findViewById(R.id.register_passwd);
        etValidCode = (EditText) this.findViewById(R.id.register_validCode);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = etPhone.getText().toString();
                if("".equals(phoneNumber)){
                    Toast.makeText(getApplicationContext(),"手机号不能为空",Toast.LENGTH_LONG).show();
                }
                String password = etPasswd.getText().toString();
                if("".equals(password)) Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_LONG).show();

                String validCode = etValidCode.getText().toString();
                if("".equals(validCode)) Toast.makeText(getApplicationContext(),"验证码不能为空",Toast.LENGTH_LONG).show();

                Map<String,String> body = new HashMap<String, String>();
                body.put("mobilePhone",phoneNumber);
                body.put("password",password);
                body.put("validCode",validCode);
                DataRequester
                        .withHttp(getApplicationContext())
                        .setUrl(Constants.APP_BASE_URL+"/Register")
                        .setMethod(DataRequester.Method.POST)
                        .setBody(body)
                        .setStringResponseListener(new DataRequester.StringResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG,response);
                                Map map = RequestUtil.parseResponse(response);
                                if(map != null && "0000".equals(map.get("code")))
                                    RegisterActivity.this.finish();
                                else
                                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG,"===================");
                                Toast.makeText(getApplicationContext(),"服务器异常!",Toast.LENGTH_LONG).show();
                            }
                        }).requestString();

            }
        });

        // 获取验证码 监听
        tvGetCode.setOnClickListener(new View.OnClickListener() {
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
                // 开启计时
                time.start();
            }
        });

        //返回按钮，返回监听
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture,long countDeonInterval){
            super(millisInFuture,countDeonInterval);
        }

        @Override
        public void onTick(long l) {
            tvGetCode.setClickable(false);
            tvGetCode.setText(l/1000+"s");
        }

        @Override
        public void onFinish() {
            tvGetCode.setText(R.string.forget_get_valid);
            tvGetCode.setClickable(true);
        }
    }
}
