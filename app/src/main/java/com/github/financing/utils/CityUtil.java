package com.github.financing.utils;

import android.content.Context;

import com.github.financing.bean.Cityinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * 城市代码
 * 
 * @author zq
 * 
 */
public class CityUtil {

	private ArrayList<String> province_list = new ArrayList<String>();
	private ArrayList<String> city_list = new ArrayList<String>();
	private ArrayList<String> bank_list = new ArrayList<String>();
	public ArrayList<String> province_list_code = new ArrayList<String>();
	public ArrayList<String> city_list_code = new ArrayList<String>();
	public ArrayList<String> bank_list_code = new ArrayList<String>();
	/** 单例 */
	public static CityUtil model;
	private Context context;

	private CityUtil() {
	}

	public ArrayList<String> getProvince_list_code() {
		return province_list_code;
	}
	public String getProvinceCode(int i){
		return province_list_code.get(i);
	}

	public ArrayList<String> getCity_list_code() {
		return city_list_code;
	}

	public String getCityCode(int i){
		return city_list_code.get(i);
	}

	public ArrayList<String> getBank_list_code() {
		return bank_list_code;
	}

	public String getBankCode(int i){
		return bank_list_code.get(i);
	}


	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static CityUtil getSingleton() {
		if (null == model) {
			model = new CityUtil();
		}
		return model;
	}

	public ArrayList<String> getProvince(List<Cityinfo> provice) {
		if (province_list_code.size() > 0) {
			province_list_code.clear();
		}
		if (province_list.size() > 0) {
			province_list.clear();
		}
		for (int i = 0; i < provice.size(); i++) {
			province_list.add(provice.get(i).getCity_name());
			province_list_code.add(provice.get(i).getId());
		}
		return province_list;

	}

	public ArrayList<String> getCity(
			HashMap<String, List<Cityinfo>> cityHashMap, String provicecode) {
		if (city_list_code.size() > 0) {
			city_list_code.clear();
		}
		if (city_list.size() > 0) {
			city_list.clear();
		}
		List<Cityinfo> city = new ArrayList<Cityinfo>();
		city = cityHashMap.get(provicecode);
		for (int i = 0; i < city.size(); i++) {
			city_list.add(city.get(i).getCity_name());
			city_list_code.add(city.get(i).getId());
		}
		return city_list;

	}

	public ArrayList<String> getBank(List<Cityinfo> bank) {
		if (bank_list_code.size() > 0) {
			bank_list_code.clear();
		}
		if (bank_list.size() > 0) {
			bank_list.clear();
		}
		for (int i = 0; i < bank.size(); i++) {
			bank_list.add(bank.get(i).getCity_name());
			bank_list_code.add(bank.get(i).getId());
		}
		return bank_list;
	}
}
