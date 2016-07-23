package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
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
import com.rameses.android.efaas.bean.HomeListItem;
import com.rameses.android.system.ChangePasswordActivity;
import com.rameses.android.system.LogoutController;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;

public class HomeActivity extends SettingsMenuActivity   {
	
	ProgressDialog progressDialog;	
	ListView list_home;
	List<HomeListItem> data;
	Activity activity;
	
	public boolean isCloseable() { return false; }	
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		
		setContentView(R.layout.activity_list);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		loadListData();
		activity = this;
		
		int SDK_INT = android.os.Build.VERSION.SDK_INT;
	    if (SDK_INT > 8) 
	    {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	                .permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	    }
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
		data = new ArrayList<HomeListItem>();
		data.add(new HomeListItem(R.drawable.masterfile,"Master Files"));
		data.add(new HomeListItem(R.drawable.settings,"Revision Settings"));
		data.add(new HomeListItem(R.drawable.faas,"FAAS"));
		data.add(new HomeListItem(R.drawable.download,"Download"));
		data.add(new HomeListItem(R.drawable.upload,"Upload"));
		data.add(new HomeListItem(R.drawable.changepassword,"Change Password"));
		data.add(new HomeListItem(R.drawable.logout,"Logout"));
		
		list_home = (ListView) findViewById(R.id.list_home);
		//list_home.setDivider(null);
		//list_home.setDividerHeight(0);
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
				
				if(title.equals("Revision Settings")){
					Intent intent = new Intent(activity, RevisionSettingActivity.class);  
					startActivity(intent); 
				}
				
				if(title.equals("FAAS")){
					Intent intent = new Intent(activity, FaasListActivity.class);  
					startActivity(intent); 
				}
				
				if(title.equals("Download")){
					Intent intent = new Intent(activity, DownloadActivity.class);  
					startActivity(intent); 
				}
				
				if(title.equals("Upload")){
					Intent intent = new Intent(activity, UploadActivity.class);  
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
