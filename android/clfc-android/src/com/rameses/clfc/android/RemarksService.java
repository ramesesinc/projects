package com.rameses.clfc.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;

import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.Task;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class RemarksService 
{
	private final int SIZE = 6;
	
	private ApplicationImpl app;
	private AppSettingsImpl appSettings;
	private Handler handler;
	private SQLTransaction remarksdb;
	private DBRemarksService remarksSvc = new DBRemarksService();
	private String mode = "";
	private MapProxy proxy;
	private Map params = new HashMap();
	private Map loanapp = new HashMap();
	private Map borrower = new HashMap();
	private Map response = new HashMap();
	private Map collector = new HashMap();
	private Map collectionSheet = new HashMap();
	private int networkStatus = 0;
	private int delay;
	private int size;
	private LoanPostingService svc = new LoanPostingService();
	private boolean hasUnpostedRemarks = false;
	private Task actionTask;
	
	public static boolean serviceStarted = false;
	
	public RemarksService(ApplicationImpl app) {
		this.app = app;
		appSettings = (AppSettingsImpl) app.getAppSettings();
	}
	
	public void start() {
		if (handler == null) { 
			handler = new Handler();
//			new RunnableImpl().run();
		} 
		if (serviceStarted == false) {
			serviceStarted = true;
			delay = appSettings.getUploadTimeout()*1000;
			createTask();
			Platform.getTaskManager().schedule(actionTask, 1000, delay);
		}
	}
	
	public void restart() {
		if (serviceStarted == true) {
			actionTask.cancel();
			actionTask = null;
			serviceStarted = false;
		}
		start();
	}
	
	private void createTask() {
		actionTask = new Task() {
			public void run() {
				List<Map> list = new ArrayList<Map>();
//				synchronized (RemarksDB.LOCK) {
//					remarksdb = new SQLTransaction("clfcremarks.db");
					DBContext ctx = new DBContext("clfcremarks.db");
					remarksSvc.setDBContext(ctx);
					remarksSvc.setCloseable(false);
					try {
//						remarksdb.beginTransaction();
						list = remarksSvc.getPendingRemarks(SIZE);					
//						remarksdb.commit();
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
//						remarksdb.endTransaction();
						ctx.close();
					}
//				}
				
				try {
					execRemarks(list);
				} catch (Throwable t) {
					t.printStackTrace();
				}

				hasUnpostedRemarks = false;
//				synchronized (RemarksDB.LOCK) {
					ctx = new DBContext("clfcremarks.db");
					remarksSvc.setDBContext(ctx);
					try {
						hasUnpostedRemarks = remarksSvc.hasUnpostedRemarks();
					} catch (Throwable t) {
						t.printStackTrace();
					}
//				}
				
				if (hasUnpostedRemarks == false) {
					serviceStarted = false;
					this.cancel();
				}
			}

			private void execRemarks(List<Map> list) {
				if (!list.isEmpty()) {
					size = (list.size() < SIZE-1? list.size() : SIZE-1);
					for (int i=0; i<size; i++) {
						proxy = new MapProxy((Map) list.get(i));
						
						mode = "ONLINE_WIFI";
						networkStatus = app.getNetworkStatus();
						if (networkStatus == 1) {
							mode = "ONLINE_MOBILE";
						}
						
						params.clear();
						params.put("sessionid", proxy.getString("billingid"));
						params.put("itemid", proxy.getString("itemid"));
						params.put("routecode", proxy.getString("routecode"));
						params.put("trackerid", proxy.getString("trackerid"));
						params.put("mode", mode);
						params.put("longitude", proxy.getDouble("lng"));
						params.put("latitude", proxy.getDouble("lat"));
						params.put("type", proxy.getString("type"));
						params.put("remarks", proxy.getString("remarks"));
						params.put("txndate", proxy.getString("txndate"));
						
						collector.clear();
						collector.put("objid", proxy.getString("collector_objid"));
						collector.put("name", proxy.getString("collector_name"));
						
						params.put("collector", collector);
						
						collectionSheet.clear();
						collectionSheet.put("detailid", proxy.getString("objid"));
						
						loanapp.clear();
						loanapp.put("objid", proxy.getString("loanapp_objid"));
						loanapp.put("appno", proxy.getString("loanapp_appno"));
						collectionSheet.put("loanapp", loanapp);
						
						borrower.clear();
						borrower.put("objid", proxy.getString("borrower_objid"));
						borrower.put("name", proxy.getString("borrower_name"));
						collectionSheet.put("borrower", borrower);
						
						params.put("collectionsheet", collectionSheet);						
						
						response.clear();
						for (int j=0; j<10; j++) {
							try {
								response = svc.updateRemarks(params);
								break;
							} catch (Throwable e) { e.printStackTrace(); }
						}
						
						if (response.containsKey("response") && response.get("response").toString().toLowerCase().equals("success")) {
							synchronized (RemarksDB.LOCK) {
								remarksdb = new SQLTransaction("clfcremarks.db");
								remarksSvc.setDBContext(remarksdb.getContext());
								try {
									remarksdb.beginTransaction();
									remarksSvc.closeRemarks(proxy.getString("objid"));
									remarksdb.commit();
								} catch (Throwable t) {
									t.printStackTrace();
								} finally {
									remarksdb.endTransaction();
								}
							}
						}
					}
				}
			}
		};
	}
}
