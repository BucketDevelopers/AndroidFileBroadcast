package com.bucketdevelopers.uft;

import com.common.methods.qrcodelibrary.Contents;
import com.common.methods.qrcodelibrary.QRCodeEncoder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

public class FullScreenQRCode extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullscreen_qr);
		TextView ipfull = (TextView) findViewById(R.id.ipfullscrn);

		ImageView qrfullscrn = (ImageView) findViewById(R.id.qrFullScreen);

		String qrData = "Error Generating QR Code!";
		qrData = (String) getIntent().getExtras().get("qrvalue");

		ipfull.setText(qrData);
		int width;

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels;

		int qrCodeDimention = width + 20;

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
				Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
				qrCodeDimention);

		try {
			Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
			qrfullscrn.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		// End QR Code Generator

	}

}
