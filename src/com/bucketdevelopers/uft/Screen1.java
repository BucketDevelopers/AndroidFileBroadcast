package com.bucketdevelopers.uft;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.bucketdevelopers.uft.ServerService;
import com.common.methods.AvailableSpaceHandler;
import com.common.methods.IpAddress;
import com.common.methods.qrcodelibrary.Contents;
import com.common.methods.qrcodelibrary.QRCodeEncoder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Screen1 extends Fragment {

	private Intent uploaddownloadservice;
	public static int PORT = 8080;
	private String qrData;
	private ImageView qrimagesmall;
	private TextView sdspaceavail;

	public static final Screen1 newInstance(String message)

	{

		Screen1 f = new Screen1();
		Bundle bdl = new Bundle(1);

		f.setArguments(bdl);
		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.screen1, container, false);
		sdspaceavail = (TextView) v.findViewById(R.id.sdspace);
		uploaddownloadservice = new Intent(getActivity(), ServerService.class);

		// Setting the SD Card Space
		updateSDSpace();

		// Start\Stop the Server

		ImageButton serverToggle = (ImageButton) v
				.findViewById(R.id.ServerEnable);
		serverToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Start The service
				if (ServerService.serverenabled != true) {

					// Bug Fix:
					// Just So that App doesnt FORCE CLOSE even for some
					// reason the UI is ******
					getActivity().stopService(uploaddownloadservice);

					// Now the Real Deal
					Log.d("tag", "Starting");
					uploaddownloadservice.putExtra("Port", PORT);
					getActivity().startService(uploaddownloadservice);
					
					updateIP(true);

				} else {
					getActivity().stopService(uploaddownloadservice);
					ServerService.serverenabled = false;
					updateIP(false);
				}
				
				updateSDSpace();
			}
		});

		// Switch to next Fragment (File Selection)
		ImageButton fileselect = (ImageButton) v
				.findViewById(R.id.fileSelector);
		fileselect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View vw) {
				ViewPager pager = (ViewPager) getActivity().findViewById(
						R.id.viewpager);
				pager.setCurrentItem(1, true);

			}
		});
		// QR Code OnClick Listeners

		qrimagesmall = (ImageView) v.findViewById(R.id.qrCode);

		// updateIP();

		qrimagesmall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent changetofullScreenqr = new Intent(getActivity(),
						FullScreenQRCode.class);
				changetofullScreenqr.putExtra("qrvalue", qrData);
				startActivity(changetofullScreenqr);
			}
		});
		// End of QR Listeners

		return v;

	}

	
	//to Update SD CARD space
	
	private void updateSDSpace() {
		String sdSpaceTextDisplay = null;

		long space = AvailableSpaceHandler.getExternalAvailableSpaceInMB();

		if (space < 50) {

			sdSpaceTextDisplay = "Warning!:(Low Space) :" + space + "MB";
		} else if (space > 1023) {
			space = AvailableSpaceHandler.getExternalAvailableSpaceInGB();
			sdSpaceTextDisplay = "Available Space: " + space + "GB";

		} else {

			sdSpaceTextDisplay = "Available Space: " + space + "MB";

		}

		sdspaceavail.setText(sdSpaceTextDisplay);
	}

	
	
	//To update IP UI
	
	void updateIP(boolean visible) {

		if (visible) {
			qrimagesmall.setVisibility(View.VISIBLE);
			if (IpAddress.getHostIPAddress().length() == 28) // Means Not
																// Connected to
																// any Network!
			{
				qrData = "Not Connected to any Network !";
			} else {
				qrData = "http://" + IpAddress.getHostIPAddress() + ":" + PORT;

			}

			// Getting Screen Width
			DisplayMetrics metrics = new DisplayMetrics();

			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(metrics);
			int width = metrics.widthPixels;
			int qrCodeDimention = width / 2;

			QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
					Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
					qrCodeDimention);

			try {
				Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
				qrimagesmall.setImageBitmap(bitmap);
			} catch (WriterException e) {
				e.printStackTrace();
			}
		} else {
			qrimagesmall.setVisibility(View.GONE);

		}

	}

}
