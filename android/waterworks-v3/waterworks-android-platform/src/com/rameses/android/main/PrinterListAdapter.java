package com.rameses.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rameses.android.R;

public class PrinterListAdapter extends BaseAdapter {

	private Context context;
	private List<Map> list = new ArrayList<Map>();
	
	public PrinterListAdapter(Context context, List<Map> list) {
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int idx) {
		// TODO Auto-generated method stub
		return list.get(idx);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int idx, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = view;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_string, null);
		}
		
		Map item = (Map) getItem(idx);
		
//		println("idx " + idx + " item " + item);
		Boolean selected = Boolean.valueOf(item.get("selected").toString());
//		println("selected-> " + selected);
		if (selected == true) {
			v.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
//			item.put("selected", false);
		} else if (selected == false) {
			v.setBackgroundColor(context.getResources().getColor(android.R.color.background_light));
		}
		
		String name = item.get("name").toString();
		TextView tv = (TextView) v.findViewById(R.id.tv_item_str);
		tv.setText(name);
		
		v.setTag(R.string.name_tag, name);
		
		return v;
	}
	
	void println(String str) {
		Log.i("PrinterListAdapter", str);
	}

}
