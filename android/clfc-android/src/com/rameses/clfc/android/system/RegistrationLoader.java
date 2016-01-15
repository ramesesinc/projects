package com.rameses.clfc.android.system;

import java.util.Map;

import android.content.Intent;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.client.android.Platform;
import com.rameses.client.android.TerminalManager;
import com.rameses.client.android.UIActionBarMain;
import com.rameses.client.interfaces.AppLoader;
import com.rameses.client.interfaces.AppLoaderCaller;
import com.rameses.client.services.TerminalService;

public class RegistrationLoader implements AppLoader 
{
	private AppLoaderCaller caller;
	
	public int getIndex() {
		return -1000;
	}

	public void setCaller(AppLoaderCaller caller) {
		this.caller = caller;
	}
	
	public void load() {
		println("started");
				
		try {
			String deviceid = TerminalManager.getDeviceId();			
			String terminalid = TerminalManager.getTerminalId();
			if (terminalid == null || terminalid.length() == 0) 
				throw new Exception("TERMINALID_NOT_FOUND");  
		
			TerminalService svc = new TerminalService(); 
			Object response = svc.findTerminal(terminalid+"");
			if (response == null) {  
				throw new Exception("TERMINALID_NOT_FOUND");
				
			} else if (response instanceof Map) {
				Map map = (Map) response;
				String macaddress = map.get("macaddress")+"";
				if (macaddress.equals(deviceid)) {
					println("ok");
					ApplicationUtil.deviceResgistered();					
					caller.resume();
				} else {
					println("macaddress does not matched");
					showRegistration();
				}
			}
		} catch (Throwable e) {
			println(e);
			e.printStackTrace();
			showRegistration();
		}
	}
	

	private void showRegistration() {
//		UIMain uimain = Platform.getMainActivity();
		UIActionBarMain uiactionbarmain = Platform.getActionBarMainActivity();
		Intent intent = new Intent(uiactionbarmain, RegistrationOptionActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		uiactionbarmain.startActivity(intent);
	}
	
	private void println(Object message) {
		if (message instanceof Throwable) {
			Throwable t = (Throwable)message;
			System.err.println("[LoaderRegistration] failed caused by " + t.getClass().getName() + ": " + t.getMessage()); 
		} else {
			System.out.println("[LoaderRegistration] " + message);
		}
	}
}
