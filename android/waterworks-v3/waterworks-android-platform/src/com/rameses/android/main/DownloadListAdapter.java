package com.rameses.android.main;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rameses.android.R;

public class DownloadListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	public DownloadListAdapter(Context context, ArrayList<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
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
	public long getItemId(int idx) {
		return idx;
	}

	@Override
	public View getView(int idx, View view, ViewGroup vg) {
		View v = view;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_download_sector, null);
		} 
		
		Map item = (Map) getItem(idx);
		
		if (item.containsKey("code")) {
			TextView name = (TextView) v.findViewById(R.id.tv_name);
			name.setText(item.get("code").toString());
		}
		
		if (item.containsKey("objid")) {
			v.setTag(R.string.id_tag, item.get("objid").toString());
		}
		
		return v;
	}

	void println(String msg) {
		Log.i("DownloadListAdapter", msg);
	}

}
