package com.rameses.clfc.android.main;

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

public class RouteAdapter extends BaseAdapter 
{
	private Activity activity;
	private List<Map> list;
	
	public RouteAdapter(Activity activity, List<Map> list) {
		this.activity = activity;
		this.list = list;
		System.out.println("[RouteAdapter]");
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
		System.out.println("pass 0.1");
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View overlay = inflater.inflate(R.layout.overlay_void_text, null);
		View vw = view;
		System.out.println("pass 0.2");
		if (vw == null) {
			System.out.println("pass 0.3");
			vw = inflater.inflate(R.layout.item_route, null);
			((RelativeLayout) vw).addView(overlay);
		}
		System.out.println("pass 0.4");
		
		MapProxy item = new MapProxy(list.get(position));
//		System.out.println("description -> "+item.getString("description")+" state -> "+item.getString("state"));
		System.out.println("pass 1");
		if ("REMITTED".equals(item.getString("state"))) {
			System.out.println("pass 2");
			((TextView) overlay).setTextColor(activity.getResources().getColor(R.color.green));
			((TextView) overlay).setText("REMITTED");
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
			overlay.setLayoutParams(layoutParams);
			vw.setClickable(false);
		} else if ("1".equals(item.getString("downloaded"))) {
			System.out.println("pass 3");
			((TextView) overlay).setTextColor(activity.getResources().getColor(R.color.red));
			((TextView) overlay).setText("DOWNLOADED");
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
			overlay.setLayoutParams(layoutParams);
			vw.setClickable(false);
		}
		System.out.println("pass 4");
		((TextView) vw.findViewById(R.id.tv_route_description)).setText(item.getString("description"));
		((TextView) vw.findViewById(R.id.tv_route_area)).setText(item.getString("area"));
		return vw;
	}

}
