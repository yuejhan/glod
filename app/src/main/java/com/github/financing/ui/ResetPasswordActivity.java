package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.financing.R;
import com.github.financing.base.BaseActivity;

/********************************************
 * 作者：Administrator
 * 时间：2016/11/27
 * 描述：
 *******************************************/
public class ResetPasswordActivity extends BaseActivity {
    private RelativeLayout rlBack;
    private String modifyPhone;
    private static final String TAG = "ResetPassword";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_reset);
        Intent intent = getIntent();
        modifyPhone = intent.getStringExtra("modifyPhone");
        Log.i(TAG,"=="+modifyPhone);
        initView();
    }

    private void initView(){
        rlBack = (RelativeLayout) this.findViewById(R.id.reset_back);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResetPasswordActivity.this.finish();
            }
        });
    }
}
