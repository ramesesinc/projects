package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup.LayoutParams;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.*;
import com.rameses.android.efaas.dialog.LandAppraisalInfo;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.efaas.dialog.StructuralMaterialInfo;
import com.rameses.android.efaas.adapter.AppraisalMenuAdapter;
import com.rameses.android.efaas.adapter.ExaminationMenuAdapter;
import com.rameses.android.efaas.bean.*;

public class BuildingFaasActivity extends ControlActivity{
	
	public boolean isCloseable() { return false; }
	
	private static TextView tdno, pin, owner, address;
	private static Button material_add, appraisal_add, examination_add;
	private static ListView material_list, examination_list;
	
	public static Activity activity;
	private static Properties faas = null;
	private static String faasid;
	private static String rpuid;
	private static Properties examination;
	private static List<ImageItem> data_image;
	private static List<AppraisalListItem> data_appraisal;
	private static List<ExaminationListItem> data_examination;
	private static int ctxMenuId;
	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		
		setContentView(R.layout.activity_faas_bldg);
		
		faasid = getIntent().getExtras().getString("faasid");
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		tdno = (TextView) findViewById(R.id.faas_tdno);
		pin = (TextView) findViewById(R.id.faas_pin);
		owner = (TextView) findViewById(R.id.faas_owner);
		address = (TextView) findViewById(R.id.faas_address);
		
		material_list = (ListView) findViewById(R.id.material_list);
		registerForContextMenu(material_list);
		
		examination_list = (ListView) findViewById(R.id.examination_list);
		registerForContextMenu(examination_list);
		
		material_add = (Button) findViewById(R.id.material_add);
		material_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//StructuralMaterialInfo material = new StructuralMaterialInfo(activity,null,rpuid);
            	//material.show();
            }
        });
		
		appraisal_add = (Button) findViewById(R.id.appraisal_add);
		appraisal_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(activity, BuildingAppraisalActivity.class);
  			  	startActivity(myIntent);
            }
        });
		
		examination_add = (Button) findViewById(R.id.examination_add);
		examination_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(activity, ExaminationActivity.class);
  			  	myIntent.putExtra("faasid", faasid);
  			  	startActivity(myIntent);
            }
        });
		
		initData();
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		String[] menuItems = new String[]{"Edit","Delete"};
		for (int i = 0; i<menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
		if(v.getId()==R.id.appraisal_list){
			ctxMenuId = R.id.appraisal_list;
			menu.setHeaderTitle("Land Detail");
		}
		if(v.getId()==R.id.examination_list){
			ctxMenuId = R.id.examination_list;
			menu.setHeaderTitle("Examination");
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  
	  if(ctxMenuId == R.id.appraisal_list){
		  AppraisalListItem appraisalItem = data_appraisal.get(info.position);
		  if(menuItemIndex == 0){
			  LandAppraisalInfo appraisal = new LandAppraisalInfo(activity,appraisalItem.getObjid(),rpuid);
              appraisal.show();
		  }
		  if(menuItemIndex == 1){
			  Map params = new HashMap();
			  params.put("objid", appraisalItem.getObjid());
			  
			  LandDetailDB db = new LandDetailDB();
			  try{
				  db.delete(params);
				  initData();
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  
	  if(ctxMenuId == R.id.examination_list){
		  ExaminationListItem examinationItem = data_examination.get(info.position);
		  if(menuItemIndex == 0){
			   Intent myIntent = new Intent(activity, ExaminationActivity.class);
			   myIntent.putExtra("objid", examinationItem.getObjid());
			   myIntent.putExtra("faasid", faasid);
			   activity.startActivity(myIntent);
		  }
		  if(menuItemIndex == 1){
			  Map params1 = new HashMap();
			  params1.put("examinationid", examinationItem.getObjid());
			  
			  Map params2 = new HashMap();
			  params2.put("objid", examinationItem.getObjid());
			  
			  ExaminationDB db = new ExaminationDB();
			  ImageDB imdb = new ImageDB();
			  try{
				  imdb.delete(params1);
				  db.delete(params2);
				  initData();
			  }catch(Throwable t){
				  new ErrorDialog(this, t).show();
			  }
		  }
	  }
	  return true;
	}
	
	public static void initData(){
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
		
		loadAppraisalData();
		loadExaminationData();
	}
	
	private static void loadAppraisalData(){
		data_appraisal = new ArrayList<AppraisalListItem>();
		try{
			Map params = new HashMap();
			params.put("landrpuid", rpuid);
			LandDetailDB db = new LandDetailDB();
			List<Map> list = db.getList(params);
			for(Map m : list){
				String objid = m.get("objid") != null ? m.get("objid").toString() : "";
				String landrpuid = m.get("landrpuid") != null ? m.get("landrpuid").toString() : "";
				String subclass_objid = m.get("subclass_objid") != null ? m.get("subclass_objid").toString() : "";
				String specificclass_objid = m.get("specificclass_objid") != null ? m.get("specificclass_objid").toString() : "";
				String actualuse_objid = m.get("actualuse_objid") != null ? m.get("actualuse_objid").toString() : "";
				String stripping_objid = m.get("stripping_objid") != null ? m.get("stripping_objid").toString() : "";
				String striprate = m.get("striprate") != null ? m.get("striprate").toString() : "";
				String areatype = m.get("areatype") != null ? m.get("areatype").toString() : "";
				String area = m.get("area") != null ? m.get("area").toString() : "";
				String areasqm = m.get("areasqm") != null ? m.get("areasqm").toString() : "";
				String areaha = m.get("areaha") != null ? m.get("areaha").toString() : "";
				
				Properties data_subclass = new LcuvSubClassDB().find(subclass_objid);
				Properties data_specificclass = new LcuvSpecificClassDB().find(specificclass_objid);
				Properties data_actualuse = new LandAssessLevelDB().find(actualuse_objid);
				Properties data_strip = new LcuvStrippingDB().find(stripping_objid);
				
				String stripping = " - ", subclass = " - ", specificclass = " - ", actualuse = " - ";
				if(data_subclass != null) subclass = data_subclass.getProperty("code") + " - " + data_subclass.getProperty("name");
				if(data_specificclass != null) specificclass = data_specificclass.getProperty("name");
				if(data_actualuse != null) actualuse = data_actualuse.getProperty("name");
				if(data_strip != null) stripping = data_strip.getProperty("classification_objid") + " - " + data_strip.getProperty("rate");
				
				data_appraisal.add(new AppraisalListItem(
						objid,
						subclass,
						specificclass,
						actualuse,
						stripping,
						areasqm
				));
			}
			material_list.setAdapter(new AppraisalMenuAdapter(activity, data_appraisal));
			LayoutParams layout = (LayoutParams) material_list.getLayoutParams();
			layout.height = (68 * data_appraisal.size());
			material_list.setLayoutParams(layout);
			
			material_list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
					
				}	
			});
		}catch(Throwable t){
			t.printStackTrace();
			new ErrorDialog(activity, t).show();
		}
	}
	
	private static void loadExaminationData(){
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
			examination_list.setLayoutParams(layout);
		}catch(Throwable t){
			t.printStackTrace();
			new ErrorDialog(activity, t).show();
		}
	}
	
}
