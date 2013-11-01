package com.master.webserver;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.common.methods.AvailableSpaceHandler;
import com.common.methods.ExternalStorage;
import com.common.methods.IpAddress;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {
	public Intent uploadservice = null;
	public static boolean serverEnabled = false;
	private static final int PORT = 8080;

	Button stopUserver;
	Button startUserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Intent for the service
		uploadservice = new Intent(MainActivity.this, UploadServerService.class);
		/*
		 * To Read From The Upload html File from assets folder
		 */

		AssetManager assetManager = getAssets();
		InputStream input;
		try {
			input = assetManager.open("uploadfile_part1.html");

			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();

			// byte buffer into a string
			String text = new String(buffer);

			// Adding the SD card size into the html response
			text += (AvailableSpaceHandler.getExternalAvailableSpaceInBytes() + ";");
			//
			input = assetManager.open("uploadfile_part2.html");

			size = input.available();
			buffer = new byte[size];
			input.read(buffer);
			input.close();

			text += new String(buffer);

			// passing the upload html String to NanoHTTPD
			uploadservice.putExtra("htmlfile", text);
		} catch (IOException e) {
			Log.d("FTDebug", e.getMessage());
		}

		/*
		 * end of Read from the html file
		 */

		startUserver = (Button) findViewById(R.id.startUS);
		stopUserver = (Button) findViewById(R.id.stopUS);

		startUserver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * To Start the upload Service
				 */
				uploadservice.putExtra("Port", PORT);
				startService(uploadservice);
				v.setEnabled(false);
				stopUserver.setEnabled(true);
				serverEnabled = true;
			}
		});

		stopUserver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(uploadservice);
				v.setEnabled(false);
				startUserver.setEnabled(true);
				serverEnabled = false;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (serverEnabled) {

			startUserver.setEnabled(false);
			stopUserver.setEnabled(true);
		} else {

			startUserver.setEnabled(true);
			stopUserver.setEnabled(false);

		}
		/*
		 * To check available SD Card Space
		 */

		TextView availableSpace = (TextView) findViewById(R.id.spaceSD);

		if (AvailableSpaceHandler.getExternalAvailableSpaceInMB() > 50) {

			availableSpace.setText("Availabe Space : "
					+ AvailableSpaceHandler.getExternalAvailableSpaceInMB()
					+ " MB ");

		} else {

			availableSpace.setText("Availabe Space : "
					+ AvailableSpaceHandler.getExternalAvailableSpaceInMB()
					+ " MB (Warning! Low Space !)");

		}

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
			Toast.makeText(MainActivity.this,
					getResources().getString(R.string.sdcard_error),
					Toast.LENGTH_LONG).show();

			Log.d("FTDebug", e.toString());
			MainActivity.this.finish();
		}

		/*
		 * To get the IP Address of the device
		 */
		TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);
		String ipaddress = IpAddress.getHostIPAddress();
		textIpaddr.setText("Please access! http://" + ipaddress + ":" + PORT);

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}// end of Class
