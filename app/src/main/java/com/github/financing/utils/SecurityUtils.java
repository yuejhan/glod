package com.github.financing.utils;

/**
 * Created by Crazz on 7/14/15.
 */

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 1.商户的私钥初始化
 * 2.商户验签的公钥初始化
 * 3.根据商户号加密数据
 * 4.验签富友返回数据
 *
 */

public class SecurityUtils {

    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    private static String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAO2u+9vkuQGYwe2Yw0XFXRdhCkvwY4328H2STJjeW2LGDJqYYQVbpw1CNwJ0hKKcpk1/APENbdv84RP7x3YEkQVNoB0uSj8qnpUsnuyBdxLdToohikvOrNRWgQx/ZvgFE+rWjka9wVaKqLUbUWxpq9GiUAPFa78kYOABD8dIMtg9AgMBAAECgYEAgpNzQiaxjLMDNyiJfrcioUlqgrWZu9BB5nqNIh5mTilHm1bDVlI3wAz0c6DXjQ5KPqDbP5KFHCoc7QGRXsC7egNBX9kNtL7ZCuYw78pE5sNM4+885fgoqaBCbnc+PxgyAqQ+ZIO5u6QKXQpEoe7PpvxCVBAGyn/1klaQVidUivECQQD87PnV05v8ibOv0N6cSpEZ8s/mdFVDSw0sFBdxMseFGY/WjDl1g9ZQCuwjrcT5S/mnYgb6MzRJn+s0rfCFlImLAkEA8JKURMVg6GqIleQq4e03uqEZ6AgErBlh2e+1/T9vgij6n/ueZysamHydZAupk3Wsfn1bkmdA4zqOCf7UZueOVwJAHDwIF8qrmyF0IahbcW8Ri6gDdWJ/MifqrIUBqO1WQJF98SFuOKQjBIRzn/gCCSJmGD1lMgENUTq88wCH3SGbyQJBAJzEuDAUe3EZM0aSOEufvQg2QV6OExVfOP+/ENYmB3FHaQLmAjRyx1MFKb9vRiMctLp80DaYaJVqq/Lhh+JDFOMCQQCXrBhjTx4KfLzfUhOVzm5D8w5sAn9Sg1jDeMwe8tyiyUBbbkw+k9qK0YLOfnwKuC3MNI5URjaKyLzilPDZZkrs";



    static String FUIOU_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDcp9NU29EfqPReJLGBS0WZwCKxORrc4IQpKbup1cF4KzQnpMCwcJXF9KW1vJ/ZzOMwAlGfhq2V96MGPOO6T/Zkesasjdmy19wnOdzDxGXu2pEMbFMDOonYxf1m5/VNs2+TZ18eyW585XefXoNlYCzg6RJmXK0fZ1UPAU9ZxgocEQIDAQAB";



    public static byte[] rsaEncrypt(byte[] bytes) throws Exception {
//        Log.i("====bytes==",new String(bytes));
        Signature signature = Signature.getInstance("SHA256withRSA", "SC");
        signature.initSign(getRSAPrivateKeyFromString(PRIVATE_KEY));
        signature.update(bytes);
        return signature.sign();
    }




    public static boolean rsaDecrypt(byte[] bytes) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA", "SC");
        signature.initSign(getRSAPrivateKeyFromString(FUIOU_PUB_KEY));
        return signature.verify(bytes);
    }

    private static PrivateKey getRSAPrivateKeyFromString(String key) throws Exception {
        byte [] clear = Base64.decode(key, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory fact = KeyFactory.getInstance("RSA", "SC");
        PrivateKey priv = fact.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return priv;
    }

    private static PublicKey getRSAPublicKeyFromString(String apiKey) throws Exception{
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "SC");
        byte[] publicKeyBytes = Base64.decode(apiKey.getBytes("UTF-8"), Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(x509KeySpec);
    }


    public static byte[] encrypt(String message, String key)
            throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    public static byte[] convertHexString(String ss)
    {
        byte digest[] = new byte[ss.length() / 2];
        for(int i = 0; i < digest.length; i++)
        {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte)byteValue;
        }

        return digest;
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    public static void main(String[] args) {
        String key = CommonUtil.joinKey();
        System.out.println(key+"     "+ CommonUtil.currentTimeFormat());

        try {
//            System.out.println(toHexString(encrypt("0105",key)));
//            System.out.println(toHexString(encrypt("1000",key)));
//            System.out.println(toHexString(encrypt("1000",key)));
//            System.out.println(toHexString(encrypt("6217000010088309300",key)));
//            System.out.println(toHexString(encrypt("371525199205141027",key)));
//            System.out.println(toHexString(encrypt("牛贝贝",key)));
//            System.out.println(toHexString(encrypt("15110227181",key)));
//            System.out.println(toHexString(encrypt("2017040521584811120170405215848111",key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
