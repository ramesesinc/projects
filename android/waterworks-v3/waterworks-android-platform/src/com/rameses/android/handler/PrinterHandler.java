package com.rameses.android.handler;

import java.util.Map;

public interface PrinterHandler {
	
	public String getName();
	public String getTemplate();
	public String getData(Map params);
}
