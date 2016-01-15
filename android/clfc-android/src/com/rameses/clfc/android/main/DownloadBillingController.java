package com.rameses.clfc.android.main;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.clfc.android.AppSettingsImpl;
import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.RemarksDB;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBSystemService;
import com.rameses.clfc.android.services.LoanBillingService;
import com.rameses.client.android.NetworkLocationProvider;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.TerminalManager;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class DownloadBillingController 
{
//	private UIActivity activity;
	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	private Map route;
	private LoanBillingService svc;
	private AppSettingsImpl settings;

	DownloadBillingController(UIActionBarActivity activity, ProgressDialog progressDialog, Map route) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.route = route;
		this.settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		svc = new LoanBillingService();
	}
	
	void execute() throws Exception {
		progressDialog.setMessage("Downloading route "+route.get("description").toString()+" - "+route.get("area").toString());
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
			route.put("downloaded", 1);
			activity.getHandler().post(new Runnable() {
				public void run() {
					((RouteListActivity) activity).loadRoutes();
				}
			});
			if (progressDialog.isShowing()) progressDialog.dismiss();
			ApplicationUtil.showShortMsg("Successfully downloaded billing!", activity);
		}
	};	
	
	private class ActionProcess implements Runnable 
	{
		public void run() {
			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			try {
				String billingid = route.get("billingid").toString();
				String routecode = route.get("code").toString();
				
				Map params = new HashMap();
				params.put("billingid", billingid);
				params.put("billdate", route.get("billdate").toString());
				params.put("routecode", routecode);
				params.put("itemid", route.get("itemid").toString());
				params.put("terminalid", TerminalManager.getTerminalId());
				params.put("userid", SessionContext.getProfile().getUserId());
				
				Location location = NetworkLocationProvider.getLocation();
				params.put("lng", (location != null? location.getLongitude() : 0.00));
				params.put("lat", (location != null? location.getLatitude() : 0.00));
				
				String trackerid = settings.getTrackerid();
				String tracker_owner = settings.getTrackerOwner();
				params.put("trackerid", trackerid);
								
				Map map = svc.downloadBilling(params);
//				
//				String mTrackerid = map.get("trackerid").toString();
//				String userid = SessionContext.getProfile().getUserId();
//				
//				if (trackerid == null || "".equals(trackerid)) {
//					settings.put("trackerid", mTrackerid);
//					settings.put("tracker_owner", userid);
//					trackerid = settings.getTrackerid();
//					
//				} else if (!trackerid.equals(mTrackerid)) {
//					if (!userid.equals(tracker_owner)) {
//						settings.put("trackerid", mTrackerid);
//						settings.put("tracker_owner", userid);
//						trackerid = settings.getTrackerid();
//					} else {
//						params = new HashMap();
//						params.put("trackerid", mTrackerid);
//						for (int i=0; i<10; i++) {
//							try {
//								svc.removeTracker(params);
//								break;
//							} catch (Exception ex) {
//								throw ex;
//							}
//						}
//					}
//				}
//				String trackerid = settings.getTrackerid();
//				if (trackerid == null || "".equals(trackerid)) {
//				settings.put("trackerid", map.get("trackerid").toString());
//				}
				
				Platform.getLogger().log("[DownloadBillingController.ActionProcess.run] map-> "+map);
				runImpl(map);
				
				MapProxy proxy = new MapProxy((Map) map.get("item"));
				params.clear();
				params.put("billingid", billingid);
				params.put("trackerid", trackerid);
				params.put("routecode", routecode); 
				params.put("itemid", proxy.getString("objid"));
				for (int i=0; i<10; i++) {
					try {
						svc.notifyBillingDownloaded(params);
						break;
					} catch (Exception ex) {
						throw ex;
					}
				}
				
				data.putString("response", "success");
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
		
		private void runImpl(Map map) throws Exception {
			SQLTransaction clfcdb = new SQLTransaction("clfc.db");
//			SQLTransaction paymentdb = new SQLTransaction("clfcpayment.db");
//			SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
//			try {
//				clfcdb.beginTransaction();
//				synchronized (MainDB.LOCK) {
//					Map params = new HashMap();
//					params.put("name", "trackerid");
//					params.put("value", map.get("trackerid").toString());
//					clfcdb.insert("sys_var", params);
//				}
//				clfcdb.commit();
//			} catch (Exception e) {
//				Map params = new HashMap();
//				params.put("trackerid", map.get("trackerid").toString());
//				for (int i=0; i<10; i++) {
//					try {
//						svc.removeTracker(params);
//						break;
//					} catch (Exception ex) {;}
//				}
//				throw e;
//			} finally {
//				clfcdb.endTransaction();
//			}
			
			try {
				clfcdb.beginTransaction();
//				paymentdb.beginTransaction();
//				remarksdb.beginTransaction();
				
				execute(clfcdb, map);
				
				clfcdb.commit();
//				paymentdb.commit();
//				remarksdb.commit();
			} catch (Exception e) {
				throw e;
			} finally {
				clfcdb.endTransaction();
//				paymentdb.endTransaction();
//				remarksdb.endTransaction();
			}
		}
		
		private void execute(SQLTransaction clfcdb, Map map) throws Exception {
			DBSystemService sysSvc = new DBSystemService();
			sysSvc.setDBContext(clfcdb.getContext());
			sysSvc.setCloseable(false);
			
			Map params = new HashMap();
//			String sql = "SELECT * FROM sys_var WHERE name='trackerid'";
//			Map record = clfcdb.find(sql);
//			if (record == null || record.isEmpty()) {
//				params.clear();
//				params.put("name", "trackerid");
//				params.put("value", map.get("trackerid").toString());
//				clfcdb.insert("sys_var", params);
//			}

			String date = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyyy-MM-dd");
			String collectorid = SessionContext.getProfile().getUserId();
			String billingid = "";//route.get("billingid").toString();
			//String sql = "SELECT * FROM sys_var WHERE name='billingid'";
			//Map record = clfcdb.find(sql);
			synchronized (MainDB.LOCK) {
				billingid = sysSvc.getBillingid(collectorid, date);
				if (billingid == null || "".equals(billingid)) {
					billingid = route.get("billingid").toString();
					params.clear();
					params.put("name", collectorid + "-" + date);
					params.put("value", billingid);
					clfcdb.insert("sys_var", params);
				}				
			}
			
			MapProxy proxy = new MapProxy((Map) map.get("item"));
			if (!proxy.isEmpty()) {
				Map item = new HashMap();
				item.put("objid", proxy.getString("objid"));
				item.put("state", "ACTIVE");
				item.put("description", route.get("description").toString());
				item.put("area", route.get("area").toString());
				item.put("billingid", proxy.getString("parentid"));
				item.put("billdate", route.get("billdate").toString());
				item.put("collectorid",collectorid);
				item.put("type", "route");
				synchronized (MainDB.LOCK) {
					clfcdb.insert("collection_group", item);
				}
			}
			/*params = new HashMap();
			params.put("routecode",route.get("code").toString());
			params.put("state", "ACTIVE");
			params.put("routedescription", route.get("description").toString());
			params.put("routearea", route.get("area").toString());
			params.put("sessionid", billingid);
			params.put("collectorid", SessionContext.getProfile().getUserId());
			clfcdb.insert("route", params);*/
			
			DBCollectionSheet csSvc = new DBCollectionSheet();
			csSvc.setDBContext(clfcdb.getContext());
			List billings = (ArrayList) map.get("billings");
			List list;
			String loanappid;
			csSvc.dropIndex();
			Map p = new HashMap();
			int size = billings.size();
						
			Map item;
			int s;
			MapProxy m, offer;
			BigDecimal amountdue = new BigDecimal("0").setScale(2);
			BigDecimal amount = amountdue;
			for (int i=0; i<size; i++) {
				proxy = new MapProxy((Map) billings.get(i));
				
				item = clfcdb.find("SELECT objid FROM collectionsheet WHERE objid=? LIMIT 1", new Object[]{proxy.getString("objid")});
				if (item != null) {
					System.out.println("item " + item);
				} else {
					amountdue = new BigDecimal(proxy.getDouble("amountdue")+"").setScale(2);
					params.clear();
					params.put("objid", proxy.getString("objid"));
					params.put("billingid", proxy.getString("billingid"));
					params.put("itemid", proxy.getString("parentid"));
					params.put("seqno", proxy.getInteger("seqno"));
					params.put("borrower_objid", proxy.getString("acctid"));
					params.put("borrower_name", proxy.getString("acctname"));
					params.put("loanapp_objid", proxy.getString("loanappid"));
					params.put("loanapp_appno", proxy.getString("appno"));
					params.put("loanapp_loanamount", proxy.getDouble("loanamount"));
					params.put("amountdue", proxy.getDouble("amountdue"));
					params.put("overpaymentamount", proxy.getDouble("overpaymentamount"));
					params.put("refno", proxy.getString("refno"));
					params.put("routecode", proxy.getString("routecode"));
					params.put("term", proxy.getInteger("term"));
					params.put("releasedate", proxy.getString("dtreleased"));
					params.put("maturitydate", proxy.getString("dtmatured"));
					params.put("dailydue", proxy.getDouble("dailydue"));
					params.put("balance", proxy.getDouble("balance"));
					params.put("interest", proxy.getDouble("interest"));
					params.put("penalty", proxy.getDouble("penalty"));
					params.put("others", proxy.getDouble("others"));
					params.put("homeaddress", proxy.getString("homeaddress"));
					params.put("collectionaddress", proxy.getString("collectionaddress"));
					params.put("type", "NORMAL");
					params.put("paymentmethod", proxy.getString("paymentmethod"));
					params.put("isfirstbill", proxy.getInteger("isfirstbill"));
					params.put("totaldays", proxy.getInteger("totaldays"));
					synchronized (MainDB.LOCK) {
						clfcdb.insert("collectionsheet", params);
					}
				}
				
				if (proxy.containsKey("notes")) {
					list = (List) proxy.get("notes");
					s = list.size();
					String txndate;
					for (int j=0; j<s; j++) {
						m = new MapProxy((Map) list.get(j));

						txndate = new SimpleDateFormat("yyyy-MM-dd").parse(m.getString("dtcreated")).toString();
//						txndate = ApplicationUtil.formatDate(java.sql.Date.valueOf(m.getString("dtcreated")), "yyyy-MM-dd");
						
						params.clear();
						params.put("objid", m.getString("objid"));
						params.put("parentid", proxy.getString("objid"));
						params.put("itemid", proxy.getString("parentid"));
						params.put("txndate", txndate);
						params.put("dtcreated", m.getString("dtcreated"));
						params.put("remarks", m.getString("remarks"));
						synchronized (MainDB.LOCK) {
							clfcdb.insert("notes", params);
						}
					}
				}
				
				if (proxy.containsKey("payments")) {
					list = (List) proxy.get("payments");
//					System.out.println("payments " + list);
					s = list.size();
					String option;
					for (int j=0; j<s; j++) {
						m = new MapProxy((Map) list.get(j));
						
						amount = new BigDecimal(m.getDouble("amount")+"");
						String posttype = "Schedule";
						int val = amount.compareTo(amountdue);
						if (val < 0) {
							posttype = "Underpayment";
						} else if (val > 0) {
							posttype = "Overpayment";
						}
						
						params.clear();
						params.put("objid", m.getString("objid"));
						params.put("parentid", m.getString("parentid"));
//						params.put("state", m.getString("state"));
						params.put("itemid", m.getString("itemid"));
						params.put("billingid", m.getString("billingid"));
//						params.put("trackerid", m.getString("trackerid"));
						params.put("collector_objid", SessionContext.getProfile().getUserId());
//						params.put("collector_name", SessionContext.getProfile().getFullName());
//						params.put("borrower_objid", m.getString("borrower_objid"));
//						params.put("borrower_name", m.getString("borrower_name"));
//						params.put("loanapp_objid", m.getString("loanapp_objid"));
//						params.put("loanapp_appno", m.getString("loanapp_appno"));
//						params.put("routecode", m.getString("routecode"));
						params.put("txndate", m.getString("txndate"));
						params.put("refno", m.getString("refno"));
						params.put("posttype", posttype);
						params.put("paytype", m.getString("paytype"));
						params.put("amount", m.getDouble("amount"));
						params.put("paidby", m.getString("paidby"));
//						params.put("isfirstbill", m.getString("isfirstbill"));
//						params.put("lng", m.getDouble("lng"));
//						params.put("lat", m.getDouble("lat"));
//						params.put("type", m.getString("type"));
						
						option = m.getString("payoption");
						params.put("payoption", option);
						
						if ("check".equals(option)) {
							params.put("bank_objid", m.getString("bank_objid"));
							params.put("bank_name", m.getString("bank_name"));
							params.put("check_no", m.getString("check_no"));
							params.put("check_date", m.getString("check_date"));
						}
						
						synchronized (MainDB.LOCK) {
							clfcdb.insert("payment", params);
						}
					}
				}
				
				if (proxy.containsKey("remarkslist")) {
					list = (List) proxy.get("remarkslist");
					s = list.size();
					int isfollowup = 0;
					for (int j=0; j<s; j++) {
						m = new MapProxy((Map) list.get(j));
						
						isfollowup = m.getInteger("isfollowup");
						params.clear();
						params.put("objid", "REM" + UUID.randomUUID().toString());
						params.put("parentid", proxy.getString("objid"));
						params.put("txndate", m.getString("txndate"));
						params.put("collectorname", m.getString("collectorname"));
						params.put("remarks", m.getString("remarks"));
						
						synchronized (MainDB.LOCK) {
							if (isfollowup == 0) {
								clfcdb.insert("collector_remarks", params);
							} else if (isfollowup == 1) {
								clfcdb.insert("followup_remarks", params);
							}
						}
					}
				}
				
				if (proxy.containsKey("remarks") && proxy.get("remarks") != null) {
					params.clear();
					params.put("objid", proxy.getString("objid"));
					params.put("billingid", proxy.getString("billingid"));
					params.put("itemid", proxy.getString("parentid"));
					params.put("remarks", proxy.getString("remarks"));
					params.put("collector_objid", SessionContext.getProfile().getUserId());
					params.put("collector_name", SessionContext.getProfile().getFullName());
//					params.put("state", "CLOSED");
//					params.put("trackerid", "UPLOADED");
//					params.put("txndate", Platform.getApplication().getServerDate().toString());
//					params.put("borrower_objid", proxy.getString("acctid"));
//					params.put("borrower_name", proxy.getString("acctname"));
//					params.put("loanapp_objid", proxy.getString("loanappid"));
//					params.put("loanapp_appno", proxy.getString("appno"));
//					params.put("routecode", proxy.getString("routecode"));
//					params.put("lng", 0.00);
//					params.put("lat", 0.00);
//					params.put("type", "SYSTEM");
					synchronized (MainDB.LOCK) {
						clfcdb.insert("remarks", params);
					}
				}
				
				if (proxy.containsKey("amnesty") && proxy.get("amnesty") != null) {
					m = new MapProxy((Map) proxy.get("amnesty"));
					if (!m.isEmpty()) {
						params.clear();
						params.put("objid", m.getString("objid"));
						params.put("parentid", proxy.getString("objid"));
						params.put("refno", m.getString("refno"));
						params.put("dtstarted", m.getString("dtstarted"));
						params.put("dtended", m.getString("dtended"));
						params.put("amnestyoption", m.getString("amnestyoption"));						
						params.put("iswaivepenalty", m.getInteger("iswaivepenalty"));
						params.put("iswaiveinterest", m.getInteger("iswaiveinterest"));
						offer = new MapProxy((Map) m.get("grantedoffer"));
						params.put("grantedoffer_amount", offer.getDouble("amount"));
						params.put("grantedoffer_days", offer.getInteger("days"));
						params.put("grantedoffer_months", offer.getInteger("months"));
						params.put("grantedoffer_isspotcash", offer.getInteger("isspotcash"));
						params.put("grantedoffer_date", offer.getString("date"));
						synchronized (MainDB.LOCK) {
							clfcdb.insert("amnesty", params);
						}
					}
				}
			}
			csSvc.addIndex();
		}
	}
}
