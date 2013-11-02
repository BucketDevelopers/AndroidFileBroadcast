package com.master.webserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


import com.common.methods.IpAddress;
import com.common.methods.MimeUtils;
import com.common.methods.XmlParser;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

import com.library.Httpdserver.NanoHTTPD;
import com.library.Httpdserver.NanoHTTPD.Response.Status;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
//import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class File_Download extends Activity implements OnClickListener{
	private static final int PORT = 8181;
	private MyHTTPD server;
	private static final int REQUEST_CODE = 100;
	Button upload;
	ListView listview;
	ArrayList<String> filearray;
    ArrayAdapter<String> arrayadapter;
    XmlParser xml;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_download);
		upload = (Button)findViewById(R.id.button1);
		listview  = (ListView)findViewById(R.id.filelist);
		upload.setOnClickListener(this);
		File xfl= new File(getFilesDir(),"list.xml");
		filearray = new ArrayList<String>();
		if(!(xfl.exists()))
     	{
     		try{
     			xfl.createNewFile();
     			FileWriter fw = new FileWriter(xfl.getAbsoluteFile());
     			BufferedWriter bw = new BufferedWriter(fw);
     			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><list></list>");
				bw.close();
     		}
     		catch(IOException e1){
     			e1.printStackTrace();
     		}
     	}
     	else{
     		xml = new XmlParser(getFilesDir());
			 filearray = xml.fileList();
         	
     	}
		arrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,filearray);
		listview.setAdapter(arrayadapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String fpath = xml.getFilePath(filearray.get(position)).toLowerCase();
				String extension = fpath.substring(fpath.lastIndexOf('.')+1);
				
				File file = new File(fpath.substring(1));
				Toast.makeText(getBaseContext(), "--"+MimeUtils.guessMimeTypeFromExtension(extension)+"--",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file),MimeUtils.guessMimeTypeFromExtension(extension));
				//intent.setData(Uri.fromFile(file)); //Remove the initial '/' and parse
				//intent.setType("text/*");				
				//intent.setType(MimeUtils.guessMimeTypeFromExtension(extension));
				startActivity(intent);
				// TODO Auto-generated method stub
				/*
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				File file = new File("storage/sdcard0/hash.txt");
				intent.setDataAndType(Uri.fromFile(file),"text/*");
				startActivity(intent);
				*/
			}
		});
	}
	public void onClick(View view) {
		Button clickedBtn = (Button) view;

        switch (clickedBtn.getId()) {
        case R.id.button1: 
        	// TODO Auto-generated method stub
        				//Intent getContentIntent = FileUtils.createGetContentIntent();
        			   // Intent intent = Intent.createChooser(getContentIntent, "Select a file");
        			   Intent intent = new Intent(this,FileChooserActivity.class);
        				startActivityForResult(intent, REQUEST_CODE);
        	            break;
		
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	        case REQUEST_CODE:   
	            if (resultCode == RESULT_OK) {  
	            	String tempFilePath,tempFileName;
	                final Uri uri = data.getData();
	                File file = FileUtils.getFile(uri);
	                tempFilePath=file.toString();
	                tempFileName=tempFilePath.substring(tempFilePath.lastIndexOf('/')+1);
	                Log.d("msg1",tempFileName);
	                XmlParser t_xml = new XmlParser(getFilesDir());
	   			 	t_xml.addFile(tempFileName, tempFilePath);
	                Log.d("msg2",tempFilePath);
	                filearray.add(tempFileName);
	                arrayadapter.notifyDataSetChanged();
	            }
	    }
	}

	@Override
	protected void onResume() {
		super.onResume();

		TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);

		String ipaddress = IpAddress.getHostIPAddress();
		textIpaddr.setText("Please access! http://" + ipaddress + ":" + PORT);

		server = new MyHTTPD();
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (server != null)
			server.stop();
	}

	// Http Response Server

	private class MyHTTPD extends NanoHTTPD {
		public MyHTTPD() {
			super(PORT);
		}

		 public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {

             XmlParser xml = new XmlParser(getFilesDir());
			 //xml.addFile("abc.txt", "/sdcard/test/abc.txt");
			 ArrayList<String> fileList = xml.fileList();
			 if(uri.contentEquals("/")){
				 StringBuilder filesHtml = new StringBuilder();
				 for(int i=0;i<fileList.size();i++)
				 {
					 filesHtml.append("<a href=\""+fileList.get(i)+"\">"+fileList.get(i)+"</a><br/>");
				 }
		 
				 StringBuilder sb = new StringBuilder();
				 sb.append("<html>");
				 sb.append("<head><title>Debug Server</title></head>");
				 sb.append(filesHtml.toString());
				 sb.append("<body>");
				 sb.append("</body>");
				 sb.append("</html>");
				 return new Response(sb.toString());
			 }
			 
		   //     File path = Environment.getExternalStorageDirectory();
			 else{
				 String fileName = uri.substring(1);
				 String fpath=xml.getFilePath(fileName);
				 File file = new File(fpath);  
				 //int ch;
				 //StringBuilder text = new StringBuilder();
				    
				 try {
		              //BufferedReader br = new BufferedReader(new FileReader(file));
					 FileInputStream in = new FileInputStream(file);
				//	 String line;
		        //      while ((ch = in.read()) != -1) {
		          //          text.append((char)ch);
		                    //text.append('\n');
		            //    }
			
				 Response res= new Response(Status.OK,"application/octet-stream",in );
				 res.addHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
				 return res;
				 }
				 catch (IOException e) {
		                //You'll need to add proper error handling here
				 }
				 return new Response("Fail!!");
		       
		    }
		}//end of serve
	} //end of class myHTTPD
}//end of main activity
