package com.rameses.android.efaas.adapter;

import java.util.List;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.HomeListItem;
import com.rameses.android.efaas.bean.MasterFileListItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExaminationMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<ExaminationListItem> data;
	
	public ExaminationMenuAdapter(Activity activity, List<ExaminationListItem> data){
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
		View rowView = inflater.inflate(R.layout.list_menu_text, null);
		TextView title = (TextView) rowView.findViewById(R.id.list_menu_text_title);
		TextView desc = (TextView) rowView.findViewById(R.id.list_menu_text_desc);
		
		ExaminationListItem item = data.get(pos);
		if(item != null) title.setText(item.getFindings());
		if(item != null) desc.setText(item.getDate());
		
		return rowView;
	}
	
	public ExaminationListItem getListItem(int pos){
		return data.get(pos);
	}

}
