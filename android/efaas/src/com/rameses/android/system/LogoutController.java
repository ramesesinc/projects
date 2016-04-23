package com.rameses.android.system;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.android.AppSettingsImpl;
import com.rameses.android.ApplicationUtil;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIActivity;
import com.rameses.client.android.UIApplication;
import com.rameses.client.android.UIDialog;
import com.rameses.client.services.LogoutService;

public class LogoutController 
{
	private UIActivity activity;
	private AppSettingsImpl settings;
	private ProgressDialog progressDialog;
	
	public LogoutController(UIActivity activity, ProgressDialog progressDialog) {
		this.activity = activity; 
		this.progressDialog = progressDialog; 
		this.settings = (AppSettingsImpl) Platform.getApplication().getAppSettings(); 
	} 

	protected boolean beforeLogout() { return true; }
	
	protected void afterLogout() {}
	
	public void logout() throws Exception { 
		if (!beforeLogout()) return;
		
		UIDialog dialog = new UIDialog(activity) {
			
			public void onApprove() {
				progressDialog.setMessage("Logging out...");
				if (!progressDialog.isShowing()) progressDialog.show();
				
				Platform.runAsync(new LogoutActionProcess()); 
			}
		};
		dialog.confirm("Are you sure you want to logout?");
	}
	
	
	private Handler errorhandler = new Handler() {  
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
	
	private Handler successhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();

			try {
				afterLogout();
			} catch(Throwable t) {
				t.printStackTrace();
			}
			
			UIApplication uiapp = Platform.getApplication();
			uiapp.logout(); 
		}
	};	
	
	private class LogoutActionProcess implements Runnable 
	{
		public void run() {
			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			try {
				runImpl();
				data.putString("response", "success");
				handler = successhandler;
				message = handler.obtainMessage();
				
			} catch(Throwable t) { 
				data.putSerializable("response", t);
				handler = errorhandler;
				message = handler.obtainMessage();				
				t.printStackTrace();
			} 
			
			message.setData(data);
			handler.sendMessage(message); 
		}
		
		private void runImpl() throws Exception {
			try { 
				new LogoutService().logout(); 
			} catch (Exception e) { 
				e.printStackTrace(); 
			}  
		}
	} 
}
