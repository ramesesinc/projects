package com.rameses.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rameses.android.R;

public class StuboutListAdapter extends BaseAdapter {

	private Context context;
	private List<Map> list = new ArrayList<Map>();
	
	public StuboutListAdapter(Context context, List<Map> list) {
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
			v = inflater.inflate(R.layout.item_stubout, null);
		}
		
		Map item = (Map) getItem(idx);

		ImageView icon = (ImageView) v.findViewById(R.id.iv_icon);
		if (item.containsKey("iconid")) {
			icon.setImageResource(Integer.parseInt(item.get("iconid").toString()));
		} else {
			icon.setImageResource(R.drawable.map);
		}

		TextView description = (TextView) v.findViewById(R.id.tv_description);
		description.setText("");
		if (item.containsKey("code")) {
			String code = item.get("code").toString();
			description.setText(code);
			v.setTag(R.string.code_tag, code);
		}

		TextView barangay = (TextView) v.findViewById(R.id.tv_barangay);
		barangay.setText("");
		if (item.containsKey("description")) {
			barangay.setText(item.get("description").toString());
		}
		
		if (item.containsKey("objid")) {
			v.setTag(R.string.id_tag, item.get("objid").toString());
		}
		
		return v;
	}

}
