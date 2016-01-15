package com.rameses.clfc.android.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class RemitRouteCollectionController 
{
//	private UIActivity activity;
	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	private Map collection;
	
	RemitRouteCollectionController(UIActionBarActivity activity, ProgressDialog progressDialog, Map collection) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.collection = collection;
	}

	void execute() throws Exception {
		String msg = "Remitting collections for " + collection.get("description").toString();
		if ("route".equals(collection.get("type").toString())) msg += "-" + collection.get("area").toString();
		progressDialog.setMessage(msg);
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if (!progressDialog.isShowing()) progressDialog.show();
			}
		});
		
		Platform.runAsync(new RemitCollectionActionProcess());
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
			activity.runOnUiThread(new Runnable() {
				public void run() {
					((RemitRouteCollectionActivity) activity).loadRoutes();
					((RemitRouteCollectionActivity) activity).loadFollowups();
					((RemitRouteCollectionActivity) activity).loadSpecials();
				}
			});
			if (progressDialog.isShowing()) progressDialog.dismiss();
//			ApplicationUtil.showShortMsg("Collection for route "+route.get("description").toString()+" - "+route.get("area").toString()+" successfully remitted.");
		}
	};
	
	private class RemitCollectionActionProcess implements Runnable {
		public void run() {
			Bundle data = new Bundle();
			Message message = null;
			Handler handler = null; 
			try {
				Map params = getParameters();
				
				LoanPostingService svc = new LoanPostingService();
				Map response = svc.remitCollection(params);					

				if (response.containsKey("response") && response.get("response").toString().toLowerCase().equals("success")) {
					synchronized (MainDB.LOCK) {
						SQLTransaction txn = new SQLTransaction("clfc.db");
//						SQLTransaction paymentdb = new SQLTransaction("clfcpayment.db");
//						DBPaymentService paymentSvc = new DBPaymentService();
//						paymentSvc.setDBContext(paymentdb.getContext());
						try {
							txn.beginTransaction();
							txn.execute("UPDATE collection_group SET state = 'REMITTED' WHERE objid=?", new Object[]{collection.get("objid").toString()});
//							txn.update(tablename, whereClause)
//							paymentSvc.closePayment(proxy.getString("objid"));
							txn.commit();
						} catch (Throwable t) {
							t.printStackTrace();
						} finally {
							txn.endTransaction();
						}
					}
				}
//				SQLTransaction txn = new SQLTransaction("clfc.db");
//				DBRouteService dbRs = new DBRouteService();
//				dbRs.setDBContext(txn.getContext());
				
//				dbRs.remitRouteByRoutecode(route.get("code").toString());
				
				data.putString("response", response.get("response").toString());
				message = successhandler.obtainMessage();
				handler = successhandler;
			} catch (Throwable t) {
				t.printStackTrace();
				data.putSerializable("response", t);
				message = errorhandler.obtainMessage();
				handler = errorhandler;
			}
			
			message.setData(data);
			handler.sendMessage(message);
		}
		
		private Map getParameters() throws Exception {
			Map params = new HashMap();
			DBContext clfcdb = new DBContext("clfc.db");
			DBContext paymentdb = new DBContext("clfcpayment.db");
			DBContext remarksdb = new DBContext("clfcremarks.db");
			DBContext requestdb = new DBContext("clfcrequest.db");
			try {
				params = getParametersImpl(clfcdb, paymentdb, remarksdb, requestdb);
			} catch (Exception e) {
				throw e;
			} finally {
				clfcdb.close();
				paymentdb.close();
				remarksdb.close();
				requestdb.close();
			}
			return params;
		}
		
		private Map getParametersImpl(DBContext clfcdb, DBContext paymentdb, DBContext remarksdb, DBContext requestdb) throws Exception {
			DBCollectionSheet collectionSheet = new DBCollectionSheet();
			collectionSheet.setDBContext(clfcdb);
			collectionSheet.setCloseable(false);
			
			DBPaymentService paymentSvc = new DBPaymentService();
			paymentSvc.setDBContext(paymentdb);
			paymentSvc.setCloseable(false);
			
			DBVoidService voidSvc = new DBVoidService();
			voidSvc.setDBContext(requestdb);
			voidSvc.setCloseable(false);
			
			DBRemarksService remarksSvc = new DBRemarksService();
			remarksSvc.setDBContext(remarksdb);
			remarksSvc.setCloseable(false);
			
			int totalcollectionsheets = 0;
			String itemid = collection.get("objid").toString();
			List<Map> list = collectionSheet.getCollectionSheetsByItem(itemid);

			if (!list.isEmpty()) {
				Map item;
				String objid;
				boolean haspayment;
				boolean hasremarks;
				int noOfPayments = 0;
				int noOfVoid = 0;
				int size = list.size();
				for (int i=0; i<size; i++) {
					item = (Map) list.get(i);
					
					haspayment = false;
					
					objid = item.get("objid").toString();
//					synchronized (RemarksDB.LOCK) {
						hasremarks = remarksSvc.hasRemarksById(objid);
//					}
					
//					synchronized (PaymentDB.LOCK) {
						noOfPayments = paymentSvc.noOfPayments(objid);
//					}
					
//					synchronized (VoidRequestDB.LOCK) {
						noOfVoid = voidSvc.noOfVoidPayments(objid);
//					}
					
					if (noOfPayments > 0 && noOfPayments > noOfVoid) {
						haspayment = true;
					}

					if (haspayment || hasremarks) {
						totalcollectionsheets++;
					}
				}
			}
			
			BigDecimal totalamount = new BigDecimal("0").setScale(2);
//			synchronized (PaymentDB.LOCK) {
				list = paymentSvc.getPaymentsByItem(itemid);
//			}
			
			if (!list.isEmpty()) {
				int size = list.size();
				Map item;
				Map request;
				for (int i=0; i<size; i++) {
					item = (Map) list.get(i);
					
//					synchronized (VoidRequestDB.LOCK) {
						request = voidSvc.findVoidRequestByPaymentid(item.get("objid").toString());
//					}
					
					if (request == null || request.isEmpty()) {
						totalamount = totalamount.add(new BigDecimal(item.get("amount").toString()));
					}
				}
			}
			
			Map params = new HashMap();
			params.put("itemid", itemid);
			params.put("sessionid", collection.get("billingid").toString());
			params.put("totalcollection", totalcollectionsheets);
			params.put("totalamount", totalamount);
			
			Map collector = new HashMap();
			collector.put("objid", SessionContext.getProfile().getUserId());
			collector.put("name", SessionContext.getProfile().getFullName());
			params.put("collector", collector);
			boolean haspayment = Boolean.valueOf(collection.get("haspayment").toString());
			params.put("haspayment", haspayment);
			
			if (haspayment == true) {
				params.put("payments", getPayments(paymentdb, requestdb, itemid));
				params.put("cbsno", collection.get("cbsno").toString());
			}
			
			return params;
		}
		
		private List<Map> getPayments(DBContext paymentdb, DBContext requestdb, String itemid) throws Exception {
			List<Map> result = new ArrayList<Map>();
			
			List<Map> payments;
//			synchronized (PaymentDB.LOCK) {
				DBPaymentService paymentSvc = new DBPaymentService();
				paymentSvc.setDBContext(paymentdb);
				try {
					payments = paymentSvc.getPaymentsByItem(itemid);
				} catch (Exception e) {
					throw e;
				}
//			}
			
			if (payments != null || !payments.isEmpty()) {
				int size = payments.size();
				Map payment, borrower, loanapp, bank, check;
				String option;
				MapProxy proxy;
				Map request;
				DBVoidService voidSvc = new DBVoidService();
				voidSvc.setDBContext(requestdb);
				voidSvc.setCloseable(false);
				for (int i=0; i<size; i++) {
					System.out.println("payment " + payments.get(i));
					proxy = new MapProxy((Map) payments.get(i));
					
//					synchronized (VoidRequestDB.LOCK) {
						try {
							request = voidSvc.findVoidRequestByPaymentid(proxy.getString("objid"));
						} catch (Exception e) {
							throw e;
						}
//					}
					
					if (request == null) {
						payment = new HashMap();
						payment.put("objid", proxy.getString("objid"));
						payment.put("routecode", proxy.getString("routecode"));
						payment.put("refno", proxy.getString("refno"));
						payment.put("amount", proxy.getDouble("amount"));
						payment.put("type", proxy.getString("paytype"));
						payment.put("paidby", proxy.getString("paidby"));
						payment.put("dtpaid", proxy.getString("txndate"));
						
						borrower = new HashMap();
						borrower.put("objid", proxy.getString("borrower_objid"));
						borrower.put("name", proxy.getString("borrower_name"));
						payment.put("borrower", borrower);
						
						loanapp = new HashMap();
						loanapp.put("objid", proxy.getString("loanapp_objid"));
						loanapp.put("appno", proxy.getString("loanapp_appno"));
						payment.put("loanapp", loanapp);
						
						option = proxy.getString("payoption");
						payment.put("option", option);
						if ("check".equals(option)) {
							bank = new HashMap();
							bank.put("objid", proxy.getString("bank_objid"));
							bank.put("name", proxy.getString("bank_name"));
							payment.put("bank", bank);
							
							check = new HashMap();
							check.put("no", proxy.getString("check_no"));
							check.put("date", proxy.getString("check_date"));
							payment.put("check", check);
						}
						
						result.add(payment);
					}
				}
			}
//			System.out.println("payments2 " + result);
			return result;
		}
	}

//	private Handler responseHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			Bundle bundle = msg.getData();
//			if(progressDialog.isShowing()) progressDialog.dismiss();
//			ApplicationUtil.showShortMsg(context, bundle.getString("response"));
//		}
//	};
//	
//	private Handler remitHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			Bundle bundle = msg.getData();
//			String description = bundle.getString("routedescription");
//			String area = bundle.getString("routearea");
//			loadRoutes();
//			if(progressDialog.isShowing()) progressDialog.dismiss();
//			ApplicationUtil.showShortMsg(context, "Successfully remitted collection for route "+description+" - "+area);
//		}
//	};

	
	
//	private class RemitRunnable implements Runnable {
//		private RouteParcelable route;
//		
//		RemitRunnable(RouteParcelable route) {
//			this.route = route;
//		}
//		
//		@Override
//		public void run() {
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("routecode", route.getCode());
//			params.put("sessionid", route.getSessionid());
//			SQLiteDatabase db = getDbHelper().getReadableDatabase();
//			params.put("totalcollection", getDbHelper().getTotalCollectionSheetsByRoutecode(db, route.getCode()));
//			params.put("totalamount", getDbHelper().getTotalCollectionsByRoutecode(db, route.getCode()));
//			db.close();
//			boolean status = false;
//			Message msg = responseHandler.obtainMessage();
//			Bundle bundle = new Bundle();
//			ServiceProxy postingProxy = ApplicationUtil.getServiceProxy(context, "DevicePostingService");
//			try {
//				Object response  = postingProxy.invoke("remitRouteCollection", new Object[]{params});
//				Map<String, Object> result = (Map<String, Object>) response;
//				if (result.containsKey("response") && result.get("response").toString().toLowerCase().equals("success")) {
//					db = getDbHelper().getWritableDatabase();
//					getDbHelper().remitRouteByCode(db, route.getCode());
//					db.close();
//				}
//				bundle.putString("routedescription", route.getDescription());
//				bundle.putString("routearea", route.getArea());
//				msg = remitHandler.obtainMessage();
//				//bundle.putString("response", "Successfully remitted collection for route "+route.getDescription()+" - "+route.getArea());
//				status = true;
//			} catch( TimeoutException te ) {
//				bundle.putString("response", "Connection Timeout!");
//			} catch( IOException ioe ) {
//				bundle.putString("response", "Error connecting to Server.");
//			} catch( Exception e ) { 
//				bundle.putString("response", e.getMessage());
//				e.printStackTrace(); 
//			} finally {
//				msg.setData(bundle);
//				if (status == true) remitHandler.sendMessage(msg);
//				else responseHandler.sendMessage(msg);
//			}
//		}
//	}
}
