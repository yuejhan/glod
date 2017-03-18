package com.github.financing.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.financing.R;
import com.github.financing.bean.Cityinfo;
import com.github.financing.requester.DataRequester;
import com.github.financing.requester.RequestUtil;
import com.github.financing.utils.CityUtil;
import com.github.financing.utils.CommonUtil;
import com.github.financing.utils.Constants;
import com.github.financing.utils.FileUtil;
import com.github.financing.views.citypacker.ScrollerCity;
import com.github.financing.views.dialog.SVProgressHUD;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/********************************************
 * 作者：Administrator
 * 时间：2017/3/14
 * 描述：
 *******************************************/
public class RegisterBankActivity extends AppCompatActivity {

    private TextView bankName,bankCityText,bindRegister,cityCancel,cityComment;
    private EditText userName,bindCertNo,bankNo;
    private RelativeLayout bankCityRl,binkBack;
    private View bankLayout,cityLayout;
    /** 滑动控件 */
    private ScrollerCity provincePicker,cityPicker,bankPicker;
    /** 选择监听 */
    private OnSelectingListener onSelectingListener;
    /** 刷新界面 */
    private static final int REFRESH_VIEW = 0x001;
    /** 临时日期 */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempBankIndex = -1;

    private Context context;
    private CityUtil citycodeUtil;
    private SVProgressHUD mSVProgressHUD;
    private boolean flag = false;
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private List<Cityinfo> bank_list = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bind_bankCity:
                    hindSoftInput();
                    flag = false;
                    bankLayout.setVisibility(View.GONE);
                    bankCityRl.setVisibility(View.VISIBLE);
                    cityLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.bind_bankName:
                    hindSoftInput();
                    flag = true;
                    cityLayout.setVisibility(View.GONE);
                    bankCityRl.setVisibility(View.VISIBLE);
                    bankLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.bind_city_comment:
                    if(flag){
                        bankName.setText(getBank_string());
                    }else{
                        bankCityText.setText(getCity_string());
                    }
                    bankCityRl.setVisibility(View.GONE);
                    break;
                case R.id.bind_city_cancel:
                    bankCityRl.setVisibility(View.GONE);
                    break;
                case R.id.bind_register:
                    registerComment();
                    break;
                case R.id.bink_bank_back:
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bank);
        // 初始化视图信息
        initView();
        // 初始化地区选择框
        initCityPicker();
        mSVProgressHUD = new SVProgressHUD(this);
        //初始化数据信息
        getaddressinfo();
    }

    private void initView(){
        userName = (EditText) this.findViewById(R.id.bind_username);
        bindCertNo = (EditText) this.findViewById(R.id.bind_certNo);
        bankName = (TextView) this.findViewById(R.id.bind_bankName);
        bankName.setOnClickListener(clickListener);
        bankNo = (EditText) this.findViewById(R.id.bind_bankNo);
        bankCityText = (TextView) this.findViewById(R.id.bind_bankCity);
        bankCityText.setOnClickListener(clickListener);
        bindRegister = (TextView) this.findViewById(R.id.bind_register);
        bindRegister.setOnClickListener(clickListener);

        bankCityRl = (RelativeLayout) this.findViewById(R.id.bind_bankCity_view);
        bankCityRl.setOnClickListener(null);
        cityCancel = (TextView) this.findViewById(R.id.bind_city_cancel);
        cityCancel.setOnClickListener(clickListener);
        cityComment = (TextView) this.findViewById(R.id.bind_city_comment);
        cityComment.setOnClickListener(clickListener);

        binkBack = (RelativeLayout) this.findViewById(R.id.bink_bank_back);
        binkBack.setOnClickListener(clickListener);

        bankLayout = this.findViewById(R.id.bank_layout);
        cityLayout = this.findViewById(R.id.city_layout);
    }

    // 获取城市信息
    private void getaddressinfo() {
        BufferedReader bufferedReader = null;
        InputStreamReader inputReader = null;
        try {
            inputReader = new InputStreamReader(getResources().getAssets().open("g.txt"));
            bufferedReader = new BufferedReader(inputReader);
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null){
                String[] split = temp.split(",");
                if("1".equals(split[2].trim())){
                    if(split[0].length()>=7){
                        split[0]=split[0].substring(1);
                    }
                    Cityinfo cityinfo = new Cityinfo();
                    cityinfo.setId(split[0].trim());
                    cityinfo.setCity_name(split[1]);
                    province_list.add(cityinfo);
                }else{
                    List<Cityinfo> cityinfos =  city_map.get(split[2].trim());
                    Cityinfo cityinfo = new Cityinfo();
                    cityinfo.setId(split[0].trim());
                    cityinfo.setCity_name(split[1].trim());

                    if(cityinfos == null){
                        cityinfos = new ArrayList<>();
                    }
                    cityinfos.add(cityinfo);
                    city_map.put(split[2].trim(),cityinfos);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
            if(inputReader != null){
                try {
                    inputReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        province_list.clear();
//        for(int i =1;i<10;i++){
//            Cityinfo cityinfo = new Cityinfo();
//            cityinfo.setId("code"+i+"");
//            cityinfo.setCity_name("省份"+i);
//            province_list.add(cityinfo);
//        }
//        city_map.clear();
//        for(int i = 1;i<10;i++){
//            ArrayList<Cityinfo> cityinfos = new ArrayList<>();
//            for(int j=1;j<6;j++){
//                Cityinfo cityinfo = new Cityinfo();
//                cityinfo.setId("code"+j+"");
//                cityinfo.setCity_name("城市" + i + "-" + j);
//                cityinfos.add(cityinfo);
//            }
//            city_map.put("code"+i+"",cityinfos);
//        }
       bank_list = citycodeUtil.getBankList();

        provincePicker.setData(citycodeUtil.getProvince(province_list));
        provincePicker.setDefault(0);

        cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(0)));
        cityPicker.setDefault(0);

        bankPicker.setData(citycodeUtil.getBank(bank_list));
        bankPicker.setDefault(0);

    }

    private void registerComment(){
        String userNameStr = userName.getText().toString();
        if(CommonUtil.checkNull(userNameStr)){
            mSVProgressHUD.showInfoWithStatus("请填写正确的用户名");
            userName.setFocusable(true);
            return;
        }
        String certNoStr = bindCertNo.getText().toString();
        if(!CommonUtil.checkCertNo(certNoStr)){
            mSVProgressHUD.showInfoWithStatus("请填写正确的身份证号码");
            bindCertNo.setFocusable(true);
            return;
        }

        String bankNameStr = bankName.getText().toString();
        if(CommonUtil.checkNull(bankNameStr)){
            mSVProgressHUD.showInfoWithStatus("请选择银行名称");
            return;
        }

        String bankNoStr = bankNo.getText().toString();
        if(!CommonUtil.checkBankNo(bankNoStr)){
            mSVProgressHUD.showInfoWithStatus("请填写正确的银行卡号");
            bankNo.setFocusable(true);
            return;
        }
        String cityStr = bankCityText.getText().toString();
        if(CommonUtil.checkNull(cityStr)){
            mSVProgressHUD.showInfoWithStatus("请选择开户行地区");
            return;
        }
        mSVProgressHUD.showWithStatus("注册中...",SVProgressHUD.SVProgressHUDMaskType.Black);
        // 请求网络
        Map<String,String> body = new HashMap<String, String>();
        body.put("bank",citycodeUtil.getBankCode(tempBankIndex));
        body.put("city_id",citycodeUtil.getCityCode(temCityIndex));
        body.put("cust_nm","");
        body.put("capAcntNo",bankNoStr);
        body.put("certif_id",certNoStr);
        body.put("realName",userNameStr);
        body.put("mobile_no", FileUtil.getStringValue("userPhone"));
        Log.e("params",body.toString());
        DataRequester
                .withHttp(getApplicationContext())
                .setUrl(Constants.APP_BASE_URL+"/Account/RegFuyu")
                .setMethod(DataRequester.Method.POST)
                .setBody(body)
                .setStringResponseListener(new DataRequester.StringResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Map map = RequestUtil.parseResponse(response);
                        if(map != null && "0000".equals(map.get("code"))){
                            mSVProgressHUD.cancelWithSuccessContext("注册成功!");
                        }else{
                            mSVProgressHUD.cancelWithErrorContext("注册失败");
                        }
                    }
                })
                .setResponseErrorListener(new DataRequester.ResponseErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(mSVProgressHUD.isShowing()){
                            mSVProgressHUD.cancelWithErrorContext("注册失败,网络异常!");
                        }
                    }
                }).requestString();

    }

    private void initCityPicker(){
        citycodeUtil = CityUtil.getSingleton();
        // 获取控件引用
        provincePicker = (ScrollerCity) findViewById(R.id.province);
        cityPicker = (ScrollerCity) findViewById(R.id.city);
        bankPicker = (ScrollerCity) findViewById(R.id.bank_list);

        provincePicker.setOnSelectListener(new ScrollerCity.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    // 城市数组
                    cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(id)));
                    cityPicker.setDefault(0);
                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        cityPicker.setOnSelectListener(new ScrollerCity.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub

            }
        });

        bankPicker.setOnSelectListener(new ScrollerCity.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (tempBankIndex != id) {
                    int lastDay = Integer.valueOf(bankPicker.getListSize());
                    if (id > lastDay) {
                        bankPicker.setDefault(lastDay - 1);
                    }
                }
                tempBankIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }


    public String getCity_string() {
        return  provincePicker.getSelectedText() + " "+cityPicker.getSelectedText();

    }
    public String getBank_string(){
        return bankPicker.getSelectedText();
    }
    private String getProviceCode(){
        return citycodeUtil.getProvinceCode(tempProvinceIndex);
    }
    private String getCityCode(){
        return citycodeUtil.getCityCode(temCityIndex);
    }
    private String getBankCode(){
        return citycodeUtil.getBankCode(tempBankIndex);
    }


    public interface OnSelectingListener {

        public void selected(boolean selected);
    }


    public void hindSoftInput() {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }

    }

}
