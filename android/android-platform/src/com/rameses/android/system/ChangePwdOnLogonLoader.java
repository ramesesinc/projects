package com.rameses.android.system;

import android.content.Intent;

import com.rameses.client.android.AbstractActivity;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.AppLoader;
import com.rameses.client.interfaces.AppLoaderCaller;

public class ChangePwdOnLogonLoader implements AppLoader 
{
	private AppLoaderCaller caller;
	
	public int getIndex() { return -998; }
	
	public void setCaller(AppLoaderCaller caller) {
		this.caller = caller;
	}	

	public void load() {		
		try { 
			String sessionid = SessionContext.getSessionId();
			if (sessionid == null || sessionid.length() == 0) {
				caller.resume();
				return; 
			}
			
			Object value = SessionContext.get("CHANGE_PWD_ON_LOGIN");
			if ("true".equals(value+"")) {
				showForm();
			} else {
				caller.resume(); 
			}
		} catch(Throwable t) { 
			UIDialog.showMessage(t); 
		} 
	}
	
	private void showForm() {
		AbstractActivity current = Platform.getCurrentActivity();
		Intent intent = new Intent(current, ChangePwdOnLogonActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		current.startActivity(intent);
	} 
	
	private void println(Object message) {
		if (message instanceof Throwable) {
			Throwable t = (Throwable)message;
			System.err.println("[ChangePwdOnLogonLoader] failed caused by " + t.getClass().getName() + ": " + t.getMessage()); 
		} else {
			System.out.println("[ChangePwdOnLogonLoader] " + message);
		}
	}	
}
