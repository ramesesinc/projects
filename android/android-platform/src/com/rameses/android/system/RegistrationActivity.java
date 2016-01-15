package com.rameses.android.system;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.client.android.Platform;
import com.rameses.client.android.TerminalManager;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.client.services.TerminalService;

public class RegistrationActivity extends SettingsMenuActivity  
{
	private ProgressDialog progressDialog; 
	private Handler handler;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.template_footer);
		RelativeLayout container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_registration_new, container, true);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		handler = new Handler();
	} 
	
	protected void onStartProcess() {
		new UIAction(this, R.id.btn_register) {
			protected void onClick() {
				try {
					doRegister();
				} catch(Throwable t) {
					UIDialog.showMessage("[ERROR] " + t.getMessage());
				}				
			}
		};
	} 

	private void doRegister() {
		String terminalkey = getValueAsString(R.id.et_registration_terminalkey);
		String registeredby = getValueAsString(R.id.et_registration_registeredby); 
		if (isEmpty(terminalkey)) {
			requestFocus(R.id.et_registration_terminalkey);
			ApplicationUtil.showShortMsg("Terminal Key is required");
			
		} else if (isEmpty(registeredby)) {
			requestFocus(R.id.et_registration_registeredby);
			ApplicationUtil.showShortMsg("Registered By is required");
			
		} else { 
			progressDialog.setMessage("Registering device...");
			if (!progressDialog.isShowing()) progressDialog.show();
			
			Platform.runAsync(new ActionProcess(terminalkey, registeredby));   
		}
	}
	
	
	private Handler errorHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();
			
			Bundle data = msg.getData();			
			Object o = data.getSerializable("response");
			if (o instanceof Throwable) {
				Throwable t = (Throwable)o;
				ApplicationUtil.showShortMsg("[ERROR] " + t.getMessage());				
			} else {
				ApplicationUtil.showShortMsg("[ERROR] " + o);
			}
		}
	};
	
	private Handler successHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();
			
			Bundle data = msg.getData();			
			ApplicationUtil.showShortMsg(data.getString("response"));
			Platform.getApplication().resumeAppLoader();
			finish();
		}
	};
	
	
	private class ActionProcess implements Runnable 
	{
		private String terminalkey;
		private String registeredby;
		
		public ActionProcess(String terminalkey, String registeredby) {
			this.terminalkey = terminalkey;
			this.registeredby = registeredby;
		}
		
		@Override
		public void run() {
			boolean success = false;			
			Bundle data = new Bundle();
			Message msg = errorHandler.obtainMessage();
			
			try {
				String deviceId = TerminalManager.getDeviceId();			
				Map<String, Object> params = new HashMap();
				params.put("terminalid", terminalkey);
				params.put("registeredby", registeredby);
				params.put("macaddress", deviceId);
	
				new TerminalService().register(params); 			
				TerminalManager.save(terminalkey);				
				
				data.putString("response", "Device successfully registered.");
				msg = successHandler.obtainMessage();
				success = true; 
			} catch (Throwable e) { 				
				data.putSerializable("response", e);
				e.printStackTrace();
				
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
