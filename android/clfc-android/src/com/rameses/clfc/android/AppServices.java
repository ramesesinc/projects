package com.rameses.clfc.android;

import com.rameses.client.android.Task;

class AppServices extends Task
{
	private ApplicationImpl app;
	private NetworkCheckerService networkChecker;
	private LocationTrackerService locationTracker;  
	
	AppServices(ApplicationImpl app) {
		networkChecker = new NetworkCheckerService(app);
		locationTracker = new LocationTrackerService(app);
	} 
	
	public void run() { 
		networkChecker.start();
		locationTracker.start(); 
//		voidRequest.start();
//		payment.start();
//		remarks.start();
//		remarksRemoved.start();
//		broadcastLocation.start();
	}
}
