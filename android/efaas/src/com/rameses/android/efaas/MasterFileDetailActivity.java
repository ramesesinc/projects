package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.rameses.android.service.MasterFileService;
import com.rameses.client.android.Platform;

public class MasterFileDetailActivity extends SettingsMenuActivity{
	
	private ProgressDialog progressDialog;
	List<MasterFileListItem> data;
	ListView list;
	String masterfile;
	List<Map> masterFileData;
	MasterFileService service;
	Activity activity;
	
	public boolean isCloseable() { return false; }	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this;
		setContentView(R.layout.activity_listview_snyc);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
		
		masterfile = getIntent().getExtras().getString("masterfile");
		ApplicationUtil.changeTitle(this, masterfile);
		
		service = new MasterFileService();
		
		loadListData();
		
		Button button = (Button) findViewById(R.id.button_sync);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
		
		if(masterfile.equals("Property Classifications")){
			PropertyClassificationDB db = new PropertyClassificationDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("code") != null ? m.get("code").toString() : "";
				String name = m.get("name") != null ? m.get("name").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		if(masterfile.equals("Kind of Buildings")){
			BldgKindDB db = new BldgKindDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("code") != null ? m.get("code").toString() : "";
				String name = m.get("name") != null ? m.get("name").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		if(masterfile.equals("Materials")){
			MaterialDB db = new MaterialDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("code") != null ? m.get("code").toString() : "";
				String name = m.get("name") != null ? m.get("name").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		if(masterfile.equals("Structures")){
			StructureDB db = new StructureDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("code") != null ? m.get("code").toString() : "";
				String name = m.get("name") != null ? m.get("name").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		if(masterfile.equals("Machines")){
			MachineDB db = new MachineDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("code") != null ? m.get("code").toString() : "";
				String name = m.get("name") != null ? m.get("name").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		if(masterfile.equals("Plants and Trees")){
			PlantTreeDB db = new PlantTreeDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("code") != null ? m.get("code").toString() : "";
				String name = m.get("name") != null ? m.get("name").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		if(masterfile.equals("Miscellaneous Items")){
			MiscItemDB db = new MiscItemDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String code = m.get("code") != null ? m.get("code").toString() : "";
				String name = m.get("name") != null ? m.get("name").toString() : "";
				data.add(new MasterFileListItem(code,name));
			}
		}
		
		if(masterfile.equals("Parameters")){
			ParameterDB db = new ParameterDB(); 
			List<Map> list = new ArrayList();
			try { 
				list = db.getList(new HashMap()); 
			} catch(Throwable t) {
				t.printStackTrace();
			}
			for ( Map m : list ) { 
				String name = m.get("name") != null ? m.get("name").toString() : "";
				String desc = m.get("description") != null ? m.get("description").toString() : "";
				data.add(new MasterFileListItem(name,desc));
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
		boolean error = false;
		String errorMsg = "";
		
		if(masterFileData == null) return;
		
		if(masterfile.equals("Property Classifications")){
			try{
				PropertyClassificationDB db1 = new PropertyClassificationDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					params.put("special", data.get("special") != null ? data.get("special").toString() : "");
					params.put("orderno", data.get("orderno") != null ? data.get("orderno").toString() : "");
					
					PropertyClassificationDB db2 = new PropertyClassificationDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
		}
		
		if(masterfile.equals("Kind of Buildings")){
			try{
				BldgKindDB db1 = new BldgKindDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					
					BldgKindDB db2 = new BldgKindDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
		}
		
		if(masterfile.equals("Materials")){
			try{
				MaterialDB db1 = new MaterialDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					
					MaterialDB db2 = new MaterialDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
		}
		
		if(masterfile.equals("Structures")){
			try{
				StructureDB db1 = new StructureDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					params.put("indexno", data.get("indexno") != null ? data.get("indexno").toString() : "");
					
					StructureDB db2 = new StructureDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
		}
		
		if(masterfile.equals("Machines")){
			try{
				MachineDB db1 = new MachineDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					
					MachineDB db2 = new MachineDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
		}
		
		if(masterfile.equals("Plants and Trees")){
			try{
				PlantTreeDB db1 = new PlantTreeDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					
					PlantTreeDB db2 = new PlantTreeDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
		}
		
		if(masterfile.equals("Miscellaneous Items")){
			try{
				MiscItemDB db1 = new MiscItemDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("code", data.get("code") != null ? data.get("code").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					
					MiscItemDB db2 = new MiscItemDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
			}
		}
		
		if(masterfile.equals("Parameters")){
			try{
				ParameterDB db1 = new ParameterDB();
				db1.clearAll();
			}catch(Throwable t){
				t.printStackTrace();
				errorMsg = t.getMessage();
				error = true;
			}
			for(Map data : masterFileData){
				try { 
					Map params = new HashMap();
					params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
					params.put("state", data.get("state") != null ? data.get("state").toString() : "");
					params.put("name", data.get("name") != null ? data.get("name").toString() : "");
					params.put("caption", data.get("caption") != null ? data.get("caption").toString() : "");
					params.put("description", data.get("description") != null ? data.get("description").toString() : "");
					params.put("paramtype", data.get("paramtype") != null ? data.get("paramtype").toString() : "");
					params.put("minvalue", data.get("minvalue") != null ? data.get("minvalue").toString() : "");
					params.put("maxvalue", data.get("maxvalue") != null ? data.get("maxvalue").toString() : "");
					
					ParameterDB db2 = new ParameterDB();
					db2.create(params);
				} catch(Throwable t) {
					t.printStackTrace();
					errorMsg = t.getMessage();
					error = true;
				}
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
			new InfoDialog(activity, "Sync Finish!").show();
			loadListData();
		}
	};
	
	private class ActionProcess implements Runnable {

		@Override
		public void run() {
			try{
	            if(masterfile.equals("Property Classifications")){
	            	masterFileData = service.getPropertyClassifications(new HashMap());
	            }
	            if(masterfile.equals("Kind of Buildings")){
	            	masterFileData = service.getBldgKinds(new HashMap());
	            }
	            if(masterfile.equals("Materials")){
	            	masterFileData = service.getMaterials(new HashMap());
	            }
	            if(masterfile.equals("Structures")){
	            	masterFileData = service.getStructures(new HashMap());
	            }
	            if(masterfile.equals("Machines")){
	            	masterFileData = service.getMachines(new HashMap());
	            }
	            if(masterfile.equals("Plants and Trees")){
	            	masterFileData = service.getPlantTrees(new HashMap());
	            }
	            if(masterfile.equals("Miscellaneous Items")){
	            	masterFileData = service.getMiscItems(new HashMap());
	            }
	            if(masterfile.equals("Parameters")){
	            	masterFileData = service.getRPTParameters(new HashMap());
	            }
			}catch(Throwable t){
				t.printStackTrace();
				
				Bundle data = new Bundle();
				data.putString("response", t.getMessage());
				
				Message msg = errorhandler.obtainMessage();
				msg.setData(data);
				
				errorhandler.sendMessage(msg);
			}
            Log.v("Master File Data", masterFileData.toString());
            saveData();
		}
		
	}

}
