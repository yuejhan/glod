package com.iosxc.android.jzhdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Crazz on 7/14/15.
 */
public class JzhWebActivity extends Activity {
    public static final String INTENT_EXTRA_KEY_APIURL = "intent_extra_key_apiurl";
    public static final String INTENT_EXTRA_KEY_POSTDATA = "intentW_extra_key_postdata";

    private WebView jzhWebView;
    private String apiUrl;
    private byte[] postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jzh_web);
        apiUrl = getIntent().getStringExtra(INTENT_EXTRA_KEY_APIURL);
        postData = getIntent().getByteArrayExtra(INTENT_EXTRA_KEY_POSTDATA);
        Log.e("jzh","URL:"+apiUrl+"\nDATA:"+new String(postData));
        jzhWebView = (WebView) findViewById(R.id.jzhWebView);
        jzhWebView.setWebChromeClient(new WebChromeClient());
        jzhWebView.postUrl(apiUrl, postData);
    }
}
