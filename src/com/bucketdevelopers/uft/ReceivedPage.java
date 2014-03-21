package com.bucketdevelopers.uft;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.common.methods.ExternalStorage;
import com.common.methods.MimeUtils;
import com.common.methods.XmlParser;
import com.ipaulpro.afilechooser.FileChooserActivity;
import com.ipaulpro.afilechooser.utils.FileUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
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

@SuppressLint("DefaultLocale")
public class ReceivedPage extends Fragment {

	protected ActionMode mActionMode;
	protected static Context ab;
	protected ListView listview;
	
	protected static MainActivity xy;
	protected Boolean selected;
	public ArrayList<Integer> selectedItems= new ArrayList<Integer>();
	Button refreshButton;
	List<String> filearray;
	ArrayAdapter<String> arrayadapter;
	File extPath;
	public static final ReceivedPage newInstance(MainActivity mainActivity)

	{

		ReceivedPage f = new ReceivedPage();
		Bundle bdl = new Bundle(1);
		xy = mainActivity;
		f.setArguments(bdl);
		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.received, container, false);
		ab = v.getContext();
		selected= false;
		refreshButton = (Button) v.findViewById(R.id.browseButton);
		listview = (ListView) v.findViewById(R.id.receivedList);
		
		//Browse Button setup
		
		
		refreshButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!selected)
				{
					try{
						
					
					File tempPath = new File(ExternalStorage.getsdcardfolderwithoutcheck());
					filearray.clear();
					filearray.addAll(Arrays.asList(tempPath.list(null)));
					Log.d("refresh2", filearray.toString());
					arrayadapter.notifyDataSetChanged();				
					}
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
		filearray = new ArrayList<String>();
		
		try {
			extPath = new File(ExternalStorage.getsdcardfolderpath());
			filearray =  new LinkedList<String>(Arrays.asList(extPath.list(null)));
			Log.d("files", filearray.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		arrayadapter = new CustomAdaptor(v.getContext(), filearray,this.selectedItems);
		listview.setAdapter(arrayadapter);
		
        listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				if(!selected)
				{
					// User clicked on the file, unselected -> Open the file
					String fpath = (extPath+"/"+filearray.get(pos)).toLowerCase();
					Log.d("files", fpath);
					String extension = fpath.substring(fpath.lastIndexOf('.') + 1);
					File file = new File(fpath.substring(1));
					if (MimeUtils.guessMimeTypeFromExtension(extension) == null)
						Toast.makeText(view.getContext(), "Unknown file type",
								Toast.LENGTH_SHORT).show();
					else {
						Intent intent = new Intent();
						intent.setAction(android.content.Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(file),
								MimeUtils.guessMimeTypeFromExtension(extension));
						startActivity(intent);
					}
					// TODO Auto-generated method stub
				}
				else{
					//user clicked on file when CAB is shown. 
					//If selected-> unselect else select.
					if(selectedItems.contains((Integer)pos))
						{
							selectedItems.remove((Integer)pos);
							mActionMode.setTitle(selectedItems.size()+" selected");
						}
					else
						{
							selectedItems.add((Integer)pos);
							mActionMode.setTitle(selectedItems.size()+" selected");
						}
					arrayadapter.notifyDataSetChanged();
					 
					}
			}
		});
       
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {
	    	@Override
	    		public boolean onItemLongClick(AdapterView<?> parent, View view,
	    	              int position, long id) {
	    		
	    	            if (mActionMode != null) {
	    	              return false;
	    	            }
	    	            selectedItems.add(position) ;
	    	            selected = true;
	    	            arrayadapter.notifyDataSetChanged();
	    	            // start the CAB using the ActionMode.Callback defined above
	    	            mActionMode = xy.startSupportActionMode( mActionModeCallback);
	    	
		    	            
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
    	mode.setTitle(selectedItems.size()+" selected");
        return false; // Return false if nothing is done
    }

    // called when the user selects a contextual menu item
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
    	File tempfile;
      switch (item.getItemId()) {
      case R.id.cab_selected:
        
				Map<String, String> filemap = XmlParser.getFileMap();
				
				Log.d("Hell", "List has " + filearray.size());

				Collections.sort(selectedItems);

				for (int i = selectedItems.size() - 1; i >= 0; i--) {
					filearray.remove((int) selectedItems.get(i));
				}
				arrayadapter.notifyDataSetChanged();

				tempfile = new File(ab.getFilesDir(), "list.xml");
				tempfile.delete();
				XmlParser.checkXml(ab.getFilesDir(), "list.xml");
				XmlParser t_xml = new XmlParser(ab.getFilesDir());

				for (int i = 0; i < filearray.size(); i++) {
					t_xml.addFile(filearray.get(i),
							filemap.get(filearray.get(i)));
				}

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
    	selected = false;
    	mActionMode = null;
    	selectedItems.clear();
    	arrayadapter.notifyDataSetChanged();
      
    }
  };
  @Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*switch (requestCode) {
		case REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
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
		}*/
	}
} 


