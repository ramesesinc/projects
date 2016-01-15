package com.rameses.clfc.android.services;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class LoanTrackerService extends AbstractService 
{
	public String getServiceName() {
		return "MobileTrackerService";
	}
	
	public Map start(Map params) {
		return (Map) invoke("start", params);
	}

}
