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
import com.rameses.util.MapProxy;

public class CapturePaymentAdapter extends BaseAdapter 
{
	private Activity activity;
	private List<Map> list;
	
	public CapturePaymentAdapter(Activity activity, List<Map> list) {
		this.activity = activity;
		this.list = list;
		System.out.println("[CapturePaymentAdapter]");
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
		View vw = view;
		if (vw == null) {
			vw = inflater.inflate(R.layout.item_string, null);
		}
		
		MapProxy proxy = new MapProxy((Map) list.get(position));
		((TextView) vw.findViewById(R.id.tv_item_str)).setText(proxy.getString("borrowername"));
		
		return vw;
//		MapProxy item = new MapProxy(list.get(position));
//
//		String type = item.getString("type");
//		if (vw == null) {
//			if ("route".equals(type)) {
//				vw = inflater.inflate(R.layout.item_route, null);
//			} else {
//				vw = inflater.inflate(R.layout.item_specialcollection, null);
//			}
//			((RelativeLayout) vw).addView(overlay);
//		}
//		
//		if ("REMITTED".equals(item.getString("state"))) {
//			((TextView) overlay).setTextColor(activity.getResources().getColor(R.color.green));
//			((TextView) overlay).setText("REMITTED");
//			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
//			overlay.setLayoutParams(layoutParams);
//			vw.setClickable(false);
//			
//		}
//		
//		if ("route".equals(type)) {
//			((TextView) vw.findViewById(R.id.tv_route_description)).setText(item.getString("description"));
//			((TextView) vw.findViewById(R.id.tv_route_area)).setText(item.getString("area"));
//		} else {
//			((TextView) vw.findViewById(R.id.tv_item_str)).setText(item.getString("description"));
//		}
//		return vw;
	}

}
