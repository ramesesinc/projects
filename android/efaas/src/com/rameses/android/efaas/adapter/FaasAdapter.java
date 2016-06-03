package com.rameses.android.efaas.adapter;

import java.util.List;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.FaasItem;
import com.rameses.android.efaas.bean.HomeItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FaasAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<FaasItem> data;
	
	public FaasAdapter(Activity activity, List<FaasItem> data){
		ctx = activity;
		this.data = data;
		inflater = ( LayoutInflater )ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup vgroup) {
		View rowView = inflater.inflate(R.layout.list_menu_faas, null);
		
		TextView pin = (TextView) rowView.findViewById(R.id.list_menu_faas_pin);
		TextView tdno = (TextView) rowView.findViewById(R.id.list_menu_faas_tdno);
		TextView name = (TextView) rowView.findViewById(R.id.list_menu_faas_name);
		
		FaasItem item = data.get(pos);
		if(item != null){
			pin.setText(item.getPin());
			name.setText(item.getName());
			tdno.setText(item.getTdNo());
		}
		
		return rowView;
	}
	
	public FaasItem getListItem(int pos){
		return data.get(pos);
	}

}
