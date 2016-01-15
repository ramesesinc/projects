package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.services.LoanBillingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.SQLTransaction;

class DownloadRoutesController 
{
//	private UIActivity activity;
	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	
	DownloadRoutesController(UIActionBarActivity activity, ProgressDialog progressDialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
	}
	
	void execute() throws Exception {
		progressDialog.setMessage("processing...");
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (!progressDialog.isShowing()) progressDialog.show();
			}
		});
		
		Platform.runAsync(new ActionProcess());
	}
	
	private Handler errorhandler = new Handler() {  
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();
			
			Bundle data = msg.getData();			
			Object o = data.getSerializable("response"); 
			if (o instanceof Throwable) {
				Throwable t = (Throwable)o;
				ApplicationUtil.showShortMsg("[ERROR] " + t.getMessage());		
			} else {
				ApplicationUtil.showShortMsg("[ERROR] " + o);	
			} 
		} 
	}; 
	
	private Handler successhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			if (progressDialog.isShowing()) progressDialog.dismiss();
			Intent intent = new Intent(activity, RouteListActivity.class);
			intent.putExtra("routes", data.getSerializable("routes"));
			intent.putExtra("followups", data.getSerializable("followups"));
			intent.putExtra("specials", data.getSerializable("specials"));
			activity.startActivity(intent);
		}
	};	
	
	private class ActionProcess implements Runnable 
	{
		public void run() {
			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			try {				
//				processDB();
				//new RouteFetcher().run();
				UserProfile prof = SessionContext.getProfile();
				String userid = (prof != null? prof.getUserId() : "");
				
				Map params = new HashMap();
				params.put("collectorid", userid);
				LoanBillingService svc = new LoanBillingService();
//				ArrayList routes = (ArrayList) svc.getRoutes(params);
				System.out.println("passing");
				Map result = svc.getForDownloadBilling(params);
				
				if (result == null) result = new HashMap();
				
				data.putSerializable("routes", ((ArrayList) result.get("routes")));
				data.putSerializable("followups",((ArrayList) result.get("followups")));
				data.putSerializable("specials", ((ArrayList) result.get("specials")));
				handler = successhandler;
				message = handler.obtainMessage();
				
			} catch(Throwable t) { 
				data.putSerializable("response", t);
				handler = errorhandler;
				message = handler.obtainMessage();				
				t.printStackTrace();
			} 
			
			message.setData(data);
			handler.sendMessage(message); 
		}
		
	}		
}
