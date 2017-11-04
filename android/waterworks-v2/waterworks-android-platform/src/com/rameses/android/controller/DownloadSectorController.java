package com.rameses.android.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.main.DownloadListActivity;
import com.rameses.android.services.MobileDownloadService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.interfaces.UserProfile;

public class DownloadSectorController {

	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	
	public DownloadSectorController(UIActionBarActivity activity, ProgressDialog progressDialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
	}
	
	public void execute() throws Exception {
		progressDialog.setMessage("processing...");
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (!progressDialog.isShowing()) progressDialog.show();
			}
		});
		
		Platform.runAsync(new FetchingSectorListProcess());
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
			
			Intent intent = new Intent(activity, DownloadListActivity.class);
			intent.putExtra("list", data.getSerializable("list"));
			activity.startActivity(intent);
			
		}
	};	
	
	private class FetchingSectorListProcess implements Runnable {
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
				params.put("userid", userid);
				MobileDownloadService svc = new MobileDownloadService();
				List<Map<String, Object>> result = svc.getSectorByUser(params);
				
				if (result == null) result = new ArrayList<Map<String, Object>>();
				
				data.putSerializable("list", (ArrayList<Map<String, Object>>) result);
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
