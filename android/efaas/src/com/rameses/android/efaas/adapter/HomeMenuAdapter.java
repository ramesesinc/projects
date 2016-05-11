package com.rameses.android.efaas.adapter;

import java.util.List;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.HomeItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<HomeItem> data;
	
	public HomeMenuAdapter(Activity activity, List<HomeItem> data){
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
		View rowView = inflater.inflate(R.layout.list_menu_image, null);
		ImageView img = (ImageView) rowView.findViewById(R.id.list_menu_image);
		TextView title = (TextView) rowView.findViewById(R.id.list_menu_title);
		
		HomeItem item = data.get(pos);
		if(item != null) img.setImageResource(item.getId());
		if(item != null) title.setText(item.getTitle());
		
		return rowView;
	}
	
	public HomeItem getListItem(int pos){
		return data.get(pos);
	}

}
