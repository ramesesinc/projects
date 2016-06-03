package com.rameses.android.efaas.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.rameses.android.R;
import com.rameses.android.db.LcuvSpecificClassDB;
import com.rameses.android.db.LcuvStrippingDB;
import com.rameses.android.db.LcuvSubClassDB;
import com.rameses.android.db.PropertyClassificationDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AppraisalInfo {
	
	private Activity activity;
	private AlertDialog.Builder dialog;
	private Spinner subclass, specificclass, actualuse, strip;
	private EditText areasqm, areaha;
	private Button save;
	private TextWatcher watcher_sqm, watcher_ha;
	private Throwable error = null;
	
	public AppraisalInfo(Activity activity){
		this.activity = activity;
		
		LayoutInflater inflater = LayoutInflater.from(activity);
		View view = inflater.inflate(R.layout.activity_appraisal, null);
		
		subclass = (Spinner) view.findViewById(R.id.appraisal_subclass);
		specificclass = (Spinner) view.findViewById(R.id.appraisal_specificclass);
		actualuse = (Spinner) view.findViewById(R.id.appraisal_actualuse);
		strip = (Spinner) view.findViewById(R.id.appraisal_strip);
		
		areasqm = (EditText) view.findViewById(R.id.appraisal_areasqm);
		areaha = (EditText) view.findViewById(R.id.appraisal_areaha);
		
		save = (Button) view.findViewById(R.id.appraisal_save);
		save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
            }
        });
		
		dialog = new AlertDialog.Builder(activity);
		dialog.setTitle("Appraisal Info");
		dialog.setView(view);
		
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
			List<Map> data_subclass = new LcuvSubClassDB().getList(new HashMap());
			List<Map> data_specificclass = new LcuvSpecificClassDB().getList(new HashMap());
			List<Map> data_actualuse = new PropertyClassificationDB().getList(new HashMap());
			List<Map> data_strip = new LcuvStrippingDB().getList(new HashMap());
			
			Log.v("data_subclass", data_subclass.toString());
			Log.v("data_specificclass", data_specificclass.toString());
			Log.v("data_actualuse", data_actualuse.toString());
			Log.v("data_strip", data_strip.toString());
			
			subclass.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,filter(data_subclass,"code","name")));
			specificclass.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,filter(data_specificclass,"name")));
			actualuse.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,filter(data_actualuse,"name")));
			strip.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item,filter(data_strip,"classification_objid","rate")));
		}catch(Throwable e){
			e.printStackTrace();
			error = e;
		}
	}
	
	private List<String> filter(List<Map> data, String...fieldname ){
		List<String> list = new ArrayList<String>();
		list.add("NO ITEM SELECTED");
		if(data == null) return list;
		for(Map m : data){
			String name = "";
			for(String fn : fieldname){
				for(Object o : m.entrySet()){
					Map.Entry me = (Map.Entry) o;
					if(me.getKey().equals(fn)) name += me.getValue() + "   ";
				}
			}
			if(!name.isEmpty()) list.add(name);
		}
		return list;
	}
	
	public void show(){
		dialog.show();
		if(error != null) new ErrorDialog(activity, error).show();
	}

}
