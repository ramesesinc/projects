package com.rameses.android.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.bean.DownloadStat;
import com.rameses.android.database.DownloadStatDB;
import com.rameses.android.services.MobileDownloadService;
import com.rameses.android.services.MobileRuleService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class DownloadSectorAccountsController {

	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	private String sectorid;
	private MobileDownloadService service = new MobileDownloadService();
	private MobileRuleService ruleSvc = new MobileRuleService();
	int downloadsize = 0;
	
	public DownloadSectorAccountsController(UIActionBarActivity activity, ProgressDialog progressDialog, String sectorid) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.sectorid = sectorid;
	}
	
	public void execute() throws Exception {
		progressDialog.setMessage("processing...");
		activity.runOnUiThread(new Runnable() {
			public void run() {
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//				progressDialog.setProgress(0);
				if (!progressDialog.isShowing()) progressDialog.show();
			}
		});
		
		Platform.runAsync(new DownloadSectorAccountsProcess());
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
//			Bundle data = msg.getData();
			if (progressDialog.isShowing()) progressDialog.dismiss();
			ApplicationUtil.showShortMsg("Download Complete! " + downloadsize + " records was downloaded!", activity);
//			
//			Intent intent = new Intent(activity, DownloadListActivity.class);
//			intent.putExtra("list", data.getSerializable("list"));
//			activity.startActivity(intent);
			
		}
	};	
	
	void println(String msg) {
		Log.i("DownloadSectorAccountsController", msg);
	}
	
	private class DownloadSectorAccountsProcess implements Runnable {
		public void run() {

			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			try {
				UserProfile prof = SessionContext.getProfile();
				String userid = (prof != null? prof.getUserId() : "");
				String username = (prof != null? prof.getFullName() : "");
				
				SQLTransaction maindb = new SQLTransaction("main.db");
				try {
					maindb.beginTransaction();
					runImpl(maindb, userid, username);
					maindb.commit();
				} catch (Exception e) {
					throw e;
				} finally {
					maindb.endTransaction();
				}	
				
				
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
		
		private void runImpl(SQLTransaction maindb, String userid, String username) throws Exception {
			DownloadStatDB ds = new DownloadStatDB();
			ds.setDBContext(maindb.getContext());
			ds.setAutoCloseConnection(false);
			
			DownloadStat stat = ds.findByUserid(userid);
			String batchid = stat.getBatchid();
			
			Map params = new HashMap();
			boolean has_stat_record = false;
			if (batchid != null) {
				if (stat.getIndexno() >= stat.getRecordcount()) {
					maindb.delete("download_state", "batchid = '" + batchid + "'");
				} else {
					has_stat_record = true;
					params.put("batchid", batchid);
				}
			}  
			
			params.put("sectorid", sectorid);
			params.put("assigneeid", userid);
			 
			batchid = service.initForDownload(params);
			
			if (has_stat_record == false) {
				Map data = new HashMap();
				data.put("batchid", batchid);
				data.put("assigneeid", userid);
				data.put("recordcount", 0);
				data.put("indexno", 0);
				maindb.insert("download_stat", data);
			}
			 
//			int recordcount = service.getBatchStatus(batchid);
			int recordcount = 0;
			while (true) {
				int xstat = service.getBatchStatus(batchid);
				
				if (xstat < 0) {
					//do nothing
				} else {
					recordcount = xstat;
					break;
				}
			}
			
			if (recordcount <= 0) {
				throw new RuntimeException("No data to download");
			}
			
			stat.setRecordcount(recordcount);
			
			if (progressDialog.isShowing()) progressDialog.dismiss();

			final int rc = recordcount;
			activity.runOnUiThread(new Runnable() {
				public void run() {
					progressDialog = new ProgressDialog(activity);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setProgress(0);
					progressDialog.setMax(rc);
					if (!progressDialog.isShowing()) progressDialog.show();
				}
			});
			
			params = new HashMap();
			params.put("recordcount", recordcount);
			maindb.update("download_stat", "batchid = '" + batchid + "'", params);
			
			int indexno = stat.getIndexno();
			int limit = 50, start = (indexno <= 0? 0 : indexno);
			stat = ds.findByBatchid(batchid);
			
			params = new HashMap();
			params.put("batchid", batchid);

			while (start < recordcount) {
				params.put("_start", start);
				params.put("_limit", limit);
				
				List<Map> list = service.download(params);

				if (list != null && !list.isEmpty()) {
					int i = 0, size = list.size();
					Map data = new HashMap();
					for (; i < size; i++) {
						data = (Map) list.get(i);
						data.put("userid", userid);
						data.put("username", username);
						saveAccount(maindb, data);
						
						data.clear();
						data.put("indexno", (start + i));
						maindb.update("download_stat", "batchid = '" + batchid + "'", data);

						progressDialog.incrementProgressBy(1);
					}
				}
				
				start += limit; 
			}
			downloadsize = recordcount;
			
			if (start >= recordcount) {
				maindb.delete("download_stat", "batchid = '" + batchid + "'");
			}

//			println("before clear sector additional info");
			clearSectorAdditionalInfo(maindb, userid);
//			println("before save additional info");
			saveSectorAdditionalInfo(maindb, userid, username);
//			println("before download rules");
			downloadRules(maindb);
//			println("after download rules");
		}
		
		private void saveAccount(SQLTransaction maindb, Map params) throws Exception {
			MapProxy proxy = new MapProxy(params);
			Map data = new HashMap();
			
			data.put("objid", proxy.getString("objid"));
			data.put("acctno", proxy.getString("acctno"));
			data.put("acctname", proxy.getString("acctname"));
			data.put("address", proxy.getString("address"));
			data.put("serialno", proxy.getString("serialno"));
			data.put("sectorid", proxy.getString("sectorid"));
			data.put("sectorcode", proxy.getString("sectorcode"));
			data.put("lastreading", proxy.getString("lastreading"));
			data.put("classificationid", proxy.getString("classificationid"));
			data.put("barcode", proxy.getString("barcode"));
			data.put("batchid", proxy.getString("batchid"));
			data.put("month", proxy.getString("month"));
			data.put("year", proxy.getString("year"));
			data.put("period", proxy.getString("period"));
			data.put("duedate", proxy.getString("duedate"));
			data.put("discodate", proxy.getString("discodate"));
			data.put("rundate", proxy.getString("rundate"));
			data.put("items", proxy.getString("items"));
			data.put("info", proxy.getString("info"));
			data.put("stuboutid", proxy.getString("stuboutid"));
			data.put("sortorder", proxy.getInteger("sortorder"));
			data.put("assignee_objid", proxy.getString("userid"));
			data.put("assignee_name", proxy.getString("username"));

			println("account " + data);
			
			maindb.insert("account", data);
		}
		
		private void clearSectorAdditionalInfo(SQLTransaction maindb, String userid) {
			maindb.delete("stubout", "assigneeid=?", new Object[]{ userid });
			maindb.delete("zone", "assigneeid=?", new Object[]{ userid });
			maindb.delete("sector_reader", "assigneeid=?", new Object[]{ userid });
			maindb.delete("sector", "assigneeid=?", new Object[]{ userid });
		}
		
		private void saveSectorAdditionalInfo(SQLTransaction maindb, String userid, String username) {
			Map params = new HashMap();
			params.put("sectorid", sectorid);
			params.put("userid", userid);
			
			Map data = new HashMap();
			
			List<Map> list = service.getReaderBySector(params);
			int i = 0, size = list.size();
			for (; i < size; i++) {
				data = (Map) list.get(i);
				createSectorReader(maindb, data);
			}
			
			list = service.getZoneBySector(params);
			size = list.size();
			for (i = 0; i < size; i++) {
				data = (Map) list.get(i);
				data.put("assigneeid", userid);
				createZone(maindb, data);
			}
			
			list = service.getStuboutsBySector(params);
			size = list.size();
			for (i = 0; i < size; i++) {
				data = (Map) list.get(i);
				data.put("assigneeid", userid);
				createStubout(maindb, data);
			}
			
		}
		
		private void createSectorReader(SQLTransaction maindb, Map params) {
			MapProxy proxy = new MapProxy(params);
			
			Map data = new HashMap();
			data.put("objid", proxy.getString("objid"));
			data.put("sectorid", proxy.getString("sectorid"));
			data.put("title", proxy.getString("title"));
			
			MapProxy assignee = new MapProxy((Map) proxy.get("assignee"));
			data.put("assigneeid", assignee.getString("objid"));
			data.put("assigneename", assignee.getString("name"));
			maindb.insert("sector_reader", data);
		}
		
		private void createZone(SQLTransaction maindb, Map params) {
			MapProxy proxy = new MapProxy(params);
			
			Map data = new HashMap();
			data.put("objid", proxy.getString("objid"));
			data.put("sectorid", proxy.getString("sectorid"));
			data.put("code", proxy.getString("code"));
			data.put("description", proxy.getString("description"));
			data.put("readerid", proxy.getString("readerid"));
			data.put("assigneeid", proxy.getString("assigneeid"));
			
			maindb.insert("zone", data);
		}
		
		private void createStubout(SQLTransaction maindb, Map params) {
			MapProxy proxy = new MapProxy(params);
			
			Map data = new HashMap();
			data.put("objid", proxy.getString("objid"));
			data.put("code", proxy.getString("code"));
			data.put("description", proxy.getString("description"));
			data.put("zoneid", proxy.getString("zoneid"));
			
			MapProxy barangay = new MapProxy((Map) proxy.get("barangay"));
			String barangayid = barangay.getString("objid");
			String barangayname = barangay.getString("name");
			
			data.put("barangayid", (barangayid != null? barangayid : ""));
			data.put("barangayname", (barangayname != null? barangayname : ""));
			data.put("assigneeid", proxy.getString("assigneeid"));

			maindb.insert("stubout", data);
		}
		
		private void downloadRules(SQLTransaction maindb) {
			List<Map> list = ruleSvc.getRules();
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
		
		private void saveRule(SQLTransaction maindb, Map params) {
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
