package com.iosxc.android.utils;

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

    private static String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9FN1w8gfXSBP1/"
            + "\r" + "fWtC4gicvB7t+XZ20Qn3eBOaMT1zYf6QtUQ1aAQKIlVDmyidA1/BOgwp07Rvc6V/" + "\r"
            + "imAEp4tOGtrP8vedgliVuqMcLeNONSdlzSW66alcayjHrb4+5IYGV9vzMk7qGLHg" + "\r"
            + "ZX++HJBUKkb1piqATvPJNFlhf1vJAgMBAAECgYA736xhG0oL3EkN9yhx8zG/5RP/" + "\r"
            + "WJzoQOByq7pTPCr4m/Ch30qVerJAmoKvpPumN+h1zdEBk5PHiAJkm96sG/PTndEf" + "\r"
            + "kZrAJ2hwSBqptcABYk6ED70gRTQ1S53tyQXIOSjRBcugY/21qeswS3nMyq3xDEPK" + "\r"
            + "XpdyKPeaTyuK86AEkQJBAM1M7p1lfzEKjNw17SDMLnca/8pBcA0EEcyvtaQpRvaL" + "\r"
            + "n61eQQnnPdpvHamkRBcOvgCAkfwa1uboru0QdXii/gUCQQDGmkP+KJPX9JVCrbRt" + "\r"
            + "7wKyIemyNM+J6y1ZBZ2bVCf9jacCQaSkIWnIR1S9UM+1CFE30So2CA0CfCDmQy+y" + "\r"
            + "7A31AkB8cGFB7j+GTkrLP7SX6KtRboAU7E0q1oijdO24r3xf/Imw4Cy0AAIx4KAu" + "\r"
            + "L29GOp1YWJYkJXCVTfyZnRxXHxSxAkEAvO0zkSv4uI8rDmtAIPQllF8+eRBT/deD" + "\r"
            + "JBR7ga/k+wctwK/Bd4Fxp9xzeETP0l8/I+IOTagK+Dos8d8oGQUFoQJBAI4Nwpfo" + "\r"
            + "MFaLJXGY9ok45wXrcqkJgM+SN6i8hQeujXESVHYatAIL/1DgLi+u46EFD69fw0w+" + "\r" + "c7o0HLlMsYPAzJw="
            + "\r";


    static String FUIOU_PUB_KEY = "MIICXgIBAAKBgQDtrvvb5LkBmMHtmMNFxV0XYQpL8GON9vB9kkyY3ltixgyamGEFW6cNQjcC" +
            "dISinKZNfwDxDW3b/OET+8d2BJEFTaAdLko/Kp6VLJ7sgXcS3U6KIYpLzqzUVoEMf2b4BRPq1o5GvcFWiqi" +
            "1G1FsaavRolADxWu/JGDgAQ/HSDLYPQIDAQABAoGBAIKTc0ImsYyzAzcoiX63IqFJaoK1mbvQQeZ6jSIe" +
            "Zk4pR5tWw1ZSN8AM9HOg140OSj6g2z+ShRwqHO0BkV7Au3oDQV/ZDbS+2QrmMO/KRObDTOPvPOX4KKmgQm" +
            "53Pj8YMgKkPmSDubukCl0KRKHuz6b8QlQQBsp/9ZJWkFYnVIrxAkEA/Oz51dOb/Imzr9DenEqRGfLP5nRV" +
            "Q0sNLBQXcTLHhRmP1ow5dYPWUArsI63E+Uv5p2IG+jM0SZ/rNK3whZSJiwJBAPCSlETFYOhqiJXkKuHtN7q" +
            "hGegIBKwZYdnvtf0/b4Io+p/7nmcrGph8nWQLqZN1rH59W5JnQOM6jgn+1GbnjlcCQBw8CBfKq5shdCGoW3" +
            "FvEYuoA3VifzIn6qyFAajtVkCRffEhbjikIwSEc5/4AgkiZhg9ZTIBDVE6vPMAh90hm8kCQQCcxLgwFHtx" +
            "GTNGkjhLn70INkFejhMVXzj/vxDWJgdxR2kC5gI0csdTBSm/b0YjHLS6fNA2mGiVaqvy4YfiQxTjAkEAl6" +
            "wYY08eCny831ITlc5uQ/MObAJ/UoNYw3jMHvLcoslAW25MPpPaitGCzn58CrgtzDSOVEY2isi84pTw2WZK7A==";



    public static byte[] rsaEncrypt(byte[] bytes) throws Exception {
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

}
