package com.rameses.android.efaas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.*;

public class AppraisalItemAdapter extends ArrayAdapter<DefaultItem> {
	
	private Context ctx;
	private List<DefaultItem> list;

	public AppraisalItemAdapter(Context context, int resource, List<DefaultItem> list) {
		super(context, resource);
		this.ctx = context;
		this.list = list;
	}
	
	public int getCount(){
       return list.size();
    }
	
	public DefaultItem getItem(int position){
       return list.get(position);
    }

    public long getItemId(int position){
       return position;
    }
	
	public View getView(int position, View convertView, ViewGroup parent) {
		DefaultItem item = list.get(position);
	    LayoutInflater inflater = ( LayoutInflater )ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View row = inflater.inflate(R.layout.list_menu_default, parent, false);
	    TextView tv = (TextView) row.findViewById(R.id.list_menu_default_textview);
	    tv.setText(item.getName());
	    return row;
	}


	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		DefaultItem item = list.get(position);
		LayoutInflater inflater = ( LayoutInflater )ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View row = inflater.inflate(R.layout.list_menu_default, parent, false);
	    TextView tv = (TextView) row.findViewById(R.id.list_menu_default_textview);
	    tv.setText(item.getName());
	    return row;
	}
		

}
