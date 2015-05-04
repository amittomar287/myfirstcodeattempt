package com.example.geofenceapp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

@SuppressLint("NewApi")
public class MainActivity<MyCursorAdapter> extends Activity implements
		LocationListener {

		// Internal geofence objects
	SimpleGeofence mGeofence = null;
	// Persistent storage for geofences
	SimpleGeofenceStore mGeofenceStorage;
	ArrayList<Geofence> mGeofenceList;

	private DrawerLayout lytRoot;
	private FrameLayout lytContent;
	private FrameLayout lytDrawerLeft;

	private HashMap<String, String> map2;
	private HashMap<String, String> mapp;
	
	private TextView t1;

	private Button BTN_SAVE_SETTING;
	private MySQLiteHelper helper;

	
	private String eMailIdString;

	private String emailSubject;
	private String emailMessage;

	private Dialog dialog;
	private Spinner spinr_radius;
	private EditText EDT_LOCATION_NAME;
	private EditText EDT_LOCATION_ADDRESS;
	private CheckBox CHK_BOX_EDT_ADDRESS;
	private Button BTN_SAVE;
	private GeofenceDataAdapter helperGeofenceDataAdapter;
	private GeofenceDataAdapter data_adapter;
	private String addName;
	private String completeAddress;
	private int spinner_icon_pos = 0;
	private int spinner_radius_pos = 0;
	static int geofenceRadius;
	private String spiner_icon;
	private String selectedRadius;
	private String add;

	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	EditText edtDefaultEmail;
	
	private String add_name = "add_name", address = "address",
			add_icon = "add_icon", add_radius = "add_radius";
	private ListView listView;

	private String[] strings_icon_text = { "Home", "Office", "Other", "Type 1",
			"Type 1", "Type 1" };
	private int arr_images_icon[] = { R.drawable.home_icon_black,
			R.drawable.office_icon, R.drawable.other_icon,
			R.drawable.home_icon, R.drawable.point_icon, R.drawable.home };
	private Spinner mySpinner_icon;

	private long milliseconds;

	ArrayList<BeanClass> list;
	BeanClass bean;
	CustomListAdapter adapter;
	private ArrayList<HashMap<String, String>> data;
	private StringBuilder mailMessageString;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected Context context;

	private TextView txtAddAddress;
	private ImageButton save;

	private String addressName;
	private String addressDetail;
	private String addressRadius;

	private double latitude;
	private double longitude;

	private double geoFenceLatitude;
	private double geoFencelongitude;

	private Calendar calendar;

	private double finalDuration;
	private String entryTimeString;
	private String mailDateString;
	private String finalDurationString;

	private TextView TXT_TOTAL_TIME;
	private String emailDefaultShare;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		
		
		calendar = Calendar.getInstance();

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar_drawer);
		t1 = (TextView) findViewById(R.id.mytext_drawer);
		TXT_TOTAL_TIME = (TextView) findViewById(R.id.txt_total_time);
		setupDrawer();
		setUpDrawerButton();

		dialog = new Dialog(MainActivity.this);
		helperGeofenceDataAdapter = new GeofenceDataAdapter(MainActivity.this);
		data_adapter = new GeofenceDataAdapter(MainActivity.this);
		txtAddAddress = (TextView) findViewById(R.id.address);
		save = (ImageButton) findViewById(R.id.save_data);
		listView = (ListView) findViewById(R.id.rowItems);

		getListData();

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			onLocationChanged(location);
		}

		locationManager.requestLocationUpdates(provider, 1000 * 60, 0, this);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialogBox();

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map = new HashMap<String, String>();
				map2 = new HashMap<String, String>();

				data = data_adapter.getGeofenceData();
				map = data.get(pos);
				// int icon = Integer.parseInt(map.get(add_icon));

				// String s1 = map.get(add_icon);
				addressName = map.get(add_name);
				addressDetail = map.get(address);
				addressRadius = map.get(add_radius);

				map2 = data_adapter.getGeofenceDataBy3Fields(addressName,
						addressDetail, addressRadius);

				Toast.makeText(
						getBaseContext(),
						" " + pos + "  " + addressName + "  .." + addressDetail
								+ "  " + addressRadius, 5000).show();
				showDialogBoxUpdate();
			}
		});

		setTotalDuration();

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latitude = location.getLatitude();
		String lat = "" + latitude;
		longitude = location.getLongitude();
		String lon = "" + longitude;
		final LatLng LOCATION = new LatLng(latitude, longitude);
		add = GetAddress(lat, lon);
		txtAddAddress.setText(add);

		float[] distance = new float[2];

		Location.distanceBetween(latitude, longitude, geoFenceLatitude,
				geoFencelongitude, distance);

		if (!(mGeofence == null)) {
			if (distance[0] > 50) {
			//	Toast.makeText(getBaseContext(), "Outside", Toast.LENGTH_LONG)
			//			.show();
				calendar = Calendar.getInstance();

				String exitTimeString = "" + calendar.get(Calendar.YEAR) + "-"
						+ (calendar.get(Calendar.MONTH) + 1) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + " "
						+ calendar.get(Calendar.HOUR) + ":"
						+ calendar.get(Calendar.MINUTE) + ":"
						+ calendar.get(Calendar.SECOND);

				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					java.util.Date d = f.parse(exitTimeString);
					java.util.Date d1 = f.parse(entryTimeString);
					milliseconds = d.getTime() - d1.getTime();

					System.out.println("---------" + milliseconds);
				} catch (Exception e) {
				}

				String timeDuration = String.valueOf(milliseconds);
				helper = new MySQLiteHelper(MainActivity.this);
				helper.updateRow(entryTimeString, exitTimeString);

				System.out.println("----------Outside-----------"
						+ finalDurationString);

			} else {

				calendar = Calendar.getInstance();

				String exitTimeString = "" + calendar.get(Calendar.YEAR) + "-"
						+ (calendar.get(Calendar.MONTH) + 1) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + " "
						+ calendar.get(Calendar.HOUR) + ":"
						+ calendar.get(Calendar.MINUTE) + ":"
						+ calendar.get(Calendar.SECOND);

				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					java.util.Date d = f.parse(exitTimeString);
					java.util.Date d1 = f.parse(entryTimeString);
					milliseconds = d.getTime() - d1.getTime();

					milliseconds = milliseconds / 1000;

					System.out.println("-------------------" + milliseconds);

					double minutes = TimeUnit.MILLISECONDS
							.toMinutes(milliseconds);

					String totalTime = String.valueOf(milliseconds / 60);

					helper = new MySQLiteHelper(MainActivity.this);
					helper.updateRow(entryTimeString, totalTime);

					getListData();
					setTotalDuration();
					System.out.println("-------final updates--" + minutes + " "
							+ totalTime);
				} catch (Exception e) {
				}

				// String timeDuration = String.valueOf(milliseconds);
				// helper = new MySQLiteHelper(MainActivity.this);
				// helper.updateRow(entryTimeString, exitTimeString);

		//		Toast.makeText(getBaseContext(), "Inside", Toast.LENGTH_LONG)
		//				.show();
				getListData();
				System.out.println("----------Inside-----------"
						+ finalDurationString);
			}
		}

	}

	public String GetAddress(String lat, String lon) {
		Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
		String ret = "";
		try {
			List<Address> addresses = geocoder.getFromLocation(
					Double.parseDouble(lat), Double.parseDouble(lon), 1);
			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder(
						"Address:\n");
				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
									"\n");
				}
				ret = strReturnedAddress.toString();
			} else {
				ret = "No Address returned!";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = "Can't get Address!";
		}
		return ret;
	}

	void showDialogBox() {
		dialog.setContentView(R.layout.dialogbox_save_address);
		spinr_radius = (Spinner) dialog.findViewById(R.id.spinner_radius);
		addSpinner(spinr_radius);

		mySpinner_icon = (Spinner) dialog
				.findViewById(R.id.spinner_select_icon);
		mySpinner_icon.setAdapter(new MyAdapter(MainActivity.this,
				R.layout.spinner_icon, strings_icon_text));

		String setAddressToEdit = txtAddAddress.getText().toString();
		EDT_LOCATION_NAME = (EditText) dialog
				.findViewById(R.id.edt_address_name);
		EDT_LOCATION_ADDRESS = (EditText) dialog.findViewById(R.id.edt_address);
		EDT_LOCATION_ADDRESS.setText(setAddressToEdit);
		EDT_LOCATION_ADDRESS.setFocusable(false);
		CHK_BOX_EDT_ADDRESS = (CheckBox) dialog
				.findViewById(R.id.check_edt_address);
		BTN_SAVE = (Button) dialog.findViewById(R.id.btn_save_address);
		BTN_SAVE.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addName = EDT_LOCATION_NAME.getText().toString();
				completeAddress = EDT_LOCATION_ADDRESS.getText().toString();
				selectedRadius = spinr_radius.getSelectedItem().toString();

				selectedRadius = spinr_radius.getSelectedItem().toString();
				spinner_radius_pos = spinr_radius.getSelectedItemPosition();

				switch (spinner_radius_pos) {
				case 0:
					geofenceRadius = 50;
					break;
				case 1:
					geofenceRadius = 60;
					break;
				case 2:
					geofenceRadius = 70;
					break;
				case 3:
					geofenceRadius = 80;
					break;
				case 4:
					geofenceRadius = 90;
					break;

				default:
					break;
				}

				spinner_icon_pos = mySpinner_icon.getSelectedItemPosition();
				if (spinner_icon_pos == 0)
					spiner_icon = "" + 0;
				if (spinner_icon_pos == 1)
					spiner_icon = "" + 1;
				if (spinner_icon_pos == 2)
					spiner_icon = "" + 2;
				if (spinner_icon_pos == 3)
					spiner_icon = "" + 3;
				if (spinner_icon_pos == 4)
					spiner_icon = "" + 4;
				if (spinner_icon_pos == 5)
					spiner_icon = "" + 5;

				calendar = Calendar.getInstance();
				entryTimeString = "" + calendar.get(Calendar.YEAR) + "-"
						+ (calendar.get(Calendar.MONTH) + 1) + "-"
						+ calendar.get(Calendar.DAY_OF_MONTH) + " "
						+ calendar.get(Calendar.HOUR) + ":"
						+ calendar.get(Calendar.MINUTE) + ":"
						+ calendar.get(Calendar.SECOND);

				System.out.println("-------------------" + entryTimeString);

				bean = new BeanClass(addName, completeAddress, spiner_icon,
						selectedRadius, entryTimeString, "0");
				helperGeofenceDataAdapter.addGeofenceData(bean);
				Toast.makeText(getBaseContext(), "Location has been saved", 1000).show();

				/*
				 * ArrayList<HashMap<String, String>>
				 * data=data_adapter.getGeofenceData(); adapter = new
				 * CustomListAdapter(getApplicationContext(), data);
				 * listView.setAdapter(adapter); adapter.notifyDataSetChanged();
				 */
				createGeofences();
				getListData();
				dialog.dismiss();

			}
		});

		CHK_BOX_EDT_ADDRESS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (CHK_BOX_EDT_ADDRESS.isChecked()) {
					EDT_LOCATION_ADDRESS.setFocusable(true);
					EDT_LOCATION_ADDRESS.setFocusableInTouchMode(true);
				} else {
					EDT_LOCATION_ADDRESS.setFocusable(false);
				}
			}
		});

		dialog.setTitle("Save Location");
		dialog.show();
	}

	void addSpinner(Spinner spinr_radius) {
		List<String> list = new ArrayList<String>();
		list.add("50 Meters");
		list.add("60 Meters");
		list.add("70 Meters");
		list.add("80 Meters");
		list.add("90 Meters");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinr_radius.setAdapter(dataAdapter);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	void getListData() {
		try {
			data.clear();
		} catch (Exception e) {
		}
		data = data_adapter.getGeofenceData();
		adapter = new CustomListAdapter(getApplicationContext(), data);
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
	}

	public class MyAdapter extends ArrayAdapter<String> {

		public MyAdapter(Context context, int textViewResourceId,
				String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.spinner_icon, parent, false);
			TextView label = (TextView) row.findViewById(R.id.txt_spinner_icon);
			label.setText(strings_icon_text[position]);

			ImageView icon = (ImageView) row
					.findViewById(R.id.img_spinner_icon);
			icon.setImageResource(arr_images_icon[position]);

			return row;
		}
	}

	void setupDrawer() {
		super.setContentView(R.layout.activity_main);
		lytRoot = (DrawerLayout) findViewById(R.id.lytRoot);
		lytContent = (FrameLayout) findViewById(R.id.lytContent);
		lytDrawerLeft = (FrameLayout) findViewById(R.id.lytDrawerLeft);

		inflateDefaultDrawer();
	}

	void inflateDefaultDrawer() {
		LayoutInflater.from(this).inflate(R.layout.drawer_default,
				lytDrawerLeft);
		ViewGroup lytRootItems = (ViewGroup) lytDrawerLeft.getChildAt(0);

		int selectedIndex = getDrawerSelectedItemIndex();
		if (selectedIndex >= 0 && selectedIndex < lytRootItems.getChildCount()) {
			View item = lytRootItems.getChildAt(selectedIndex);
			item.setSelected(true);
		}

		for (int i = 0; i < lytRootItems.getChildCount(); i++) {
			View child = lytRootItems.getChildAt(i);
			child.setTag(i);
			child.setOnClickListener(onDrawerItemClicked);
		}
	}

	View.OnClickListener onDrawerItemClicked = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int item = (Integer) v.getTag();

			lytRoot.closeDrawer(lytDrawerLeft);

			if (item == getDrawerSelectedItemIndex())
				return;

			switch (item) {
			case 0:
				showDialogBoxSetting();
				break;

			case 2:
				showDialogBoxEmail();
				break;

			}
		}
	};

	protected int getDrawerSelectedItemIndex() {
		return -1;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (lytRoot.isDrawerOpen(lytDrawerLeft)) {
				lytRoot.closeDrawer(lytDrawerLeft);
			} else {
				lytRoot.openDrawer(lytDrawerLeft);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	void setUpDrawerButton() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		ImageView imageOpenDrawer = (ImageView) findViewById(R.id.image_drawer_open);
		imageOpenDrawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (lytRoot.isDrawerOpen(Gravity.LEFT)) {
					lytRoot.closeDrawer(Gravity.LEFT);
				} else {
					lytRoot.openDrawer(Gravity.LEFT);
				}

			}
		});

	}

	void showDialogBoxSetting() {
		dialog.setContentView(R.layout.dialogbox_setting);
		spinr_radius = (Spinner) dialog
				.findViewById(R.id.spinner_radius_setting);
		addSpinner(spinr_radius);
spinner_radius_pos=6;
		
	
		
		
		edtDefaultEmail=(EditText)dialog.findViewById(R.id.edt_default_email);
		if(!(sharedPref.getString("DEFAULT_EMAIL", "")==null))
		{
		edtDefaultEmail.setText(sharedPref.getString("DEFAULT_EMAIL", ""));
		}
		BTN_SAVE_SETTING = (Button) dialog.findViewById(R.id.btn_save_setting);
		BTN_SAVE_SETTING.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				spinner_radius_pos = spinr_radius.getSelectedItemPosition();		
				switch (spinner_radius_pos) {
				case 0:	geofenceRadius = 50;
					break;
				case 1: geofenceRadius = 60;
					break;
				case 2: geofenceRadius = 70;
					break;
				case 3: geofenceRadius = 80;
					break;
				case 4: geofenceRadius = 90;
					break;
				default:
					break;
				}

			emailDefaultShare=edtDefaultEmail.getText().toString().trim();
				
		if(!(edtDefaultEmail.equals("")))
		{
				editor.putString("DEFAULT_EMAIL", emailDefaultShare);
				editor.commit();
				System.out.println("--------------"+sharedPref.getString("DEFAULT_EMAIL", ""));
		}		
		if(!(spinner_radius_pos==6))
		{
		createGeofences();
		}
				Toast.makeText(getBaseContext(), "Settings Saved", 1000).show();
				dialog.dismiss();
			}
		});

		dialog.setTitle("Title...");
		dialog.show();
	}

	void showDialogBoxEmail() {

		calendar = Calendar.getInstance();
		mailDateString = ("" + calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-" + calendar
				.get(Calendar.DAY_OF_MONTH)).trim();

		emailMessage = getMailData();

		String toMailIdString=sharedPref.getString("DEFAULT_EMAIL", "");
		
		Intent i = new Intent(Intent.ACTION_SEND);
		
		
		i.setType("message/rfc822");
//		 i.putExtra(Intent.EXTRA_EMAIL , toMailIdString);
//		i.putExtra(android.content.Intent.EXTRA_EMAIL,toMailIdString);
		i.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { toMailIdString });
		i.putExtra(Intent.EXTRA_SUBJECT,"Location Report");
		i.putExtra(Intent.EXTRA_TEXT, emailMessage);
		try {
			startActivity(Intent.createChooser(i, "Send mail"));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(MainActivity.this,
					"There are no email applications installed.",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void createGeofences() {
		// Instantiate a new geofence storage area
		mGeofenceStorage = new SimpleGeofenceStore(this);
		// Instantiate the current List of geofences
		mGeofenceList = new ArrayList<Geofence>();
		// Instantiate a SimpleGeofence object

		geoFenceLatitude = latitude;
		geoFencelongitude = longitude;

		mGeofence = new SimpleGeofence("1", // Geofence ID
				latitude, // Latitude
				longitude, // Longitude
				geofenceRadius, // Radius
				Geofence.NEVER_EXPIRE, // Expiration time in milliseconds
				Geofence.GEOFENCE_TRANSITION_ENTER); // Records only entry
		// transitions
		// Store this flat version
		mGeofenceStorage.setGeofence("1", mGeofence);
		mGeofenceList.add(mGeofence.toGeofence());

	}

	void showDialogBoxUpdate() {
		String addressIcon = map2.get(add_icon);
		final String addressName = map2.get(add_name);
		String addressDetail = map2.get(address);
		String addressRadius = map2.get(add_radius);

		dialog.setContentView(R.layout.dialogbox_save_address);
		spinr_radius = (Spinner) dialog.findViewById(R.id.spinner_radius);
		addSpinner(spinr_radius);
		// spinr_radius.setSelection(mySpinner_radius.indexOf(addressRadius));

		if (addressRadius.equalsIgnoreCase("60 Meters")) {
			spinr_radius.setSelection(1);
		} else if (addressRadius.equals("70 Meters")) {
			spinr_radius.setSelection(2);
		} else if (addressRadius.equals("80 Meters")) {
			spinr_radius.setSelection(3);
		} else if (addressRadius.equals("90 Meters")) {
			spinr_radius.setSelection(4);
		}

		mySpinner_icon = (Spinner) dialog
				.findViewById(R.id.spinner_select_icon);
		mySpinner_icon.setAdapter(new MyAdapter(MainActivity.this,
				R.layout.spinner_icon, strings_icon_text));
		if (addressIcon.equalsIgnoreCase("" + 0)) {
			mySpinner_icon.setSelection(0);
		}
		if (addressIcon.equalsIgnoreCase("" + 1)) {
			mySpinner_icon.setSelection(1);
		}
		if (addressIcon.equalsIgnoreCase("" + 2)) {
			mySpinner_icon.setSelection(2);
		}
		if (addressIcon.equalsIgnoreCase("" + 3)) {
			mySpinner_icon.setSelection(3);
		}
		if (addressIcon.equalsIgnoreCase("" + 4)) {
			mySpinner_icon.setSelection(4);
		}
		if (addressIcon.equalsIgnoreCase("" + 5)) {
			mySpinner_icon.setSelection(5);
		}

		EDT_LOCATION_NAME = (EditText) dialog
				.findViewById(R.id.edt_address_name);
		EDT_LOCATION_NAME.setText(addressName);
		EDT_LOCATION_ADDRESS = (EditText) dialog.findViewById(R.id.edt_address);
		EDT_LOCATION_ADDRESS.setFocusable(false);
		EDT_LOCATION_ADDRESS.setText(addressDetail);

		CHK_BOX_EDT_ADDRESS = (CheckBox) dialog
				.findViewById(R.id.check_edt_address);
		BTN_SAVE = (Button) dialog.findViewById(R.id.btn_save_address);
		BTN_SAVE.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addName = EDT_LOCATION_NAME.getText().toString();
				completeAddress = EDT_LOCATION_ADDRESS.getText().toString();
				selectedRadius = spinr_radius.getSelectedItem().toString();
				spinner_icon_pos = mySpinner_icon.getSelectedItemPosition();
				if (spinner_icon_pos == 0)
					spiner_icon = "" + 0;
				if (spinner_icon_pos == 1)
					spiner_icon = "" + 1;
				if (spinner_icon_pos == 2)
					spiner_icon = "" + 2;
				if (spinner_icon_pos == 3)
					spiner_icon = "" + 3;
				if (spinner_icon_pos == 4)
					spiner_icon = "" + 4;
				if (spinner_icon_pos == 5)
					spiner_icon = "" + 5;

				bean = new BeanClass(addName, completeAddress, spiner_icon,
						selectedRadius);
				helperGeofenceDataAdapter.updateGeofenceData(bean, addressName);
				// helperGeofenceDataAdapter.addGeofenceData(bean);
				Toast.makeText(getBaseContext(), "Record Updated", 1000).show();
				dialog.dismiss();
				getListData();
			}
		});

		CHK_BOX_EDT_ADDRESS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (CHK_BOX_EDT_ADDRESS.isChecked()) {
					EDT_LOCATION_ADDRESS.setFocusable(true);
					EDT_LOCATION_ADDRESS.setFocusableInTouchMode(true);
				} else {
					EDT_LOCATION_ADDRESS.setFocusable(false);
				}
			}
		});

		dialog.setTitle("Update Location");
		dialog.show();
	}

	public void setTotalDuration() {
		int finalTime = 0;
		try {
			data.clear();
		} catch (Exception e) {

		}
		data = data_adapter.getGeofenceData();

		mapp = new HashMap<String, String>();

		for (int i = 0; i < data.size(); i++) {
			mapp = data.get(i);
			String s5 = mapp.get("add_duration");

			int time = Integer.parseInt(s5);

			finalTime = time + finalTime;
		}

		TXT_TOTAL_TIME.setText(finalTime + "m");

	}

	public String getMailData() {
		data.clear();
		data = data_adapter.getGeofenceData();
		mapp = new HashMap<String, String>();
		mailMessageString = new StringBuilder();

		for (int i = 0; i < data.size(); i++) 
		{
			mapp = data.get(i);
			String addressEntryTimeStringMail = mapp.get("address_entryTime").trim();
			String compareDate = addressEntryTimeStringMail.substring(0, 9).trim();
		
			if (mailDateString.equals(compareDate)) 
			{
				String addressStringMail = mapp.get("address");
				String addressNameStringMail = mapp.get("add_name");
				String addressDurationStringMail = mapp.get("add_duration");
				mailMessageString.append(i+1+": "+addressNameStringMail + "\n"
						+ addressStringMail + "Duration: "
						+ addressDurationStringMail + "min\n\n");
			}

		}

		return mailMessageString.toString();

	}

}