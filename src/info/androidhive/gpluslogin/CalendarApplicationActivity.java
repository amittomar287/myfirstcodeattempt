package com.example.calendartask;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.view.View;
import android.widget.Toast;

public class CalendarApplicationActivity  extends Activity {  
     
    public static final String[] EVENT_PROJECTION = new String[] {  
            Calendars._ID, // 0  
            Calendars.ACCOUNT_NAME, // 1  
            Calendars.CALENDAR_DISPLAY_NAME // 2  
    };  
  
    
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
    }  
  
    public void onClick(View view) {  
 
    	 ContentResolver cr =this.getContentResolver();
         ContentValues values = new ContentValues();

         values.put(CalendarContract.Events.DTSTART,System.currentTimeMillis());
         values.put(CalendarContract.Events.TITLE, "this is android implementation");
         values.put(CalendarContract.Events.DESCRIPTION, "ssdmsdsdmsdsdsdjsd");

         TimeZone timeZone = TimeZone.getDefault();
         values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

         // default calendar
         values.put(CalendarContract.Events.CALENDAR_ID, 1);

         values.put(CalendarContract.Events.RRULE, "FREQ=WEEKLY;COUNT=11;WKST=SU;BYDAY=TU,TH");
         //for one hour
         values.put(CalendarContract.Events.DURATION, "+P1H");

         values.put(CalendarContract.Events.HAS_ALARM, 1);

         // insert event to calendar
         Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
         
    }  
}
