package com.bucketdevelopers.uft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.common.methods.ExternalStorage;
import com.common.methods.XmlParser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	MyPageAdapter pageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// SPlash shit..
		XmlParser.checkXml(getFilesDir(), "list.xml"); // Create xml file

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
		fList.add(MyFragment.newInstance("Fragment 2"));
		fList.add(MyFragment.newInstance("Fragment 3"));

		return fList;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
