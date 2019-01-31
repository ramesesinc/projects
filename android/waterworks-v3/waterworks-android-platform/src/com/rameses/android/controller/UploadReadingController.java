package com.rameses.android.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.main.UploadActivity;
import com.rameses.android.services.MobileUploadService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class UploadReadingController {

	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	private List readingList;
	
	public UploadReadingController(UIActionBarActivity activity, ProgressDialog progressDialog, List readingList) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.readingList = readingList;
	}
	
	public void execute() throws Exception {
		progressDialog.setMessage("processing...");
		activity.runOnUiThread(new Runnable() {
			public void run() {
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setProgress(0);
				progressDialog.setMax(readingList.size());
				if (!progressDialog.isShowing()) progressDialog.show();
			}
		});
		
		Platform.runAsync(new UploadReadingProcess());
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
			activity.getHandler().post(new Runnable() {
				public void run() {
					((UploadActivity) activity).reloadNoOfAccounts();
				}
			});
			
//			Intent intent = new Intent(activity, DownloadListActivity.class);
//			intent.putExtra("list", data.getSerializable("list"));
//			activity.startActivity(intent);
			
		}
	};	
	
	void println(String msg) {
		Log.i("UploadReadingController", msg);
	}
	
	private class UploadReadingProcess implements Runnable {
		
		private MobileUploadService svc = new MobileUploadService();
		
		public void run() {
			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			
			SQLTransaction maindb = new SQLTransaction("main.db");
			try {
				maindb.beginTransaction();
//				processDB();
				//new RouteFetcher().run();
//				UserProfile prof = SessionContext.getProfile();
//				String userid = (prof != null? prof.getUserId() : "");
				
//				ReadingDB readingdb = new ReadingDB();
//				DBContext ctx = new DBContext("main.db");
//				List list = readingdb.getReadingByAssigneeid(userid);
				runImpl(maindb, readingList);
//				Map params = new HashMap();
//				params.put("userid", userid);
//				MobileDownloadService svc = new MobileDownloadService();
//				List<Map<String, Object>> result = svc.getSectorByUser(params);
//				
//				if (result == null) result = new ArrayList<Map<String, Object>>();
				
//				data.putSerializable("list", (ArrayList<Map<String, Object>>) result);
				maindb.commit();
				handler = successhandler;
				message = handler.obtainMessage();
				
			} catch(Throwable t) { 
				data.putSerializable("response", t);
				handler = errorhandler;
				message = handler.obtainMessage();				
				t.printStackTrace();
			} finally {
				maindb.endTransaction();
			}
			
			message.setData(data);
			handler.sendMessage(message);
		}
		
		private void runImpl(SQLTransaction maindb, List list) throws Exception {
			Iterator itr = list.iterator();
			
			UserProfile prof = SessionContext.getProfile();
			String userid = (prof != null? prof.getUserId() : "");
			String name = (prof != null? prof.getFullName() : "");
			
//			while (itr.hasNext()) {
//				println("item " + itr.next());
//			} 
			
			while (itr.hasNext()) {
				MapProxy proxy = new MapProxy((Map) itr.next());
//				println(map + "");
				Map account = new HashMap();
				account.put("objid", proxy.getString("acctid"));
				
				Map params = new HashMap();
				params.put("objid", proxy.getString("objid"));
				params.put("account", account);				
				params.put("reading", proxy.getInteger("reading"));
				params.put("dtreading", proxy.getString("dtreading"));
				params.put("userid", userid);
				params.put("name", name);
				params.put("amount", proxy.getDouble("amtdue"));
				params.put("batchid", proxy.getString("batchid"));
								
				Map result = svc.upload(params);
				
				if (!result.isEmpty()) {
					maindb.delete("reading", "acctid = '" + proxy.getString("acctid") + "'");
					maindb.delete("account", "objid = '" + proxy.getString("acctid") + "'");
				}

				progressDialog.incrementProgressBy(1);
			}
		}
	}
}
