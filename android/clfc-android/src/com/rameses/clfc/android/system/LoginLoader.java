package com.rameses.clfc.android.system;

import android.content.Intent;

import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarMain;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.AppLoader;
import com.rameses.client.interfaces.AppLoaderCaller;

public class LoginLoader implements AppLoader
{
	private AppLoaderCaller caller;
	
	public int getIndex() { return -999; }

	public void setCaller(AppLoaderCaller caller) {
		this.caller = caller;
	}
	
	public void load() {
		println("started");

		Platform.getApplication().syncServerDate();		
		String sessionid = SessionContext.getSessionId();
		if (sessionid == null || sessionid.length() == 0) {
			try {
				showLogin();
			} catch(Throwable t) {
				UIDialog.showMessage(t); 
			} 
		} else {
			Platform.getApplication().restartSuspendTimer();
			caller.resume(); 
		} 
	}
	
	private void showLogin() {
		UIActionBarMain uiactionbarmain = Platform.getActionBarMainActivity();
		Intent intent = new Intent(uiactionbarmain, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		uiactionbarmain.startActivity(intent);
	}
	
	private void println(Object message) {
		if (message instanceof Throwable) {
			Throwable t = (Throwable)message;
			System.err.println("[LoginLoader] failed caused by " + t.getClass().getName() + ": " + t.getMessage()); 
		} else {
			System.out.println("[LoginLoader] " + message);
		}
	}
}
