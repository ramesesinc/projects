package com.rameses.clfc.android.services;

import java.util.Map;

import com.rameses.client.services.AbstractService;

public class LoanBillingService extends AbstractService
{

	public String getServiceName() {
		return "MobileBillingService";
	} 
	
	public Map getForDownloadBilling(Map params) {
		return (Map) invoke("getForDownloadBilling", params);
	}
	
	public Map downloadBilling(Map params) {
		return (Map) invoke("downloadBilling", params);
	}
	
	public Map downloadSpecialCollection(Map params) {
		return (Map) invoke("downloadSpecialCollection", params);
	}
	
	public void removeTracker(Map params) {
		invoke("removeTracker", params);
	}
	
	public void notifyBillingDownloaded(Map params) {
		invoke("notifyBillingDownloaded", params);
	}
	
	public void notifySpecialCollectionDownloaded(Map params) {
		invoke("notifySpecialCollectionDownloaded", params);
	}
}