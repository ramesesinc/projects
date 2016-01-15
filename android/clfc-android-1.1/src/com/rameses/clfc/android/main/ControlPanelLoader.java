package com.rameses.clfc.android.main;

import android.content.Intent;

import com.rameses.client.android.AbstractActionBarActivity;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.AppLoader;
import com.rameses.client.interfaces.AppLoaderCaller;

public class ControlPanelLoader implements AppLoader 
{
	private AppLoaderCaller caller;
	
	public int getIndex() { return 0; }
	
	public void setCaller(AppLoaderCaller caller) {
		this.caller = caller;
	}	

	public void load() {
		try { 
			println("started");
//			AbstractActivity current = Platform.getCurrentActivity();
			AbstractActionBarActivity current = Platform.getCurrentActionBarActivity();
			Intent intent = new Intent(current, ControlPanelActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			current.startActivity(intent);
			caller.resume();
		} catch(Throwable t) { 
			UIDialog.showMessage(t); 
		} 
	}
	
	private void println(Object message) {
		if (message instanceof Throwable) {
			Throwable t = (Throwable)message;
			System.err.println("[ControlPanelLoader] failed caused by " + t.getClass().getName() + ": " + t.getMessage()); 
		} else {
			System.out.println("[ControlPanelLoader] " + message);
		}
	}	
}
