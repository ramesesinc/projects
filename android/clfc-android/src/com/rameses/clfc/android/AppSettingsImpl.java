package com.rameses.clfc.android;

import java.util.Map;

import com.rameses.client.android.AppSettings;
import com.rameses.client.android.Platform;

public class AppSettingsImpl extends AppSettings 
{
	AppSettingsImpl() {
		
	}
	
	protected void afterSave(Map data) {
		ApplicationImpl app = (ApplicationImpl) Platform.getApplication();
		if (app != null) {
			app.locationTrackerSvc.restart();
//			app.paymentSvc.restart();
//			app.broadcastLocationSvc.restart();
//			app.remarksRemovedSvc.restart();
//			app.remarksSvc.restart();
//			app.voidRequestSvc.restart();
		}
	}
	
	public String getAppHost(int networkStatus) {
		String host = getOnlineHost(); 
		if (networkStatus == 0) host = getOfflineHost();
		
		return host + ":" + getPort(); 
	}
	
	public String getCollectorState() {
		return getString("collector_state", "logout");
	}
	
	public String getAppState() {  
		return getString("app_state", "idle");  
	}
	
	public String getOnlineHost() {  
		return getString("host_online", "121.97.60.200"); 
	}
	
	public String getOfflineHost() {  
		return getString("host_offline", "121.97.60.200"); 
	}
	
	public int getPort() {  
		return getInt("host_port", 8070); 
	}
	
	public int getSessionTimeout() {  
		return getInt("timeout_session", 3); 
	}
	
	public int getUploadTimeout() { 
		return getInt("timeout_upload", 5); 
	}
	
	public int getTrackerTimeout() { 
		return getInt("timeout_tracker", 10); 
	} 
	
	public String getDebugEnabled() {
		return getString("debug_enabled", "false");
	}
	
	public String getTrackerid() {
		return getString("trackerid", null);
	}
	
	public String getTrackerOwner() {
		return getString("tracker_owner", null);
	}
	
	public String getCaptureid() {
		return getString("captureid", null);
	}
	
	public String getKey() {
		return "clfc";
	}
}
