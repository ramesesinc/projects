package com.rameses.android;

import android.content.Context;

import com.rameses.android.handler.OneilPrinterHandler;
import com.rameses.android.handler.PrinterHandler;
import com.rameses.android.handler.ZebraPrinterHandler;
import com.rameses.client.android.AppSettings;

public class AppSettingsImpl extends AppSettings 
{
	AppSettingsImpl() {
		
	}
	
	public String getHost() {
		return getString("setting_host", "192.168.254.1");
	}
	
	public int getPort() {
		return getInt("setting_port", 8070);
	}
	
	public String getContext() {
		return getString("setting_context", "etracs25");
	}
	
	public String getCluster() {
		return getString("setting_cluster", "osiris3");
	}
	
	public int getReadTimeout() {
		return getInt("setting_readtimeout", 30000);
	}
	
	public String getPrinterName() {
		return getString("printer_name", "XXXXJ134800315");
	}
	
	public PrinterHandler getPrinterHandler(Context ctx) {
		PrinterHandler handler = null;
		
		String ph = getString("printer_handler", "ZEBRA");
		
		if ("ZEBRA".equals(ph)) {
			handler = new ZebraPrinterHandler(ctx);
		} else if ("ONEIL".equals(ph)) {
			handler = new OneilPrinterHandler(ctx);
		}
		
		return handler;
	}
		
//	public String getAppHost(int networkStatus) {
//		String host = getOnlineHost(); 
//		if (networkStatus == 0) host = getOfflineHost();
//		
//		return host + ":" + getPort(); 
//	}
//	
//	public String getAppState() {  
//		return getString("app_state", "idle");  
//	}
//	
//	public String getOnlineHost() {  
//		return getString("host_online", "121.97.60.200"); 
//	}
//	
//	public String getOfflineHost() {  
//		return getString("host_offline", "121.97.60.200"); 
//	}
//	
//	public int getPort() {  
//		return getInt("host_port", 8070); 
//	}
//	
//	public int getSessionTimeout() {  
//		return getInt("timeout_session", 3); 
//	}
//	
//	public int getUploadTimeout() { 
//		return getInt("timeout_upload", 5); 
//	}
//	
//	public int getTrackerTimeout() { 
//		return getInt("timeout_tracker", 10); 
//	} 
//	
//	public String getDebugEnabled() {
//		return getString("debug_enabled", "false");
//	}
//	
//	public String getTrackerid() {
//		return getString("trackerid", null);
//	}
}
