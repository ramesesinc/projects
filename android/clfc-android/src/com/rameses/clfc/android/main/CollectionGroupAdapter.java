package com.rameses.clfc.android.main;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.R;
import com.rameses.util.MapProxy;

public class CollectionGroupAdapter extends BaseAdapter 
{
	private Activity activity;
	private List<Map> list;
	private SimpleDateFormat outputFormat = new SimpleDateFormat("MMM-dd-yyyy");
	
	public CollectionGroupAdapter(Activity activity, List<Map> list) {
		this.activity = activity;
		this.list = list;
		System.out.println("[CollectionGroupAdapter]");
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
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View overlay = inflater.inflate(R.layout.overlay_void_text, null);
		View vw = view;
		
		System.out.println("item " + list.get(position));
		MapProxy item = new MapProxy(list.get(position));

		if (vw == null) {
			System.out.println("null position " + position);
			vw = inflater.inflate(R.layout.item_route, null);
//			if ("route".equals(type)) {
//			} else {
//				vw = inflater.inflate(R.layout.item_string, null);
//			}
			((RelativeLayout) vw).addView(overlay);
		}
		
		if ("REMITTED".equals(item.getString("state"))) {
			((TextView) overlay).setTextColor(activity.getResources().getColor(R.color.green));
			((TextView) overlay).setText("REMITTED");
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
			overlay.setLayoutParams(layoutParams);
			vw.setClickable(false);
		}

		TextView description = (TextView) vw.findViewById(R.id.tv_route_description);
		TextView area = (TextView) vw.findViewById(R.id.tv_route_area);
		TextView billdate = (TextView) vw.findViewById(R.id.tv_route_billdate);
		 
		description.setText(item.getString("description"));
//		System.out.println("billdate " + item.getString("billdate"));
		billdate.setText(outputFormat.format(java.sql.Date.valueOf(item.getString("billdate"))));

		if (position != (list.size() - 1)) {
			RelativeLayout rl = (RelativeLayout) vw;
			rl.removeViewInLayout(vw.findViewById(R.id.view1)); 
		}
		if ("route".equals(item.getString("type"))) {
//			System.out.println("position " + position + " route " + ((Map) list.get(position)).get("area").toString());
			area.setText(item.getString("area"));
			area.setVisibility(View.VISIBLE);
		} else {
//			System.out.println("position " + position);
			area.setVisibility(View.GONE);
		}
		
//		if ("route".equals(type)) {
//			((TextView) vw.findViewById(R.id.tv_route_description)).setText(item.getString("description"));
//			((TextView) vw.findViewById(R.id.tv_route_area)).setText(item.getString("area"));
//		} else {
//			((TextView) vw.findViewById(R.id.tv_item_str)).setText(item.getString("description"));
//		}
		return vw;
	}

}
