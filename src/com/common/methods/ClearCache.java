package com.common.methods;

import android.util.Log;
import java.io.File;

public class ClearCache {

	// Function to delete cache Files
			private static  void deleteCacheFiles(String sDir) {

				File[] faFiles = new File(sDir).listFiles();
				for (File file : faFiles) {
					if (file.getName().endsWith(".cache")) {
						file.delete();
						Log.d("FTDebug", file.getAbsolutePath() + "deleted!");
					}

				}
			}
			
			public static void clean(){
				
				Log.d("FTDebug", "Cleanup Started!");
				deleteCacheFiles(ExternalStorage.getsdcardfolderwithoutcheck()+'/');
				Log.d("FTDebug", "Cleanup Ended!");
			}
	
}
