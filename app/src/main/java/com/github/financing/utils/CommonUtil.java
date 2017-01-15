package com.github.financing.utils;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonUtil {

    private static final String regPhone = "^1\\d{10}$";
    private static final String number = "^((([1-9]{1}//d{0,9}))|([0]{1}))((//.(//d){2}))?$";
    public static int dip2px(Context ctx,float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    } 

    public static boolean checkNumber(String value){
        Pattern pattern = Pattern.compile(number);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
  
    /** 
     *	pxתdp
     */  
    public static int px2dip(Context ctx,float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }


    // 富有表单提交
    public static String makePostHTML(String apiURl,Map<String,String> formData){
        String html = "<!DOCTYPE HTML><html><body><form id='sbform' action='%s' method='post'>%s</form><script type='text/javascript'>document.getElementById('sbform').submit();</script></body></html>";
        List<String> list = new ArrayList<>(formData.size());
        String input = "<input type='hidden' name='%s' value='%s'/>";
        for(Map.Entry<String,String> entry:formData.entrySet()){
            list.add(String.format(input,entry.getKey(),entry.getValue()));
        }
        return String.format(html, apiURl, StringUtils.join(list, "\n"));
    }

    // 手机号校验
    public static boolean checkPhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile(regPhone);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // 密码校验
    public static boolean checkPassword(String password){
        return password != null;
    }

    //

}
