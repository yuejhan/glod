package com.github.financing.requester;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.financing.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2016/12/4
 * 描述：
 *******************************************/
public class RequestUtil {
    public static ObjectMapper mapper = new ObjectMapper();
    public static Map parseResponse(String response){
        try{
            return mapper.readValue(response, HashMap.class);
        }catch (Exception e){
            return null;
        }
    }
}
