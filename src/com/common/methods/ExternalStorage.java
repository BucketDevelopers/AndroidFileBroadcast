package com.common.methods;

import android.app.Activity;
import android.os.Environment;

import java.io.File;

public class ExternalStorage extends Activity{

	public static boolean checksdcardstate() {

	        String state = Environment.getExternalStorageState();
	        boolean mExternalStorageAvailable = false;
	        boolean mExternalStorageWriteable = false;

	        if (Environment.MEDIA_MOUNTED.equals(state)) {
	            // We can read and write the media
	            mExternalStorageAvailable = mExternalStorageWriteable = true;
	        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	            // We can only read the media
	            mExternalStorageAvailable = true;
	            mExternalStorageWriteable = false;
	        } else {
	            // Something else is wrong. It may be one of many other states, but
	            // all we need
	            // to know is we can neither read nor write
	            mExternalStorageAvailable = mExternalStorageWriteable = false;
	        }

	        if (mExternalStorageAvailable == true
	                && mExternalStorageWriteable == true) {
	            return true;
	        } else {
	            return false;
	        }
	    }	
		
	
	public static String getsdcardfolderpath() throws Exception{
		
		if(checksdcardstate()){
			
			File sdcardpath= Environment.getExternalStorageDirectory();
			String appsdfolder=sdcardpath.toString()+"/webserverdata";
			return appsdfolder;
			
		}else{
			
			Exception e=new Exception("Oops ! External Storage is not Available !");
			throw e;
		}

	}
	
	public static String getsdcardfolderwithoutcheck(){
		
		File sdcardpath= Environment.getExternalStorageDirectory();
		String appsdfolder=sdcardpath.toString()+"/webserverdata";
		return appsdfolder;
		
		
	}
	
	
}
