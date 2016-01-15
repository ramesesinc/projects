package com.rameses.clfc.android.main;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class CollectionDateAdapter extends BaseAdapter 
{
	private Activity activity;
	private List<Map> list;
	private SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");

	public CollectionDateAdapter(Activity activity, List<Map> list) {
		this.activity = activity;
		this.list = list;
	}
	
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vw = view;
		if (vw == null) {
			vw = inflater.inflate(R.layout.item_string, null);
		}
		
		TextView str = (TextView) vw.findViewById(R.id.tv_item_str);
		str.setTextAppearance(activity, android.R.style.TextAppearance_Medium);
		
		Map item = (Map) list.get(position);
		if (item.containsKey("billdate")) {
			String date = item.get("billdate").toString();
			String str_date = df.format(parseDate(date));
			str.setText(str_date);
		}
		
		return vw;
	}

	private Date parseDate(Object date) {
		if (date == null) return null;
		if (date instanceof Date) {
			return (Date) date;
		} else {
			return java.sql.Date.valueOf(date.toString());
		}
	}
	
}
