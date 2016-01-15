package com.rameses.clfc.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;

import com.rameses.clfc.android.db.DBSpecialCollectionPendingService;
import com.rameses.clfc.android.db.DBSystemService;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.Task;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.Base64Cipher;
import com.rameses.util.MapProxy;

public class SpecialCollectionService 
{
	private final int SIZE = 6;
	
	private ApplicationImpl app;
	private AppSettingsImpl appSettings;
	private Handler handler;
	private DBSpecialCollectionPendingService service = new DBSpecialCollectionPendingService();
	private DBSystemService systemSvc = new DBSystemService();
//	private LoanPostingService postingSvc = new LoanPostingService();
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	private Task actionTask;

	private boolean serviceStarted = false;
	
	public SpecialCollectionService(ApplicationImpl app) {
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
			createTask();
			Platform.getTaskManager().schedule(actionTask, 1000, 1000);
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
				DBContext ctx = new DBContext("clfcspecialcollection.db");
				service.setDBContext(ctx);
				service.setCloseable(false);
				
				synchronized (SpecialCollectionDB.LOCK) {
					try {
						list = service.getPendingSpecialCollection(SIZE);
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
						ctx.close();
					}
				}
				
				try {
					execRequest(list);
				} catch (Throwable t) {
					t.printStackTrace();
				}
				
				
				boolean hasUnpostedRequests = true;
				ctx = new DBContext("clfcspecialcollection.db");
				service.setDBContext(ctx);
				service.setCloseable(false);
				synchronized (SpecialCollectionDB.LOCK) {
					try {
						list = service.getPendingSpecialCollection(SIZE);
						if (list.isEmpty() || list.size() == 0) {
							hasUnpostedRequests = false;
						}
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
						ctx.close();
					}
				}
				
				if (hasUnpostedRequests == false) {
					serviceStarted = false;
					this.cancel();
				}
			}
			
			private void execRequest(List<Map> list) throws Exception {
				if (!list.isEmpty()) {
					int size = (list.size() < SIZE-1? list.size() : SIZE-1);
					MapProxy proxy;
					Map params, collector, response, param;

					String collectorid, date, objid, rp;
					
//					String collectorid = SessionContext.getProfile().getUserId();
//					String date = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyyy-MM-dd");
//					
//					DBContext ctx = new DBContext("clfc.db");
//					systemSvc.setDBContext(ctx);
//					systemSvc.setCloseable(false);
//					synchronized (MainDB.LOCK) {
//						
//					}
					
					for (int i=0; i<size; i++) {
						proxy = new MapProxy((Map) list.get(i));
						
						objid = proxy.getString("objid");
						
						params = new HashMap();
						params.put("objid", objid);
						params.put("specialcollectionid", objid);
						params.put("state", "PENDING");
						params.put("remarks", proxy.getString("remarks"));
						
						collectorid = proxy.getString("collector_objid");
						String xdate = proxy.getString("txndate");
						date = "";
						if (xdate != null && !xdate.equals("")) {
							date = df.format(df.parse(xdate)); 
						} 
						params.put("billingid", systemSvc.getBillingid(collectorid, date));
						
						collector = new HashMap();
						collector.put("objid", collectorid);
						collector.put("name", proxy.getString("collector_name"));
						params.put("collector", collector);
						
						response = new HashMap();
						
						param = new HashMap();
						String enc = new Base64Cipher().encode(params);
						param.put("encrypted", enc);
						
						LoanPostingService postingSvc = new LoanPostingService();
						response = postingSvc.postSpecialCollectionRequestEncrypt(param);
						
						rp = "";
						if (response.containsKey("response")) {
							rp = MapProxy.getString(response, "response").toLowerCase();
						}
						
						if (rp.equals("success")) {
							removePending(params);
						}
					}
				}
			}
			
			private void removePending(Map params) throws Exception {
				SQLTransaction pendingscdb = new SQLTransaction("clfcspecialcollection.db");
				SQLTransaction maindb = new SQLTransaction("clfc.db");
				
				try {
					pendingscdb.beginTransaction();
					maindb.beginTransaction();
					
					removePendingImpl(params, pendingscdb, maindb);
					
					pendingscdb.commit();
					maindb.commit();
				} catch (Exception e) {
					throw e;
				} finally {
					pendingscdb.endTransaction();
					maindb.endTransaction();
				}
			}
			
			private void removePendingImpl(Map params, SQLTransaction pendingscdb, 
					SQLTransaction maindb) throws Exception {
				
				String objid = MapProxy.getString(params, "objid");
				pendingscdb.delete("specialcollection", "objid = '" + objid + "'");

				String sql = "UPDATE specialcollection SET state = 'FOR_DOWNLOAD' WHERE objid = '" + objid + "'";
				maindb.execute(sql);
			}
		};
	}
	
	
	
	
	
}
