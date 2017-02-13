package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
		
		if(revisionsetting.equals("Building Revision Setting")){
			BldgRySettingDB db = new BldgRySettingDB(); 
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
		
		if(data.isEmpty()){
			setContentView(R.layout.activity_listview_snyc_empty);
		}else{
			setContentView(R.layout.activity_listview_snyc);
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
	}
	
	void saveData(){
		if(revisionsetting.equals("Land Revision Setting")){
			createLandRySettingData();
		}
		
		if(revisionsetting.equals("Building Revision Setting")){
			createBuildingRySettingData();
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
			
			Iterator<Map> it = null;
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

			it = landassesslevellist.iterator();
			while(it.hasNext()){
				Map data = it.next();
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

			it = landassesslevelrangelist.iterator();
			while(it.hasNext()){
				Map data = it.next();
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

			it = lcuvspecificclasslist.iterator();
			while(it.hasNext()){
				Map data = it.next();
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

			it = lcuvsubclasslist.iterator();
			while(it.hasNext()){
				Map data = it.next();
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
			
			finishDownload(error,errorMsg);
		}
	}
	
	private void createBuildingRySettingData(){
		boolean error = false;
		String errorMsg = "";
		
		if(revisionSettingData != null){
			clearBuildingRySettingData();
			
			List<Map> bldgrysettings = (List<Map>) revisionSettingData.get("bldgrysetting");
			List<Map> bldgassesslevels = (List<Map>) revisionSettingData.get("bldgassesslevel");
			List<Map> bldgassesslevelranges = (List<Map>) revisionSettingData.get("bldgassesslevelrange");
			List<Map> bldgtypes = (List<Map>) revisionSettingData.get("bldgtype");
			List<Map> bldgtype_depreciations = (List<Map>) revisionSettingData.get("bldgtype_depreciation");
			List<Map> bldgtype_storeyadjustments = (List<Map>) revisionSettingData.get("bldgtype_storeyadjustment");
			List<Map> bldgkindbucc = (List<Map>) revisionSettingData.get("bldgkindbucc");
			List<Map> bldgadditionalitem = (List<Map>) revisionSettingData.get("bldgadditionalitem");
			
			for(Map map : bldgrysettings){
				try{
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : "");
					params.put("state", map.get("state") != null ? map.get("state").toString() : "");
					params.put("ry", map.get("ry") != null ? map.get("ry").toString() : 0);
					params.put("predominant", map.get("predominant") != null ? map.get("predominant").toString() : 0);
					params.put("depreciatecoreanditemseparately", map.get("depreciatecoreanditemseparately") != null ? map.get("depreciatecoreanditemseparately").toString() : 0);
					params.put("computedepreciationbasedonschedule", map.get("computedepreciationbasedonschedule") != null ? map.get("computedepreciationbasedonschedule").toString() : 0);
					params.put("straightdepreciation", map.get("straightdepreciation") != null ? map.get("straightdepreciation").toString() : "");
					params.put("calcbldgagebasedondtoccupied", map.get("calcbldgagebasedondtoccupied") != null ? map.get("calcbldgagebasedondtoccupied").toString() : 0);
					params.put("appliedto", map.get("appliedto") != null ? map.get("appliedto").toString() : "");
					params.put("previd", map.get("previd") != null ? map.get("previd").toString() : "");
					
					BldgRySettingDB db = new BldgRySettingDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			for(Map map : bldgassesslevels){
				try{
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : "");
					params.put("bldgrysettingid", map.get("bldgrysettingid") != null ? map.get("bldgrysettingid").toString() : "");
					params.put("classification_objid", map.get("classification_objid") != null ? map.get("classification_objid").toString() : null);
					params.put("code", map.get("code") != null ? map.get("code").toString() : "");
					params.put("name", map.get("name") != null ? map.get("name").toString() : "");
					params.put("fixrate", map.get("fixrate") != null ? map.get("fixrate").toString() : 0);
					params.put("rate", map.get("rate") != null ? map.get("rate").toString() : 0);
					params.put("previd", map.get("previd") != null ? map.get("previd").toString() : "");
					
					BldgAssessLevelDB db = new BldgAssessLevelDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			for(Map map : bldgassesslevelranges){
				try{
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : null);
					params.put("bldgassesslevelid", map.get("bldgassesslevelid") != null ? map.get("bldgassesslevelid").toString() : "");
					params.put("bldgrysettingid", map.get("bldgrysettingid") != null ? map.get("bldgrysettingid").toString() : "");
					params.put("mvfrom", map.get("mvfrom") != null ? map.get("mvfrom").toString() : 0);
					params.put("mvto", map.get("mvto") != null ? map.get("mvto").toString() : 0);
					params.put("rate", map.get("rate") != null ? map.get("rate").toString() : 0);

					BldgAssessLevelRangeDB db = new BldgAssessLevelRangeDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			for(Map map : bldgtypes){
				try{
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : null);
					params.put("bldgrysettingid", map.get("bldgrysettingid") != null ? map.get("bldgrysettingid").toString() : "");
					params.put("code", map.get("code") != null ? map.get("code").toString() : "");
					params.put("name", map.get("name") != null ? map.get("name").toString() : "");
					params.put("basevaluetype", map.get("basevaluetype") != null ? map.get("basevaluetype").toString() : "");
					params.put("residualrate", map.get("residualrate") != null ? map.get("residualrate").toString() : 0);
					params.put("previd", map.get("previd") != null ? map.get("previd").toString() : "");
					
					BldgTypeDB db = new BldgTypeDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			for(Map map : bldgtype_depreciations){
				try{
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : "");
					params.put("bldgtypeid", map.get("bldgtypeid") != null ? map.get("bldgtypeid").toString() : "");
					params.put("bldgrysettingid", map.get("bldgrysettingid") != null ? map.get("bldgrysettingid").toString() : "");
					params.put("agefrom", map.get("agefrom") != null ? map.get("agefrom").toString() : 0);
					params.put("ageto", map.get("ageto") != null ? map.get("ageto").toString() : 0);
					params.put("rate", map.get("rate") != null ? map.get("rate").toString() : 0);
					
					BldgTypeDepreciationDB db = new BldgTypeDepreciationDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			for(Map map : bldgtype_storeyadjustments){
				try{
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : "");
					params.put("bldgtypeid", map.get("bldgtypeid") != null ? map.get("bldgtypeid").toString() : "");
					params.put("bldgrysettingid", map.get("bldgrysettingid") != null ? map.get("bldgrysettingid").toString() : "");
					params.put("floorno", map.get("floorno") != null ? map.get("floorno").toString() : "");
					params.put("rate", map.get("rate") != null ? map.get("rate").toString() : 0);
					params.put("previd", map.get("previd") != null ? map.get("previd").toString() : "");
					
					BldgTypeStoreyAdjustmentDB db = new BldgTypeStoreyAdjustmentDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			for(Map map : bldgkindbucc){
				try{
					Map bldgkind = (Map) map.get("bldgkind");
					
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : "");
					params.put("bldgrysettingid", map.get("bldgrysettingid") != null ? map.get("bldgrysettingid").toString() : "");
					params.put("bldgtypeid", map.get("bldgtypeid") != null ? map.get("bldgtypeid").toString() : "");
					params.put("bldgkind_objid", bldgkind.get("objid") != null ? bldgkind.get("objid").toString() : "");
					params.put("basevaluetype", map.get("basevaluetype") != null ? map.get("basevaluetype").toString() : "");
					params.put("basevalue", map.get("basevalue") != null ? map.get("basevalue").toString() : 0.00);
					params.put("minbasevalue", map.get("minbasevalue") != null ? map.get("minbasevalue").toString() : 0.00);
					params.put("maxbasevalue", map.get("maxbasevalue") != null ? map.get("maxbasevalue").toString() : 0.00);
					params.put("gapvalue", map.get("gapvalue") != null ? map.get("gapvalue").toString() : 0);
					params.put("minarea", map.get("minarea") != null ? map.get("minarea").toString() : 0.00);
					params.put("maxarea", map.get("maxarea") != null ? map.get("maxarea").toString() : 0.00);
					params.put("bldgclass", map.get("bldgclass") != null ? map.get("bldgclass").toString() : "");
					params.put("previd", map.get("previd") != null ? map.get("previd").toString() : "");
					
					BldgKindBuccDB db = new BldgKindBuccDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			for(Map map : bldgadditionalitem){
				try{
					Map params = new HashMap();
					params.put("objid", map.get("objid") != null ? map.get("objid").toString() : "");
					params.put("bldgrysettingid", map.get("bldgrysettingid") != null ? map.get("bldgrysettingid").toString() : "");
					params.put("code", map.get("code") != null ? map.get("code").toString() : "");
					params.put("name", map.get("name") != null ? map.get("name").toString() : "");
					params.put("unit", map.get("unit") != null ? map.get("unit").toString() : "");
					params.put("expr", map.get("expr") != null ? map.get("expr").toString() : "");
					params.put("previd", map.get("previd") != null ? map.get("previd").toString() : "");
					params.put("type", map.get("type") != null ? map.get("type").toString() : "");
					params.put("addareatobldgtotalarea", map.get("addareatobldgtotalarea") != null ? map.get("addareatobldgtotalarea").toString() : "");
					
					BldgAdditionalItemDB db = new BldgAdditionalItemDB();
					db.create(params);
				}catch(Throwable t){
					errorMsg = t.getMessage();
					error = true;
					t.printStackTrace();
				}
			}
			
			finishDownload(error,errorMsg);
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
	
	private void clearBuildingRySettingData(){
		try{
			new BldgTypeStoreyAdjustmentDB().clearAll();
			new BldgTypeDepreciationDB().clearAll();
			new BldgTypeDB().clearAll();
			new BldgAssessLevelRangeDB().clearAll();
			new BldgAssessLevelDB().clearAll();
			new BldgRySettingDB().clearAll();
			new BldgKindBuccDB().clearAll();
			new BldgAdditionalItemDB().clearAll();
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.getMessage());
		}
	}
	
	private void finishDownload(boolean error, String errorMsg){
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
	            
	            if(revisionsetting.equals("Building Revision Setting")){
	            	revisionSettingData = service.getBuildingRevisionSettingData();
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
