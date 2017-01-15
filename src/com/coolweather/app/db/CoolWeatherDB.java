package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class CoolWeatherDB {
	/*
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";
	/*
	 *数据库版本 
	 */
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	/*
	 * 将构造方法私有化
	 */
	
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	/*
	 * 获取CoolWeatherDB的实例。
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	/*
	 * 将Province实例存储到数据库
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.provinceName);
			values.put("province_code", province.provinceCode);
			db.insert("Province", null, values);
		}
	}
	/*
	 * 从数据库读取全国所有的省份信息。
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.id = cursor.getInt(cursor.getColumnIndex("id"));
				province.provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
				province.provinceCode = cursor.getString(cursor.getColumnIndex("province_code"));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	/*
	 *  将City实例存储到数据库
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.cityName);
			values.put("city_code", city.cityCode);
			values.put("province_id", city.provinceId);
			db.insert("City", null, values);
		}
	}
	/*
	 * 从数据库读取某省下所有得城市信息。
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.id = cursor.getInt(cursor.getColumnIndex("id"));
				city.cityName = cursor.getString(cursor.getColumnIndex("city_name"));
				city.cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
				city.provinceId = provinceId;
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	/*
	 * 将County实例存储到数据库
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.countyName);
			values.put("county_code", county.countyCode);
			values.put("city_id", county.cityId);
			db.insert("County", null, values);
		}
	}
	/*
	 * 从数据库读取某城市下所有县的信息
	 */
	public List<County> loadCounties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.id = cursor.getInt(cursor.getColumnIndex("id"));
				county.countyName = cursor.getString(cursor.getColumnIndex("county_name"));
				county.countyCode = cursor.getString(cursor.getColumnIndex("county_code"));
				county.cityId = cityId;
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
}
