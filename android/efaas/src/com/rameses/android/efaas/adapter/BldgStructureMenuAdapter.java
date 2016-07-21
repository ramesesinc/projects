package com.rameses.android.efaas.adapter;

import java.util.List;
import java.util.Properties;

import com.rameses.android.R;
import com.rameses.android.db.MaterialDB;
import com.rameses.android.db.StructureDB;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.HomeListItem;
import com.rameses.android.efaas.bean.BldgStructure;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BldgStructureMenuAdapter extends BaseAdapter {
	
	LayoutInflater inflater = null;
	Context ctx;
	List<BldgStructure> data;
	
	public BldgStructureMenuAdapter(Activity activity, List<BldgStructure> data){
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
		View rowView = inflater.inflate(R.layout.list_menu_bldgstructure, null);
		
		TextView floor = (TextView) rowView.findViewById(R.id.bldgstructure_floorno);
		TextView structure = (TextView) rowView.findViewById(R.id.bldgstructure_structure);
		TextView material = (TextView) rowView.findViewById(R.id.bldgstructure_material);
		
		BldgStructure item = data.get(pos);
		if(item != null){
			floor.setText(item.getFloorNo());
			
			String structureid = item.getStructureId();
			if(structureid != null){
				try{
					StructureDB db = new StructureDB();
					Properties data = db.find(structureid);
					
					structure.setText(data.getProperty("name"));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			String materialid = item.getMaterialId();
			if(materialid != null){
				try{
					MaterialDB db = new MaterialDB();
					Properties data = db.find(materialid);
					
					material.setText(data.getProperty("name"));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return rowView;
	}
	
	public BldgStructure getListItem(int pos){
		return data.get(pos);
	}

}
