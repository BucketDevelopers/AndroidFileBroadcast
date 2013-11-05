package com.common.methods;

import com.master.webserver.R;
import com.master.webserver.UploadServerService;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UI_updater {

	private static int PORT;
	private static ImageButton wifihotspotToggle;
	private static ImageButton dataToggle;
	private static ImageButton wifiNetToggle;
	private static TextView startstatus;
	private static TextView modestatus;
	private static ImageView orb;
	private static TextView speed;
	private static TextView filename;
	private static ImageView progressimviewleft;
	private static ImageView progressimviewright;
	private static AnimationDrawable progressanimleft;
	private static AnimationDrawable progressanimright;
	private static TextView textIpaddr;
	private static LinearLayout transferstatus;
	private static LinearLayout connectionTogglesgroup;
	private static TextView availableSpace;
	public static boolean speedviewenable = false;
	public static int modeSelected = 0;
	public static Context appContext;

	public static void ui_initializer(int PORT, ImageButton wifihotspotToggle,
			ImageButton dataToggle, ImageButton wifiNetToggle,
			TextView startstatus, TextView modestatus, ImageView orb,
			TextView speed, TextView filename, ImageView progressimviewleft,
			ImageView progressimviewright, AnimationDrawable progressanimleft,
			AnimationDrawable progressanimright, TextView textIpaddr,
			TextView availableSpace, LinearLayout transferstatus,
			LinearLayout connectionTogglesgroup, Context appContext) {

		UI_updater.PORT = PORT;
		UI_updater.wifihotspotToggle = wifihotspotToggle;
		UI_updater.dataToggle = dataToggle;
		UI_updater.wifiNetToggle = wifiNetToggle;
		UI_updater.startstatus = startstatus;
		UI_updater.modestatus = modestatus;
		UI_updater.orb = orb;
		UI_updater.speed = speed;
		UI_updater.filename = filename;
		UI_updater.progressimviewleft = progressimviewleft;
		UI_updater.progressimviewright = progressimviewright;
		UI_updater.progressanimleft = progressanimleft;
		UI_updater.progressanimright = progressanimright;
		UI_updater.textIpaddr = textIpaddr;
		UI_updater.availableSpace = availableSpace;
		UI_updater.transferstatus = transferstatus;
		UI_updater.connectionTogglesgroup = connectionTogglesgroup;
		UI_updater.appContext = appContext;

	}

	public static void updateIP() {
		if (UploadServerService.serverenabled) {

			String IPAddress = "http://" + IpAddress.getHostIPAddress() + ":"
					+ PORT;
			textIpaddr.setText(IPAddress);

		} else {

			textIpaddr.setText("Flash is not Running");

		}
	}

	public static void updateSDspace() {
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

	public static void updateServerStatus() {

		if (UploadServerService.serverenabled && modeSelected == 0) {

			modeSelected = 3;

		}

		UI_updater.updateSDspace();

		wifihotspotToggle.setColorFilter(Color.argb(0, 30, 201, 244));
		wifiNetToggle.setColorFilter(Color.argb(0, 0, 201, 244));
		dataToggle.setColorFilter(Color.argb(0, 30, 201, 244));
		modestatus.setText("Please Select the Mode: ");
		startstatus.setText("Start Flash");

		orb.setImageResource(R.drawable.ic_launcher);
		startstatus.setText("Start Flash");
		UI_updater.updateIP();
		Log.d("msg", "Mode selected Value here: " + modeSelected);
		Log.d("msg", "serverenabled Value here: "
				+ UploadServerService.serverenabled);

		switch (modeSelected) {

		case 1:
			if (UploadServerService.serverenabled) {
				orb.setImageResource(R.drawable.hotspot);
				startstatus.setText("Stop Flash");
			}
			modestatus.setText("Wifi Hotspot Mode");
			wifihotspotToggle.setColorFilter(Color.argb(255, 30, 131, 244));
			break;
		case 2:
			if (UploadServerService.serverenabled) {

				orb.setImageResource(R.drawable.wifi);
				startstatus.setText("Stop Flash");
			}
			modestatus.setText("Wifi Network Mode");

			wifiNetToggle.setColorFilter(Color.argb(255, 30, 131, 244));

			break;
		case 3:
			if (UploadServerService.serverenabled) {
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

	public static void update() {

		UI_updater.updateServerStatus();
		UI_updater.updateSDspace();
		UI_updater.updateIP();

	}


}
