package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.rameses.android.efaas.bean.MasterFileItem;
import com.rameses.android.service.MasterFileService;
import com.rameses.android.service.RevisionSettingService;
import com.rameses.client.android.Platform;

public class RevisionSettingDetailActivity extends SettingsMenuActivity {
	
	private ProgressDialog progressDialog;
	List<MasterFileItem> data;
	ListView list;
	String revisionsetting;
	Map revisionSettingData;
	Context ctx;
	RevisionSettingService service;
	
	public boolean isCloseable() { return false; }	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		ctx = this;
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
            	progressDialog.setMessage("Please wait...");
    			if (!progressDialog.isShowing()) progressDialog.show();		
    			Platform.runAsync(new ActionProcess());
            }
        });
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	void loadListData(){
		data = new ArrayList<MasterFileItem>();
		
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
				data.add(new MasterFileItem(code,name));
			}
		}
		
		list = (ListView) findViewById(R.id.listview_snyc);
		list.setAdapter(new MasterFileMenuAdapter(this,data));
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				MasterFileMenuAdapter a = (MasterFileMenuAdapter) adapter.getAdapter();
				String title = a.getListItem(pos).getTitle();
			}	
		});
	}
	
	void saveData(){
		if(revisionsetting.equals("Land Revision Setting")){
			createLandRySettingData();
		}
	}
	
	private void createLandRySettingData(){
		boolean error = false;
		String errorMsg = "";
		
		if(revisionSettingData != null){
			List<Map> landrysettinglist = (List<Map>) revisionSettingData.get("landrysetting");
			List<Map> landassesslevellist = (List<Map>) revisionSettingData.get("landassesslevel");
			List<Map> landassesslevelrangelist = (List<Map>) revisionSettingData.get("landassesslevelrange");
			List<Map> lcuvspecificclasslist = (List<Map>) revisionSettingData.get("lcuvspecificlass");
			List<Map> lcuvsubclasslist = (List<Map>) revisionSettingData.get("lcuvsubclass");
			List<Map> lcuvstrippinglist = (List<Map>) revisionSettingData.get("lcuvstripping");
			List<Map> landadjustmenttypelist = (List<Map>) revisionSettingData.get("landadjustmenttype");
			
			Iterator<Map> it = null;
			
			it = landrysettinglist.iterator();
			LandRySettingDB landrysettingdb = new LandRySettingDB();
			while(it.hasNext()){
				Map data = it.next();
				try{
					landrysettingdb.create(data);
				}catch(Throwable t){
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
			
			it = landassesslevellist.iterator();
			LandAssessLevelDB landassessleveldb = new LandAssessLevelDB();
			while(it.hasNext()){
				Map data = it.next();
				try{
					landassessleveldb.create(data);
				}catch(Throwable t){
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
			
			it = landassesslevelrangelist.iterator();
			LandAssessLevelRangeDB landassesslevelrangedb = new LandAssessLevelRangeDB();
			while(it.hasNext()){
				Map data = it.next();
				try{
					landassesslevelrangedb.create(data);
				}catch(Throwable t){
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
			
			it = lcuvspecificclasslist.iterator();
			LcuvSpecificClassDB lcuvspecificclassdb = new LcuvSpecificClassDB();
			while(it.hasNext()){
				Map data = it.next();
				try{
					lcuvspecificclassdb.create(data);
				}catch(Throwable t){
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
			
			it = lcuvsubclasslist.iterator();
			LcuvSubClassDB lcuvsubclassdb = new LcuvSubClassDB();
			while(it.hasNext()){
				Map data = it.next();
				try{
					lcuvsubclassdb.create(data);
				}catch(Throwable t){
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
			
			it = lcuvstrippinglist.iterator();
			LcuvStrippingDB lcuvstrippingdb = new LcuvStrippingDB();
			while(it.hasNext()){
				Map data = it.next();
				try{
					lcuvstrippingdb.create(data);
				}catch(Throwable t){
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
			
			it = landadjustmenttypelist.iterator();
			LandAdjustmentTypeDB landadjustmenttypedb = new LandAdjustmentTypeDB();
			while(it.hasNext()){
				Map data = it.next();
				try{
					landadjustmenttypedb.create(data);
				}catch(Throwable t){
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
			
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
			ApplicationUtil.showShortMsg("Sync Finish!");
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
