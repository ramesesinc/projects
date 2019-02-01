package com.rameses.android;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
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
//		showLongMsg(msg, Platform.getMainActivity());
		showLongMsg(msg, Platform.getActionBarMainActivity());
	}
	
	public static void showLongMsg(String msg, Activity activity) {
		if (activity == null) return;
		
		showLongMsg(msg, (Context) activity);
	}

	public static void showLongMsg(String msg, Context ctx) {
		if (ctx == null);
		
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}

	public static void showShortMsg(String msg) {
//		showShortMsg(msg, Platform.getMainActivity());
		showShortMsg(msg, Platform.getActionBarMainActivity());
	}

	public static void showShortMsg(String msg, Activity activity) {
		if (activity == null) return;
		
		showShortMsg(msg, (Context) activity);
	}
	
	public static void showShortMsg(String msg, Context ctx) {
		if (ctx == null) return;

		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
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
	
	private static UIApplication getApplication() {
		return Platform.getApplication();
	}
	
	public static String getAppCluster() {
		return ((AppSettingsImpl) getApplication().getAppSettings()).getCluster();
	}
	
	public static String getAppContext() {
		return ((AppSettingsImpl) getApplication().getAppSettings()).getContext();
	}
	
	public static String getAppHost() {
		UIApplication app = getApplication();
		AppSettingsImpl sets = (AppSettingsImpl) app.getAppSettings();
		String ip = sets.getHost();
		int port = sets.getPort();
		return ip + ":" + String.valueOf(port);
	}

	public static String readingFormat(String val) {
		String str = "";
		
		int s = val.length();
		if (s > 6) {
			int idx = s - 6	;
			str = val.substring(idx, s);
		} else {
			str = "";
			s = 6 - s;
			for (int i = 0; i < s; i++) {
				str += "0";
			}
			str += val;
		}
		
		
		return str;
	}
	
	
//	public static String getAppHost(int networkStatus) {
//		UIApplication app = Platform.getApplication();
//		return ((AppSettingsImpl) app.getAppSettings()).getAppHost(networkStatus);
//	}	
	
	public static String formatDate(Date date, String format) {
		System.out.println("date -> "+date+" format -> "+format);
		if (date == null) return null;
		
		return new java.text.SimpleDateFormat(format).format(date);
	}
	
	private ApplicationUtil() {
	}
}
