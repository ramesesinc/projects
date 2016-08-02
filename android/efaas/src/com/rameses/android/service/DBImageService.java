package com.rameses.android.service;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class DBImageService extends AbstractService {

	@Override
	public String getServiceName() {
		return "DBImageService";
	}
	
	public Map saveHeader(Map image){
		return (Map) invoke("saveHeader",image);
	}
	
	public Map saveItem(Map item){
		return (Map) invoke("saveItem",item);
	}

}
