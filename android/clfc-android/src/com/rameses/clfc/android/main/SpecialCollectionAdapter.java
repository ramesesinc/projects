package com.rameses.clfc.android.main;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rameses.clfc.android.R;

public class SpecialCollectionAdapter extends BaseAdapter 
{
	private Activity activity;
	private List<Map> list;
	
	public SpecialCollectionAdapter(Activity activity, List<Map> list) {
		this.activity = activity;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = view;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_specialcollection, null);
		}
		
		Map item = (Map) list.get(position);
		((TextView) v.findViewById(R.id.tv_name)).setText(item.get("name").toString());
		((TextView) v.findViewById(R.id.tv_remarks)).setText(item.get("remarks").toString());

//		System.out.println("state -> "+item.get("state").toString());
		if (item.get("state").toString().equals("DOWNLOADED")) {
			v.setClickable(false);
			((ImageView) v.findViewById(R.id.iv_specialcollection_approved)).setImageResource(R.drawable.btn_check_on);
		}
		return v;
	}

}
