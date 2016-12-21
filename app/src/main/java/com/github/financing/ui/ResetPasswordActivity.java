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
public class ResetPasswordActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private TextView tvConfirm;
    private EditText etPasswd,etPassAgain;
    private String modifyPhone;
    private String validCode;
    private static final String TAG = "ResetPassword";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_reset);
        Intent intent = getIntent();
        modifyPhone = intent.getStringExtra(Constants.MOBILEPHONE);
        validCode = intent.getStringExtra(Constants.VALIDCODE);
        Log.i(TAG,"===modifyPhone==="+modifyPhone + "===validCode===="+validCode);
        initView();
    }

    private void initView(){
        rlBack = (RelativeLayout) this.findViewById(R.id.reset_back);
        tvConfirm = (TextView) this.findViewById(R.id.reset_confirm);
        etPasswd = (EditText) this.findViewById(R.id.reset_passwd);
        etPassAgain  = (EditText) this.findViewById(R.id.reset_passwd_again);


        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwd = etPasswd.getText().toString();
                String passwdAgain = etPassAgain.getText().toString();
                // 密码的校验规则
                if(CommonUtil.checkPassword(passwd)){
                    Toast.makeText(ResetPasswordActivity.this, "密码格式有误", Toast.LENGTH_SHORT).show();
                }
                if(!passwd.equals(passwdAgain))
                    Toast.makeText(ResetPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();

                Map<String,String> body = new HashMap<String, String>();
                body.put("mobilePhone",modifyPhone);
                body.put("newPwd",passwd);
                body.put("validCode",validCode);
                Log.i(TAG,body.toString());
                DataRequester
                        .withHttp(getApplicationContext())
                        .setUrl(Constants.APP_BASE_URL+"/updatePassword")
                        .setMethod(DataRequester.Method.POST)
                        .setBody(body)
                        .setStringResponseListener(new DataRequester.StringResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG,response);
                                Map map = RequestUtil.parseResponse(response);
                                if(map != null && "0000".equals(map.get("code")))
                                    ResetPasswordActivity.this.finish();
                                else
                                    Toast.makeText(ResetPasswordActivity.this,"重置密码失败",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG,"===================");
                                Toast.makeText(ResetPasswordActivity.this,"服务器异常!",Toast.LENGTH_LONG).show();
                            }
                        }).requestString();
            }
        });

        //返回按钮 监听
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPasswordActivity.this.finish();
            }
        });
    }
}
