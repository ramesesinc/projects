package com.rameses.android.system;

import java.io.Serializable;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.client.android.Platform;
import com.rameses.client.android.TerminalManager;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.client.services.TerminalService;

public class RegistrationOptionActivity extends SettingsMenuActivity 
{
	private ProgressDialog progressDialog;
	
	public boolean isCloseable() { return false; }	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.template_footer);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		inflater.inflate(R.layout.activity_registration_option, layout, true);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
	}
	
	public void onStartProcess() { 
		super.onStartProcess();
		System.out.println("app settings -> "+getApp().getAppSettings().getAll());
		new UIAction(this, R.id.btn_next) {
			protected void onClick() {
				try {
					doNext();
				} catch(Throwable t) {
					UIDialog.showMessage("[ERROR] " + t.getMessage());
				}  
			} 
		};
	}
		
	protected void afterBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}	
	
	private void doNext() {
		String tag = getValueAsString(R.id.rg_registration);
		if (tag.equals("register")) {
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
			
		} else if (tag.equals("recover")) {
			progressDialog.setMessage("Recovering terminal...");
			if (!progressDialog.isShowing()) progressDialog.show();
			
			Platform.runAsync(new RecoveryProcess()); 
		} 
	}
	
	
	private Handler successHandler = new Handler() {  
		@Override
		public void handleMessage(Message msg) { 
			Bundle data = msg.getData();
			String terminalid = data.getString("terminalid");
			TerminalManager.save(terminalid); 

			if (progressDialog.isShowing()) progressDialog.dismiss();
			ApplicationUtil.showShortMsg("Terminal successfully recovered.");
			Platform.getApplication().resumeAppLoader();
		}
	};
		
	private Handler errorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if (progressDialog.isShowing()) progressDialog.dismiss();
			
			Serializable o = data.getSerializable("response");
			if (o instanceof Throwable) {
				Throwable t = (Throwable)o;
				ApplicationUtil.showShortMsg("[ERROR] "+ t.getMessage());
			} else {
				ApplicationUtil.showShortMsg(o+"");
			}
		}
	}; 
	
	private class RecoveryProcess implements Runnable 
	{
		@Override
		public void run() {		
			boolean success = false;
			Bundle data = new Bundle();			
			Message msg = errorHandler.obtainMessage();
			
			try {
				String deviceId = TerminalManager.getDeviceId();
				Map result = new TerminalService().recover(deviceId);
				if (result == null || result.isEmpty()) 
					throw new Exception("This device is not yet registered.");
					
				data.putString("terminalid", result.get("terminalid").toString());
				msg = successHandler.obtainMessage();				
				success = true;
			} catch(Throwable e) { 
				data.putSerializable("response", e); 
			} finally {
				msg.setData(data);
				if (success) {
					successHandler.sendMessage(msg);
				} else {
					errorHandler.sendMessage(msg); 
				}
			}
		}
	} 		
}
