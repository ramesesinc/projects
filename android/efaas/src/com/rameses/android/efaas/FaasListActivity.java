package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.db.FaasDB;
import com.rameses.android.efaas.adapter.FaasMenuAdapter;
import com.rameses.android.efaas.bean.FaasListItem;

public class FaasListActivity extends SettingsMenuActivity {
	
	private ProgressDialog progressDialog;
	ListView list;
	List<FaasListItem> data;
	Activity activity;
	
	public boolean isCloseable() { return false; }
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this;
		
		setContentView(R.layout.activity_faaslist);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
		
		loadData("");
		
		ApplicationUtil.changeTitle(this, "FAAS");
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	private void initComponents(){
		list = (ListView) findViewById(R.id.faas_list);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				FaasMenuAdapter a = (FaasMenuAdapter) adapter.getAdapter();
				String faasid = a.getListItem(pos).getObjid();
				String type = a.getListItem(pos).getRpuType();
				if(type.equalsIgnoreCase("land")){
					Intent intent = new Intent(activity, TabFaasLandActivity.class);
					intent.putExtra("faasid", faasid);
					startActivity(intent); 
				}
				if(type.equalsIgnoreCase("bldg")){
					Intent intent = new Intent(activity, TabFaasBuildingActivity.class);
					intent.putExtra("faasid", faasid);
					startActivity(intent); 
				}
			}	
		});
	}
	
	private void loadData(String searchtext){
		data = new ArrayList<FaasListItem>();
		try{
			List<Map> listData = new FaasDB().getList(new HashMap());
			Iterator<Map> i = listData.iterator();
			while(i.hasNext()){
				Map m = i.next();
				Log.v("FAAS DATA", m.toString());
				String faasid = m.get("objid").toString();
				String pin = m.get("fullpin").toString();
				String name = m.get("owner_name").toString();
				String tdno = m.get("tdno").toString();
				String rputype = m.get("rpu_type").toString();
				data.add(new FaasListItem(faasid, pin, tdno, name, rputype));
			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationUtil.showShortMsg(e.toString());
		}
		if(data.isEmpty()){
			setContentView(R.layout.activity_faaslist_empty);
		}else{
			setContentView(R.layout.activity_faaslist);
			initComponents();
			list.setAdapter(new FaasMenuAdapter(this,data));
		}
	}

}
