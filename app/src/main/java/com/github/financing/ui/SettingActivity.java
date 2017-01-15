package com.github.financing.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.financing.R;
import com.github.financing.utils.FileUtil;

/********************************************
 * 作者：Administrator
 * 时间：2017/1/15
 * 描述：
 *******************************************/
public class SettingActivity extends AppCompatActivity {

    private TextView tvloginPw,tvTradePw,tvLogout,tvUsername,tvVersion;
    private RelativeLayout rlBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }
    private void initView(){
        rlBack = (RelativeLayout) this.findViewById(R.id.setting_back);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
        tvloginPw = (TextView) this.findViewById(R.id.setting_login_pw);
        tvTradePw = (TextView) this.findViewById(R.id.setting_trade_pw);
        tvUsername = (TextView) this.findViewById(R.id.setting_username);
        tvVersion = (TextView) this.findViewById(R.id.setting_version);
        tvLogout = (TextView)this.findViewById(R.id.setting_logout);
        tvUsername.setText(FileUtil.getStringValue("userPhone"));
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.clearPs();
                setResult(4);
                finish();
            }
        });
    }
}
