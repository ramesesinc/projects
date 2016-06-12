package com.rameses.android.efaas.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.rameses.android.R;
import com.rameses.android.db.LandAssessLevelDB;
import com.rameses.android.db.LandDetailDB;
import com.rameses.android.db.LcuvSpecificClassDB;
import com.rameses.android.db.LcuvStrippingDB;
import com.rameses.android.db.LcuvSubClassDB;
import com.rameses.android.db.PropertyClassificationDB;
import com.rameses.android.efaas.FaasActivity;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.bean.DefaultItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AppraisalInfo extends AlertDialog.Builder{

	public boolean CANCELLED = false;
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private Spinner subclass, specificclass, actualuse, strip;
	private EditText areasqm, areaha;
	private TextWatcher watcher_sqm, watcher_ha;
	private Throwable error = null;
	private List<Map> data_subclass, data_specificclass, data_actualuse, data_strip;
	private String rpuid;
	private boolean done = false;
	private Context ctx;
	
	public AppraisalInfo(Context ctx, String rpuid) {
		super(ctx);
		this.ctx = ctx;
		this.rpuid = rpuid;
		
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View view = inflater.inflate(R.layout.activity_appraisal, null);
		
		subclass = (Spinner) view.findViewById(R.id.appraisal_subclass);
		specificclass = (Spinner) view.findViewById(R.id.appraisal_specificclass);
		actualuse = (Spinner) view.findViewById(R.id.appraisal_actualuse);
		strip = (Spinner) view.findViewById(R.id.appraisal_strip);
		
		areasqm = (EditText) view.findViewById(R.id.appraisal_areasqm);
		areaha = (EditText) view.findViewById(R.id.appraisal_areaha);
		
		builder = new AlertDialog.Builder(ctx);
		builder.setTitle("Appraisal Info");
		builder.setView(view);
		builder.setPositiveButton("Save", null);
		builder.setNegativeButton("Cancel", null);
		dialog = builder.create();
		
		loadData();
		
		watcher_sqm = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String s = areasqm.getText().toString();
				if(!s.isEmpty()){
					double val = Double.parseDouble(s);
					areaha.removeTextChangedListener(watcher_ha);
					areaha.setText(String.format("%.4f", val/10000));
					areaha.addTextChangedListener(watcher_ha);
				}
			}	   
		};
		
		watcher_ha = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String s = areaha.getText().toString();
				if(!s.isEmpty()){
					double val = Double.parseDouble(s);
					areasqm.removeTextChangedListener(watcher_sqm);
					areasqm.setText(String.valueOf(val*10000));
					areasqm.addTextChangedListener(watcher_sqm);
				}
			}	   
		};
		
		areasqm.addTextChangedListener(watcher_sqm);
		areaha.addTextChangedListener(watcher_ha);
	}
	
	private void loadData(){
		try{
			data_subclass = new LcuvSubClassDB().getList(new HashMap());
			data_specificclass = new LcuvSpecificClassDB().getList(new HashMap());
			data_actualuse = new LandAssessLevelDB().getList(new HashMap());
			data_strip = new LcuvStrippingDB().getList(new HashMap());
			
			subclass.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_subclass,"code","name")));
			specificclass.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_specificclass,"name")));
			actualuse.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_actualuse,"name")));
			strip.setAdapter(new AppraisalItemAdapter(ctx,android.R.layout.simple_spinner_item,filter(data_strip,"classification_objid","rate")));
			
			subclass.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
	            	DefaultItem item = (DefaultItem) adapterView.getAdapter().getItem(position);
	            }
	            @Override
	            public void onNothingSelected(AdapterView<?> adapter) {  }
	        });
			specificclass.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
	            	DefaultItem item = (DefaultItem) adapterView.getAdapter().getItem(position);
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
	
	private void doSave(){
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
    	
    	if(stripping_data == null || strip.getSelectedItemPosition() == 0){
    		new InfoDialog(ctx, "Strip is required!").show();
    		return;
    	}
    	
    	if(areasqm.getText().toString().isEmpty()){
    		new InfoDialog(ctx, "Area (sqm) is required!").show();
    		return;
    	}
    	
    	if(areaha.getText().toString().isEmpty()){
    		new InfoDialog(ctx, "Area (ha) is required!").show();
    		return;
    	}
    	
        Map data = new HashMap();
        data.put("objid", UUID.randomUUID().toString());
        data.put("landrpuid", rpuid);
        data.put("subclass_objid", subclass_data.get("objid").toString());
        data.put("specificclass_objid", specificclass_data.get("objid").toString());
        data.put("actualuse_objid", actualuse_data.get("objid").toString());
        data.put("stripping_objid", stripping_data.get("objid").toString());
        data.put("striprate", stripping_data.get("rate").toString());
        data.put("areatype", "");
        data.put("area", "");
        data.put("areasqm", areasqm.getText().toString());
        data.put("areaha", areaha.getText().toString());
        
        try{
        	LandDetailDB db = new LandDetailDB();
        	db.create(data);
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
				doSave();
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
