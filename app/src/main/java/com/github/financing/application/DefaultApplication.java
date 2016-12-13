package com.github.financing.application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import com.github.financing.requester.SSLCertificateValidation;
import com.github.financing.requester.SelfSSLSocketFactory;
import com.github.financing.utils.NetType;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by user on 2016/10/20.
 */
public class DefaultApplication extends Application{

    private static final String TAG = "DefaultApplication";

    private static DefaultApplication mApplication;
    // 创建http请求队列
    private RequestQueue mDefaultQueue;
    // 创建自定义证书的HTTPS请求队列
    private RequestQueue mSelfSslQueue;
    // 创建默认的HTTPS请求队列
    private RequestQueue mDefalutSslQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }


    /**
     *单列模式获取application对象
     */
    public static DefaultApplication getInstance(){
        return mApplication;
    }

    /**
     * 获取http请求队列
     */
    public RequestQueue getDefaultQueue(){
        if(mDefaultQueue == null){
            mDefaultQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mDefaultQueue;
    }

    /**
     * 获取默认证书的HTTPS请求队列
     */
    public RequestQueue getDefaultSslQueue(){
        if(mDefalutSslQueue == null){
            BasicNetwork netWork = new BasicNetwork(new HurlStack());
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
            mDefalutSslQueue = new RequestQueue(cache, netWork);
            mDefalutSslQueue.start();
            //TODO:校验
            SSLCertificateValidation.trustAllCertificate();
        }
        return mDefalutSslQueue;
    }

    /**
     * 获取自定义证书的HTTPS请求队列
     */
    public RequestQueue getSelfSslQueue(){
        if(mSelfSslQueue == null){
            SSLSocketFactory sslSocketFactory = SelfSSLSocketFactory.getSSLSocketFactory(getApplicationContext());
            BasicNetwork network = new BasicNetwork(new HurlStack(null, sslSocketFactory));
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
            mSelfSslQueue = new RequestQueue(cache,network);
            mSelfSslQueue.start();

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        return mSelfSslQueue;
    }

    /**
     * 获取网络类型
     *    可以使用getsubType获取手机网络的具体运营商的类型
     */
    public NetType getNewWorkType(){
        ConnectivityManager connectMgr = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if(networkInfo == null || (!networkInfo.isConnected())){
            return NetType.NOTHING;
        }
        if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return NetType.WIFI;
        else return NetType.MOBILE;
    }

    /**
     * 获取手机IP 地址
     * @return
     */
//    public String getPhoneIp(){
//        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//    }


}
