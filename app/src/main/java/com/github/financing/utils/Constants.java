package com.github.financing.utils;

public class Constants {

    public static final String APP_BASE_URL = "http://www.thjcf.com.cn/MI/Common";
    //手机服务端基本地址
    public static final String APP_SERVER_BASE_URL = "http://192.168.1.22";
    // 版本检查
    public static final String APP_SERVER_CHECKVERSION = APP_SERVER_BASE_URL + "/api/checkVersion";
    public static final String INTENT_API_DATA_KEY_DATA = "intent_api_data_key_data";


    //手机app服务端（前置）地址
//    private static final String APP_SERVER_BASE_URL = "http://192.168.199.184:8080";

    public static final String APP_SERVER_APP_WEB_REG_URL = APP_SERVER_BASE_URL+"/jzh/appWebReg.do";
    public static final String APP_SERVER_500001_URL = APP_SERVER_BASE_URL+"/jzh/api500001.do";
    public static final String APP_SERVER_500002_URL = APP_SERVER_BASE_URL+"/jzh/api500002.do";
    public static final String APP_SERVER_500003_URL = APP_SERVER_BASE_URL+"/jzh/api500003.do";
    public static final String APP_SERVER_400101_URL = APP_SERVER_BASE_URL+"/jzh/api400101.do";
    public static final String APP_SERVER_APP_SIGN_URL = APP_SERVER_BASE_URL+"/jzh/appSign.do";


    //富友接口地址
    private static final String JZH_API_BASE_URL = "http://www-1.fuiou.com:9057/jzh";

    public static final String JZH_API_APP_WEB_REG_URL = JZH_API_BASE_URL+"/app/appWebReg.action";
    public static final String JZH_API_APP_500001_URL = JZH_API_BASE_URL+"/app/500001.action";
    public static final String JZH_API_APP_500002_URL = JZH_API_BASE_URL+"/app/500002.action";
    public static final String JZH_API_APP_500003_URL = JZH_API_BASE_URL+"/app/500003.action";
    public static final String JZH_API_APP_400101_URL = JZH_API_BASE_URL+"/app/400101.action";
    public static final String JZH_API_APP_SIGN_URL = JZH_API_BASE_URL+"/app/appSign_Card.action";

    public static final String trustStoreFileName = "server.cer";



    public static final String MOBILEPHONE = "mobliePhone";
    public static final String VALIDCODE = "validCode";
}
