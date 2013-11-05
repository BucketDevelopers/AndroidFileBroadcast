package com.master.webserver;

import java.io.File;

import com.common.methods.ExternalStorage;
import com.common.methods.XmlParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class Splash extends Activity {
	Intent mainIntent;
	private final int SPLASH_DISPLAY_LENGHT = 2000;
	private boolean nosdcarderror = true;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.splashscreen);
		XmlParser.checkXml(getFilesDir(), "list.xml");	//Create xml file
		/*
		 * New Handler to start the Menu-Activity and close this Splash-Screen
		 * after some seconds.
		 */
		/*
		 * New Handler to start the Menu-Activity and close this Splash-Screen
		 * after some seconds.
		 */
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				/* Create an Intent that will start the Menu-Activity. */

				if (nosdcarderror) {
					mainIntent = new Intent(Splash.this, firstPage.class);
					Splash.this.startActivity(mainIntent);

				}
				Splash.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);

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
			nosdcarderror = false;
			Toast.makeText(Splash.this,
					getResources().getString(R.string.sdcard_error),
					Toast.LENGTH_LONG).show();

			Log.d("FTDebug", e.toString());
			Splash.this.finish();

		}

	}

}