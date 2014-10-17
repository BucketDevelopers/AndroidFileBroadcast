package com.bucketdevelopers.uft;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReceivedListHandler extends ArrayAdapter<String>{
	protected File temppath;
	protected static List<String> values = Arrays.asList((String[]) null);
	private final Context context;
	 
	public ReceivedListHandler(Context context,List<String> objects) {
		super(context, R.layout.list_text,objects);
		this.context = context;
		this.values = objects;
		}
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_text, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.tv_list);
	    textView.setText(values.get(position));
	   
	    return rowView;
	  }
}

