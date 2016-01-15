package com.rameses.clfc.android;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.rameses.client.android.AbstractActionBarActivity;

class NetworkCheckerService 
{
	private ApplicationImpl app;
	private NetworkInfo networkInfo;
	private Handler handler;
	
	public NetworkCheckerService(ApplicationImpl app) {
		this.app = app;
	}
	
	public void start() {
		if (handler == null) { 
			handler = new Handler();
			new RunnableImpl().run(); 
		} 
	}
		
	private class RunnableImpl implements Runnable 
	{
		NetworkCheckerService root = NetworkCheckerService.this;
		
		public void run() {
			try {
				runImpl();
			} catch(Throwable t) {
				t.printStackTrace();
			}
			
			try {
				root.handler.postDelayed(this, 1000); 
			} catch(Throwable t) {
				t.printStackTrace();
			}			
		}
		
		private void runImpl() throws Exception {
			networkInfo = app.getConnectivityManager().getActiveNetworkInfo();
			
			final String[] modes = new String[]{"NOT CONNECTED"};
			if (networkInfo != null && networkInfo.isConnected()) {
				if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
					app.setNetworkStatus(1); 
					modes[0] = "ONLINE_MOBILE";
				} else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					app.setNetworkStatus(0);
					modes[0] = "ONLINE_WIFI";
				} else {
					app.setNetworkStatus(3);
				}
			} else {
				app.setNetworkStatus(3);
			}
			
			final AbstractActionBarActivity activity = app.getCurrentActionBarActivity();
			if (activity == null) return;
			
			activity.runOnUiThread(new Runnable() {
				public void run() {
					View v = activity.findViewById(R.id.tv_mode);
					if (v != null) ((TextView) v).setText(modes[0]);
				} 
			}); 
		}
	}
}