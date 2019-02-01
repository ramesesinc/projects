package com.rameses.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.android.R;

public class RateListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Map> list = new ArrayList<Map>();
	private Context context;
	
	public RateListAdapter(Context context, List<Map> list) {
		this.context = context;
		this.list = list;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int idx) {
		return list.get(idx);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int idx, View view, ViewGroup parent) {
		View v = view;
		if (v == null) {
			v = inflater.inflate(R.layout.item_rate, null);
		}
		
		Map item = (Map) getItem(idx);

		TextView tv = (TextView) v.findViewById(R.id.tv_condition);
		tv.setText("");
		if (item.containsKey("condition")) {
			tv.setText(item.get("condition").toString());
		}
		
		tv = (TextView) v.findViewById(R.id.tv_action);
		tv.setText("");
		if (item.containsKey("action")) {
			tv.setText(item.get("action").toString());
		}
		
		int i = idx % 2;
//		println("idx " + idx + " result " + i + " " + (i != 0) + " item " + item.get("condition").toString());

		v.setBackgroundColor(context.getResources().getColor(android.R.color.white));
		if (i != 0) {
//			RelativeLayout container = (RelativeLayout) v.findViewById(R.id.rl_container);
			v.setBackgroundColor(Color.parseColor("#E8F5E9"));
		}
		
		
		return v;
	}
	
	void println(String str) {
		Log.i("RateListAdapter", str);
	}

}
