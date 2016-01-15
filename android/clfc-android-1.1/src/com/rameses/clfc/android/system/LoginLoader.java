package com.rameses.clfc.android.system;

import android.content.Intent;
import android.util.Log;

import com.rameses.clfc.android.AppSettingsImpl;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.SuspendTimer;
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
		Platform.getApplication().pauseSuspendTimer();
		
		println("before synch date");
		Platform.getApplication().syncServerDate();
		println("after synch date");
		
		String sessionid = SessionContext.getSessionId();
		println("sessionid: " + sessionid); 
		
		AppSettingsImpl settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		String collector_state = settings.getCollectorState();
		println("collector state: " + collector_state);
		println("server date: " + Platform.getApplication().getServerDate());
		 
		/*
		SessionContext sess = (SessionContext) settings.getSession();
		if (sess != null) {
			println("sessionid: " + sess.get("sessionid").toString());
		}
		*/ 
		
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
			Log.i("LoginLoader", "failed caused by " + t.getClass().getName() + ": " + t.getMessage()); 
		} else {
			Log.i("LoginLoader", message.toString());
		}
	}
}
