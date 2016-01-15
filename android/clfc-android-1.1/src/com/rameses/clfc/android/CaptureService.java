package com.rameses.clfc.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.util.Log;

import com.rameses.clfc.android.db.DBCapturePayment;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.Task;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.Base64Cipher;
import com.rameses.util.MapProxy;

public class CaptureService 
{
	private final int SIZE = 6;
	
	private ApplicationImpl app;
	private AppSettingsImpl settings;
	private Handler handler;
	private Task actionTask;
	private SQLTransaction capturedb;
	private DBCapturePayment capturePayment = new DBCapturePayment();
	private boolean hasPayments = false;
	private int size, networkStatus;
	private MapProxy proxy;
	private String mode;
	private Map params = new HashMap();
	private Map collector = new HashMap();
	private Map payment = new HashMap();
	private Map bank = new HashMap();
	private Map check = new HashMap();
	private Map response = new HashMap();
//	private LoanPostingService svc = new LoanPostingService();
	
	private boolean serviceStarted = false;
	
	public CaptureService(ApplicationImpl app) {
		this.app = app;
		settings = (AppSettingsImpl) app.getAppSettings();
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
	
	private void println(String msg) {
		Log.i("CaptureService", msg);
	}
	
	private void createTask() {
		actionTask = new Task() {
			public void run() {
				List<Map> list = new ArrayList<Map>();
				synchronized (CaptureDB.LOCK) {
//					capturedb = new SQLTransaction("clfccapture.db");
					DBContext ctx = new DBContext("clfccapture.db");
					capturePayment.setDBContext(ctx);
					capturePayment.setCloseable(false);
					try {
//						capturedb.beginTransaction();
						list = capturePayment.getPendingPayments(SIZE);
//						capturedb.commit();
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
//						capturedb.endTransaction();
						ctx.close();
					}
				}
				
				execPayments(list);
				
				hasPayments = true;
				
				synchronized (CaptureDB.LOCK) {
					DBContext ctx = new DBContext("clfccapture.db");
					 capturePayment.setDBContext(ctx);
					 capturePayment.setCloseable(false);
					try {
						hasPayments = capturePayment.hasPendingPayments();
					} catch (Throwable t) {
						t.printStackTrace();
						hasPayments = true;
					} finally {
						ctx.close();
					}
				}
//				if (list.size() == SIZE) {
//					hasPayments = true;
//				}
//				
				if (hasPayments == false) {
					serviceStarted = false;
					this.cancel();
				}				
			}
					
			private void execPayments(List<Map> list) {
				if (!list.isEmpty()) {
					size = (list.size() < SIZE-1? list.size() : SIZE-1);
					String option, sql;
					for(int i=0; i<size; i++) {
						proxy = new MapProxy((Map) list.get(i));
						
						mode = "ONLINE_WIFI";
						networkStatus = app.getNetworkStatus();
						if (networkStatus == 1) {
							mode = "ONLINE_MOBILE";
						}
						
						if (networkStatus == 3) {
							break;
						}

						params = new HashMap();
						params.put("captureid", proxy.getString("captureid"));
						params.put("sessionid", proxy.getString("billingid"));
						params.put("state", proxy.getString("state"));
						params.put("trackerid", proxy.getString("trackerid"));
						params.put("lng", proxy.getDouble("lng"));
						params.put("lat", proxy.getDouble("lat"));
						params.put("txndate", proxy.getString("txndate"));
						params.put("type", "CAPTURE");
						params.put("mode", mode);
//						params.put("sessionid", proxy.getString("billingid"));
//						params.put("itemid", proxy.getString("itemid"));
//						params.put("trackerid", proxy.getString("trackerid"));
//						params.put("routecode", proxy.getString("routecode"));
//						params.put("mode", mode);
//						params.put("longitude", proxy.getDouble("lng"));
//						params.put("latitude", proxy.getDouble("lat"));
//						params.put("type", proxy.getString("type"));
//						
						collector.clear();
						collector.put("objid", proxy.getString("collector_objid"));
						collector.put("name", proxy.getString("collector_name"));
						
						params.put("collector", collector);						
						
						payment.clear();
						payment.put("objid", proxy.getString("objid"));
						payment.put("borrowername", proxy.getString("borrowername"));
						payment.put("refno", proxy.getString("refno"));
						payment.put("dtpaid", proxy.getString("dtpaid"));
						payment.put("type", proxy.getString("paytype"));
						payment.put("amount", proxy.getString("amount"));
						payment.put("paidby", proxy.getString("paidby"));
						option = proxy.getString("payoption");
						payment.put("option", option);
						
						if ("check".equals(option)) {
							bank.clear();
							bank.put("objid", proxy.getString("bank_objid"));
							bank.put("name", proxy.getString("bank_name"));
							payment.put("bank", bank);
							
							check.clear();
							check.put("no", proxy.getString("check_no"));
							check.put("date", proxy.getString("check_date"));
							payment.put("check", check);
						}
						
						params.put("payment", payment); 
						
						println("capture service");
						Map param = new HashMap();
						String enc = new Base64Cipher().encode(params);
						param.put("encrypted", enc);
						
						println("app.host " + ApplicationUtil.getAppHost());
						
						if (response == null) response = new HashMap();
						response.clear();
						for (int j=0; j<10; j++) {
							try {
								LoanPostingService svc = new LoanPostingService();
								response = svc.postCapturePaymentEncrypt(param);
								break;
							} catch (Throwable e) {
								println("exception " + e.getMessage());
								e.printStackTrace();
							} 
						}
						
						println("response " + response);
						
						if (response != null && response.containsKey("response")) {
							String str = MapProxy.getString(response, "response").toLowerCase();//response.get("response").toString();
//							println("response string " + str);
							if (str.equals("success")) {
								synchronized (CaptureDB.LOCK) {
									capturedb = new SQLTransaction("clfccapture.db");
									try {
										capturedb.beginTransaction();
										sql = "UPDATE capture_payment SET state = 'CLOSED' WHERE objid=?";
										capturedb.execute(sql, new Object[]{proxy.getString("objid")});
										capturedb.commit();
									} catch (Throwable t) {
										println("error" + t.getMessage());
										t.printStackTrace();
									} finally {
										capturedb.endTransaction();
									}
								}
							}
						}
						
//						if (response.containsKey("response") && response.get("response").toString().toLowerCase().equals("success")) {
//							synchronized (CaptureDB.LOCK) {
//								capturedb = new SQLTransaction("clfccapture.db");
//								try {
//									capturedb.beginTransaction();
//									sql = "UPDATE capture_payment SET state = 'CLOSED' WHERE objid=?";
//									capturedb.execute(sql, new Object[]{proxy.getString("objid")});
//									capturedb.commit();
//								} catch (Throwable t) {
//									t.printStackTrace();
//								} finally {
//									capturedb.endTransaction();
//								}
//							}
//						}
					}
				}
			}
		};
	}
}

