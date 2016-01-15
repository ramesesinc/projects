package com.rameses.android;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.widget.Toast;

import com.rameses.client.android.Platform;
import com.rameses.client.android.UIApplication;


public final class ApplicationUtil 
{
	private static boolean isDeviceRegistered = false;
	
	public static boolean getIsDeviceRegistered() { 
		return isDeviceRegistered; 
	}
	
	public static void deviceResgistered() {
		isDeviceRegistered = true;
	}

	public static void showLongMsg(String msg) {
		showLongMsg(msg, Platform.getMainActivity());
	}
	
	public static void showLongMsg(String msg, Activity activity) {
		Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
	}

	public static void showShortMsg(String msg) {
		showShortMsg(msg, Platform.getMainActivity());
	}
	
	public static void showShortMsg(String msg, Activity activity) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	public static Map<String, Object> createMenuItem(String id, String text, String subtext, int iconid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("iconid", iconid);
		
		String t = "";
		if (text != null) t = text;
		map.put("text", t);
		
		String s = "";
		if (subtext != null) s = subtext;
		map.put("subtext", s);
		
		return map;
	}
	
	public static String getAppHost(int networkStatus) {
		UIApplication app = Platform.getApplication();
		return ((AppSettingsImpl) app.getAppSettings()).getAppHost(networkStatus);
	}	
	
	public static String formatDate(Date date, String format) {
		System.out.println("date -> "+date+" format -> "+format);
		if (date == null) return null;
		
		return new java.text.SimpleDateFormat(format).format(date);
	}
	
	private ApplicationUtil() {
	}
}
