package com.rameses.android.efaas.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import com.rameses.android.R;
import com.rameses.android.db.LandAssessLevelDB;
import com.rameses.android.db.LandDetailDB;
import com.rameses.android.db.LcuvSpecificClassDB;
import com.rameses.android.db.LcuvStrippingDB;
import com.rameses.android.db.LcuvSubClassDB;
import com.rameses.android.efaas.FaasActivity;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.bean.DefaultItem;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AppraisalInfo extends AlertDialog.Builder{

	public boolean CANCELLED = false;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Spinner subclass, specificclass, actualuse, strip, areatype;
	private EditText area, unitvalue;
	private Throwable error = null;
	private List<Map> data_subclass, data_specificclass, data_actualuse, data_strip;
	private List<DefaultItem> data_areatype;
	private String objid, rpuid;
	private boolean done = false;
	private Context ctx;
	
	public AppraisalInfo(Context ctx, String objid, String rpuid) {
		super(ctx);
		this.ctx = ctx;
		this.objid = objid;
		this.rpuid = rpuid;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_appraisal, null);
		
		subclass = (Spinner) view.findViewById(R.id.appraisal_subclass);
		specificclass = (Spinner) view.findViewById(R.id.appraisal_specificclass);
		actualuse = (Spinner) view.findViewById(R.id.appraisal_actualuse);
		strip = (Spinner) view.findViewById(R.id.appraisal_strip);
		areatype = (Spinner) view.findViewById(R.id.appraisal_areatype);
		
		area = (EditText) view.findViewById(R.id.appraisal_area);
		unitvalue = (EditText) view.findViewById(R.id.appraisal_unitvalue);
		
		builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Appraisal Info");
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
			data_subclass = new LcuvSubClassDB().getList(new HashMap());
			data_specificclass = new LcuvSpecificClassDB().getList(new HashMap());
			data_actualuse = new LandAssessLevelDB().getList(new HashMap());
			data_strip = new LcuvStrippingDB().getList(new HashMap());
			
			data_areatype = new ArrayList<DefaultItem>();
			data_areatype.add(new DefaultItem("0","NO ITEM SELECTED"));
			data_areatype.add(new DefaultItem("1","SQM"));
			data_areatype.add(new DefaultItem("2","HA"));
			
			subclass.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_subclass,"code","name")));
			specificclass.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_specificclass,"name")));
			actualuse.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_actualuse,"name")));
			strip.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_strip,"classification_objid","rate")));
			areatype.setAdapter(new AppraisalItemAdapter(ctx, android.R.layout.simple_spinner_item,data_areatype));
			
			subclass.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
	            	DefaultItem item = (DefaultItem) adapterView.getAdapter().getItem(position);
	            	String subclassid = item.getObjid();
	            	Map params = new HashMap();
	            	params.put("objid", subclassid);
	            	try{
	            		Properties prop = new LcuvSubClassDB().find(params);
	            		if(prop != null){
	            			String val = prop.getProperty("unitvalue");
		            		unitvalue.setText(val != null ? val : "0.00");
	            		}
	            	}catch(Throwable t){
	            		new ErrorDialog(ctx, t).show();
	            	}
	            	loadComponentData();
	            }
	            @Override
	            public void onNothingSelected(AdapterView<?> adapter) {  }
	        });
			specificclass.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
	            	DefaultItem item = (DefaultItem) adapterView.getAdapter().getItem(position);
	            	String specificclassid = item.getObjid();
	            	try{
	            		Map params1 = new HashMap();
		            	params1.put("specificclass_objid", specificclassid);
	            		data_subclass = new LcuvSubClassDB().getList(params1);
	            		subclass.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_subclass,"code","name")));
	            		
	            		Map params2 = new HashMap();
	            		params2.put("objid", specificclassid);
	            		Properties prop = new LcuvSpecificClassDB().find(params2);
	            		if(prop != null){
	            			String type = prop.getProperty("areatype");
		            		if(type.equalsIgnoreCase("sqm")){
		            			areatype.setSelection(1);
		            		}
		            		if(type.equalsIgnoreCase("ha")){
		            			areatype.setSelection(2);
		            		}
		            		Map params3 = new HashMap();
		            		params3.put("classification_objid", prop.getProperty("classification_objid"));
		            		data_strip = new LcuvStrippingDB().getList(params3);
		            		strip.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_strip,"classification_objid","rate")));
	            		}
	            	}catch(Throwable t){
	            		new ErrorDialog(ctx, t).show();
	            	}
	            	loadComponentData();
	            }
	            @Override
	            public void onNothingSelected(AdapterView<?> adapter) {  }
	        });
			actualuse.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
	            	DefaultItem item = (DefaultItem) adapterView.getAdapter().getItem(position);
	            }
	            @Override
	            public void onNothingSelected(AdapterView<?> adapter) {  }
	        });
			strip.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
	            	DefaultItem item = (DefaultItem) adapterView.getAdapter().getItem(position);
	            }
	            @Override
	            public void onNothingSelected(AdapterView<?> adapter) {  }
	        });
			
			loadComponentData();
		}catch(Throwable e){
			new ErrorDialog(ctx, e).show();
			error = e;
		}
	}
	
	private void loadComponentData(){
		if(objid != null){
			try{
				Map params = new HashMap();
				params.put("objid", objid);
				
				LandDetailDB db = new LandDetailDB();
				Properties prop = db.find(params);
				
				int pos = 1;
				for(Map m : data_subclass){
					if(m.get("objid").toString().equals(prop.getProperty("subclass_objid"))){
						subclass.setSelection(pos);
					}
					pos++;
				}
				
				pos = 1;
				for(Map m : data_specificclass){
					if(m.get("objid").toString().equals(prop.getProperty("specificclass_objid"))){
						specificclass.setSelection(pos);
					}
					pos++;
				}
				
				pos = 1;
				for(Map m : data_actualuse){
					if(m.get("objid").toString().equals(prop.getProperty("actualuse_objid"))){
						actualuse.setSelection(pos);
					}
					pos++;
				}
				
				pos = 1;
				for(Map m : data_strip){
					if(m.get("objid").toString().equals(prop.getProperty("stripping_objid"))){
						strip.setSelection(pos);
					}
					pos++;
				}
				
				unitvalue.setText(prop.getProperty("unitvalue"));
				area.setText(prop.getProperty("area"));
			}catch(Throwable e){
				new ErrorDialog(ctx, e).show();
				error = e;
			}
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
	
	private void doCreate(){
		Map subclass_data = find((DefaultItem)subclass.getSelectedItem(),data_subclass);
		Map specificclass_data = find((DefaultItem)specificclass.getSelectedItem(),data_specificclass);
    	Map actualuse_data = find((DefaultItem)actualuse.getSelectedItem(),data_actualuse);
    	Map stripping_data = find((DefaultItem)strip.getSelectedItem(),data_strip);
    	
    	if(subclass_data == null || subclass.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Subclass is required!").show();
    		return;
    	}
    	
    	if(specificclass_data == null || specificclass.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Specific Class is required!").show();
    		return;
    	}
    	
    	if(actualuse_data == null || actualuse.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Actual Use is required!").show();
    		return;
    	}
    	
    	if(areatype.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Area Type is required!").show();
    		return;
    	}
    	
    	if(area.getText().toString().isEmpty()){
    		new InfoDialog(ctx, "Area is required!").show();
    		return;
    	}
    	
    	String areaType = "";
    	double areasqm = 0.00, areaha = 0.00;
    	double areaval = Double.parseDouble(area.getText().toString());
    	
    	if(areatype.getSelectedItemPosition() == 1){
    		areaType = "sqm";
    		areasqm = areaval;
    		areaha = areaval / 10000;
    	}
    	if(areatype.getSelectedItemPosition() == 2){
    		areaType = "ha";
    		areasqm = areaval * 10000;
    		areaha = areaval;
    	}
    	
        Map data = new HashMap();
        data.put("objid", UUID.randomUUID().toString());
        data.put("landrpuid", rpuid);
        data.put("subclass_objid", subclass_data.get("objid").toString());
        data.put("specificclass_objid", specificclass_data.get("objid").toString());
        data.put("specificclass_areatype", specificclass_data.get("areatype").toString());
        data.put("specificclass_classification_objid", specificclass_data.get("classification_objid").toString());
        data.put("actualuse_objid", actualuse_data.get("objid").toString());
        data.put("actualuse_code", actualuse_data.get("code").toString());
        data.put("actualuse_name", actualuse_data.get("name").toString());
        data.put("actualuse_classification_objid", actualuse_data.get("classification_objid").toString());
        data.put("stripping_objid", stripping_data != null ? stripping_data.get("objid") : "");
        data.put("stripping_rate", stripping_data != null ? stripping_data.get("rate") : "0");
        data.put("striprate", stripping_data != null ? stripping_data.get("rate") : "0");
        data.put("areatype", areaType);
        data.put("area", area.getText().toString());
        data.put("areasqm", String.valueOf(areasqm));
        data.put("areaha", String.valueOf(areaha));
        data.put("basevalue", unitvalue.getText().toString());
        data.put("unitvalue", unitvalue.getText().toString());
        data.put("taxable", "1");
        data.put("basemarketvalue", "0.00");
        data.put("adjustment", "0.00");
        data.put("landvalueadjustment", "0.00");
        data.put("actualuseadjustment", "0.00");
        data.put("marketvalue", "0.00");
        data.put("assesslevel", "0.00");
        data.put("assessedvalue", "0.00");
        
        try{
        	LandDetailDB db = new LandDetailDB();
        	db.create(data);
        }catch(Throwable t){
        	new ErrorDialog(ctx, t).show();
        	return;
        }
        done = true;
	}
	
	private void doUpdate(){
		Map subclass_data = find((DefaultItem)subclass.getSelectedItem(),data_subclass);
		Map specificclass_data = find((DefaultItem)specificclass.getSelectedItem(),data_specificclass);
    	Map actualuse_data = find((DefaultItem)actualuse.getSelectedItem(),data_actualuse);
    	Map stripping_data = find((DefaultItem)strip.getSelectedItem(),data_strip);
    	
    	if(subclass_data == null || subclass.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Subclass is required!").show();
    		return;
    	}
    	
    	if(specificclass_data == null || specificclass.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Specific Class is required!").show();
    		return;
    	}
    	
    	if(actualuse_data == null || actualuse.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Actual Use is required!").show();
    		return;
    	}
    	
    	if(areatype.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Area Type is required!").show();
    		return;
    	}
    	
    	if(area.getText().toString().isEmpty()){
    		new InfoDialog(ctx, "Area is required!").show();
    		return;
    	}
    	
    	String areaType = "";
    	double areasqm = 0.00, areaha = 0.00;
    	double areaval = Double.parseDouble(area.getText().toString());
    	
    	if(areatype.getSelectedItemPosition() == 1){
    		areaType = "sqm";
    		areasqm = areaval;
    		areaha = areaval / 10000;
    	}
    	if(areatype.getSelectedItemPosition() == 2){
    		areaType = "ha";
    		areasqm = areaval * 10000;
    		areaha = areaval;
    	}

        Map data = new HashMap();
        data.put("objid", objid);
        data.put("landrpuid", rpuid);
        data.put("subclass_objid", subclass_data.get("objid").toString());
        data.put("specificclass_objid", specificclass_data.get("objid").toString());
        data.put("specificclass_areatype", specificclass_data.get("areatype").toString());
        data.put("specificclass_classification_objid", specificclass_data.get("classification_objid").toString());
        data.put("actualuse_objid", actualuse_data.get("objid").toString());
        data.put("actualuse_code", actualuse_data.get("code").toString());
        data.put("actualuse_name", actualuse_data.get("name").toString());
        data.put("actualuse_classification_objid", actualuse_data.get("classification_objid").toString());
        data.put("stripping_objid", stripping_data != null ? stripping_data.get("objid") : null);
        data.put("stripping_rate", stripping_data != null ? stripping_data.get("rate") : "0");
        data.put("striprate", stripping_data != null ? stripping_data.get("rate") : "0.00");
        data.put("areatype", areaType);
        data.put("area", area.getText().toString());
        data.put("areasqm", String.valueOf(areasqm));
        data.put("areaha", String.valueOf(areaha));
        data.put("basevalue", unitvalue.getText().toString());
        data.put("unitvalue", unitvalue.getText().toString());
        
        try{
        	LandDetailDB db = new LandDetailDB();
        	db.update(data);
        }catch(Throwable t){
        	new ErrorDialog(ctx, t).show();
        	return;
        }
        done = true;
	}
	
	@Override
	public AlertDialog show(){
		dialog.show();
		if(error != null) new ErrorDialog(ctx, error).show();
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(objid == null){
					doCreate();
				}else{
					doUpdate();
				}
				if(done){
					dialog.dismiss();
					FaasActivity.initData();
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
