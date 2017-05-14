package com.github.financing.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.financing.R;
import com.github.financing.base.BaseFragment;
import com.github.financing.requester.DataRequester;
import com.github.financing.ui.BonusActivity;
import com.github.financing.ui.LoginActivity;
import com.github.financing.ui.MainActivity;
import com.github.financing.ui.RegisterBankActivity;
import com.github.financing.ui.SettingActivity;
import com.github.financing.ui.WebViewActivity;
import com.github.financing.utils.BusiConstant;
import com.github.financing.utils.CommonUtil;
import com.github.financing.utils.Constants;
import com.github.financing.utils.FileUtil;
import com.github.financing.utils.SecurityUtils;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/9
 * 描述：
 *******************************************/
public class PersonalFragment extends BaseFragment {
    private View view;
    private TextView tvLogin,tvSetting,tvRecharde,tvWithhold,tvInterest,tvInvestment,tvWithdrawRecords,tvBonus;
    private LinearLayout llOK;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: 首先判断是否应登录
            // 响应按钮事件
            switch (view.getId()){
                case R.id.me_recharde:
                    rechardeClick();
                    break;
                case R.id.me_withhold:
                    break;
                case R.id.me_setting:
                    settingClick();
                    break;
                case R.id.me_login:
                    loginClick();
                    break;
                case R.id.me_interest:
                    startCommonRecycle("我的收益");
                    break;
                case R.id.me_investment:
                    startCommonRecycle("我的出借");
                    break;
                case R.id.me_withdraw_records:
                    startCommonRecycle("我的提现");
                    break;
                case R.id.me_bonus:
                    startCommonRecycle("我的红包");
                    break;

            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("person","===================oncreateView===================");
        if(view == null){
            view = inflater.inflate(R.layout.fragment_personal, null);
        }else{
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        llOK = (LinearLayout) view.findViewById(R.id.me_ok);
        tvLogin = (TextView)view.findViewById(R.id.me_login);
        String userPhone = FileUtil.getStringValue(BusiConstant.USERPHONE);
        if(userPhone != null && !"".equals(userPhone)){
            llOK.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.GONE);
            tvSetting = (TextView) view.findViewById(R.id.me_setting);
            tvRecharde = (TextView) view.findViewById(R.id.me_recharde);
            tvWithhold = (TextView)view.findViewById(R.id.me_withhold);
            tvInterest = (TextView) view.findViewById(R.id.me_interest);
            tvInterest.setOnClickListener(clickListener);
            tvInvestment = (TextView) view.findViewById(R.id.me_investment);
            tvInvestment.setOnClickListener(clickListener);
            tvWithdrawRecords = (TextView) view.findViewById(R.id.me_withdraw_records);
            tvWithdrawRecords.setOnClickListener(clickListener);
            tvBonus = (TextView) view.findViewById(R.id.me_bonus);
            tvBonus.setOnClickListener(clickListener);

            // 设置按钮
            tvSetting.setOnClickListener(clickListener);
            // 充值按钮
            tvRecharde.setOnClickListener(clickListener);
            // 提现按钮
            tvWithhold.setOnClickListener(clickListener);
        }else{
            tvLogin.setVisibility(View.VISIBLE);
            llOK.setVisibility(View.GONE);
            tvLogin.setOnClickListener(clickListener);
        }

        return view;

    }

    /**
     * 设置按钮的响应事件
     */
    private void settingClick(){
        String userCheck = FileUtil.getStringValue(BusiConstant.USERPHONE);
        if(userCheck != null && !"".equals(userCheck)){
            Intent intent = new Intent();
            Log.e("mainactivity",getActivity().toString());
            intent.setClass(getActivity(), SettingActivity.class);
            startActivityForResult(intent,4002);
        }
    }

    /**
     * 登录的按钮响应事件
     */
    private void loginClick(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivityForResult(intent,4001);
    }

    /**
     * 充值按钮响应事件
     */
    private void rechardeClick(){
        String hasAccount = FileUtil.getStringValue(BusiConstant.HASACCOUNT);
        if(hasAccount==null || "".equals(hasAccount)){
//            dialog();
            sendRequest();
        }else{
            sendRequest();
        }
    }
    /**
     * 请求网络方法
     */
    private void sendRequest(){
        String userPhone = FileUtil.getStringValue(BusiConstant.USERPHONE);
        String token = FileUtil.getStringValue(BusiConstant.TOKEN);
        if(userPhone == null || "".equals(userPhone)){
            Toast.makeText(getActivity(),"请重新登录",Toast.LENGTH_SHORT).show();
            return;
        }
        final Map<String,String> body = new HashMap<String, String>();
        body.put("cust_no",userPhone);
        body.put("mchnt_txn_ssn",CommonUtil.currentTimeFormat());
        body.put("amt","");
        body.put("location","");
        body.put("token",token);
        DataRequester
                .withHttp(getActivity())
                .setUrl(Constants.APP_BASE_URL+"/Account/PayData")
                .setMethod(DataRequester.Method.POST)
                .setBody(body)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            Map map = objectMapper.readValue(response.getBytes(), Map.class);
                            HashMap<String, String> params = new HashMap<>();
                            if(map != null && "0000".equals(map.get("code"))){
                                body.put("signature",map.get("message").toString());
                                body.put("mchnt_cd",BusiConstant.MCHNT_CD);
                                body.remove("token");
                                goteWebView(Constants.FUYOU_BASE_URL,body);
                            }else{
                                Toast.makeText(getActivity(),"服务器异常",Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }).requestString();
    }

    public void goteWebView(String url,Map<String,String> formatData){
        String postData = CommonUtil.makePostHTML(url, formatData);
        Intent intent = new Intent(getActivity(),WebViewActivity.class);
        intent.putExtra(Constants.INTENT_API_DATA_KEY_DATA, postData);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("person","------------------"+requestCode+"------------------------");
        ((MainActivity)getActivity()).refresh();
    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage("您还没有开通富有账户,立即开户？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setClass(getActivity(), RegisterBankActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void startCommonRecycle(String title){
        String userCheck = FileUtil.getStringValue(BusiConstant.USERPHONE);
        if(userCheck != null && !"".equals(userCheck)) {
            Intent intent = new Intent();
            intent.putExtra("title",title);
            intent.setClass(getActivity(), BonusActivity.class);
            startActivity(intent);
        }
    }
}
