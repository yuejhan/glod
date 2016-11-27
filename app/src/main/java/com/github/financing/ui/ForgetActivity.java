package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.financing.R;
import com.github.financing.base.BaseActivity;
import com.github.financing.utils.CommonUtil;

/********************************************
 * 作者：Administrator
 * 时间：2016/11/27
 * 描述：
 *******************************************/
public class ForgetActivity extends BaseActivity {
    private TextView tvNext;
    private RelativeLayout rlBack;
    private EditText etPhone;
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

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetActivity.this.finish();
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhone.getText().toString();
                if(!CommonUtil.checkPhoneNumber(phone)){
                    Toast.makeText(getApplicationContext(),"手机号格式有误",Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("modifyPhone",phone);
                intent.setClass(ForgetActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
