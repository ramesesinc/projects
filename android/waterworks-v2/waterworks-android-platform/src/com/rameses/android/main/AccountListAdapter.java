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
import com.rameses.util.MapProxy;

public class AccountListAdapter extends BaseAdapter {

	private Context context;
	private List<Map> list = new ArrayList<Map>();
	
	public AccountListAdapter(Context context, List<Map> list) {
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
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int idx, View view, ViewGroup vg) {
		View v = view;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_account, null);
		}
		
		Map item = (Map) getItem(idx);
		v.setTag(R.string.index_tag, idx);
		
//		println("readingid " + MapProxy.getString(item, "readingid"));

		String readingid = MapProxy.getString(item, "readingid");
		ImageView icon = (ImageView) v.findViewById(R.id.iv_icon);
		if (readingid != null) {
			icon.setImageResource(R.drawable.check);
		} else {
			if (item.containsKey("iconid")) {
				icon.setImageResource(Integer.parseInt(item.get("iconid").toString()));
			} else {
				icon.setImageResource(R.drawable.user);
			}
		}

		TextView acctno = (TextView) v.findViewById(R.id.tv_acctno);
		acctno.setText("");
		if (item.containsKey("serialno")) {
			acctno.setText(item.get("serialno").toString());
		}

		TextView name = (TextView) v.findViewById(R.id.tv_name);
		name.setText("");
		if (item.containsKey("acctname")) {
			name.setText(item.get("acctname").toString());
		}

		TextView address = (TextView) v.findViewById(R.id.tv_address);
		address.setText("");
		if (item.containsKey("address")) {
			address.setText(item.get("address").toString());
		}
		
		if (item.containsKey("objid")) {
			v.setTag(R.string.id_tag, item.get("objid").toString());
		}
		
		
//		if ((idx - 1) > 0) {
//			item = (Map) getItem(idx - 1);
//			if (item.containsKey("objid")) {
//				v.setTag(R.string.previd_tag, item.get("objid").toString());
//			}
//		}
//		
//		if ((idx + 1) < getCount()) {
//			item = (Map) getItem(idx + 1);
//			if (item.containsKey("objid")) {
//				v.setTag(R.string.nextid_tag, item.get("objid").toString());
//			}
//		}
		
		
		return v;
	}
	
	void println(String str) {
		Log.i("AccountListAdapter", str);
	}

}
