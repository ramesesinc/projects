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
import android.widget.LinearLayout;
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
		
		setContentView(R.layout.activity_home);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		initData();
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
	
	void initData(){
		LinearLayout layout_masterfile = (LinearLayout)findViewById(R.id.home_masterfile);
		layout_masterfile.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(activity, MasterFileActivity.class);  
				startActivity(intent); 
		    }
		});
		
		LinearLayout layout_revision = (LinearLayout)findViewById(R.id.home_revisionsetting);
		layout_revision.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(activity, RevisionSettingActivity.class);  
				startActivity(intent); 
		    }
		});
		
		LinearLayout layout_faas = (LinearLayout)findViewById(R.id.home_faas);
		layout_faas.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(activity, FaasListActivity.class);  
				startActivity(intent);
		    }
		});
		
		LinearLayout layout_download = (LinearLayout)findViewById(R.id.home_download);
		layout_download.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(activity, DownloadActivity.class);  
				startActivity(intent); 
		    }
		});
		
		LinearLayout layout_upload = (LinearLayout)findViewById(R.id.home_upload);
		layout_upload.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	Intent intent = new Intent(activity, UploadActivity.class);  
				startActivity(intent); 
		    }
		});
		
		LinearLayout layout_password = (LinearLayout)findViewById(R.id.home_password);
		layout_password.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	doChangePassword();
		    }
		});
		
		LinearLayout layout_logout = (LinearLayout)findViewById(R.id.home_logout);
		layout_logout.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	try { doLogout(); } catch(Exception e){ e.printStackTrace(); }
		    }
		});
	}
	
}
