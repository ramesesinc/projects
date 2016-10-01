package com.rameses.android.efaas.adapter;

import java.util.List;
import com.rameses.android.R;
import com.rameses.android.efaas.bean.AdjustmentItem;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdjustmentMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<AdjustmentItem> data;
	
	public AdjustmentMenuAdapter(Context ctx, List<AdjustmentItem> data){
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
		
		TextView code = (TextView) rowView.findViewById(R.id.adjustment_code);
		TextView name = (TextView) rowView.findViewById(R.id.adjustment_name);
		
		AdjustmentItem item = data.get(pos);
		if(item != null){
			code.setText(item.getCode());
			name.setText(item.getName());
		}
		
		return rowView;
	}
	
	public AdjustmentItem getListItem(int pos){
		return data.get(pos);
	}

}
