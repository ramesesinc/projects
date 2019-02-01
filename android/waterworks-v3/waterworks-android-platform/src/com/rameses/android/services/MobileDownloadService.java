package com.rameses.android.services;

import java.util.List;
import java.util.Map;

import com.rameses.client.services.AbstractService;

public class MobileDownloadService extends AbstractService {

	public String getServiceName() {
		return "WaterworksMobileDownloadService";
	}
	
	public List<Map<String, Object>> getSectorByUser(Map params) {
		return (List<Map<String, Object>>) invoke("getSectorByUser", params);
	}

	public String initForDownload(Map params) {
		return (String) invoke("initForDownload", params);
	}
	
	public int getBatchStatus(String batchid) {
		return Integer.parseInt(invoke("getBatchStatus", batchid).toString());
	}
	
	public List<Map> download(Map params) {
		return (List<Map>) invoke("download", params);
	}
	
	public List<Map> getReaderBySector(Map params) {
		return (List<Map>) invoke("getReaderBySector", params);
	}
	
	public List<Map> getZoneBySector(Map params) {
		return (List<Map>) invoke("getZoneBySector", params);
	}
	
	public List<Map> getStuboutsBySector(Map params) {
		return (List<Map>) invoke("getStuboutsBySector", params);
	}
}
