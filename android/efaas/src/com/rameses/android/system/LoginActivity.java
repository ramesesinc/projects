package com.rameses.android.system;

import java.util.HashMap;
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
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIApplication;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserSetting;
import com.rameses.client.services.LoginService;

public class LoginActivity extends SettingsMenuActivity 
{
	private ProgressDialog progressDialog;
	
	public boolean isCloseable() { return false; }

	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		
		setContentView(R.layout.template_footer);
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_login, rl_container, true);
		
		RelativeLayout login_container = (RelativeLayout) findViewById(R.id.login_container);
		inflater.inflate(R.layout.dialog_login, login_container, true);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
	}
	
	protected void afterBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		requestFocus(R.id.login_username);		
		new UIAction(this, R.id.btn_login) {
			protected void onClick() {
				try {
					doLogin();
				} catch(Throwable t) {
					UIDialog.showMessage("[ERROR] " + t.getMessage()); 
				}
			}
		};
	}
	
	private void doLogin() {
		String username = getValueAsString(R.id.login_username);
		String password = getValueAsString(R.id.login_password);
		if (isEmpty(username)) {
			requestFocus(R.id.login_username);
			ApplicationUtil.showShortMsg("Username is required");
			
		} else if (isEmpty(password)) {
			requestFocus(R.id.login_password);
			ApplicationUtil.showShortMsg("Password is required");
			
		} else {
			progressDialog.setMessage("Logging in...");
			if (!progressDialog.isShowing()) progressDialog.show();
						
			Platform.runAsync(new ActionProcess(username, password));
		}		
	}
	
	private Handler errorhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {	
			try { 
				Bundle data = msg.getData();			
				Object o = data.getSerializable("response"); 
				if (o instanceof Throwable) {
					Throwable t = (Throwable)o;
					ApplicationUtil.showShortMsg("[ERROR] " + t.getMessage());
				} else {
					ApplicationUtil.showShortMsg("[ERROR] " + o);	
				}
			} finally { 
				if (progressDialog.isShowing()) progressDialog.dismiss(); 
			}
		}
	}; 
	
	private Handler successhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();

			Bundle data = msg.getData();			
			Map map = (Map) data.getSerializable("response");
			UIApplication app = Platform.getApplication();
			app.getAppSettings().putAll(map);
			app.restartSuspendTimer();
			app.resumeAppLoader();
		}
	};
	
	private class ActionProcess implements Runnable 
	{
		private String username;
		private String password;
		
		public ActionProcess(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public void run() { 
			boolean success = false;			
			Bundle data = new Bundle();
			Message msg = errorhandler.obtainMessage();
			
			try {
				new LoginService().login(username, password);
				
				UserSetting userSets = SessionContext.getSettings();
				String onlinehost 	= userSets.getOnlineHost();
				String offlinehost 	= userSets.getOfflineHost();
				int sessiontimeout 	= userSets.getSessionTimeout();
				int uploadtimeout 	= userSets.getUploadDelay();
				int trackertimeout 	= userSets.getTrackerDelay();
				int port 			= userSets.getPort();
				
				HashMap<String,Object> map = new HashMap();
				if (port > 0) map.put("host_port", port);				
				if (onlinehost != null) map.put("host_online", onlinehost);
				if (offlinehost != null) map.put("host_offline", offlinehost);
				if (uploadtimeout > 0) map.put("timeout_upload", uploadtimeout);				
				if (sessiontimeout > 0) map.put("timeout_session", sessiontimeout);
				if (trackertimeout > 0) map.put("timeout_tracker", trackertimeout);
				
				data.putSerializable("response", map);
				msg = successhandler.obtainMessage();				
				success = true;
				
			} catch (Throwable e) {
				data.putSerializable("response", e);
				e.printStackTrace();
				
			} finally { 
				msg.setData(data);
				if (success) {
					successhandler.sendMessage(msg);
				} else {
					errorhandler.sendMessage(msg);
				}
			}
		}
	}
}
