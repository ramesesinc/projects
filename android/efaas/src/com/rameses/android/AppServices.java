package com.rameses.android;

import com.rameses.client.android.Task;

class AppServices extends Task
{
	private ApplicationImpl app;
	private NetworkCheckerService networkChecker;
	
	AppServices(ApplicationImpl app) {
		networkChecker = new NetworkCheckerService(app);
	} 
	
	public void run() { 
		networkChecker.start();
	}
}
