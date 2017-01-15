package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.github.financing.utils.CommonUtil;
import com.github.financing.utils.Constants;
import com.github.financing.utils.FileUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2016/11/27
 * 描述：登录界面
 *******************************************/
public class LoginActivity extends BaseActivity{
    private TextView tvLogin,tvRegister,tvForget;
    private RelativeLayout rlBack;
    private EditText etUserName,etPassword;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        tvLogin = (TextView) this.findViewById(R.id.login_login);
        rlBack = (RelativeLayout) this.findViewById(R.id.login_back);
        tvForget = (TextView) this.findViewById(R.id.login_forget);
        tvRegister = (TextView) this.findViewById(R.id.login_register);
        etUserName = (EditText) this.findViewById(R.id.login_username);
        etPassword = (EditText) this.findViewById(R.id.login_password);

        // 注册按钮，监听
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


        // 返回按钮，监听
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });

        // 忘记密码 监听
        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,ForgetActivity.class);
                startActivity(intent);
            }
        });

        // 登录按钮 监听
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = etUserName.getText().toString();
                if("".equals(userName)) Toast.makeText(getApplicationContext(),"手机号不能为空",Toast.LENGTH_LONG).show();
                if(!CommonUtil.checkPhoneNumber(userName)){
                    Toast.makeText(getApplicationContext(),"手机号码格式错误",Toast.LENGTH_LONG).show();
                    return;
                }
                String password = etPassword.getText().toString();
                if(!CommonUtil.checkPassword(password)){
                    Toast.makeText(getApplicationContext(),"密码格式有误!",Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String,String> body = new HashMap<String, String>();
                body.put("mobilePhone",userName);
                body.put("password",password);

                DataRequester
                .withHttp(getApplicationContext())
                .setUrl(Constants.APP_BASE_URL+"/Login")
                .setMethod(DataRequester.Method.POST)
                .setBody(body)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,response);
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            Map map = mapper.readValue(response, Map.class);
                            if(map.get("token") == null || "".equals(map.get("token"))){
                                Toast.makeText(getApplicationContext(),"服务器异常",Toast.LENGTH_LONG).show();
                                return;
                            }
                            if("0000".equals(map.get("code"))){
                                FileUtil.putValue("token",map.get("token").toString());
                                FileUtil.putValue("userPhone",userName);
                                LoginActivity.this.finish();
                            }else{
                                Toast.makeText(getApplicationContext(),map.get("message").toString(),Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,"===================");
                        Toast.makeText(getApplicationContext(),"服务器异常!",Toast.LENGTH_LONG).show();
                    }
                }).requestString();
            }
        });
    }
}
