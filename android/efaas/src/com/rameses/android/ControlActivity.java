package com.rameses.android;

import android.widget.TextView;

import com.rameses.android.R;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActivity;

public class ControlActivity extends UIActivity 
{
	
	public ApplicationImpl getApp() {
		return (ApplicationImpl) Platform.getApplication(); 
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		String mode = "NOT CONNECTED";		
		int networkStatus = getApp().getNetworkStatus();
		if (networkStatus == 0) {
			mode = "ONLINE_WIFI"; 
		} else if (networkStatus == 1) {
			mode = "ONLINE_MOBILE";  
		}
		if(isActivated())((TextView) findViewById(R.id.tv_mode)).setText(mode);		
	}

	
	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		if (SessionContext.getSessionId() != null) {
			Platform.getApplication().restartSuspendTimer();
		} 
	}
	
	public boolean isActivated(){
		return false;
	}
}
