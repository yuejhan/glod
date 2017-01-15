package com.github.financing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.financing.application.DefaultApplication;

/********************************************
 * 作者：Administrator
 * 时间：2017/1/15
 * 描述：
 *******************************************/
public class FileUtil {

    public static void putValue(String key,String value){
        DefaultApplication instance = DefaultApplication.getInstance();
        SharedPreferences sp = instance.getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        Log.e("FileIo put",key+"--"+value);
        edit.putString(key,value);
        edit.commit();
    }

    public static String getStringValue(String key){
        DefaultApplication instance = DefaultApplication.getInstance();
        SharedPreferences sp = instance.getSharedPreferences("USER", Context.MODE_PRIVATE);
        String value = sp.getString(key, "");
        Log.e("FileIo  get",key+"--"+value);
        return value;
    }

    public static void clearPs(){
        DefaultApplication instance = DefaultApplication.getInstance();
        SharedPreferences sp = instance.getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
}
