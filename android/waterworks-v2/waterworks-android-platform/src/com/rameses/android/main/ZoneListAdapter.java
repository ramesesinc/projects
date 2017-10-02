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
import android.widget.ImageView;
import android.widget.TextView;

import com.rameses.android.R;

public class ZoneListAdapter extends BaseAdapter {

	private Context context;
	private List<Map> list = new ArrayList<Map>();
	
	public ZoneListAdapter(Context context, List<Map> list) {
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
	public long getItemId(int idx) {
		// TODO Auto-generated method stub
		return idx;
	}

	@Override
	public View getView(int idx, View view, ViewGroup viewGroup) {
		View v = view;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_zone, null);
		}
		
		Map item = (Map) getItem(idx);
		
		ImageView icon = (ImageView) v.findViewById(R.id.iv_icon);
		if (item.containsKey("iconid")) {
			icon.setImageResource(Integer.parseInt(item.get("iconid").toString()));
		} else {
			icon.setImageResource(R.drawable.map);
		}

		TextView title = (TextView) v.findViewById(R.id.tv_title);
		title.setText("");
		if (item.containsKey("code")) {
			title.setText(item.get("code").toString() + " - " + item.get("code").toString());
		}
		
		TextView description = (TextView) v.findViewById(R.id.tv_description);
		description.setText("");
		if (item.containsKey("description")) {
			String desc = item.get("description").toString();
			description.setText(desc);
			v.setTag(R.string.description_tag, desc);
		}
		
		if (item.containsKey("objid")) {
			v.setTag(R.string.id_tag, item.get("objid"));
		}

		return v;
	}
	
	void println(String msg) {
		Log.i("ZoneListAdapter", msg);
	}

}
