package com.rameses.android;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rameses.android.R;

public class MenuAdapter extends BaseAdapter 
{
	private Context context;
	private ArrayList<Map<String, Object>> list;
	
	public MenuAdapter(Context context, ArrayList<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		View v = view;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_menu, null);
		}
		
		ImageView iv_icon =  (ImageView) v.findViewById(R.id.iv_icon);
		TextView tv_text = (TextView) v.findViewById(R.id.tv_text);
		TextView tv_subtext = (TextView) v.findViewById(R.id.tv_subtext);
		
		Map<String, Object> m = (Map<String, Object>) list.get(position);
		iv_icon.setImageResource(Integer.parseInt(m.get("iconid").toString()));
		tv_text.setText(m.get("text")+"");
		
		Object ov = m.get("subtext");
		tv_subtext.setText(ov==null? "": ov.toString());
		return v;
	}

}
