package com.master.webserver;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.common.methods.ExternalStorage;
import com.common.methods.IntentHelper;
import com.common.methods.UI_updater;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class firstPage extends SherlockActivity {
	public UI_updater UI;
	private int PORT = 8080;
	private ImageButton wifihotspotToggle;
	private ImageButton dataToggle;
	private ImageButton wifiNetToggle;
	private OnClickListener changeChecker;
	private TextView startstatus;
	private TextView modestatus;
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
	private Intent uploaddownloadservice;
	private TextView textIpaddr;
	private TextView availableSpace;
	private LinearLayout transferstatus;
	private LinearLayout connectionTogglesgroup;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.firstscreen);

		// Data elements initialization
		uploaddownloadservice = new Intent(firstPage.this,
				ServerService.class);

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

		availableSpace = (TextView) findViewById(R.id.spaceSD);
		startstatus = (TextView) findViewById(R.id.startstatus);
		modestatus = (TextView) findViewById(R.id.modestatus);
		textIpaddr = (TextView) findViewById(R.id.ipaddress);
		speed = (TextView) findViewById(R.id.speed);
		filename = (TextView) findViewById(R.id.currentfilename);

		wifihotspotToggle = (ImageButton) findViewById(R.id.wifihotspot);
		wifiNetToggle = (ImageButton) findViewById(R.id.wifinetwork);
		dataToggle = (ImageButton) findViewById(R.id.dataconnection);

		transferstatus = (LinearLayout) findViewById(R.id.transferStatusLayout);
		connectionTogglesgroup = (LinearLayout) findViewById(R.id.connectiontogglesLayout);

		// setting fonts

		startstatus.setTypeface(robotoregular);
		modestatus.setTypeface(robotoregular);
		textIpaddr.setTypeface(robotolight);
		speed.setTypeface(robotolight);
		filename.setTypeface(robotoregular);
		// onEvent Listeners

		// checkChangeListener
		changeChecker = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Setting the tint color to blue

				if (v.getId() == wifihotspotToggle.getId()) {

					UI.modeSelected = 1;

				}
				if (v.getId() == wifiNetToggle.getId()) {
					UI.modeSelected = 2;
				}
				if (v.getId() == dataToggle.getId()) {
					UI.modeSelected = 3;
				}

				UI.updateServerStatus();
			}

		};

		// onClickListener
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.Orb) {

					// Start The service
					if (ServerService.serverenabled != true) {

						// Bug Fix:
						// Just So that App doesnt FORCE CLOSE even for some
						// reason the UI is ******
						stopService(uploaddownloadservice);

						// Now the Real Deal

						uploaddownloadservice.putExtra("Port", PORT);
						IntentHelper.addObjectForKey(UI, "UIObj");
						startService(uploaddownloadservice);

					} else {
						stopService(uploaddownloadservice);
						ServerService.serverenabled = false;
						UI.modeSelected = 0;
						UI.updateServerStatus();
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

		// Initializing the UI
		UI=new UI_updater();
		UI.ui_initializer(PORT, wifihotspotToggle, dataToggle,
				wifiNetToggle, startstatus, modestatus, orb, speed, filename,
				progressimviewleft, progressimviewright, progressanimleft,
				progressanimright, textIpaddr, availableSpace, transferstatus,
				connectionTogglesgroup, firstPage.this);

		UI.update();
		//
		//
		//
		// // Starting the Animation(This has to be changed)
		//
		// progressanimleft.start();
		// progressanimright.start();

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
			// Intent aboutIntent = new Intent(firstPage.this, About.class);
			// firstPage.this.startActivity(aboutIntent);
			Intent mainIntent = new Intent(firstPage.this, File_Download.class);
			firstPage.this.startActivity(mainIntent);

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

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
