package com.rameses.android.efaas.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rameses.android.R;
import com.rameses.android.db.BldgStructureDB;
import com.rameses.android.db.MaterialDB;
import com.rameses.android.db.StructureDB;
import com.rameses.android.efaas.FaasBuildingActivity;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.bean.BldgStructure;
import com.rameses.android.efaas.bean.DefaultItem;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class StructuralMaterialInfo extends AlertDialog.Builder {
	
	public boolean CANCELLED = false;
	private String objid, rpuid;
	private boolean done = false;
	private BldgStructure bs;
	private Context ctx;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Throwable error = null;
	private List<Map> data_structure, data_material;
	private Spinner structure, material;
	private EditText floorno;
	
	public StructuralMaterialInfo(Context ctx, BldgStructure bs){
		super(ctx);
		this.ctx = ctx;
		this.bs = bs;
		if(bs != null){
			this.objid = bs.getObjid();
			this.rpuid = bs.getBldgRpuId();
		}
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_materials, null);
		
		floorno = (EditText) view.findViewById(R.id.material_floorno);
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
			
			if(bs != null){
				floorno.setText(bs.getFloorNo());
				
				int pos = 1;
				for(Map m : data_structure){
					String structureid = bs.getStructureId();
					if(structureid != null) if(structureid.equals(m.get("objid").toString())) structure.setSelection(pos);
					pos++;
				}
				
				pos = 1;
				for(Map m : data_material){
					String materialid = bs.getMaterialId();
					if(materialid != null) if(materialid.equals(m.get("objid").toString())) material.setSelection(pos);
					pos++;
				}
			}
		}catch(Throwable e){
			new ErrorDialog(ctx, e).show();
			error = e;
		}
	}
	
	private void doSave(){
		try{
			Map structure_data = find((DefaultItem)structure.getSelectedItem(),data_structure);
			Map material_data = find((DefaultItem)material.getSelectedItem(),data_material);
			
			if(floorno.getText().toString().isEmpty()){
				new InfoDialog(ctx, "Floor No. is required!").show();
	    		return;
			}
			
			if(structure_data == null || structure.getSelectedItemPosition() == 0){
	    		new InfoDialog(ctx, "Structure is required!").show();
	    		return;
	    	}
			
			if(material_data == null || material.getSelectedItemPosition() == 0){
	    		new InfoDialog(ctx, "Material is required!").show();
	    		return;
	    	}
			
			Map params = new HashMap();
			params.put("bldgrpuid", rpuid);
			params.put("structure_objid", structure_data.get("objid"));
			params.put("material_objid", material_data.get("objid"));
			params.put("floor", floorno.getText());
			
			BldgStructureDB db = new BldgStructureDB();
			if(objid == null){
				params.put("objid", UUID.randomUUID().toString());
				db.create(params);
			}else{
				params.put("objid", objid);
				db.update(params);
			}
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
	
	private Map find(DefaultItem item, List<Map> data){
		Map m = null;
		for(Map map : data){
			String id = map.get("objid") != null ? map.get("objid").toString() : "";
			if(item.getObjid().equals(id)){
				return map;
			}
		}
		return m;
	}
	
	@Override
	public AlertDialog show(){
		dialog.show();
		if(error != null) new ErrorDialog(ctx, error).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				doSave();
				if(error == null){
					FaasBuildingActivity.loadMaterialInfo();
					dialog.dismiss();
				}
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
