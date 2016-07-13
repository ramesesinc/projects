package com.rameses.android.efaas.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rameses.android.R;
import com.rameses.android.db.MaterialDB;
import com.rameses.android.db.StructureDB;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.bean.DefaultItem;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

public class StructuralMaterialInfo extends AlertDialog.Builder{
	
	public boolean CANCELLED = false;
	private String objid, rpuid;
	private boolean done = false;
	private Context ctx;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Throwable error = null;
	private List<Map> data_structure, data_material;
	private Spinner structure, material;
	
	public StructuralMaterialInfo(Context ctx, String objid, String rpuid){
		super(ctx);
		this.ctx = ctx;
		this.objid = objid;
		this.rpuid = rpuid;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_materials, null);
		
		structure = (Spinner) view.findViewById(R.id.material_structure);
		material = (Spinner) view.findViewById(R.id.material_material);
		
		builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Structural Material Info");
		builder.setView(view);
		builder.setNegativeButton("Cancel", null);
		if(this.objid == null){
			builder.setPositiveButton("Save", null);
		}else{
			builder.setPositiveButton("Update", null);
		}
		
		dialog = builder.create();
		
		loadData();
	}
	
	private void loadData(){
		try{
			data_structure = new StructureDB().getList(new HashMap());
			data_material = new MaterialDB().getList(new HashMap());
			
			structure.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_structure,"code","name")));
			material.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_material,"code","name")));
		}catch(Throwable e){
			new ErrorDialog(ctx, e).show();
			error = e;
		}
	}
	
	private List<DefaultItem> filter(List<Map> data, String...fieldname ){
		List<DefaultItem> list = new ArrayList<DefaultItem>();
		list.add(new DefaultItem("0","NO ITEM SELECTED"));
		if(data == null) return list;
		for(Map m : data){
			String name = "";
			for(String fn : fieldname){
				for(Object o : m.entrySet()){
					Map.Entry me = (Map.Entry) o;
					if(me.getKey().equals(fn)) name += me.getValue() + "   ";
				}
			}
			String objid = m.get("objid") != null ? m.get("objid").toString() : "";
			if(!name.isEmpty()) list.add(new DefaultItem(objid,name));
		}
		return list;
	}
	
	@Override
	public AlertDialog show(){
		dialog.show();
		if(error != null) new ErrorDialog(ctx, error).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {

			}
		});
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				CANCELLED = true;
				dialog.dismiss();
			}
		});
		AlertDialog d =  this.create();
		d.setCancelable(false);
		return d;
	}

}
