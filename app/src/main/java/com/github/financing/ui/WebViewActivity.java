package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.github.financing.R;
import com.github.financing.base.BaseActivity;
import com.github.financing.utils.Constants;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/22
 * 描述：
 *******************************************/
public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    String postData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        postData = intent.getStringExtra(Constants.INTENT_API_DATA_KEY_DATA);
        Log.i("webView",postData);
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadData(postData ,"text/html", "UTF-8");
    }
}
