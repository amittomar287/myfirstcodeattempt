package com.example.geofenceapp;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

	public static String add_name="add_name",address="address",add_icon="add_icon",add_duration="add_duration";
	int myicon;	
	static class ViewHolder 
	{
		TextView txt_location;
		TextView txt_tym_duration;
		ImageView img_location;
		
	}
	private Context mContext;
	private LayoutInflater mInflater=null;
	private ArrayList<HashMap<String, String>> mItems;
   
	public CustomListAdapter(Context context, ArrayList<HashMap<String, String>> items) 
	{
		Log.e("call ", "consutracrw");
		mContext = context;
		if (mItems != null && mItems.size() > 0) 
		{
			mItems.clear();
			mItems = null;
		}
		mItems = items;
		
	}

	@Override
	public int getCount() {
		Log.e("size", mItems.size()+"s");
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}
	
	@Override
	public void notifyDataSetChanged() // Create this function in your adapter class
	{
	    super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final HashMap<String, String> map = mItems.get(position);
		if (convertView == null) 
		{	
			mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_row_items, null);
			holder = new ViewHolder();
			holder.txt_location = (TextView) convertView.findViewById(R.id.add_name);
			holder.txt_tym_duration = (TextView) convertView.findViewById(R.id.add_duration);
			holder.img_location=(ImageView) convertView.findViewById(R.id.add_icon);
		
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Log.e("mydata", ""+map.get(add_icon)+  ",,,,,,,,,,,"+map.get(add_duration)+",,,,,,");
		int icon = Integer.parseInt(map.get(add_icon));
		if(icon==0)
			myicon=R.drawable.home_icon_black;
		if(icon==1)
			myicon=R.drawable.office_icon;
		if(icon==2)
			myicon=R.drawable.other_icon;
		if(icon==3)
			myicon=R.drawable.home_icon;
		if(icon==4)
			myicon=R.drawable.point_icon;
		if(icon==5)
			myicon=R.drawable.home;
		
		//holder.img_location.setImageResource(icon);
		holder.img_location.setImageResource(myicon);
		holder.txt_location.setText("" + map.get(add_name));
		holder.txt_tym_duration.setText("" + map.get(add_duration)+"m");
	
		
		
		return convertView;
	}
}
