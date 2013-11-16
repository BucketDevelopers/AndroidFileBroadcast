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
import com.common.methods.AvailableSpaceHandler;
import com.common.methods.ClearCache;
import com.common.methods.ExternalStorage;
import com.common.methods.IntentHelper;
import com.common.methods.UI_updater;
import com.common.methods.XmlParser;
import com.library.Httpdserver.NanoHTTPD;
import com.library.Httpdserver.NanoHTTPD.Response.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ServerService extends Service {
	public static NotificationCompat.Builder mBuilder;
	public static NotificationManager manager;
	private MyHTTPD server;
	public int PORT;
	public String htmldata;
	public static boolean serverenabled;
	private UI_updater UI;

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
		Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
		if (server != null) {
			server.stop();
			ServerService.serverenabled = false;
			removeNotification();
		}
	}

	@Override
	public void onStart(Intent intent, int startid) {

		ServerService.serverenabled = true;
		PORT = intent.getExtras().getInt("Port");
		Toast.makeText(this, "Upload Service started ", Toast.LENGTH_SHORT)
				.show();

		UI = (UI_updater) IntentHelper.getObjectForKey("UIObj");

		UI.updateServerStatus();

		Log.d("FTDebug", "Upload Server Started!");

		server = new MyHTTPD(getApplicationContext(), UI);
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
			super(PORT);
		}

		public MyHTTPD(Context parentContext, UI_updater uI) {
			super(PORT, parentContext, UI);

		}

		@Override
		public Response serve(String uri, Method method,
				Map<String, String> header, Map<String, String> parms,
				Map<String, String> files)

		{

			/*
			 * COMMENT V=2.0 This is the serve method which is responsible for
			 * handling requests from browser. Depending on the uri, we can do
			 * the corresponding action
			 * 
			 * In case uri is / we need to display filelist and the upload form
			 * 
			 * In case the uri is /upload we need to first give a response and
			 * then do the processing.
			 * 
			 * For all other uri of form /xyz we need to send the file xyz to
			 * the \ client
			 */
			if (uri.contentEquals("/")) {

				StringBuilder sb = new StringBuilder();
				XmlParser xml = new XmlParser(getFilesDir());
				ArrayList<String> fileList = xml.fileList();
				StringBuilder filesHtml = new StringBuilder();
				for (int i = 0; i < fileList.size(); i++) {
					filesHtml.append("<a href=\"" + fileList.get(i) + "\">"
							+ fileList.get(i) + "</a><br/>");
				}

				sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" "
						+ "xml:lang=\"en\" lang=\"en\">"
						+ "<head><meta http-equiv=\"Content-Type\" "
						+ "content=\"text/html; charset=UTF-8\" />"
						+ "<title> File Server </title></head><body>"
						+ "<script>\n  var extract=function(answer){\n "
						+ "document.myform.filenamebackup.value=answer;\n "
						+ "var availablespace= ");

				sb.append(AvailableSpaceHandler
						.getExternalAvailableSpaceInBytes() + ";\n");

				sb.append("var file = document.getElementById('loadfile').files[0];\n"
						+ "document.myform.filesize.value=file.size;\n"
						+ "if(availablespace<(2*file.size)){\n"
						+ "alert(\"The Receivers SD Card Doesnt have enough space for this file"
						+ " to be stored.\\nNote: The receiver must have atleast twice the space "
						+ "as that of the file you are sending.\\n"
						+ "Space Required:\"+((2*file.size)/(1024*1024))+\" MB \"+\"\\nThe Space"
						+ " on Device is : \"+(availablespace/(1024*1024))+\" MB \");\n }\nelse{"
						+ " document.getElementById('upform').submit();\n"
						+ " }\n }\n " + "</script>");

				sb.append(filesHtml.toString());

				sb.append("<form name=\"myform\" id=\"upform\" method=\"post\" enctype=\"multipart/form-data\" action = \"upload\"> "
						+ "<input type=\"file\" id=\"loadfile\" name=\"myfile\">"
						+ "<input type=\"hidden\" name=\"filenamebackup\" value=\"nofile\"> "
						+ "<input type=\"hidden\" name=\"filesize\" value=\"nullsize\"> "
						+ "<input type=\"button\" value=\"Upload\" onClick=\"extract(document.myform.myfile.value)\">  "
						+ "</form> " + "</body> " + "</html> ");

				return new Response(sb.toString());
			} else if (uri.contentEquals("/upload")) {

				// To Clean Cache File Created in the Process
				ClearCache.clean();

				return new Response(
						"<center><h1>Oops! This was not supposed to happen ! My Bad ! :P </h1></center><br><center><h1>Please Reload Again!</h1></center></h1></center><br><center><h5>U Forgot one of patchenable Flag!</h5></center>");
			}

			else if (uri.contentEquals("/video")) {

				StringBuilder sb = new StringBuilder();

				sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" "
						+ "xml:lang=\"en\" lang=\"en\">"
						+ "\n<head><meta http-equiv=\"Content-Type\" "
						+ "content=\"text/html; charset=UTF-8\" />"
						+ "\n<title> Video Server </title>\n</head>\n<body>");
				sb.append("\n<img style='height:100%;' src='raspvideo.jpg'>");
				sb.append("\n</body> " + "\n</html> ");

				return new Response(sb.toString());
			} 
			else if (uri.contentEquals("/raspvideo.jpg")) {

				FileInputStream in = null;
				try {
					in = new FileInputStream(ExternalStorage.getsdcardfolderwithoutcheck()+"/raspvideo.jpg");
				} catch (FileNotFoundException e) {

				}
				Response res = new Response(Status.OK,
						"application/octet-stream", in);
				res.addHeader("Content-Disposition", "attachment; filename=\""
						+"raspvideo.jpg"+ "\"");
				return res;
			}

			else {
				String fileName = uri.substring(1);
				String fpath = XmlParser.getFilePath(fileName);
				File file = new File(fpath);

				try {

					FileInputStream in = new FileInputStream(file);
					Response res = new Response(Status.OK,
							"application/octet-stream", in);
					res.addHeader("Content-Disposition",
							"attachment; filename=\"" + fileName + "\"");
					return res;
				} catch (IOException e) {
					// You'll need to add proper error handling here
				}
				return new Response("Fail!!");

			}
		}

	}

}
