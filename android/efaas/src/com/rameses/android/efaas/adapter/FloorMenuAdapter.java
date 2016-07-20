package com.rameses.android.efaas.adapter;

import java.util.List;
import com.rameses.android.R;
import com.rameses.android.efaas.bean.*;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FloorMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<FloorItem> data;
	
	public FloorMenuAdapter(Context ctx, List<FloorItem> data){
		ctx = ctx;
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
		View rowView = inflater.inflate(R.layout.list_menu_floor, null);
		
		TextView floorno = (TextView) rowView.findViewById(R.id.floor_floorno);
		TextView floorarea = (TextView) rowView.findViewById(R.id.floor_floorarea);
		
		FloorItem item = data.get(pos);
		if(item != null){
			floorno.setText(item.getFloorNo());
			floorarea.setText(item.getFloorArea());
		}
		
		return rowView;
	}
	
	public FloorItem getListItem(int pos){
		return data.get(pos);
	}

}
