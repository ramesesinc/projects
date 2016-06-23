package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.rameses.android.efaas.adapter.HomeMenuAdapter;
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
		
		list = (ListView) findViewById(R.id.faas_list);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				FaasMenuAdapter a = (FaasMenuAdapter) adapter.getAdapter();
				String faasid = a.getListItem(pos).getObjid();
				Intent intent = new Intent(activity, FaasActivity.class);
				intent.putExtra("faasid", faasid);
				startActivity(intent); 
			}	
		});
		
		loadData("");
		
		ApplicationUtil.changeTitle(this, "FAAS");
		
		ActionBar bar = getActionBar();
	    //bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#a6e20d")));
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	private void loadData(String searchtext){
		data = new ArrayList<FaasListItem>();
		try{
			List<Map> listData = new FaasDB().getList(new HashMap());
			System.err.println("LIST DATA: " + listData);
			Iterator<Map> i = listData.iterator();
			while(i.hasNext()){
				Map m = i.next();
				Log.v("FAAS DATA", m.toString());
				String faasid = m.get("objid").toString();
				String pin = m.get("fullpin").toString();
				String name = m.get("owner_name").toString();
				String tdno = m.get("tdno").toString();
				data.add(new FaasListItem(faasid, pin, tdno, name));
			}
		}catch(Exception e){
			e.printStackTrace();
			ApplicationUtil.showShortMsg(e.toString());
		}
		list.setAdapter(new FaasMenuAdapter(this,data));
	}

}