package com.common.methods;

import java.text.NumberFormat;

import com.master.webserver.R;
import com.master.webserver.ServerService;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UI_updater {

	private int PORT;
	private ImageButton wifihotspotToggle;
	private ImageButton dataToggle;
	private ImageButton wifiNetToggle;
	private TextView startstatus;
	private TextView modestatus;
	private ImageView orb;
	private TextView speed;
	private TextView filename;
	private ImageView progressimviewleft;
	private ImageView progressimviewright;
	private AnimationDrawable progressanimleft;
	private AnimationDrawable progressanimright;
	private TextView textIpaddr;
	private LinearLayout transferstatus;
	private LinearLayout connectionTogglesgroup;
	private TextView availableSpace;
	public boolean speedviewenable = false;
	public int modeSelected = 0;
	public Context appContext;
	private TextView transferProgressPercent;
	private ProgressBar fileProgressBar;

	public void ui_initializer(int PORT, ImageButton wifihotspotToggle,
			ImageButton dataToggle, ImageButton wifiNetToggle,
			TextView startstatus, TextView modestatus, ImageView orb,
			TextView speed, TextView filename, ImageView progressimviewleft,
			ImageView progressimviewright, AnimationDrawable progressanimleft,
			AnimationDrawable progressanimright, TextView textIpaddr,
			TextView availableSpace, LinearLayout transferstatus,
			LinearLayout connectionTogglesgroup,
			TextView transferProgressPercent, ProgressBar fileProgressBar,
			Context appContext) {

		this.PORT = PORT;
		this.wifihotspotToggle = wifihotspotToggle;
		this.dataToggle = dataToggle;
		this.wifiNetToggle = wifiNetToggle;
		this.startstatus = startstatus;
		this.modestatus = modestatus;
		this.orb = orb;
		this.speed = speed;
		this.filename = filename;
		this.progressimviewleft = progressimviewleft;
		this.progressimviewright = progressimviewright;
		this.progressanimleft = progressanimleft;
		this.progressanimright = progressanimright;
		this.textIpaddr = textIpaddr;
		this.availableSpace = availableSpace;
		this.transferstatus = transferstatus;
		this.connectionTogglesgroup = connectionTogglesgroup;
		this.appContext = appContext;
		this.transferProgressPercent = transferProgressPercent;
		this.fileProgressBar = fileProgressBar;
	}

	public void updateIP() {
		if (ServerService.serverenabled) {

			String IPAddress = "http://" + IpAddress.getHostIPAddress() + ":"
					+ PORT;
			textIpaddr.setText(IPAddress);

		} else {

			textIpaddr.setText("Flash is not Running");

		}
	}

	public void updateSDspace() {
		long freespace = AvailableSpaceHandler.getExternalAvailableSpaceInMB();
		if (freespace <= 50) {

			String spacetext = "Available Space : " + freespace + " MB "
					+ "(Warning:Low Space!)";
			availableSpace.setText(spacetext);

		} else {

			String spacetext = "Available Space : " + freespace + " MB";
			availableSpace.setText(spacetext);
		}
	}

	public void updateServerStatus() {

		if (ServerService.serverenabled && modeSelected == 0) {

			modeSelected = 3;

		}

		this.updateSDspace();

		wifihotspotToggle.setColorFilter(Color.argb(0, 30, 201, 244));
		wifiNetToggle.setColorFilter(Color.argb(0, 0, 201, 244));
		dataToggle.setColorFilter(Color.argb(0, 30, 201, 244));
		modestatus.setText("Please Select the Mode: ");
		startstatus.setText("Start Flash");

		orb.setImageResource(R.drawable.ic_launcher);
		startstatus.setText("Start Flash");
		this.updateIP();
		Log.d("msg", "Mode selected Value here: " + modeSelected);
		Log.d("msg", "serverenabled Value here: " + ServerService.serverenabled);

		switch (modeSelected) {

		case 1:
			if (ServerService.serverenabled) {
				orb.setImageResource(R.drawable.hotspot);
				startstatus.setText("Stop Flash");
			}
			modestatus.setText("Wifi Hotspot Mode");
			wifihotspotToggle.setColorFilter(Color.argb(255, 30, 131, 244));
			break;
		case 2:
			if (ServerService.serverenabled) {

				orb.setImageResource(R.drawable.wifi);
				startstatus.setText("Stop Flash");
			}
			modestatus.setText("Wifi Network Mode");

			wifiNetToggle.setColorFilter(Color.argb(255, 30, 131, 244));

			break;
		case 3:
			if (ServerService.serverenabled) {
				// ----------------------------------------------
				// Creating the alert to warn the user

				// ----------------------------------------------
				orb.setImageResource(R.drawable.data);
				startstatus.setText("Stop Flash");
			}
			modestatus.setText("Internet or Data Mode");

			dataToggle.setColorFilter(Color.argb(255, 30, 131, 244));

			break;

		}

	}

	public void updateSpeedandProgress(double dataspeed, float progress) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumFractionDigits(2);
		formatter.setMaximumFractionDigits(2);
		String progressString = formatter.format(progress);
		Log.d("FTDebug", progressString);
		Log.d("FTDebug", dataspeed + "");
		fileProgressBar.setProgress((int) progress);
		// transferProgressPercent.setText(progressString + " %");

	}

	public void update() {

		this.updateServerStatus();
		this.updateSDspace();
		this.updateIP();

	}

}
