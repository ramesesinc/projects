package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.efaas.adapter.HomeMenuAdapter;
import com.rameses.android.efaas.adapter.MasterFileMenuAdapter;
import com.rameses.android.efaas.bean.MasterFileItem;

public class SubMasterFileActivity extends SettingsMenuActivity{
	
	private ProgressDialog progressDialog;
	List<MasterFileItem> data;
	ListView list;
	String masterfile;
	
	public boolean isCloseable() { return false; }	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.activity_masterfile_snyc);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
		
		masterfile = getIntent().getExtras().getString("masterfile");
		ApplicationUtil.changeTitle(this, masterfile);
		
		loadListData();
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	void loadListData(){
		data = new ArrayList<MasterFileItem>();
		
		list = (ListView) findViewById(R.id.listview_snyc);
		list.setAdapter(new MasterFileMenuAdapter(this,data));
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				HomeMenuAdapter a = (HomeMenuAdapter) adapter.getAdapter();
				String title = a.getListItem(pos).getTitle();
				
			}	
		});
	}

}
