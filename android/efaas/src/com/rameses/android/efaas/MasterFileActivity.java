package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.efaas.adapter.HomeMenuAdapter;
import com.rameses.android.efaas.bean.HomeListItem;

public class MasterFileActivity extends SettingsMenuActivity {
	
	private ProgressDialog progressDialog;
	List<HomeListItem> data;
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
		data = new ArrayList<HomeListItem>();
		data.add(new HomeListItem(R.drawable.file,"Property Classifications"));
		data.add(new HomeListItem(R.drawable.file,"Kind of Buildings"));
		data.add(new HomeListItem(R.drawable.file,"Materials"));
		data.add(new HomeListItem(R.drawable.file,"Structures"));
		data.add(new HomeListItem(R.drawable.file,"Machines"));
		data.add(new HomeListItem(R.drawable.file,"Plants and Trees"));
		data.add(new HomeListItem(R.drawable.file,"Miscellaneous Items"));
		data.add(new HomeListItem(R.drawable.file,"Parameters"));
		
		list = (ListView) findViewById(R.id.list_home);
		list.setAdapter(new HomeMenuAdapter(this,data));
		//list.setDivider(null);
		//list.setDividerHeight(0);
		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				HomeMenuAdapter a = (HomeMenuAdapter) adapter.getAdapter();
				String title = a.getListItem(pos).getTitle();
				Intent intent = new Intent(activity, MasterFileDetailActivity.class);
				intent.putExtra("masterfile", title);
				startActivity(intent); 
			}	
		});
	}

}
