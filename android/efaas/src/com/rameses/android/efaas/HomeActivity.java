package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.efaas.adapter.HomeMenuAdapter;
import com.rameses.android.efaas.bean.HomeItem;
import com.rameses.android.system.ChangePasswordActivity;
import com.rameses.android.system.LogoutController;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;

public class HomeActivity extends SettingsMenuActivity   {
	
	ProgressDialog progressDialog;	
	ListView list_home;
	List<HomeItem> data;
	Activity activity;
	
	public boolean isCloseable() { return false; }	
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		
		setContentView(R.layout.activity_list);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		loadListData();
		activity = this;
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	} 
	
	protected void afterActivityChanged() {
		Platform.getInstance().disposeAllExcept(this);
	}
	
	protected void afterBackPressed() {
		if (SessionContext.getSessionId() != null) {
			Platform.getApplication().suspendSuspendTimer();
		} 
	}
	
	protected void doLogout() throws Exception {
		new LogoutController(this, progressDialog).logout();
	}
	
	protected void doChangePassword() {
		Intent intent = new Intent(this, ChangePasswordActivity.class);  
		startActivity(intent); 
	}
	
	void loadListData(){
		data = new ArrayList<HomeItem>();
		data.add(new HomeItem(R.drawable.masterfile,"Master Files"));
		data.add(new HomeItem(R.drawable.revision,"Revision Settings"));
		data.add(new HomeItem(R.drawable.change_password,"Change Password"));
		data.add(new HomeItem(R.drawable.logout,"Logout"));
		
		list_home = (ListView) findViewById(R.id.list_home);
		list_home.setAdapter(new HomeMenuAdapter(this,data));
		list_home.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
				HomeMenuAdapter a = (HomeMenuAdapter) adapter.getAdapter();
				String title = a.getListItem(pos).getTitle();
				
				if(title.equals("Master Files")){
					Intent intent = new Intent(activity, MasterFileActivity.class);  
					startActivity(intent); 
				}
				if(title.equals("Change Password")){
					doChangePassword();
				}
				if(title.equals("Logout")){
					try { doLogout(); } catch(Exception e){ e.printStackTrace(); }
				}
			}	
		});
	}

}
