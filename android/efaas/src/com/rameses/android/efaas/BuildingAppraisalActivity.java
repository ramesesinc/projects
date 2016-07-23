package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.BldgAssessLevelDB;
import com.rameses.android.db.BldgKindDB;
import com.rameses.android.db.BldgTypeDB;
import com.rameses.android.db.PropertyClassificationDB;
import com.rameses.android.efaas.adapter.AppraisalItemAdapter;
import com.rameses.android.efaas.bean.DefaultItem;
import com.rameses.android.efaas.dialog.ActualUseInfo;
import com.rameses.android.efaas.dialog.ErrorDialog;

public class BuildingAppraisalActivity extends ControlActivity{
	
	public boolean isCloseable() { return false; }
	
	private Spinner classification, structuraltype, bldgkind;
	private EditText basefloorarea, floorarea;
	private Button actualuse_add;
	private List<Map> data_classification, data_structuraltype, data_bldgkind;
	private Activity activity;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.activity_appraisal_bldg);
		ApplicationUtil.changeTitle(this, "Building Appraisal");
		this.activity = this;
		
		classification = (Spinner) findViewById(R.id.appraisal_classification);
		structuraltype = (Spinner) findViewById(R.id.appraisal_structuraltype);
		bldgkind = (Spinner) findViewById(R.id.appraisal_buildingkind);
		basefloorarea = (EditText) findViewById(R.id.appraisal_basefloorarea);
		floorarea = (EditText) findViewById(R.id.appraisal_floorarea);
		
		actualuse_add = (Button) findViewById(R.id.actualuse_add);
		actualuse_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	ActualUseInfo info = new ActualUseInfo(activity, null, null);
            	info.show();
            }
        });
		
		loadData();
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	private void loadData(){
		try{
			data_classification = new PropertyClassificationDB().getList(new HashMap());
			data_structuraltype = new BldgTypeDB().getList(new HashMap());
			data_bldgkind = new BldgKindDB().getList(new HashMap());
			
			classification.setAdapter(new AppraisalItemAdapter(this,android.R.layout.simple_spinner_item,filter(data_classification,"code","name")));
			structuraltype.setAdapter(new AppraisalItemAdapter(this,android.R.layout.simple_spinner_item,filter(data_structuraltype,"code","name")));
			bldgkind.setAdapter(new AppraisalItemAdapter(this,android.R.layout.simple_spinner_item,filter(data_bldgkind,"code","name")));
		}catch(Throwable e){
			new ErrorDialog(this, e).show();
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

}
