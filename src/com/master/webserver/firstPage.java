package com.master.webserver;

import java.io.IOException;
import java.io.InputStream;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.common.methods.AvailableSpaceHandler;
import com.common.methods.ExternalStorage;
import com.common.methods.IpAddress;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class firstPage extends SherlockActivity {

	protected static final int PORT = 8080;
	private ImageButton wifihotspotToggle;
	private ImageButton dataToggle;
	private ImageButton wifiNetToggle;
	private OnClickListener changeChecker;
	private TextView startstatus;
	private TextView modestatus;
	private TextView ipaddressdisplay;
	private ImageView orb;
	private ImageButton shareButton;
	private Typeface robotoregular;
	private TextView speed;
	private TextView filename;
	private Typeface robotolight;
	private ImageView progressimviewleft;
	private ImageView progressimviewright;
	private AnimationDrawable progressanimleft;
	private AnimationDrawable progressanimright;
	protected Intent uploaddownloadservice;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.firstscreen);

		// Data elements initialisation
		uploaddownloadservice = new Intent(firstPage.this,
				UploadServerService.class);

		// Font
		robotoregular = Typeface.createFromAsset(getAssets(),
				"Roboto-Regular.ttf");
		robotolight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

		// assigning views and initial values
		orb = (ImageView) findViewById(R.id.Orb);
		progressimviewleft = (ImageView) findViewById(R.id.progrleft);
		progressimviewleft.setBackgroundResource(R.drawable.transferanim);
		progressimviewright = (ImageView) findViewById(R.id.progrright);
		progressimviewright.setBackgroundResource(R.drawable.transferanimright);

		progressanimleft = (AnimationDrawable) progressimviewleft
				.getBackground();
		progressanimright = (AnimationDrawable) progressimviewright
				.getBackground();

		shareButton = (ImageButton) findViewById(R.id.shareButton);

		startstatus = (TextView) findViewById(R.id.startstatus);
		modestatus = (TextView) findViewById(R.id.modestatus);
		ipaddressdisplay = (TextView) findViewById(R.id.ipaddress);
		speed = (TextView) findViewById(R.id.speed);
		filename = (TextView) findViewById(R.id.currentfilename);
		wifihotspotToggle = (ImageButton) findViewById(R.id.wifihotspot);
		wifiNetToggle = (ImageButton) findViewById(R.id.wifinetwork);
		dataToggle = (ImageButton) findViewById(R.id.dataconnection);

		// setting fonts

		startstatus.setTypeface(robotoregular);
		modestatus.setTypeface(robotoregular);
		ipaddressdisplay.setTypeface(robotolight);
		speed.setTypeface(robotolight);
		filename.setTypeface(robotoregular);
		// onEvent Listeners

		// checkChangeListener
		changeChecker = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Setting the tint color to blue

				wifihotspotToggle.setColorFilter(Color.argb(0, 30, 201, 244));
				wifiNetToggle.setColorFilter(Color.argb(0, 0, 201, 244));
				dataToggle.setColorFilter(Color.argb(0, 30, 201, 244));

				if (v.getId() == wifihotspotToggle.getId()) {

					wifihotspotToggle.setColorFilter(Color.argb(255, 250, 164,
							12));

				}
				if (v.getId() == wifiNetToggle.getId()) {

					wifiNetToggle.setColorFilter(Color.argb(255, 30, 131, 244));

				}
				if (v.getId() == dataToggle.getId()) {
					dataToggle.setColorFilter(Color.argb(255, 30, 131, 244));

				}

				/*
				 * To get the IP Address of the device
				 */
				TextView textIpaddr = (TextView) findViewById(R.id.ipaddress);
				String ipaddress = IpAddress.getHostIPAddress();
				textIpaddr.setText("http://" + ipaddress + ":" + PORT);
				// End of Setting the IP

			}

		};

		// onClickListener
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.Orb) {

					// Start The service
					if (UploadServerService.onoff == false) {
						
						startstatus.setText("Stop Flash");
						
						
						// Bug Fix:
						// Just So that App doesnt FORCE CLOSE even for some
						// reason the UI is ******
						stopService(uploaddownloadservice);

						// Now the Real Deal
						/*
						 * To Read From The Upload html File from assets folder
						 * and preparing the intent
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
							text += (AvailableSpaceHandler
									.getExternalAvailableSpaceInBytes() + ";");
							//
							input = assetManager.open("uploadfile_part2.html");

							size = input.available();
							buffer = new byte[size];
							input.read(buffer);
							input.close();

							text += new String(buffer);

							// passing the upload html String to NanoHTTPD
							uploaddownloadservice.putExtra("htmlfile", text);
						} catch (IOException e) {
							Log.d("FTDebug", e.getMessage());
						}

						uploaddownloadservice.putExtra("Port", PORT);
						startService(uploaddownloadservice);

					} else {
						startstatus.setText("Start Flash");
						stopService(uploaddownloadservice);

					}

				} else if (v.getId() == R.id.shareButton) {
					/* Create an intent for sharing IP */
					Intent sharingIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					String shareBody = "Here is the share content body";
					sharingIntent.putExtra(
							android.content.Intent.EXTRA_SUBJECT,
							"Sending You a File");
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							shareBody);
					startActivity(Intent.createChooser(sharingIntent,
							"Share IP info via"));

				}

			}
		};

		// Setting the Listeners

		wifihotspotToggle.setOnClickListener(changeChecker);
		wifiNetToggle.setOnClickListener(changeChecker);
		dataToggle.setOnClickListener(changeChecker);
		shareButton.setOnClickListener(clickListener);
		orb.setOnClickListener(clickListener);

		// Starting the Animation(This has to be changed)

		progressanimleft.start();
		progressanimright.start();

	}

	/*
	 * 
	 * ActionBar Customization and menu
	 */
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {

		case R.id.about:
			Intent aboutIntent = new Intent(firstPage.this, About.class);
			firstPage.this.startActivity(aboutIntent);
			break;
		case R.id.settings:
			Intent settingsIntent = new Intent(firstPage.this, Settings.class);
			firstPage.this.startActivity(settingsIntent);
			break;

		}
		return true;
	}

	// End of Menu and ActionBar

	@Override
	protected void onResume() {
		super.onResume();

		TextView availableSpace = (TextView) findViewById(R.id.spaceSD);

		// Check whether user switched to USB Storage Mode or not
		try {
			ExternalStorage.getsdcardfolderpath(); // This will check if sd card
													// is available
		} catch (Exception e) {

			Toast.makeText(firstPage.this,
					getResources().getString(R.string.sdcard_error),
					Toast.LENGTH_LONG).show();
			Log.d("FTDebug", e.toString());
			this.finish();

		}

		if (AvailableSpaceHandler.getExternalAvailableSpaceInMB() > 50) {

			availableSpace.setText("Availabe Space : "
					+ AvailableSpaceHandler.getExternalAvailableSpaceInMB()
					+ " MB ");

		} else {

			availableSpace.setText("Availabe Space : "
					+ AvailableSpaceHandler.getExternalAvailableSpaceInMB()
					+ " MB (Warning! Low Space !)");

		}

		if (UploadServerService.onoff) {

			// Reinitialise the UI
			
			startstatus.setText("Stop Flash");
			
			
			

		} else {

			// Reinitialise the UI
			startstatus.setText("Start Flash");
			
			
			
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
