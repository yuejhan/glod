package com.github.financing.requester;

import android.util.Log;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * Created by user on 2016/10/20.
 */
public class SSLCertificateValidation {

    /**
     * 信任所有的证书
     */
    public static void trustAllCertificate(){
        try{
            // 设置tls方式
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // new一个自定义的TurstManager数组，自定义类中不做任何操作
            TrustManager[] trustManagers = {new NullX509TrustManager()};
            // 初始化
            sslContext.init(null,trustManagers,null);
            //
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            //
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());

        }catch (Exception e){
            e.printStackTrace();
            Log.e("TLS exception",e.getMessage());
        }
    }

    /**
     * 自定义类实现X509TrustManager接口，但方法不做实现
     */
    private static class NullX509TrustManager implements X509TrustManager{

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 自定义类实现HostnameVerifier接口，验证方法直接返回true，默认信任所有
     */
    private static class NullHostNameVerifier implements HostnameVerifier{

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
