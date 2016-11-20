package com.iosxc.android.jzhdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.iosxc.android.utils.SecurityUtils;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String JZH_API_BASE_URL = "http://116.239.4.195:9056/jzh";
    private static final String PAGE_NOTIFY_URL = "http://103.16.127.68:8018/Result/FuiouCallBack";
    private static final String BACK_NOTIFY_URL = "http://103.16.127.68:8018/Result/FuiouNotify";
    private static final String MCHNT_CD = "0002900F0339996";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void on500001Click(View view) throws Exception{
        Map<String, String> paramMap = new HashMap<String, String>(7);
        paramMap.put("mchnt_cd",MCHNT_CD);
        paramMap.put("mchnt_txn_ssn",genSSn());
        paramMap.put("login_id","18611429112");
        paramMap.put("amt","100");
        paramMap.put("page_notify_url",PAGE_NOTIFY_URL);
        paramMap.put("back_notify_url",BACK_NOTIFY_URL);
        Intent jzhWebApiIntent = new Intent(this,JzhWebActivity.class);
        jzhWebApiIntent.putExtra(JzhWebActivity.INTENT_EXTRA_KEY_APIURL, JZH_API_BASE_URL+"/app/500001.action");
        jzhWebApiIntent.putExtra(JzhWebActivity.INTENT_EXTRA_KEY_POSTDATA, paramMap2bytes(paramMap));
        startActivity(jzhWebApiIntent);
    }

    public void on500002Click(View view) throws Exception{
        Map<String, String> paramMap = new HashMap<String, String>(7);
        paramMap.put("mchnt_cd",MCHNT_CD);
        paramMap.put("mchnt_txn_ssn",genSSn());
        paramMap.put("login_id","18611429112");
        paramMap.put("amt","100");
        paramMap.put("page_notify_url",PAGE_NOTIFY_URL);
        paramMap.put("back_notify_url",BACK_NOTIFY_URL);
        Intent jzhWebApiIntent = new Intent(this,JzhWebActivity.class);
        jzhWebApiIntent.putExtra(JzhWebActivity.INTENT_EXTRA_KEY_APIURL, JZH_API_BASE_URL + "/app/500002.action");
        jzhWebApiIntent.putExtra(JzhWebActivity.INTENT_EXTRA_KEY_POSTDATA,paramMap2bytes(paramMap));
        startActivity(jzhWebApiIntent);
    }

    public void on500003Click(View view) throws Exception {
        Map<String, String> paramMap = new HashMap<String, String>(7);
        paramMap.put("mchnt_cd",MCHNT_CD);
        paramMap.put("mchnt_txn_ssn",genSSn());
        paramMap.put("login_id","18611429112");
        paramMap.put("amt","100");
        paramMap.put("page_notify_url",PAGE_NOTIFY_URL);
        paramMap.put("back_notify_url",BACK_NOTIFY_URL);
        Intent jzhWebApiIntent = new Intent(this,JzhWebActivity.class);
        jzhWebApiIntent.putExtra(JzhWebActivity.INTENT_EXTRA_KEY_APIURL, JZH_API_BASE_URL+"/app/500003.action");
        jzhWebApiIntent.putExtra(JzhWebActivity.INTENT_EXTRA_KEY_POSTDATA,paramMap2bytes(paramMap));
        startActivity(jzhWebApiIntent);
    }

    //测试用，请勿生产使用
    private String genSSn(){
        return UUID.randomUUID().toString().substring(2).replaceAll("-","");
    }

    private byte[] paramMap2bytes(Map<String,String> paramMap) throws Exception {
        Object[] keys  =  paramMap.keySet().toArray();
        Arrays.sort(keys);
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<keys.length;i++){
            Object key = keys[i];
            sb.append(i==keys.length-1?paramMap.get(key):(paramMap.get(key)+"|"));
        }
        String str4Sign = sb.toString();
        Log.e("jzh", "STRING 4 SIGN:" + str4Sign);
        paramMap.put("signature",genSign(str4Sign));
        StringBuilder postStrSb = new StringBuilder();
        Object[] set = paramMap.entrySet().toArray();
        for(int i=0;i<set.length;i++){
            Map.Entry<String,String> entry  = (Map.Entry<String, String>) set[i];
            postStrSb.append(String.format("%s=%s",entry.getKey(),(i==set.length-1?entry.getValue():(entry.getValue()+"&"))));
        }
        return postStrSb.toString().getBytes("UTF-8");
    }

    private String genSign(String str4Sign) throws Exception {
        String sign = Base64.encodeToString(SecurityUtils.rsaEncrypt(str4Sign.getBytes("UTF-8")),Base64.DEFAULT);
        return URLEncoder.encode(sign,"UTF-8");
    }
}
