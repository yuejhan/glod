package com.github.financing.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.financing.R;
import com.github.financing.base.BaseActivity;
import com.github.financing.bean.CheckVersionBean;
import com.github.financing.requester.DataRequester;
import com.github.financing.utils.Constants;

import java.io.IOException;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/30
 * 描述：
 *******************************************/
public class SplashActivity extends BaseActivity{
    private static final String TAG_SPLASHACTIVITY = "**splashactivity**";
    private final int CODE_URL_ERROR = 0;
    private final int CODE_NETWORK_ERROR = 1;
    private final int CODE_JSON_ERROR = 2;
    private final int CODE_ENTER_HOME = 3;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CODE_URL_ERROR:
                    Toast.makeText(SplashActivity.this,"下载异常!",Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case CODE_NETWORK_ERROR:
                    Toast.makeText(SplashActivity.this,"网络连接异常!",Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this,"数据解析异常!",Toast.LENGTH_LONG).show();
                    enterHome();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        checkVersion();
    }

    /**
     * 获取版本名称
     */
    private String getVersionName(){
        PackageInfo packageInfo = getPackageInfo();
        return packageInfo == null ? "" : packageInfo.versionName;
    }

    /**
     * 获取版本号
     */
    private int getVersionCode(){
        PackageInfo packageInfo = getPackageInfo();
        return packageInfo == null ? 1 : packageInfo.versionCode;
    }

    /**
     * 获取包管理器
     */
    private PackageInfo getPackageInfo(){
        PackageManager pm = this.getPackageManager();
        try {
            return pm.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG_SPLASHACTIVITY,e.getMessage());
        }
        return null;
    }

    /**
     * 跳转到主界面
     */
    private void enterHome(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 检查版本号
     */
    private void checkVersion(){
        DataRequester.withHttp(this).setUrl(Constants.APP_SERVER_CHECKVERSION)
                .setMethod(DataRequester.Method.POST)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG_SPLASHACTIVITY,"checkversion response:"+response);
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            CheckVersionBean bean = mapper.readValue(response, CheckVersionBean.class);
                            // 判断是否有更新
                            if(bean.getVersionCode() > getVersionCode()){
                                // 弹出更新对话框
                                mHandler.sendMessageDelayed(mHandler.obtainMessage(CODE_ENTER_HOME),2000);
                            }else{
                                // 跳转主页
                                mHandler.sendMessageDelayed(mHandler.obtainMessage(CODE_ENTER_HOME),2000);
                            }

                        } catch (IOException e) {
                           Log.e(TAG_SPLASHACTIVITY,e.getMessage());
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(CODE_JSON_ERROR),2000);
                        }
                    }
                })
                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        //弹出错误信息
//                        Toast.makeText(SplashActivity.this,"网络连接异常!",Toast.LENGTH_LONG).show();
//                        //跳转主界面
//                        enterHome();
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(CODE_NETWORK_ERROR),2000);
                    }
                }).requestString();
    }
}
