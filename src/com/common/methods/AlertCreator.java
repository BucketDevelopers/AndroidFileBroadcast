package com.common.methods;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertCreator {


	void create(Context ctx,String Title,String Message) {
		AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(ctx);

		myAlertDialog.setTitle("--- Title ---");
		myAlertDialog.setMessage("Alert Dialog Message");
		myAlertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						// do something when the OK button is clicked
					}
				});
		myAlertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						// do something when the Cancel button is clicked
					}
				});
		myAlertDialog.show();

	}

	
}
