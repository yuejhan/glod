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
    public static int dip2px(Context ctx,float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    } 
 
  
    /** 
     *	pxתdp
     */  
    public static int px2dip(Context ctx,float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }


    public static String makePostHTML(String apiURl,Map<String,String> formData){
        String html = "<!DOCTYPE HTML><html><body><form id='sbform' action='%s' method='post'>%s</form><script type='text/javascript'>document.getElementById('sbform').submit();</script></body></html>";
        List<String> list = new ArrayList<>(formData.size());
        String input = "<input type='hidden' name='%s' value='%s'/>";
        for(Map.Entry<String,String> entry:formData.entrySet()){
            list.add(String.format(input,entry.getKey(),entry.getValue()));
        }
        return String.format(html, apiURl, StringUtils.join(list, "\n"));
    }

    public static boolean checkPhoneNumber(String phoneNumber){
        Pattern pattern = Pattern.compile(regPhone);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean checkPassword(String password){
        return password != null;
//        return password != null && (password.length() > 6) ;
    }
}
