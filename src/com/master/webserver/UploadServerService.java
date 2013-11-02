package com.master.webserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.common.methods.ClearCache;
import com.common.methods.ExternalStorage;
import com.library.Httpdserver.NanoHTTPD;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UploadServerService extends Service {
	public static NotificationCompat.Builder mBuilder;
	public static NotificationManager manager;
	private MyHTTPD server;
	public int PORT;
	public String htmldata;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	public static void updateNotification(String Title, String msg, Context ctx) {

		mBuilder.setContentTitle(Title);
		mBuilder.setContentText(msg);
		Notification not = mBuilder.getNotification();
		not.flags = Notification.FLAG_ONGOING_EVENT;
		manager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(100, not);

		/*
		 * End of Notification Management
		 */

	}

	public void removeNotification() {
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(100);
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
		if (server != null) {
			server.stop();
			removeNotification();
		}
	}

	@Override
	public void onStart(Intent intent, int startid) {
		PORT = intent.getExtras().getInt("Port");
		htmldata = intent.getExtras().getString("htmlfile");
		Toast.makeText(this, "Upload Service started ", Toast.LENGTH_LONG)
				.show();

		Log.d("FTDebug", "Upload Server Started!");

		server = new MyHTTPD(getApplicationContext());
		ClearCache.clean();

		try {
			server.start();
		} catch (IOException e) {
			Log.d("FTDebug", e.getMessage());
		}

		/*
		 * Creating the Notification when the server starts to notify the user
		 */

		Intent notificationIntent = new Intent(this, firstPage.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("File Server is Running")
				.setContentText("Just Started :)")
				.setContentIntent(contentIntent).setAutoCancel(false)
		// .setProgress(100, 30, false)
		;

		updateNotification("File Server Started", " Waiting to Connect !",
				getApplicationContext());
		/*
		 * End of Notification Management
		 */

	}

	// Http Response Server

	private class MyHTTPD extends NanoHTTPD {

		public MyHTTPD() {
			super(PORT, htmldata);
		}

		public MyHTTPD(Context parentContext) {
			super(PORT, htmldata, parentContext);
		}

		@Override
		public Response serve(String uri, Method method,
				Map<String, String> header, Map<String, String> parms,
				Map<String, String> files)

		{

			/*
			 * This will happen but nothing will be displayed as already a
			 * response would have gone because of a Patch in Nano HTTPD file
			 * Thus The Text sent here will be of no use But this function is
			 * required for background Data Processing!
			 * 
			 * This was supposedly the Old output This can still be used but the
			 * response will be ultra slow as response is sent after all the
			 * processing is done. This can be used for debugging purposes as it
			 * displays all the header info but file upload will be slow. if
			 * debug values have to be enabled set the patchenable value to
			 * false and set the patchenable variable value in NanoHTTPD.java to
			 * false.
			 */

			boolean patchenable = true;
			if (!patchenable) {
				StringBuilder sb = new StringBuilder();
				sb.append("<html>");
				sb.append("<head>");

				sb.append("<title>File Server</title></head>");
				sb.append("<body>");
				sb.append("<script>");
				sb.append("var extract=function(answer){");
				sb.append(";document.myform.filenamebackup.value=answer;document.getElementById('upform').submit();}");
				sb.append("</script>");

				sb.append("<form name=\"myform\" id='upform' method='post' enctype='multipart/form-data'>");
				sb.append("<input type=\"file\" name=\"myfile\">");
				sb.append("<input type=\"hidden\" name=\"filenamebackup\" value=\"nofile\">");
				sb.append("<input type=\"button\" value=\"Upload\" onClick=\"extract(document.myform.myfile.value)\">");
				sb.append("	</form>");

				sb.append("<h1>Response</h1>");
				sb.append("<p><blockquote><b>URI -</b> ")
						.append(String.valueOf(uri)).append("<br />");
				sb.append("<b>Method -</b> ").append(String.valueOf(method))
						.append("</blockquote></p>");
				sb.append("<h3>Headers</h3><p><blockquote>")
						.append(String.valueOf(header))
						.append("</blockquote></p>");
				sb.append("<h3>Parms</h3><p><blockquote>")
						.append(String.valueOf(parms))
						.append("</blockquote></p>");
				sb.append("<h3>Files</h3><p><blockquote>")
						.append(String.valueOf(files))
						.append("</blockquote></p>");

				sb.append("</body>");
				sb.append("</html>");
				return new Response(sb.toString());

			}

			/*
			 * To rename the Temp File Created into Actual File Name
			 */

			if (files.get("myfile") != null
					&& parms.get("filenamebackup") != null) {
				/*
				 * myfile is the Temp Cache file name fileName is the actual
				 * File name we get from the extra variable in the form data
				 * ..ie filenamebackup
				 */
				int index = parms.get("filenamebackup").lastIndexOf("\\");
				String fileName = parms.get("filenamebackup").substring(
						index + 1);

				File from = new File(
						ExternalStorage.getsdcardfolderwithoutcheck(),
						new File(files.get("myfile")).getName());
				File to = new File(
						ExternalStorage.getsdcardfolderwithoutcheck(), fileName);

				from.renameTo(to);

				UploadServerService.updateNotification(
						"File Server is Processing", "Cleaning Up Temp Files",
						getApplicationContext());

				// To Clean Cache File Created in the Process
				ClearCache.clean();

				UploadServerService.updateNotification("File Saved", "File: "
						+ fileName, getApplicationContext());
				
					
				
				
			}

			return new Response(
					"<center><h1>Oops! This was not supposed to happen ! My Bad ! :P </h1></center><br><center><h1>Please Reload Again!</h1></center></h1></center><br><center><h5>U Forgot one of patchenable Flag!</h5></center>");
		}
	}

}
