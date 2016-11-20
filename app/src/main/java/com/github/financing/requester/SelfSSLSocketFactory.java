package com.github.financing.requester;

import android.content.Context;
import android.provider.SyncStateContract;

import com.github.financing.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by user on 2016/10/20.
 */
public class SelfSSLSocketFactory {

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            return setCertificates(context, context.getAssets().open(Constants.trustStoreFileName)) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }

    /**
     * 产生SSLsocketfactory
     * @param context 上下文
     * @param certificates 证书读入流
     */
    private static SSLSocketFactory setCertificates(Context context,InputStream... certificates){
        try{
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates){
                String certificateAlias = Integer.toString(index++);

                keyStore.setCertificateEntry(certificateAlias,certificateFactory.generateCertificate(certificate));

                // 关闭流
                if(certificate != null){
                    try {
                        certificate.close();
                    }catch (IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            }

            // 获取SSL的SSLcontext实例
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // 初始化keystore
//            KeyStore.getInstance()

            return sslContext.getSocketFactory();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
