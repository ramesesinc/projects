package com.rameses.android.services;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class MobileUploadService extends AbstractService {

	@Override
	public String getServiceName() {
		return "WaterworksMobileUploadService";
	}

	public Map upload(Map params) {
		return (Map) invoke("upload", params);
	}
}
