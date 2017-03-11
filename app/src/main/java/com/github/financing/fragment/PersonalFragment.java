package com.github.financing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.financing.R;
import com.github.financing.base.BaseFragment;
import com.github.financing.requester.DataRequester;
import com.github.financing.ui.LoginActivity;
import com.github.financing.ui.MainActivity;
import com.github.financing.ui.SettingActivity;
import com.github.financing.ui.WebViewActivity;
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
    private TextView tvLogin,tvSetting,tvRecharde,tvWithhold;
    private LinearLayout llOK;
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
        String userPhone = FileUtil.getStringValue("userPhone");
        if(userPhone != null && !"".equals(userPhone)){
            llOK.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.GONE);
            tvSetting = (TextView) view.findViewById(R.id.me_setting);
            tvRecharde = (TextView) view.findViewById(R.id.me_recharde);
            tvWithhold = (TextView)view.findViewById(R.id.me_withhold);
            // 设置按钮
            tvSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userCheck = FileUtil.getStringValue("userPhone");
                    if(userCheck != null && !"".equals(userCheck)){
                        Intent intent = new Intent();
                        Log.e("mainactivity",getActivity().toString());
                        intent.setClass(getActivity(), SettingActivity.class);
                        startActivityForResult(intent,4002);
                    }
                }
            });
            // 充值按钮
            tvRecharde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO:充值请求逻辑
                    Map<String,String> body = new HashMap<String, String>();
                    body.put("mchnt_cd","0001000F0096241");
                    body.put("mchnt_txn_ssn","11032302065863805666");
                    body.put("cust_no","17300010020");
                    body.put("location","0020");
                    body.put("amt","1000");
                    try {
                        String a = new String(SecurityUtils.rsaEncrypt("1000|17300010020|0020|0001000F0096241|11032302065863805666".getBytes("UTF-8")));
                        Log.i("===sing===",a);
                        body.put("signature",a);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    goteWebView(Constants.FUYOU_BASE_URL,body);
//                    DataRequester
//                            .withHttp(getActivity())
//                            .setUrl(Constants.APP_BASE_URL+"/")
//                            .setMethod(DataRequester.Method.POST)
//                            .setBody(body)
//                            .setStringResponseListener(new DataRequester.StringResponseListener() {
//                                @Override
//                                public void onResponse(String response) {
//                                    ObjectMapper objectMapper = new ObjectMapper();
//                                    Map map = new HashMap();
//                                    try {
//                                        map = objectMapper.readValue(response.getBytes(), Map.class);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                    goteWebView(Constants.JZH_API_APP_500002_URL,map);
//                                }
//                            })
//                            .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//
//                                }
//                            }).requestString();
                }
            });
            // 提现按钮
            tvWithhold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,String> body = new HashMap<String, String>();
                    DataRequester
                            .withHttp(getActivity())
                            .setUrl(Constants.APP_BASE_URL+"/")
                            .setMethod(DataRequester.Method.POST)
                            .setBody(body)
                            .setStringResponseListener(new DataRequester.StringResponseListener() {
                                @Override
                                public void onResponse(String response) {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    Map map = new HashMap();
                                    try {
                                        map = objectMapper.readValue(response.getBytes(), Map.class);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    goteWebView(Constants.FUYOU_BASE_URL,map);
                                }
                            })
                            .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }).requestString();
                }
            });
        }else{
            tvLogin.setVisibility(View.VISIBLE);
            llOK.setVisibility(View.GONE);
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    startActivityForResult(intent,4001);
                }
            });
        }

        return view;

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

    //测试用，请勿生产使用
    private String genSSn(){
        return UUID.randomUUID().toString().substring(2).replaceAll("-","");
    }
}
