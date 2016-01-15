package com.rameses.clfc.android.services;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class LoanLocationService extends AbstractService 
{
	public String getServiceName() {
		return "MobileLocationService";
	}
	
	public Map postLocationEncrypt(Map params) {
		return (Map) invoke("postLocationEncrypt", params);
	}
	
	public Map postLocation(Map params) {
		return (Map) invoke("postLocation", params);
	}
	
	public Map createTrackerEncrypt(Map params) {
		return (Map) invoke("createTrackerEncrypt", params);
	}
	
	public Map createTracker(Map params) {
		return (Map) invoke("createTracker", params);
	}

}
