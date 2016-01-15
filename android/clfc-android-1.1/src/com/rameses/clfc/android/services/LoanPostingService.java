package com.rameses.clfc.android.services;

import java.util.List;
import java.util.Map;

import com.rameses.client.services.AbstractService;

public class LoanPostingService extends AbstractService
{
	public String getServiceName() {
		return "MobilePostingService";
	}
	
	public Map voidPaymentEncrypt(Map params) {
		return (Map) invoke("voidPaymentEncrypt", params);
	}
	
	public Map voidPayment(Map params) {
		return (Map) invoke("voidPayment", params);
	}
	
	public Map postPaymentEncrypt(Map params) {
		return (Map) invoke("postPaymentEncrypt", params);
	}
	
	public Map postPayment (Map params) {
		return (Map) invoke("postPayment", params);
	}
	
	public Map updateRemarksEncrypt(Map params) {
		return (Map) invoke("updateRemarksEncrypt", params);
	}
	
	public Map updateRemarks(Map params) {
		return (Map) invoke("updateRemarks", params);
	}
	
	public Map removeRemarksEncrypt(Map params) {
		return (Map) invoke("removeRemarksEncrypt", params);
	}
	
	public Map removeRemarks(Map params) {
		return (Map) invoke("removeRemarks", params);
	}
	
	public Map checkVoidPaymentRequestEncrypt(Map params) {
		return (Map) invoke("checkVoidPaymentRequestEncrypt", params);
	}
	
	public Map checkVoidPaymentRequest(Map params) {
		return (Map) invoke("checkVoidPaymentRequest", params);
	}
	
	public Map postSpecialCollectionRequestEncrypt(Map params) {
		return (Map) invoke("postSpecialCollectionRequestEcncrypt", params);
	}
	
	public Map postSpecialCollectionRequest(Map params) {
		return (Map) invoke("postSpecialCollectionRequest", params);
	}
	
	public Map downloadSpecialCollection(Map params) {
		return (Map) invoke("downloadSpecialCollection", params);
	}
	
	public Map remitCollectionEncrypt(Map params) {
		return (Map) invoke("remitCollectionEncrypt", params);
	}
	
	public Map remitCollection(Map params) {
		return (Map) invoke("remitCollection", params);
	}
	
	public Map postCapturePaymentEncrypt(Map params) {
		return (Map) invoke("postCapturePaymentEncrypt", params);
	}
	
	public Map postCapturePayment(Map params) {
		return (Map) invoke("postCapturePayment", params);
	}
}
