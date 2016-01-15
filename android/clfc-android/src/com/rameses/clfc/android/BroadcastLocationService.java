package com.rameses.clfc.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;

import com.rameses.clfc.android.db.DBLocationTracker;
import com.rameses.clfc.android.services.LoanLocationService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.Task;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class BroadcastLocationService 
{
	private final int SIZE = 6;
	
	private ApplicationImpl app;
	private Handler handler;
	private SQLTransaction trackerdb;
	private DBLocationTracker locationTracker = new DBLocationTracker();
	private MapProxy proxy;
	private Map params;
	private Map response = new HashMap();
	private int listSize;
	private LoanLocationService svc = new LoanLocationService();
	private boolean hasLocationTrackers = false;
	private Task actionTask;
	
	public boolean serviceStarted = false;
	
	public BroadcastLocationService(ApplicationImpl app) {
		this.app = app;
	}
	
	public void start() { 
		if (handler == null) { 
			handler = new Handler();
//			new RunnableImpl().run(); 
		} 
		if (serviceStarted == false) {
			serviceStarted = true;
			createTask();
			Platform.getTaskManager().schedule(actionTask, 1000, 5000);
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
				synchronized (TrackerDB.LOCK) {
//					trackerdb = new SQLTransaction("clfctracker.db")
					DBContext ctx = new DBContext("clfctracker.db");
					locationTracker.setDBContext(ctx);
					locationTracker.setCloseable(false);
					try {
//						trackerdb.beginTransaction();
						list = locationTracker.getLocationTrackers(SIZE);					
//						trackerdb.commit();
					} catch (Throwable t) {
						t.printStackTrace();
					} finally { 
//						trackerdb.endTransaction();
						ctx.close();
					}
				}
				
				execTracker(list);
				
				hasLocationTrackers = false;
				if (list.size() == SIZE) {
					hasLocationTrackers = true;
				}
				
				if (hasLocationTrackers == false) {
					serviceStarted = false;
					this.cancel();
				}
				
//				synchronized (TrackerDB.LOCK) {
//					DBContext ctx = new DBContext("clfctracker.db");
//					locationTracker.setDBContext(ctx);
//					try {
//						hasLocationTrackers = locationTracker.hasLocationTrackers();
//						
//					} catch (Throwable t) {
//						t.printStackTrace();
//					}
//				}

//				if (hasLocationTrackers == true) {
//					Platform.getTaskManager().schedule(runnableImpl, 5000);
//				} else if (hasLocationTrackers == false) {
//					serviceStarted = false;
//				}
			}

			private void execTracker(List<Map> list) {			
				if (!list.isEmpty()) {
					listSize = (list.size() < SIZE-1? list.size() : SIZE-1);
					for (int i=0; i<listSize; i++) {
						proxy  = new MapProxy((Map) list.get(i));
						
//						params.clear();
						params = new HashMap();
						params.put("objid", proxy.getString("objid"));
						params.put("trackerid", proxy.getString("trackerid"));
						params.put("txndate", proxy.getString("txndate"));
						params.put("lng", proxy.getDouble("lng"));
						params.put("lat", proxy.getDouble("lat"));
						params.put("state", 1);
						
						response.clear();
						for (int j=0; j<10; j++) {
							try {
								response = svc.postLocation(params);
								break;
							} catch (Throwable e) {;}
						}
						
						if (response.containsKey("response") && response.get("response").toString().toLowerCase().equals("success")) {
							synchronized (TrackerDB.LOCK) {
								trackerdb = new SQLTransaction("clfctracker.db");
								locationTracker.setDBContext(trackerdb.getContext());
								try {
									trackerdb.beginTransaction();
									trackerdb.delete("location_tracker", "objid=?", new Object[]{proxy.getString("objid")});
									trackerdb.commit();
								} catch (Throwable t) {
									t.printStackTrace();
								} finally {
									trackerdb.endTransaction();
								}
							}
						}
					}
				}
			}
		};
	}
	
}
