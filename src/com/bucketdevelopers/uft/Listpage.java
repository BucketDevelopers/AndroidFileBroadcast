package com.bucketdevelopers.uft;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import com.common.methods.MimeUtils;
import com.common.methods.XmlParser;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Listpage extends Fragment {

	private static final int REQUEST_CODE = 122;
	protected Object mActionMode;
	protected static Context ab;
	protected ListView listview;
	protected static MainActivity xy;
	public int selectedItem = -1;
	Button addFiles;
	ArrayList<String> filearray;
	ArrayAdapter<String> arrayadapter;
	XmlParser xml;
	public static final Listpage newInstance(MainActivity mainActivity)

	{

		Listpage f = new Listpage();
		Bundle bdl = new Bundle(1);
		xy = mainActivity;
		f.setArguments(bdl);
		return f;

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list, container, false);
		ab = v.getContext();
		addFiles = (Button) v.findViewById(R.id.addButton);
		listview = (ListView) v.findViewById(R.id.listview);

		
		//addFiles Button setup
		
		
		addFiles.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent( v.getContext(), FileChooserActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		
		
		
	    filearray = new ArrayList<String>();
		xml = new XmlParser(v.getContext().getFilesDir());
		filearray = xml.fileList();

		arrayadapter = new ArrayAdapter<String>(v.getContext(),R.layout.list_text, filearray);
	     listview.setAdapter(arrayadapter);
        listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				
				// User clicked on the file -> Open the file
				String fpath = XmlParser.getFilePath(
						filearray.get(pos)).toLowerCase();
				String extension = fpath.substring(fpath
						.lastIndexOf('.') + 1);
				File file = new File(fpath.substring(1));
				if (MimeUtils
						.guessMimeTypeFromExtension(extension) == null)
					Toast.makeText(view.getContext(),
							"Unknown file type",
							Toast.LENGTH_SHORT).show();
				else {
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(
							Uri.fromFile(file),
							MimeUtils
									.guessMimeTypeFromExtension(extension));
					startActivity(intent);
				}
				// TODO Auto-generated method stub
				
			}
		});
       
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
	    	@Override
	    		public boolean onItemLongClick(AdapterView<?> parent, View view,
	    	              int position, long id) {
	    		
	    	            if (mActionMode != null) {
	    	              return false;
	    	            }
	    	            selectedItem = position;
	    	            listview.setItemChecked(position, true);

	    	            // start the CAB using the ActionMode.Callback defined above
	    	            mActionMode = xy.startSupportActionMode( mActionModeCallback);
	    	           view.setSelected(true);
		    	            
	    	            return true;
	    	          }
	    	        });
	
		return v;

	}

private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

    // called when the action mode is created; startActionMode() was called
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
      // Inflate a menu resource providing context menu items
      MenuInflater inflater = mode.getMenuInflater();
      // assumes that you have "contexual.xml" menu resources
      inflater.inflate(R.menu.cab, menu);
      return true;
    }

    // the following method is called each time 
    // the action mode is shown. Always called after
    // onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
    	mode.setTitle(filearray.get(selectedItem));
    	
      return false; // Return false if nothing is done
    }

    // called when the user selects a contextual menu item
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    	File tempfile;
      switch (item.getItemId()) {
      case R.id.cab_selected:
        
        Map<String, String> filemap = XmlParser
				.getFileMap();
		Log.d("Hell",
				"List has " + filearray.size());
		filearray.remove(selectedItem);
		arrayadapter.notifyDataSetChanged();
		tempfile = new File(ab.getFilesDir(),
				"list.xml");
		tempfile.delete();
		XmlParser.checkXml(ab.getFilesDir(),
				"list.xml");
		XmlParser t_xml = new XmlParser(
				ab.getFilesDir());
		Log.d("Hell",
				"List has " + filearray.size());
		for (int i = 0; i < filearray.size(); i++) {
			// Log.d("attempt",
			// "Trying "+filearray.get(i)+" at "+filemap.get(filearray.get(i)));
			t_xml.addFile(filearray.get(i),
					filemap.get(filearray.get(i)));
		}
		Toast.makeText(ab, "Removed",
				Toast.LENGTH_SHORT).show();
		
        // the Action was executed, close the CAB
        mode.finish();
        return true;
      case R.id.cab_clear_list:
    	  
    	  
    	  tempfile = new File(ab.getFilesDir(),
					"list.xml");
			tempfile.delete();
			XmlParser.checkXml(ab.getFilesDir(),
					"list.xml");
			filearray.clear();
			arrayadapter.notifyDataSetChanged();
    	  mode.finish();
    	  return true;
      default:
        return false;
      }
    }

    // called when the user exits the action mode
    public void onDestroyActionMode(ActionMode mode) {
    	listview.setItemChecked(selectedItem, false);
    	mActionMode = null;
      selectedItem = -1;
      
    }
  };

  private void show() {
    Toast.makeText(Listpage.ab,
        String.valueOf(selectedItem), Toast.LENGTH_SHORT).show();
  }
  @Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:
			if (resultCode == xy.RESULT_OK) {
				String tempFilePath, tempFileName;
				final Uri uri = data.getData();
				File file = FileUtils.getFile(uri);
				tempFilePath = file.toString();
				tempFileName = tempFilePath.substring(tempFilePath
						.lastIndexOf('/') + 1);
				Log.d("msg1", tempFileName);
				XmlParser t_xml = new XmlParser(ab.getFilesDir());
				t_xml.addFile(tempFileName, tempFilePath);
				Log.d("msg2", tempFilePath);
				arrayadapter.add(tempFileName);
			}
		}
	}
} 

