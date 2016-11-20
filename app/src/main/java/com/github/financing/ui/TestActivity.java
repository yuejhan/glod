package com.github.financing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.financing.R;
import com.github.financing.base.BaseActivity;
import com.github.financing.requester.DataRequester;
import com.github.financing.utils.CommonUtil;
import com.github.financing.utils.Constants;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/10/20.
 */
public class TestActivity extends BaseActivity {

    public static final String HTTP = "http://192.168.1.22:8080/api/getDate";
    public static final String HTTPS = "http://www-1.fuiou.com:9057/jzh/app/500002.action";

    public TextView tvResult;
    public Button testButton;
    RequestQueue requestQueue;
    StringRequest sr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        tvResult = (TextView)findViewById(R.id.tv_result);
        testButton = (Button) findViewById(R.id.test_request);
        requestQueue = Volley.newRequestQueue(this);
        sr = new StringRequest(Request.Method.POST,HTTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TestActivity.this, "HTTP/GET,StringRequest successfully.", Toast.LENGTH_SHORT).show();
                tvResult.setText(response);
                ObjectMapper objectMapper = new ObjectMapper();
                Map map = new HashMap();
                try {
                     map = objectMapper.readValue(response.getBytes(), Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                goteWebView(HTTPS,map);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                stringRequestGetHttpExample();
                requestQueue.add(sr);
            }
        });
    }

    public void goteWebView(String url,Map<String,String> formatData){
        String postData = CommonUtil.makePostHTML(url, formatData);
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra(Constants.INTENT_API_DATA_KEY_DATA, postData);
        startActivity(intent);
    }

    private void stringRequestGetHttpExample(){
        DataRequester.withHttp(this)
                .setUrl(HTTP)
                .setMethod(DataRequester.Method.POST)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TestActivity.this, "HTTP/GET,StringRequest successfully.", Toast.LENGTH_SHORT).show();
                        tvResult.setText(response);
//                        goteWebView("http://www-1.fuiou.com:9057/jzh",);
                    }
                })
                .requestString();
    }

    private void stringRequestPostHttpExample(){

        HashMap<String, String> body = new HashMap<String, String>() ;
        body.put( "name", "xxx" );
        body.put( "age", "20" );

        DataRequester.withHttp(this)
                .setUrl(HTTP)
                .setBody(body)
                .setMethod(DataRequester.Method.POST)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(TestActivity.this, "HTTP/POST,StringRequest successfully.", Toast.LENGTH_SHORT).show();
                        tvResult.setText(response);
                    }
                })
                .requestString();
    }

    private void jsonRequestGetHttpsExample(){
        DataRequester.withDefaultHttps(this)
                .setUrl(HTTPS)
                .setJsonResponseListener(new DataRequester.JsonResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String s = response.getString("data");
                            tvResult.setText(s);
                            Toast.makeText(TestActivity.this, "HTTPS/GET, JsonRequest successfully.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResult.setText(error.getMessage());
                    }
                })
                .requestJson();
    }

    private void jsonRequestPostHttpsExample(){
        JSONObject json = new JSONObject();
        try{
            json.put( "name", "xxx" );
            json.put( "age", "20" );
        }catch (Exception e){
            e.printStackTrace();
        }

        DataRequester.withDefaultHttps(this)
                .setUrl(HTTPS)
                .setBody(json)
                .setJsonResponseListener(new DataRequester.JsonResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String data = response.getString("data");
                            tvResult.setText(data);

                            Toast.makeText(TestActivity.this, "HTTPS/POST, JsonRequest successfully.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResult.setText(error.getMessage());
                    }
                })
                .requestJson();
    }
}
