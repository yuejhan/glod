package com.github.financing.views.citypacker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.github.financing.R;
import com.github.financing.bean.Cityinfo;
import com.github.financing.utils.CityUtil;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;


/**
 * 城市Picker
 * 
 * @author zq
 * 
 */
public class CityPickerView extends LinearLayout {
	/** 滑动控件 */
	private ScrollerCity provincePicker;
	private ScrollerCity cityPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;

	private Context context;
    private CityUtil citycodeUtil;
	private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
	private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
	private static ArrayList<String> province_list_code = new ArrayList<String>();
	private static ArrayList<String> city_list_code = new ArrayList<String>();

	private String city_code_string;
	private String city_string;

	public CityPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
	}

	public CityPickerView(Context context) {
		super(context);
		this.context = context;
        getaddressinfo();
	}

	// 获取城市信息
	private void getaddressinfo() {
        for(int i =1;i<10;i++){
            Cityinfo cityinfo = new Cityinfo();
            cityinfo.setId(i+"");
            cityinfo.setCity_name("省份"+i);
            province_list.add(cityinfo);
        }
        for(int i = 1;i<10;i++){
            ArrayList<Cityinfo> cityinfos = new ArrayList<>();
            for(int j=1;j<6;j++){
                Cityinfo cityinfo = new Cityinfo();
                cityinfo.setId(j+"");
                cityinfo.setCity_name("城市" + i + "-" + j);
                cityinfos.add(cityinfo);
            }
            city_map.put(i+"",cityinfos);
        }

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);

        citycodeUtil = CityUtil.getSingleton();
		// 获取控件引用
		provincePicker = (ScrollerCity) findViewById(R.id.province);
		cityPicker = (ScrollerCity) findViewById(R.id.city);

		provincePicker.setData(citycodeUtil.getProvince(province_list));
		provincePicker.setDefault(0);

		cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(0)));
		cityPicker.setDefault(0);


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

	public String getCity_code_string() {
		return city_code_string;
	}

	public String getCity_string() {
		city_string = provincePicker.getSelectedText()
				+ cityPicker.getSelectedText();
		return city_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
