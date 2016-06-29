package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.db.*;
import com.rameses.android.efaas.adapter.MasterFileMenuAdapter;
import com.rameses.android.efaas.bean.MasterFileListItem;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.service.RevisionSettingService;
import com.rameses.client.android.Platform;

public class RevisionSettingDetailActivity extends SettingsMenuActivity {
	
	private ProgressDialog progressDialog;
	List<MasterFileListItem> data;
	ListView list;
	String revisionsetting;
	Map revisionSettingData;
	Activity activity;
	RevisionSettingService service;
	
	public boolean isCloseable() { return false; }	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this;
		setContentView(R.layout.activity_listview_snyc);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
		
		revisionsetting = getIntent().getExtras().getString("revisionsetting");
		ApplicationUtil.changeTitle(this, revisionsetting);
		
		service = new RevisionSettingService();
		
		loadListData();
		
		Button button = (Button) findViewById(R.id.button_sync);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(!revisionsetting.equals("Land Revision Setting")){
            		new InfoDialog(activity,"This operation is not yet supported!").show();
            		return;
            	}
            	progressDialog.setMessage("Please wait...");
    			if (!progressDialog.isShowing()) progressDialog.show();		
    			Platform.runAsync(new ActionProcess());
            }
        });
        
        ActionBar bar = getActionBar();
	    //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a6e20d")));
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	void loadListData(){
		data = new ArrayList<MasterFileListItem>();
		
		if(revisionsetting.equals("Land Revision Setting")){
			LandRySettingDB db = new LandRySettingDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("ry") != null ? m.get("ry").toString() : "";
				String name = m.get("appliedto") != null ? m.get("appliedto").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		list = (ListView) findViewById(R.id.listview_snyc);
		list.setAdapter(new MasterFileMenuAdapter(this,data));
		list.setBackgroundResource(0);
		if(data.isEmpty()) list.setBackgroundResource(R.drawable.empty);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				MasterFileMenuAdapter a = (MasterFileMenuAdapter) adapter.getAdapter();
				String title = a.getListItem(pos).getTitle();
			}	
		});
	}
	
	void saveData(){
		System.out.println("saveData entered....");
		if(revisionsetting.equals("Land Revision Setting")){
			createLandRySettingData();
		}
	}
	
	private void createLandRySettingData(){
		boolean error = false;
		String errorMsg = "";
		
		if(revisionSettingData != null){
			clearLandRySettingData();
			
			List<Map> landrysettinglist = (List<Map>) revisionSettingData.get("landrysetting");
			List<Map> landassesslevellist = (List<Map>) revisionSettingData.get("landassesslevel");
			List<Map> landassesslevelrangelist = (List<Map>) revisionSettingData.get("landassesslevelrange");
			List<Map> lcuvspecificclasslist = (List<Map>) revisionSettingData.get("lcuvspecificclass");
			List<Map> lcuvsubclasslist = (List<Map>) revisionSettingData.get("lcuvsubclass");
			List<Map> lcuvstrippinglist = (List<Map>) revisionSettingData.get("lcuvstripping");
			List<Map> landadjustmenttypelist = (List<Map>) revisionSettingData.get("landadjustmenttype");
			
			Log.v("RAMESES","landrysettinglist : " + landrysettinglist);
			Log.v("RAMESES","landassesslevellist : " + landassesslevellist);
			Log.v("RAMESES","landassesslevelrangelist : " + landassesslevelrangelist);
			Log.v("RAMESES","lcuvspecificclasslist : " + lcuvspecificclasslist);
			Log.v("RAMESES","lcuvsubclasslist : " + lcuvsubclasslist);
			Log.v("RAMESES","lcuvstrippinglist : " + lcuvstrippinglist);
			Log.v("RAMESES","landadjustmenttypelist : " + landadjustmenttypelist);
			
			Iterator<Map> it = null;
			
			Log.v("RAMESES","=================================================");
			Log.v("RAMESES","LANDRYSETTING : \n");
			it = landrysettinglist.iterator();
			while(it.hasNext()){
				Map data = it.next();
				try{
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("ry", data.get("ry") != null ? data.get("ry").toString() : "");
					params.put("appliedto", data.get("appliedto") != null ? data.get("appliedto").toString() : "");
					params.put("previd", data.get("previd") != null ? data.get("previd").toString() : "");
					
					LandRySettingDB landrysettingdb = new LandRySettingDB();
					landrysettingdb.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			Log.v("RAMESES","=================================================\n");
			
			Log.v("RAMESES","=================================================");
			Log.v("RAMESES","LANDASSESSLEVEL : \n");
			it = landassesslevellist.iterator();
			while(it.hasNext()){
				Map data = it.next();
				System.out.println(data + "\n");
				try{
					Map classification = (Map) data.get("classification");
					
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("landrysettingid", data.get("landrysettingid") != null ? data.get("landrysettingid").toString() : "");
					params.put("classification_objid", classification.get("objid") != null ? classification.get("objid").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					params.put("fixrate", data.get("fixrate") != null ? data.get("fixrate").toString() : "");
					params.put("rate", data.get("rate") != null ? data.get("rate").toString() : "");
					params.put("previd", data.get("previd") != null ? data.get("previd").toString() : "");
					
					LandAssessLevelDB landassessleveldb = new LandAssessLevelDB();
					landassessleveldb.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			Log.v("RAMESES","=================================================\n");
			
			Log.v("RAMESES","=================================================");
			Log.v("RAMESES","LANDASSESSLEVELRANGE : \n");
			it = landassesslevelrangelist.iterator();
			while(it.hasNext()){
				Map data = it.next();
				System.out.println(data + "\n");
				try{
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("landassesslevelid", data.get("landassesslevelid") != null ? data.get("landassesslevelid").toString() : "");
					params.put("landrysettingid", data.get("landrysettingid") != null ? data.get("landrysettingid").toString() : "");
					params.put("mvfrom", data.get("mvfrom") != null ? data.get("mvfrom").toString() : "");
					params.put("mvto", data.get("mvto") != null ? data.get("mvto").toString() : "");
					params.put("rate", data.get("rate") != null ? data.get("rate").toString() : "");
					
					LandAssessLevelRangeDB landassesslevelrangedb = new LandAssessLevelRangeDB();
					landassesslevelrangedb.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			Log.v("RAMESES","=================================================\n");
			
			Log.v("RAMESES","=================================================");
			Log.v("RAMESES","LCUVSPECIFICCLASS : \n");
			it = lcuvspecificclasslist.iterator();
			while(it.hasNext()){
				Map data = it.next();
				System.out.println(data + "\n");
				try{
					Map classification = (Map) data.get("classification");
					
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("landrysettingid", data.get("landrysettingid") != null ? data.get("landrysettingid").toString() : "");
					params.put("classification_objid", classification.get("objid") != null ? classification.get("objid").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					params.put("areatype", data.get("areatype") != null ? data.get("areatype").toString() : "");
					params.put("previd", data.get("previd") != null ? data.get("previd").toString() : "");
					
					LcuvSpecificClassDB lcuvspecificclassdb = new LcuvSpecificClassDB();
					lcuvspecificclassdb.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			Log.v("RAMESES","=================================================\n");
			
			Log.v("RAMESES","=================================================");
			Log.v("RAMESES","LCUVSUBCLASS : \n");
			it = lcuvsubclasslist.iterator();
			while(it.hasNext()){
				Map data = it.next();
				System.out.println(data + "\n");
				try{
					Map specificclass = (Map) data.get("specificclass");
					
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("specificclass_objid", specificclass.get("objid") != null ? specificclass.get("objid").toString() : "");
					params.put("landrysettingid", data.get("landrysettingid") != null ? data.get("landrysettingid").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					params.put("unitvalue", data.get("unitvalue") != null ? data.get("unitvalue").toString() : "");
					params.put("previd", data.get("previd") != null ? data.get("previd").toString() : "");
					
					LcuvSubClassDB lcuvsubclassdb = new LcuvSubClassDB();
					lcuvsubclassdb.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			Log.v("RAMESES","=================================================\n");
			
			Log.v("RAMESES","=================================================");
			Log.v("RAMESES","LCUVSTRIPPING : \n");
			it = lcuvstrippinglist.iterator();
			while(it.hasNext()){
				Map data = it.next();
				try{
					Map classification = (Map) data.get("classification");
					
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("landrysettingid", data.get("landrysettingid") != null ? data.get("landrysettingid").toString() : "");
					params.put("classification_objid", classification.get("objid") != null ? classification.get("objid").toString() : "");
					params.put("striplevel", data.get("striplevel") != null ? data.get("striplevel").toString() : "");
					params.put("rate", data.get("rate") != null ? data.get("rate").toString() : null);
					params.put("previd", data.get("previd") != null ? data.get("previd").toString() : "");
					
					LcuvStrippingDB lcuvstrippingdb = new LcuvStrippingDB();
					lcuvstrippingdb.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			Log.v("RAMESES","=================================================\n");
			
			Log.v("RAMESES","=================================================");
			Log.v("RAMESES","LANDADJUSTMENTTYPE : \n");
			it = landadjustmenttypelist.iterator();
			while(it.hasNext()){
				Map data = it.next();
				try{
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("landrysettingid", data.get("landrysettingid") != null ? data.get("landrysettingid").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					params.put("expr", data.get("expr") != null ? data.get("expr").toString() : "");
					params.put("appliedto", data.get("appliedto") != null ? data.get("appliedto").toString() : "");
					params.put("previd", data.get("previd") != null ? data.get("previd").toString() : "");
					params.put("idx", data.get("idx") != null ? data.get("idx").toString() : "");
					
					LandAdjustmentTypeDB landadjustmenttypedb = new LandAdjustmentTypeDB();
					landadjustmenttypedb.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			Log.v("RAMESES","=================================================\n");
			
			if(!error){
				Bundle data = new Bundle();
				data.putString("response", "success");
				
				Message msg = successhandler.obtainMessage();
				msg.setData(data);
				
				successhandler.sendMessage(msg);
			}else{
				Bundle data = new Bundle();
				data.putString("response", errorMsg);
				
				Message msg = errorhandler.obtainMessage();
				msg.setData(data);
				
				errorhandler.sendMessage(msg);
			}
		}
	}
	
	private void clearLandRySettingData(){
		try{
			new LandRySettingDB().clearAll();
			new LandAssessLevelDB().clearAll();
			new LandAssessLevelRangeDB().clearAll();
			new LcuvSpecificClassDB().clearAll();
			new LcuvSubClassDB().clearAll();
			new LcuvStrippingDB().clearAll();
			new LandAdjustmentTypeDB().clearAll();
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.getMessage());
		}
	}
	
	private Handler errorhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {	
			try { 
				Bundle data = msg.getData();			
				String error = data.getString("response");
				ApplicationUtil.showShortMsg(error);
			} finally { 
				if (progressDialog.isShowing()) progressDialog.dismiss(); 
			}
		}
	}; 
	
	private Handler successhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss(); 
			new InfoDialog(activity,"Sync Finish!").show();
			loadListData();
		}
	};
	
	private class ActionProcess implements Runnable {

		@Override
		public void run() {
			try{
	            if(revisionsetting.equals("Land Revision Setting")){
	            	revisionSettingData = service.getLandRevisionSettingData();
	            }
			}catch(Throwable t){
				t.printStackTrace();
				
				Bundle data = new Bundle();
				data.putString("response", t.getMessage());
				
				Message msg = errorhandler.obtainMessage();
				msg.setData(data);
				
				errorhandler.sendMessage(msg);
			}
            saveData();
		}
		
	}

}
