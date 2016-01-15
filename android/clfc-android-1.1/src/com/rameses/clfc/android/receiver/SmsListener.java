package com.rameses.clfc.android.receiver;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.rameses.clfc.android.AppSettingsImpl;
import com.rameses.client.android.Platform;

public class SmsListener extends BroadcastReceiver {
	
	private String KEY = ((AppSettingsImpl) Platform.getApplication().getAppSettings()).getKey();	
	
	public void onReceive(Context ctx, Intent intent) {
		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			final Bundle bundle = intent.getExtras();
			 
	        try {
				if (bundle != null) {
				     
					final Object[] pdusObj = (Object[]) bundle.get("pdus");
					 
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();
					String message = currentMessage.getDisplayMessageBody();
					
					String key = message.substring(0, 4);
					System.out.println("KEY " + KEY);
					if (KEY.equals(key.toLowerCase())) { 
					    	String msgtext = "";
					    	String code = message.substring(5, 9);
					    	if ("1000".equals(code)) {
					    		msgtext = checkPhoneStatus(ctx, phoneNumber); 
					    	}
					    	
					    	if (message != null && !"".equals(message)) {
						        SmsManager sms = SmsManager.getDefault();
						        sms.sendTextMessage(phoneNumber, null, msgtext, null, null);
					    	}
					}
				} // bundle is null
	 
	        } catch (Exception e) {
	            Log.e("SmsReceiver", "Exception smsReceiver" +e);
	             
	        }
		}
	}

	private String checkPhoneStatus(Context ctx, String phoneNumber) {
		WifiManager wifimngr = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		LocationManager locationmngr = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		String message = "Wi-fi enabled: " + wifimngr.isWifiEnabled() + "\n";
		
		boolean mobileDataEnabled = false; // Assume disabled
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
		    Class cmClass = Class.forName(cm.getClass().getName());
		    Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
		    method.setAccessible(true); // Make the method callable
		// get the setting for "mobile data"
		    mobileDataEnabled = (Boolean)method.invoke(cm);
		} catch (Exception e) {
		    // Some problem accessible private API
		// TODO do whatever error handling you want here
		}
		
		message += "Mobile enabled: " + mobileDataEnabled + "\n";
		message += "GPS enabled: " + locationmngr.isProviderEnabled(LocationManager.GPS_PROVIDER) + "\n";
		message += "Network enabled: " + locationmngr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return message;
	}
}
