package com.rameses.android.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.rameses.android.AppSettingsImpl;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIActivity;

public class SettingsActivity extends UIActivity 
{
	private ProgressDialog progressDialog;
	private Spinner spinner;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState); 
		setContentView(R.layout.template_footer);
		setTitle("Settings");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_settings, rl_container, true);
		progressDialog = new ProgressDialog(this);
		
		spinner = (Spinner) findViewById(R.id.spinner_debug_enabled);
		
		setProperty(R.id.et_host_port, "datatype", "integer");
		setProperty(R.id.et_timeout_session, "datatype", "integer");
		setProperty(R.id.et_timeout_upload, "datatype", "integer");
		setProperty(R.id.et_timeout_tracker, "datatype", "integer");
		requestFocus(R.id.et_host_online);
	}

	protected void onStartProcess() {
		super.onStartProcess();

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.boolean_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
		Object port = getValue(R.id.et_host_port);		
		Object onlinehost = getValue(R.id.et_host_online); 
		Object offlinehost = getValue(R.id.et_host_offline);
		Object sessiontimeout = getValue(R.id.et_timeout_session);
		Object uploadtimeout = getValue(R.id.et_timeout_upload);
		Object trackertimeout = getValue(R.id.et_timeout_tracker);
		Object debugenabled = spinner.getSelectedItem();
		
		if (isEmpty(onlinehost)) {
			requestFocus(R.id.et_host_online); 
			ApplicationUtil.showShortMsg("Host for mobile is required");
			
		} else if (isEmpty(offlinehost)) {
			requestFocus(R.id.et_host_offline); 
			ApplicationUtil.showShortMsg("Host for WIFI is required");
			
		} else if (isEmpty(port)) {
			requestFocus(R.id.et_host_port); 
			ApplicationUtil.showShortMsg("Port is required");
			
		} else if (isEmpty(sessiontimeout)) {
			requestFocus(R.id.et_timeout_session); 
			ApplicationUtil.showShortMsg("Session timeout is required");
			
		} else if (isEmpty(uploadtimeout)) {
			requestFocus(R.id.et_timeout_upload); 
			ApplicationUtil.showShortMsg("Upload timeout is required");
			
		} else if (isEmpty(trackertimeout)) {
			requestFocus(R.id.et_timeout_tracker); 
			ApplicationUtil.showShortMsg("Tracker timeout is required");
			
		} else { 
			progressDialog.setMessage("Saving settings");
			if (!progressDialog.isShowing()) progressDialog.show();
			
			Map<String,Object> map = new HashMap();
			map.put("host_port", port);
			map.put("host_online", onlinehost);
			map.put("host_offline", offlinehost);
			map.put("timeout_session", sessiontimeout);
			map.put("timeout_upload", uploadtimeout);
			map.put("timeout_tracker", trackertimeout);
			map.put("debug_enabled", debugenabled);
			Platform.getApplication().getAppSettings().putAll(map); 	
						
			if (progressDialog.isShowing()) progressDialog.dismiss(); 
			ApplicationUtil.showShortMsg("Settings successfully saved!");			
		}
	}
	
	private void loadSettings() {
		AppSettingsImpl sets = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		setValue(R.id.et_host_port, sets.getPort());		
		setValue(R.id.et_host_online, sets.getOnlineHost());
		setValue(R.id.et_host_offline, sets.getOfflineHost());
		setValue(R.id.et_timeout_session, sets.getSessionTimeout());
		setValue(R.id.et_timeout_upload, sets.getUploadTimeout());
		setValue(R.id.et_timeout_tracker, sets.getTrackerTimeout());
		
		if (sets.getDebugEnabled().equals("true")) {
			spinner.setSelection(0);
		} else {
			spinner.setSelection(1);
		}
//		setValue(R.id.et_debugEnabled, sets.getDebugEnabled());
	} 	
}
