package com.common.methods;

import com.master.webserver.firstPage;

import android.widget.LinearLayout;
import android.widget.TextView;

public class UI_updater {

	private static boolean serveronoff;
	private static boolean speedviewenable;
	private static LinearLayout connectionTogglesgroup;
	private static LinearLayout transferstatus;
	private static TextView availableSpace;
	private static TextView textIpaddr;
	private static TextView startstatus;
	private static int PORT;

	
	public static void inti_firstpage_UI(boolean serveronoff,
			boolean speedviewenable, LinearLayout connectionTogglesgroup,
			LinearLayout transferstatus, TextView textIpaddr,
			TextView availableSpace, TextView startstatus, int PORT) {
		
		UI_updater.serveronoff = serveronoff;
		UI_updater.speedviewenable = speedviewenable;
		UI_updater.connectionTogglesgroup = connectionTogglesgroup;
		UI_updater.transferstatus = transferstatus;
		UI_updater.availableSpace = availableSpace;
		UI_updater.textIpaddr = textIpaddr;
		UI_updater.startstatus = startstatus;
		UI_updater.PORT = PORT;

	}

	public static void updatefirstpageUI() {

		if (speedviewenable) {

			transferstatus.setVisibility(LinearLayout.VISIBLE);
			connectionTogglesgroup.setVisibility(LinearLayout.GONE);

		} else {
			transferstatus.setVisibility(LinearLayout.GONE);
			connectionTogglesgroup.setVisibility(LinearLayout.VISIBLE);

		}

		// To set the available sdcard field
		if (AvailableSpaceHandler.getExternalAvailableSpaceInMB() > 50) {

			availableSpace.setText("Availabe Space : "
					+ AvailableSpaceHandler.getExternalAvailableSpaceInMB()
					+ " MB ");

		} else {

			availableSpace.setText("Availabe Space : "
					+ AvailableSpaceHandler.getExternalAvailableSpaceInMB()
					+ " MB (Warning! Low Space !)");

		}

		if (serveronoff) {

			// To get the IP Address of the device
			String ipaddress = IpAddress.getHostIPAddress();
			textIpaddr.setText("http://" + ipaddress + ":" + PORT);
			// End of Setting the IP

			startstatus.setText("Stop Flash");

		} else {
			startstatus.setText("Start Flash");
			textIpaddr.setText("Flash Not Started");
		}

	}


	public static void updateUI_firstPage(firstPage fp){
		
		
		
	}

}

