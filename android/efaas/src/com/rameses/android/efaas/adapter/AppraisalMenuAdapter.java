package com.rameses.android.efaas.adapter;

import java.util.List;

import com.rameses.android.R;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.FaasListItem;
import com.rameses.android.efaas.bean.HomeListItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppraisalMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<AppraisalListItem> data;
	
	public AppraisalMenuAdapter(Activity activity, List<AppraisalListItem> data){
		ctx = activity;
		this.data = data;
		inflater = ( LayoutInflater )ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int pos) {
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View view, ViewGroup vgroup) {
		View rowView = inflater.inflate(R.layout.list_menu_appraisal, null);
		
		TextView subclass = (TextView) rowView.findViewById(R.id.appraisal_subclass);
		TextView specificclass = (TextView) rowView.findViewById(R.id.appraisal_specificclass);
		TextView actualuse = (TextView) rowView.findViewById(R.id.appraisal_actualuse);
		TextView strip = (TextView) rowView.findViewById(R.id.appraisal_strip);
		TextView area = (TextView) rowView.findViewById(R.id.appraisal_areasqm);
		
		AppraisalListItem item = data.get(pos);
		if(item != null){
			subclass.setText(item.getSubClass());
			specificclass.setText(item.getSpecificClass());
			actualuse.setText(item.getActualUse());
			strip.setText(item.getRate());
			area.setText(item.getArea());
		}
		
		return rowView;
	}
	
	public AppraisalListItem getListItem(int pos){
		return data.get(pos);
	}

}
