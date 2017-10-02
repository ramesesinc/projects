package com.rameses.android.system;

import android.content.Intent;

import com.rameses.client.android.AbstractActionBarActivity;
import com.rameses.client.android.AbstractActivity;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.AppLoader;
import com.rameses.client.interfaces.AppLoaderCaller;

public abstract class AbstractHomeLoader implements AppLoader 
{
	private AppLoaderCaller caller;
	
	public int getIndex() { return 0; }
	
	public void setCaller(AppLoaderCaller caller) {
		this.caller = caller;
	}	

	protected abstract Class<?> getActivityClass(); 	
	
	public void load() {
		try { 
			println("started ...");
			Class<?> homeActivityClass = getActivityClass();
			if (homeActivityClass == null) {
				throw new Exception("Please specify your home activity class");
			}
			
//			AbstractActivity current = Platform.getCurrentActivity();
			AbstractActionBarActivity current = Platform.getCurrentActionBarActivity();
			Intent intent = new Intent(current, homeActivityClass);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			current.startActivity(intent);
			caller.resume();
		} catch(Throwable t) { 
			t.printStackTrace();
			UIDialog.showMessage(t); 
		} 
	}
	
	private void println(Object message) {
		String classname = getClass().getSimpleName();
		if (message instanceof Throwable) {
			Throwable t = (Throwable)message;
			System.err.println("["+classname+"] failed caused by " + t.getClass().getName() + ": " + t.getMessage()); 
		} else {
			System.out.println("["+classname+"] " + message);
		}
	}	
}
