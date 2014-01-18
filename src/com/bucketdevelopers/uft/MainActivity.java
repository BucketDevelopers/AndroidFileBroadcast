package com.bucketdevelopers.uft;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.common.methods.ExternalStorage;
import com.common.methods.XmlParser;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	MyPageAdapter pageAdapter;
	WifiManager wifi_manager;
	private WifiConfiguration config;

	public enum WIFI_AP_STATE {
		WIFI_AP_STATE_DISABLING, WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING, WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// initialising XML list for file selection..
		XmlParser.checkXml(getFilesDir(), "list.xml"); // Create xml file

		// For wifi and hotspot management
		wifi_manager = (WifiManager) getSystemService(ServerService.WIFI_SERVICE);

		// Processing in the splash

		/*
		 * To create the folder /sdcard/webserverdata
		 */
		try {
			String path = ExternalStorage.getsdcardfolderpath();
			File temppath = new File(path);
			if (!temppath.exists()) {
				temppath.mkdir();

			}

		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "Error: SD card unavailable.",
					Toast.LENGTH_LONG).show();

			Log.d("FTDebug", e.toString());
			MainActivity.this.finish();

		}

		// Setting up Indicators (page indicators)
		final View pg1indicator = findViewById(R.id.page1);
		final View pg2indicator = findViewById(R.id.page2);
		final View pg3indicator = findViewById(R.id.page3);

		pg1indicator.setBackgroundColor(0XFF18a4df);

		// For Top ActionBar
		android.support.v7.app.ActionBar acbar = getSupportActionBar();
		acbar.setCustomView(R.layout.actionbar_top); // load your layout
		acbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_SHOW_CUSTOM); // show it

		// For Fragments Setup

		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setAdapter(pageAdapter);

		pager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) {
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			public void onPageSelected(int position) {
				// Toast.makeText(getApplicationContext(), "page: " + position,
				// Toast.LENGTH_SHORT).show();

				switch (position) {

				case 0:
					pg1indicator.setBackgroundColor(0XFF18a4df);
					pg2indicator.setBackgroundColor(0XFF607086);
					pg3indicator.setBackgroundColor(0XFF607086);

					break;
				case 1:
					pg1indicator.setBackgroundColor(0XFF607086);
					pg2indicator.setBackgroundColor(0XFF18a4df);
					pg3indicator.setBackgroundColor(0XFF607086);

					break;
				case 2:
					pg1indicator.setBackgroundColor(0XFF607086);
					pg2indicator.setBackgroundColor(0XFF607086);
					pg3indicator.setBackgroundColor(0XFF18a4df);

					break;
				}

			}
		});

	}

	private List<Fragment> getFragments() {

		List<Fragment> fList = new ArrayList<Fragment>();
		fList.add(Screen1.newInstance(""));
		fList.add(Listpage.newInstance(this));
		fList.add(MyFragment.newInstance("Fragment 3"));

		return fList;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.wifi_hotspot:
			// SWITCH ON?OFF WIFI HOTSPOT HERE
			if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
				Toast.makeText(getApplicationContext(),
						"Hotspot is not supported by your Version of Android ",
						Toast.LENGTH_LONG).show();
			} else {

				if (isWifiApEnabled()) {
					enableHotspot(false);
				} else {
					enableHotspot(true);
				}
			}
			return true;
		case R.id.wifi:
			// Toggling Wifi

			enableHotspot(false);
			wifi_manager = (WifiManager) this
					.getSystemService(Context.WIFI_SERVICE);
			if (wifi_manager.isWifiEnabled()) {
				wifi_manager.setWifiEnabled(false);
			} else {
				wifi_manager.setWifiEnabled(true);
				MainActivity.this.startActivity(new Intent(
						WifiManager.ACTION_PICK_WIFI_NETWORK));
			}

			return true;

		case R.id.data_connection:

			if (checkMobileData()) {

				try {
					turnData(false);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Please turn off your data manually",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			} else {

				try {
					turnData(true);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Please turn on your data manually",
							Toast.LENGTH_LONG).show();

					e.printStackTrace();
				}

			}

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/************************************************************************/
	// Methods for wifi hotspot and data Connection managements start
	//
	/************************************************************************/
	/*
	 * Method to Toggle Hotspot
	 */

	private void enableHotspot(boolean enabled) {

		WifiConfiguration wifi_configuration = null;
		wifi_manager.setWifiEnabled(false);

		try {
			// USE REFLECTION TO GET METHOD "SetWifiAPEnabled"
			Method method = wifi_manager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, boolean.class);
			method.invoke(wifi_manager, wifi_configuration, enabled);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		if (enabled) {
			// To Display SSID
			Method[] methods = wifi_manager.getClass().getDeclaredMethods();
			for (Method m : methods) {
				if (m.getName().equals("getWifiApConfiguration")) {
					try {
						config = (WifiConfiguration) m.invoke(wifi_manager);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

					Toast.makeText(getApplicationContext(),
							"Hotspot Created : " + config.SSID,
							Toast.LENGTH_LONG).show();
					// here, the "config" variable holds the info, your SSID is
					// in
					// config.SSID
				}
			}

		}
	}// end of WifiToggle

	// Checking wifi Hotspot state

	/* the following method is for getting the wifi hotspot state */

	public WIFI_AP_STATE getWifiApState() {
		try {

			Method method = wifi_manager.getClass().getMethod("getWifiApState");

			int tmp = ((Integer) method.invoke(wifi_manager));

			// Fix for Android 4
			if (tmp > 10) {
				tmp = tmp - 10;
			}

			return WIFI_AP_STATE.class.getEnumConstants()[tmp];
		} catch (Exception e) {
			Log.e(this.getClass().toString(), "", e);
			return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
		}
	}

	/**
	 * Return whether Wi-Fi Hotspot is enabled or disabled.
	 * 
	 * @return {@code true} if Wi-Fi AP is enabled
	 * @see #getWifiApState()
	 */
	public boolean isWifiApEnabled() {
		return getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
	}

	// Enable/Disable Data Connection
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void turnData(boolean ON) throws Exception {

		int bv = android.os.Build.VERSION.SDK_INT;
		if (bv == Build.VERSION_CODES.FROYO) {

			Log.i("version:", "Found Froyo");
			try {
				Method dataConnSwitchmethod;
				Class telephonyManagerClass;
				Object ITelephonyStub;
				Class ITelephonyClass;
				TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext()
						.getSystemService(Context.TELEPHONY_SERVICE);

				telephonyManagerClass = Class.forName(telephonyManager
						.getClass().getName());
				Method getITelephonyMethod = telephonyManagerClass
						.getDeclaredMethod("getITelephony");
				getITelephonyMethod.setAccessible(true);
				ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
				ITelephonyClass = Class.forName(ITelephonyStub.getClass()
						.getName());

				if (ON) {
					dataConnSwitchmethod = ITelephonyClass
							.getDeclaredMethod("enableDataConnectivity");

				} else {
					dataConnSwitchmethod = ITelephonyClass
							.getDeclaredMethod("disableDataConnectivity");
				}
				dataConnSwitchmethod.setAccessible(true);
				dataConnSwitchmethod.invoke(ITelephonyStub);
			} catch (Exception e) {
				Log.e("Error:", e.toString());
			}

		} else {
			Log.i("version:", "Found Gingerbread+");
			final ConnectivityManager conman = (ConnectivityManager) getApplicationContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class
					.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(iConnectivityManager, ON);
		}
	}

	// Method to check mobile data state
	public boolean checkMobileData() {
		ConnectivityManager connManager1 = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mMobile = connManager1
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mMobile.isConnected()) {
			return true;
		} else {
			return false;
		}

	}

}// end of Activity
