package com.rameses.android.efaas.adapter;

import java.util.List;
import com.rameses.android.R;
import com.rameses.android.efaas.bean.AdjustmentItem;
import com.rameses.android.efaas.bean.ParameterItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ParameterMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<ParameterItem> data;
	
	public ParameterMenuAdapter(Context ctx, List<ParameterItem> data){
		this.ctx = ctx;
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
		View rowView = inflater.inflate(R.layout.list_menu_adjustment, null);
		
		TextView code = (TextView) rowView.findViewById(R.id.adjustment_type);
		TextView name = (TextView) rowView.findViewById(R.id.adjustment_parameter);
		
		ParameterItem item = data.get(pos);
		if(item != null){
			code.setText(item.getParameter());
			name.setText(String.valueOf(item.getValue()));
		}
		
		return rowView;
	}
	
	public ParameterItem getListItem(int pos){
		return data.get(pos);
	}

}
