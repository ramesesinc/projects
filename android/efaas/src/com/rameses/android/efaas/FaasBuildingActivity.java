package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.BldgRpuDB;
import com.rameses.android.db.ExaminationDB;
import com.rameses.android.db.FaasDB;
import com.rameses.android.efaas.adapter.ExaminationMenuAdapter;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.efaas.dialog.StructuralMaterialInfo;

public class FaasBuildingActivity extends ControlActivity {
	
	public boolean isCloseable() { return false; }
	
	private static TextView tdno, pin, owner, address;
	private static TextView bldgage, effectiveage, depreciationpercentage, depreciationvalue;
	private static TextView floorcount, percentcompleted, datecompleted, dateoccupied;
	private static TextView permitno, issuancedate;
	private static Button material_add, appraisal_add, examination_add;
	private static ListView material_list, examination_list;
	
	public static Activity activity;
	private static Properties faas = null;
	private static Properties bldgrpu = null;
	private static String faasid;
	private static String rpuid;
	private static Properties examination;
	private static List<ImageItem> data_image;
	private static List<AppraisalListItem> data_appraisal;
	private static List<ExaminationListItem> data_examination;
	private static int ctxMenuId;
	
	private static ListView menu;
	private static ScrollView contentView;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		setContentView(R.layout.activity_faas);
		faasid = getIntent().getExtras().getString("faasid");
		
		contentView = (ScrollView) findViewById(R.id.faas_contentview);
		menu = (ListView) findViewById(R.id.faas_menu);
		
		final List<String> data_menu = new ArrayList<String>();
		data_menu.add("General Information");
		data_menu.add("Structural Materials");
		data_menu.add("Building Appraisal");
		data_menu.add("Examination Information");
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data_menu);
        menu.setAdapter(arrayAdapter); 
        menu.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos, long arg3) {
				contentView.removeAllViews();
				if(pos == 0){
					loadGeneralInfo();
				}
				if(pos == 1){
					loadMaterialInfo();
				}
				if(pos == 2){
					loadAppraisalInfo();
				}
				if(pos == 3){
					loadExaminationInfo();
				}
			}	
		});
        
        loadGeneralInfo();
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	public static void loadGeneralInfo(){
		View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.activity_faas_bldg_general, contentView, false);
	    contentView.addView(view);
	    
		tdno = (TextView) view.findViewById(R.id.faas_tdno);
		pin = (TextView) view.findViewById(R.id.faas_pin);
		owner = (TextView) view.findViewById(R.id.faas_owner);
		address = (TextView) view.findViewById(R.id.faas_address);
		bldgage = (TextView) view.findViewById(R.id.faas_buildingage);
		effectiveage = (TextView) view.findViewById(R.id.faas_effectivegage);
		depreciationpercentage = (TextView) view.findViewById(R.id.faas_depreciationpercentage);
		depreciationvalue = (TextView) view.findViewById(R.id.faas_depreciationvalue);
		floorcount = (TextView) view.findViewById(R.id.faas_floorcount);
		percentcompleted = (TextView) view.findViewById(R.id.faas_percentcompleted);
		datecompleted = (TextView) view.findViewById(R.id.faas_datecompleted);
		dateoccupied = (TextView) view.findViewById(R.id.faas_dateoccupied);
		permitno = (TextView) view.findViewById(R.id.faas_permitno);
		issuancedate = (TextView) view.findViewById(R.id.faas_permitissuedate);
		
		try{
			FaasDB db = new FaasDB();
			faas = db.find(faasid);
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
		
		if(faas != null){
			ApplicationUtil.changeTitle(activity, faas.getProperty("owner_name"));
			
			rpuid = faas.getProperty("rpuid");
			
			tdno.setText(faas.getProperty("tdno"));
			pin.setText(faas.getProperty("fullpin"));
			owner.setText(faas.getProperty("owner_name"));
			address.setText(faas.getProperty("owner_address"));
		}else{
			new InfoDialog(activity, "Record not found!").show();
		}
		
		try{
			Map params = new HashMap();
			params.put("objid", rpuid);
			
			BldgRpuDB db = new BldgRpuDB();
			bldgrpu = db.find(params);
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
		
		if(bldgrpu != null){
			bldgage.setText(bldgrpu.getProperty("bldgage"));
			effectiveage.setText(bldgrpu.getProperty("effectiveage"));
			depreciationpercentage.setText(bldgrpu.getProperty("depreciation"));
			depreciationvalue.setText(bldgrpu.getProperty("depreciationvalue"));
			floorcount.setText(bldgrpu.getProperty("floorcount"));
			percentcompleted.setText(bldgrpu.getProperty("percentcompleted"));
			datecompleted.setText(bldgrpu.getProperty("dtcompleted"));
			dateoccupied.setText(bldgrpu.getProperty("dtoccupied"));
			permitno.setText(bldgrpu.getProperty("permitno"));
			issuancedate.setText(bldgrpu.getProperty("permitdate"));
		}
	}
	
	public static void loadMaterialInfo(){
		View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.activity_faas_bldg_materials, contentView, false);
	    contentView.addView(view);
	    
	    material_list = (ListView) view.findViewById(R.id.material_list);
		activity.registerForContextMenu(material_list);
		
		material_add = (Button) view.findViewById(R.id.material_add);
		material_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	StructuralMaterialInfo material = new StructuralMaterialInfo(activity,null,rpuid);
            	material.show();
            }
        });
	}
	
	public static void loadAppraisalInfo(){
		View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.activity_faas_bldg_appraisal, contentView, false);
	    contentView.addView(view);
	    
	    appraisal_add = (Button) view.findViewById(R.id.appraisal_add);
		appraisal_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(activity, BuildingAppraisalActivity.class);
  			  	activity.startActivity(myIntent);
            }
        });
	}
	
	public static void loadExaminationInfo(){
		View view = LayoutInflater.from(activity.getBaseContext()).inflate(R.layout.activity_faas_land_examination, contentView, false);
	    contentView.addView(view);
	    
	    examination_list = (ListView) view.findViewById(R.id.examination_list);
		activity.registerForContextMenu(examination_list);
		
		examination_add = (Button) view.findViewById(R.id.examination_add);
		examination_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(activity, ExaminationActivity.class);
  			  	myIntent.putExtra("faasid", faasid);
  			  	activity.startActivity(myIntent);
            }
        });
		
		loadExaminationData();
	}
	
	public static void loadExaminationData(){
		try{
			data_examination = new ArrayList<ExaminationListItem>();
			Map params = new HashMap();
			params.put("parent_objid", faasid);
			
			ExaminationDB db = new ExaminationDB();
			List<Map> list = db.getList(params);
			for(Map m : list){
				String id = m.get("objid") != null ? m.get("objid").toString() : "";
				String date = m.get("dtinspected") != null ? m.get("dtinspected").toString() : "";
				String findings = m.get("findings") != null ? m.get("findings").toString() : "";
				data_examination.add(new ExaminationListItem(id,findings,date));
			}
			
			examination_list.setAdapter(new ExaminationMenuAdapter(activity,data_examination));
			examination_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					ExaminationMenuAdapter a = (ExaminationMenuAdapter) adapter.getAdapter();
					ExaminationListItem item = a.getListItem(pos);
					Intent myIntent = new Intent(activity, ExaminationActivity.class);
					myIntent.putExtra("objid", item.getObjid());
	  			  	myIntent.putExtra("faasid", faasid);
	  			  	activity.startActivity(myIntent);
				}	
			});
			LayoutParams layout = (LayoutParams) examination_list.getLayoutParams();
			layout.height = (100 * data_examination.size());
			
		}catch(Throwable t){
			t.printStackTrace();
			new ErrorDialog(activity, t).show();
		}
	}

}
