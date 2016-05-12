package com.rameses.android.service;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class RevisionSettingService extends AbstractService {

	@Override
	public String getServiceName() {
		return "EfaasRevisionSettingService";
	}
	
	public Map getLandRevisionSettingData() throws Exception{
		return (Map) invoke("getLandRevisionSettingData");
	}

}
