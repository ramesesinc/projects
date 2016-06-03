package com.rameses.android.service;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class DownloadService extends AbstractService {

	@Override
	public String getServiceName() {
		return "EfaasDownloadService";
	}
	
	public Map getFaas(String tdno){
		return (Map) invoke("getFaas",tdno);
	}

}
