package com.example.geofenceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

	static String GEOFENCE_DATABASE = "geofence.sqlite";

	public MySQLiteHelper(Context context) {
		
		super(context, GEOFENCE_DATABASE, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table GeofenceData(address_name TEXT,address TEXT,address_icon TEXT,address_radius TEXT,address_entry_time TEXT,address_time_duration TEXT)");
	
	}
	
	public void updateRow(String entryTime,String exitTime)
	{
		
		SQLiteDatabase db = this.getWritableDatabase();
		String strSQL = "UPDATE GeofenceData SET address_time_duration = '"+ exitTime+"' WHERE address_entry_time = '"+ entryTime+"'";
	
//		UPDATE Customers
//		SET ContactName='Alfred Schmidt', City='Hamburg'
//		WHERE CustomerName='Alfreds Futterkiste';

		
		db.execSQL(strSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
