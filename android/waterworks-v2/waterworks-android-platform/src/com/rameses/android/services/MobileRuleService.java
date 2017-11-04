package com.rameses.android.services;

import java.util.List;
import java.util.Map;

import com.rameses.client.services.AbstractService;

public class MobileRuleService extends AbstractService  {

	public String getServiceName() {
		return "WaterworksMobileRuleService";
	}
	
	public List<Map> getRules() {
		return (List<Map>) invoke("getRules");
	}

}
