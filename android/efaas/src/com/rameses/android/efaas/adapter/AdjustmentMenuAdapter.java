package com.rameses.android.efaas.adapter;

import java.util.List;
import com.rameses.android.R;
import com.rameses.android.efaas.bean.AdjustmentItem;
import com.rameses.android.efaas.bean.AdjustmentListItem;
import com.rameses.android.efaas.bean.ParameterItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdjustmentMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<AdjustmentListItem> data;
	
	public AdjustmentMenuAdapter(Context ctx, List<AdjustmentListItem> data){
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
		
		TextView type = (TextView) rowView.findViewById(R.id.adjustment_type);
		TextView parameter = (TextView) rowView.findViewById(R.id.adjustment_parameter);
		
		AdjustmentListItem item = data.get(pos);
		if(item != null){
			type.setText(item.getItem().getName());
			for(ParameterItem param : item.getList()){
				if(!parameter.getText().toString().isEmpty()) parameter.setText(parameter.getText().toString() + "\n\n" + param.getParameter() + "   -  " + param.getValue());
				if(parameter.getText().toString().isEmpty()) parameter.setText(param.getParameter() + "   -  " + param.getValue());
			}
		}
		
		return rowView;
	}
	
	public AdjustmentListItem getListItem(int pos){
		return data.get(pos);
	}

}
