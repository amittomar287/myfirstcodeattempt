package com.example.geofenceapp;


import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GeofenceDataAdapter {

	private static SQLiteDatabase db;
	BeanClass bean;
	ArrayList<HashMap<String, String>> array_list;
	public static String add_name="add_name",address="address",add_icon="add_icon",add_radius="add_radius",add_duration="add_duration",address_entryTime="address_entryTime";
	

	public GeofenceDataAdapter(Context context) {
		bean  = new BeanClass();
		array_list = new ArrayList<HashMap<String, String>>();
	

		if(db==null) {
			MySQLiteHelper helper = new MySQLiteHelper(context);
			db = helper.getWritableDatabase();
		}
	}

	public void addGeofenceData(BeanClass bean) {

		String sql = "insert into GeofenceData values('"+bean.address_name+"','"+bean.address+"','"+bean.address_icon+"','"+bean.address_radius+"','"+bean.address_entryTime+"','"+bean.address_TimeDuration+"')";
		db.execSQL(sql);

	}

	public ArrayList<HashMap<String,String>> getGeofenceData() {

		String sql = "select * from GeofenceData";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToLast();
		int finalTime=0;
		while(cursor.isBeforeFirst()==false){
			HashMap<String, String> map = new HashMap<String, String>();

			String s1 = cursor.getString(0);
			String s2 = cursor.getString(1);
			String s3 = cursor.getString(2);
			String s4 = cursor.getString(3);
			String s6 = cursor.getString(4);
			String s5 = cursor.getString(5);
		//	Log.e("data", ""+s1+  ",,,,,,,,,,,"+s2+",,,,,,"+s3+",,,,,,,"+s4);
			
			map.put(address, s2);
			map.put(add_icon, s3);
			map.put(add_radius, s4);
			map.put(add_name, s1);
			map.put(add_duration, s5);
			map.put(address_entryTime, s6);
			
			int time = Integer.parseInt(s5);
			
			finalTime=time+finalTime;
			
			System.out.println("----------finaltime"+finalTime);
			
			array_list.add(map);
			
			cursor.moveToPrevious();
			
		}
		
		
		
		return array_list;

	}
	
	
	
	public HashMap<String,String> getGeofenceDataBy3Fields(String address_name, String addressDetail, String address_radius) {
		HashMap<String, String> map = new HashMap<String, String>();
		BeanClass bean ;
		String sql = "select * from GeofenceData where address_name='"+address_name+"'";
		//String sql = "select * from GeofenceData";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		
		String s1 = cursor.getString(0);
		String s2 = cursor.getString(1);
		String s3 = cursor.getString(2);
		String s4 = cursor.getString(3);
		String s5 = cursor.getString(5);

		Log.e("3 Fields  ", ""+s1+  ",,,,,,,,,,,"+s2+",,,,,,"+s3+",,,,,,,"+s4);
		
		map.put(address, s2);
		map.put(add_icon, s3);
		map.put(add_radius, s4);
		map.put(add_name, s1);
		map.put(add_duration, s5);
		return map;
		
	}
	public void updateGeofenceData(BeanClass bean,String address_name){
		
		String sql = "update GeofenceData set address_name='"+bean.address_name+"',address='"+bean.address+"',address_icon='"+bean.address_icon+"',address_radius='"+bean.address_radius+"' where address_name='"+address_name+"'" ;                  
		//db.rawQuery(sql, null);
		db.execSQL(sql);
				
	}
	
	
	
	
}




