package com.rameses.android.system;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rameses.android.AppSettingsImpl;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIActivity;
import com.rameses.client.android.UIApplication;

public class SettingsActivity extends UIActivity 
{
	private ProgressDialog progressDialog;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState); 
		setContentView(R.layout.template_footer);
		setTitle("Connection Setting");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_settings, rl_container, true);
		progressDialog = new ProgressDialog(this);
		
		setProperty(R.id.et_portnumber, "datatype", "integer");
		setProperty(R.id.et_readtimeout, "datatype", "integer");
		requestFocus(R.id.et_ipaddress);
	}

	protected void onStartProcess() {
		super.onStartProcess();
//
		loadSettings();
       
		new UIAction(this, R.id.btn_save) {
			protected void onClick() {
				try { 
					save(); 
				} catch(Throwable t) {
					ApplicationUtil.showShortMsg("[ERROR] " + t.getMessage()); 
				} finally { 
					if (progressDialog.isShowing()) progressDialog.dismiss(); 					
				} 
			} 
		};
	}
	
	private void save() {
		Object ip = getValue(R.id.et_ipaddress);
		Object port = getValue(R.id.et_portnumber);
		Object context = getValue(R.id.et_context);
		Object cluster = getValue(R.id.et_cluster);
		Object readtimeout = getValue(R.id.et_readtimeout);
		
		
		if (isEmpty(ip)) {
			requestFocus(R.id.et_ipaddress);
			ApplicationUtil.showShortMsg("IP Address is required.");
		} else if (isEmpty(port)) {
			requestFocus(R.id.et_portnumber);
			ApplicationUtil.showShortMsg("Port Number is required.");
		} else if (isEmpty(context)) {
			requestFocus(R.id.et_context);
			ApplicationUtil.showShortMsg("Context is required.");
		} else if (isEmpty(cluster)) {
			requestFocus(R.id.et_cluster);
			ApplicationUtil.showShortMsg("Cluster is required.");
		} else if (isEmpty(readtimeout)) {
			requestFocus(R.id.et_readtimeout);
			ApplicationUtil.showShortMsg("Read Timeout is required.");
		} else {
			progressDialog.setMessage("Saving settings");
			if (!progressDialog.isShowing()) progressDialog.show();
			
			Map<String,Object> map = new HashMap();
			map.put("setting_host", ip);
			map.put("setting_port", port);
			map.put("setting_context", context);
			map.put("setting_cluster", cluster);
			map.put("setting_readtimeout", readtimeout);
			Platform.getApplication().getAppSettings().putAll(map); 	
			
			UIApplication app = Platform.getApplication();
			Map appenv = app.getAppEnv();
			appenv.put("app.context", ApplicationUtil.getAppContext());
			appenv.put("app.cluster", ApplicationUtil.getAppCluster());
			appenv.put("app.host", ApplicationUtil.getAppHost());
						
			if (progressDialog.isShowing()) progressDialog.dismiss(); 
			ApplicationUtil.showShortMsg("Settings successfully saved!");	
		}
		
//		Object port = getValue(R.id.et_host_port);		
//		Object onlinehost = getValue(R.id.et_host_online); 
//		Object offlinehost = getValue(R.id.et_host_offline);
//		Object sessiontimeout = getValue(R.id.et_timeout_session);
//		Object uploadtimeout = getValue(R.id.et_timeout_upload);
//		Object trackertimeout = getValue(R.id.et_timeout_tracker);
//		Object debugenabled = spinner.getSelectedItem();
//		
//		if (isEmpty(onlinehost)) {
//			requestFocus(R.id.et_host_online); 
//			ApplicationUtil.showShortMsg("Host for mobile is required");
//			
//		} else if (isEmpty(offlinehost)) {
//			requestFocus(R.id.et_host_offline); 
//			ApplicationUtil.showShortMsg("Host for WIFI is required");
//			
//		} else if (isEmpty(port)) {
//			requestFocus(R.id.et_host_port); 
//			ApplicationUtil.showShortMsg("Port is required");
//			
//		} else if (isEmpty(sessiontimeout)) {
//			requestFocus(R.id.et_timeout_session); 
//			ApplicationUtil.showShortMsg("Session timeout is required");
//			
//		} else if (isEmpty(uploadtimeout)) {
//			requestFocus(R.id.et_timeout_upload); 
//			ApplicationUtil.showShortMsg("Upload timeout is required");
//			
//		} else if (isEmpty(trackertimeout)) {
//			requestFocus(R.id.et_timeout_tracker); 
//			ApplicationUtil.showShortMsg("Tracker timeout is required");
//			
//		} else { 
//			progressDialog.setMessage("Saving settings");
//			if (!progressDialog.isShowing()) progressDialog.show();
//			
//			Map<String,Object> map = new HashMap();
//			map.put("host_port", port);
//			map.put("host_online", onlinehost);
//			map.put("host_offline", offlinehost);
//			map.put("timeout_session", sessiontimeout);
//			map.put("timeout_upload", uploadtimeout);
//			map.put("timeout_tracker", trackertimeout);
//			map.put("debug_enabled", debugenabled);
//			Platform.getApplication().getAppSettings().putAll(map); 	
//						
//			if (progressDialog.isShowing()) progressDialog.dismiss(); 
//			ApplicationUtil.showShortMsg("Settings successfully saved!");			
//		}
	}
	
	private void loadSettings() {
		AppSettingsImpl sets = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		
		setValue(R.id.et_ipaddress, sets.getHost());
		setValue(R.id.et_portnumber, sets.getPort());
		setValue(R.id.et_context, sets.getContext());
		setValue(R.id.et_cluster, sets.getCluster());
		setValue(R.id.et_readtimeout, sets.getReadTimeout());
	} 	
}
