package com.rameses.android.efaas.adapter;

import java.util.List;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.ImageItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageItemAdapter extends BaseAdapter{
	
	LayoutInflater inflater = null;
	Context ctx;
	List<ImageItem> data;
	
	public ImageItemAdapter(Activity activity, List<ImageItem> data){
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
		View rowView = inflater.inflate(R.layout.image_menu, null);
		
		ImageView image = (ImageView) rowView.findViewById(R.id.image_menu_view);
		TextView title = (TextView) rowView.findViewById(R.id.image_menu_text);
		
		ImageItem item = data.get(pos);
		if(item != null){
			image.setImageBitmap(item.getImage());
			title.setText(item.getTitle());
		}
		
		return rowView;
	}
	
	public ImageItem getListItem(int pos){
		return data.get(pos);
	}
}
