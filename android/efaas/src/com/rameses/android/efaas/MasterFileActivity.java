package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.efaas.adapter.HomeMenuAdapter;
import com.rameses.android.efaas.bean.HomeItem;

public class MasterFileActivity extends SettingsMenuActivity {
	
	private ProgressDialog progressDialog;
	List<HomeItem> data;
	ListView list;
	Activity activity;
	
	public boolean isCloseable() { return false; }	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.activity_list);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
		
		loadListData();
		ApplicationUtil.changeTitle(this, "Master Files");
		activity = this;
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	void loadListData(){
		data = new ArrayList<HomeItem>();
		data.add(new HomeItem(R.drawable.masterfile,"Property Classifications"));
		data.add(new HomeItem(R.drawable.masterfile,"Kind of Buildings"));
		data.add(new HomeItem(R.drawable.masterfile,"Materials"));
		data.add(new HomeItem(R.drawable.masterfile,"Structures"));
		data.add(new HomeItem(R.drawable.masterfile,"Machines"));
		data.add(new HomeItem(R.drawable.masterfile,"Plants and Trees"));
		data.add(new HomeItem(R.drawable.masterfile,"Miscellaneous Items"));
		data.add(new HomeItem(R.drawable.masterfile,"Parameters"));
		
		list = (ListView) findViewById(R.id.list_home);
		list.setAdapter(new HomeMenuAdapter(this,data));
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				HomeMenuAdapter a = (HomeMenuAdapter) adapter.getAdapter();
				String title = a.getListItem(pos).getTitle();
				Intent intent = new Intent(activity, SubMasterFileActivity.class);
				intent.putExtra("masterfile", title);
				startActivity(intent); 
			}	
		});
	}

}
