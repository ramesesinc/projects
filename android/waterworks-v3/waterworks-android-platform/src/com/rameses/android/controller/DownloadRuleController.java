package com.rameses.android.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.main.RateListActivity;
import com.rameses.android.services.MobileRuleService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class DownloadRuleController {

	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	private MobileRuleService service = new MobileRuleService();
	
	public DownloadRuleController(UIActionBarActivity activity, ProgressDialog progressDialog) {
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
		
		Platform.runAsync(new FetchingRuleListProcess());
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
			if (progressDialog.isShowing()) progressDialog.dismiss();
			activity.getHandler().post(new Runnable() {
				public void run() {
					((RateListActivity) activity).loadList();
				}
			});
//			Intent intent = new Intent(activity, DownloadListActivity.class);
//			intent.putExtra("list", data.getSerializable("list"));
//			activity.startActivity(intent);
			
		}
	};	
	
	private class FetchingRuleListProcess implements Runnable {
		public void run() {
			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			try {
				
				SQLTransaction maindb = new SQLTransaction("main.db");
				try {
					maindb.beginTransaction();
					downloadRules(maindb);
					maindb.commit();
				} catch (Exception e) {
					throw e;
				} finally {
					maindb.endTransaction();
				}
				
//				processDB();
				//new RouteFetcher().run();
//				UserProfile prof = SessionContext.getProfile();
//				String userid = (prof != null? prof.getUserId() : "");
//				
//				Map params = new HashMap();
//				params.put("userid", userid);
//				MobileDownloadService svc = new MobileDownloadService();
//				List<Map<String, Object>> result = svc.getSectorByUser(params);
//				
//				if (result == null) result = new ArrayList<Map<String, Object>>();
//				
//				data.putSerializable("list", (ArrayList<Map<String, Object>>) result);
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

		
		private void downloadRules(SQLTransaction maindb) throws Exception {
			List<Map> list = service.getRules();
			if (list != null && !list.isEmpty()) {
				maindb.execute("DELETE FROM rule");
			}
			
			Map data = new HashMap();
			int i = 0, size = list.size();
			for (; i < size; i++) {
				data = (Map) list.get(i);
				saveRule(maindb, data);
			}
		}
		
		private void saveRule(SQLTransaction maindb, Map params) throws Exception {
			MapProxy proxy = new MapProxy(params);
			
			Map data = new HashMap();
			data.put("salience", proxy.getInteger("salience"));
			data.put("condition", proxy.getString("condition"));
			data.put("var", proxy.getString("var"));
			data.put("action", proxy.getString("action"));

			maindb.insert("rule", data);
		}
	}
}
