package com.rameses.clfc.android.main;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rameses.clfc.android.R;

public class NotesAdapter extends BaseAdapter {

	private Activity activity;
	private List list;
	public static int INDEX = "INDEX".hashCode();
	
	public NotesAdapter(Activity activity, List list) {
		this.activity = activity;
		this.list = list;
	}
	
	public int getCount() {
		return list.size();
	}

	public Object getItem(int index) {
		return list.get(index);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int index, View view, ViewGroup container) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View v = view;
		if (v == null) {
			v = inflater.inflate(R.layout.item_string, null);
			v.setTag(INDEX, index);
		}
		
		Map item = (Map) list.get(index);
		((TextView) v.findViewById(R.id.tv_item_str)).setText(item.get("remarks").toString());
		
		return v;
	}

}
