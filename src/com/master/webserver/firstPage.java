package com.master.webserver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class firstPage extends Activity {

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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.firstscreen);

		// Font
		robotoregular = Typeface.createFromAsset(getAssets(),
				"Roboto-Regular.ttf");
		robotolight = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");

		// findviewbyIds
		orb = (ImageView) findViewById(R.id.Orb);
		progressimviewleft=(ImageView) findViewById(R.id.progrleft);
		progressimviewright=(ImageView) findViewById(R.id.progrright);
		
		progressimviewleft.setBackgroundResource(R.drawable.transferanim);
		progressimviewright.setBackgroundResource(R.drawable.transferanim);
		
		progressanimleft=(AnimationDrawable) progressimviewleft.getBackground();
		progressanimright=(AnimationDrawable) progressimviewright.getBackground();
		
		progressanimleft.start();
		progressanimright.start();
		
		
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
			@SuppressLint("NewApi")
			// ColorFilter is supported only from Froyo
			@Override
			public void onClick(View v) {
				// Setting the tint color to blue
				wifihotspotToggle.setColorFilter(Color.argb(255, 250, 164, 12));
				dataToggle.setColorFilter(Color.argb(255, 30, 131, 244));
				wifiNetToggle.setColorFilter(Color.argb(255, 30, 131, 244));

				if (v.getId() != wifihotspotToggle.getId()) {

					wifihotspotToggle.setColorFilter(Color
							.argb(0, 30, 201, 244));
				}
				if (v.getId() != wifiNetToggle.getId()) {
					wifiNetToggle.setColorFilter(Color.argb(0, 0, 201, 244));
				}
				if (v.getId() != dataToggle.getId()) {
					dataToggle.setColorFilter(Color.argb(0, 30, 201, 244));
				}

			}

		};

		// onClickListener
		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.Orb) {
					/* Create an Intent that will start the Menu-Activity. */

					Intent mainIntent = new Intent(firstPage.this,
							MainActivity.class);
					firstPage.this.startActivity(mainIntent);
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

	}

}
