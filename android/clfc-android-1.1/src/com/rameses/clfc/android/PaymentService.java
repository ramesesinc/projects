package com.rameses.clfc.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.util.Log;

import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.Task;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.Base64Cipher;
import com.rameses.util.MapProxy;

public class PaymentService 
{
	private final int SIZE = 6;
	
	private ApplicationImpl app;
	private AppSettingsImpl appSettings;
	private Handler handler;
	private SQLTransaction paymentdb;
	private DBPaymentService paymentSvc = new DBPaymentService();
	
	private String mode = "";
	private Map params = new HashMap();
	private Map loanapp = new HashMap();
	private Map payment = new HashMap(); 
	private Map response = new HashMap();
	private Map borrower = new HashMap();
	private Map collector = new HashMap();
	private Map collectionSheet = new HashMap();
	private Map bank = new HashMap();
	private Map check = new HashMap();
	private int networkStatus = 0;
	private MapProxy proxy;
	private int size;
	private int delay;
	//private LoanPostingService svc = new LoanPostingService();
	private boolean hasUnpostedPayments;
	private Task actionTask;

	private boolean serviceStarted = false;
	
	public PaymentService(ApplicationImpl app) {
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
				System.out.println("[PaymentService.run]");
				List<Map> list = new ArrayList<Map>();
				DBContext ctx = null;
				synchronized (PaymentDB.LOCK) {
//					paymentdb = new SQLTransaction("clfcpayment.db");
					ctx = new DBContext("clfcpayment.db");
					paymentSvc.setDBContext(ctx);
					paymentSvc.setCloseable(false);
					try {
//						paymentdb.beginTransaction();
						list = paymentSvc.getPendingPayments(SIZE);
//						paymentdb.commit();
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
//						paymentdb.endTransaction();
						ctx.close();
					}
				}
				
				try {
					execPayment(list);
				} catch (Throwable t) {
					t.printStackTrace();
				}

				hasUnpostedPayments = true;
				synchronized (PaymentDB.LOCK) {
					ctx = new DBContext("clfcpayment.db");
					paymentSvc.setDBContext(ctx);
					paymentSvc.setCloseable(false);
					try {
						list = paymentSvc.getPendingPayments(SIZE);
						if (list.isEmpty() || list.size() == 0) {
							hasUnpostedPayments = false;
						}
					} catch (Throwable t) {
						t.printStackTrace();
					} finally {
						ctx.close();
					}
				}
//				if (list.size() == SIZE) {
//					hasUnpostedPayments = true;
//				}

				if (hasUnpostedPayments == false) {
					serviceStarted = false;
					this.cancel();
				}
			}

			private void execPayment(List<Map> list) {
				
				if (!list.isEmpty()) {
					size = (list.size() < SIZE-1? list.size() : SIZE-1);
					String option;
					for(int i=0; i<size; i++) {
						proxy = new MapProxy((Map) list.get(i));
						
						mode = "ONLINE_WIFI";
						networkStatus = app.getNetworkStatus();
						if (networkStatus == 1) {
							mode = "ONLINE_MOBILE";
						} else if (networkStatus == 3) {
							break;
						}

						params.clear();
						params.put("sessionid", proxy.getString("billingid"));
						params.put("itemid", proxy.getString("itemid"));
						params.put("trackerid", proxy.getString("trackerid"));
						params.put("routecode", proxy.getString("routecode"));
						params.put("mode", mode);
						params.put("longitude", proxy.getDouble("lng"));
						params.put("latitude", proxy.getDouble("lat"));
						params.put("type", proxy.getString("type"));
						
						collector.clear();
						collector.put("objid", proxy.getString("collector_objid"));
						collector.put("name", proxy.getString("collector_name"));
						
						params.put("collector", collector);
						
//						System.out.println("loanappid -> "+map.containsKey("loanappid"));
//						mCollectionSheet = collectionSheetSvc.findCollectionSheetByLoanappid(proxy.getString("loanappid"));//clfcdb.find(sql, new Object[]{proxy.getString("loanappid")});//dbCs.findCollectionSheetByLoanappid();
						collectionSheet.clear();
						collectionSheet.put("detailid", proxy.getString("parentid"));
						
						loanapp.clear();
						loanapp.put("objid", proxy.getString("loanapp_objid"));
						loanapp.put("appno", proxy.getString("loanapp_appno"));
						collectionSheet.put("loanapp", loanapp);
						
						borrower.clear();
						borrower.put("objid", proxy.getString("borrower_objid"));
						borrower.put("name", proxy.getString("borrower_name"));
						collectionSheet.put("borrower", borrower);
						
						params.put("collectionsheet", collectionSheet);
						
						payment.clear();
						payment.put("objid", proxy.getString("objid"));
						payment.put("refno", proxy.getString("refno"));
						payment.put("txndate", proxy.getString("txndate"));
						payment.put("type", proxy.getString("paytype"));
						payment.put("amount", proxy.getString("amount"));
						payment.put("paidby", proxy.getString("paidby"));
						payment.put("isfirstbill", proxy.getString("isfirstbill"));
						payment.put("overpaymentamount", proxy.getDouble("overpaymentamount"));
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
						
						Map param = new HashMap();
						String enc = new Base64Cipher().encode(params);
						param.put("encrypted", enc);
						
						println("app.host " + ApplicationUtil.getAppHost());
						
						if (response == null) response = new HashMap();
						response.clear();
						for (int j=0; j<10; j++) {
							try {
								println("app.host " + ApplicationUtil.getAppHost());
								LoanPostingService svc = new LoanPostingService();
								response = svc.postPaymentEncrypt(param);
								closePayment(response, proxy.getString("objid"));
								break;
							} catch (Throwable e) {
								e.printStackTrace();
							} 
						}
					}
				}
			}
			
			private void println(String msg) {
				Log.i("PaymentService", msg);
			}
			
			private void closePayment(Map response, String objid) throws Exception {
				String str = "";
				if (response.containsKey("response")) {
					str = response.get("response").toString().toLowerCase();
				}
				if ("success".equals(str)) {
					synchronized (PaymentDB.LOCK) {
						paymentdb = new SQLTransaction("clfcpayment.db");
						paymentSvc.setDBContext(paymentdb.getContext());
						try {
							paymentdb.beginTransaction();
							paymentSvc.closePayment(objid);
							paymentdb.commit();
						} catch (Throwable t) {
							t.printStackTrace();
						} finally {
							paymentdb.endTransaction();
						}
					}
				}
			}
		};
	}
	
	
}
