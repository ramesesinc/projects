package com.rameses.clfc.android.services;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class LoanLocationService extends AbstractService 
{
	public String getServiceName() {
		return "MobileLocationService";
	}
	
	public Map postLocation(Map params) {
		return (Map) invoke("postLocation", params);
	}
	
	public Map createTracker(Map params) {
		return (Map) invoke("createTracker", params);
	}

}
