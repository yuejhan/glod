package com.github.financing.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.financing.R;

/********************************************
 * 作者：Administrator
 * 时间：2017/3/14
 * 描述：
 *******************************************/
public class RegisterBankActivity extends AppCompatActivity {

    private TextView bankCityView;
    private RelativeLayout bankCityRl;
    private View bindMask;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bind_bankCity:
                    bindMask.setVisibility(View.VISIBLE);
//                    bankCityRl.setVisibility(View.VISIBLE);
                    break;
                case R.id.bind_mask:
                    bankCityRl.setVisibility(View.GONE);
                    bindMask.setVisibility(View.GONE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bank);
        initView();
    }

    private void initView(){
        bankCityView = (TextView) this.findViewById(R.id.bind_bankCity);
        bankCityView.setOnClickListener(clickListener);
        bindMask = this.findViewById(R.id.bind_mask);
        bindMask.setOnClickListener(clickListener);
        bankCityRl = (RelativeLayout) this.findViewById(R.id.bind_bankCity_view);
    }
}
