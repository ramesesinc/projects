package com.rameses.clfc.android.system;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rameses.clfc.android.R;

public class SpinnerAdapter extends BaseAdapter 
{
	private Activity activity;
	private ArrayList<String> list;
	
	public SpinnerAdapter(Activity activity, ArrayList<String> list) {
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = view;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item_string, null);
		}
		
		TextView tv_str = (TextView) v.findViewById(R.id.tv_item_str);
		String str = list.get(index);
		tv_str.setText(str);
		
		return v;
	}

}
