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

public class RemarksAdapter extends BaseAdapter {

	private Activity activity;
	private List list;
	public static int INDEX = "INDEX".hashCode();
	
	public RemarksAdapter(Activity activity, List list) {
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
			v = inflater.inflate(R.layout.item_remarks, null);
			v.setTag(INDEX, index);
		}
		
		Map remark = (Map) list.get(index);
		((TextView) v.findViewById(R.id.tv_txndate)).setText(remark.get("txndate").toString());
		((TextView) v.findViewById(R.id.tv_collector)).setText(remark.get("collectorname").toString());
		((TextView) v.findViewById(R.id.tv_remarks)).setText(remark.get("remarks").toString());		
		
		return v;
	}

}
