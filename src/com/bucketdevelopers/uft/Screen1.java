package com.bucketdevelopers.uft;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.bucketdevelopers.uft.ServerService;
import com.qrcode.library.Contents;
import com.qrcode.library.QRCodeEncoder;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Screen1 extends Fragment {

	private Intent uploaddownloadservice;
	private int PORT=8080;
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
		
		uploaddownloadservice= new Intent(getActivity(), ServerService.class);

		
		// Start\Stop the Server
		
		ImageButton serverToggle = (ImageButton) v
				.findViewById(R.id.ServerEnable);
		serverToggle.setOnClickListener(new OnClickListener() {
		

			@Override
			public void onClick(View v) {

				// Start The service
				ServerService.serverenabled=false;
				if (ServerService.serverenabled != true) {

					// Bug Fix:
					// Just So that App doesnt FORCE CLOSE even for some
					// reason the UI is ******
					getActivity().stopService(uploaddownloadservice);

					// Now the Real Deal
					Log.d("tag","Starting");
					uploaddownloadservice.putExtra("Port", PORT);
					getActivity().startService(uploaddownloadservice);

				} else {
					getActivity().stopService(uploaddownloadservice);
					ServerService.serverenabled = false;
				}
				
			}
		});

		// Switch to next Fragment
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
		// QR Code Generator begin

		ImageView imageView = (ImageView) v.findViewById(R.id.qrCode);

		String qrData = "Its MMD - for now";
		int qrCodeDimention = 500;

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
				Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
				qrCodeDimention);

		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			imageView.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		// End QR Code Generator

		return v;

	}

}
