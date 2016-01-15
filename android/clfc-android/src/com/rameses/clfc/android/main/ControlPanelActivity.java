package com.rameses.clfc.android.main;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import au.com.bytecode.opencsv.CSVWriter;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.CaptureDB;
import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.PaymentDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.RemarksDB;
import com.rameses.clfc.android.VoidRequestDB;
import com.rameses.clfc.android.db.DBCapturePayment;
import com.rameses.clfc.android.db.DBCollectionGroup;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.db.DBSystemService;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.clfc.android.system.ChangePasswordActivity;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class ControlPanelActivity extends ControlActivity 
{
	private ProgressDialog progressDialog;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private GridView gv_menu;
	private String txndate;
	private DBCollectionGroup colGroup = new DBCollectionGroup();
	//private DBRouteService routeSvc = new DBRouteService();
	//private DBSystemService systemSvc = new DBSystemService();
	private Map<String, Object> item;
	private String itemId; 
	
	public boolean isCloseable() { return false; }	
		
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		
		setTitle("CLFC Collection - ILS");
		setContentView(R.layout.template_footer);
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_control_panel, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		gv_menu = (GridView) findViewById(R.id.gv_menu);	
		
		
//		System.out.println("list " + list);
//		System.out.println("session context " + SessionContext.get);
		
//		System.out.println("orgid " + SessionContext.getProfile().get("ORGID"));
//		List list = (List) SessionContext.get("ORGID");
//		System.out.println("list " + list);
		
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
//		System.out.println("roles-> "+SessionContext.getProfile().getRoles());
		
//		System.out.println("serverDate -> " + Platform.getApplication().getServerDate().toString());
		DBContext clfcdb = new DBContext("clfc.db");
		DBSystemService systemSvc = new DBSystemService();
		systemSvc.setDBContext(clfcdb);
		//routeSvc.setDBContext(clfcdb);

		String userid = SessionContext.getProfile().getUserId();
		String date = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyyy-MM-dd");
		boolean hasBillingid = false;
		
		try {
			hasBillingid = systemSvc.hasBillingid(userid, date);
//			Map item = clfcdb.find("SELECT name FROM sys_var WHERE name=? LIMIT 1", new Object[]{userid});
//			if (item != null || !item.isEmpty()) {
//				hasBillingid = true;
//			}
		} catch (Throwable t) {
			t.printStackTrace();
			hasBillingid = false;
		}
		
		txndate = null;
//		System.out.println("userid " + userid);
		try {
			if (colGroup.hasCollectionGroupByCollector(userid)) {
				txndate = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "MMM dd, yyyy");
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		list.clear();
		list.add(ApplicationUtil.createMenuItem("download", "Download", null, R.drawable.download));
		list.add(ApplicationUtil.createMenuItem("payment", "Payment(s)", txndate, R.drawable.payment));
		list.add(ApplicationUtil.createMenuItem("posting", "Posting", null, R.drawable.posting));		
		if (hasBillingid) {
			list.add(ApplicationUtil.createMenuItem("capture", "Capture", null, R.drawable.payment));
		}
		list.add(ApplicationUtil.createMenuItem("request", "Request", null, R.drawable.request));
		list.add(ApplicationUtil.createMenuItem("remit", "Remit", null, R.drawable.remit));
		list.add(ApplicationUtil.createMenuItem("tracker", "Tracker", null, R.drawable.tracker));
		list.add(ApplicationUtil.createMenuItem("changepassword", "Change Password", null, R.drawable.change_password));
		list.add(ApplicationUtil.createMenuItem("logout", "Logout", null, R.drawable.logout));
			
		gv_menu.setAdapter(new MenuAdapter(this, list));
		gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try { 
					selectionChanged(parent, view, position, id); 
				} catch (Throwable t) {
					UIDialog.showMessage(t, ControlPanelActivity.this); 
				}
			} 
		}); 
	} 
	
	protected void afterActivityChanged() {
		Platform.getInstance().disposeAllActionBarExcept(this);
	}
	
	protected void afterBackPressed() {
		if (SessionContext.getSessionId() != null) {
			Platform.getApplication().suspendSuspendTimer();
		} 
	}
	
	private void selectionChanged(AdapterView<?> parent, View view, int position, long id) throws Exception {
		item = (Map<String, Object>) parent.getItemAtPosition(position);
		itemId = item.get("id").toString();
		if (itemId.equals("logout")) {
			new LogoutController(this, progressDialog).execute(); 
			
		} else if (itemId.equals("capture")) {
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivity(intent);
			
		} else if (itemId.equals("changepassword")) { 
			Intent intent = new Intent(this, ChangePasswordActivity.class);  
			startActivity(intent); 
			
		} else if (itemId.equals("download")) {			
			/* 
			 * 
			 * check if there unremitted billings 
			 * if there aren't any, continue DownloadRoutesControler
			 * else inform user dn create file before deleting inside the db
			 * 
			 * */
			exportPreviousBillings();
						
		} else if (itemId.equals("payment")) {
			Intent intent = new Intent(this, CollectionGroupListActivity.class);
			startActivity(intent);
			
		} else if (itemId.equals("posting")) {
			Intent intent = new Intent(this, PostingListActivity.class);
			startActivity(intent);
			
		} else if (itemId.equals("tracker")) {
			Intent intent = new Intent(this, TrackerActivity.class);
			startActivity(intent);
			
		} else if (itemId.equals("request")) {
			Map roles = SessionContext.getProfile().getRoles();
			
			if (roles == null) return;

			Intent intent = new Intent(this, SpecialCollectionActivity.class);
			startActivity(intent);
//			if (roles.containsKey("LOAN.FIELD_COLLECTOR")) {
//				DBContext clfcdb = new DBContext("clfc.db");
//				systemSvc.setDBContext(clfcdb);
//				
//				if (systemSvc.hasBillingid()) {
//					startActivity(intent);
//				} else {
//					ApplicationUtil.showShortMsg("You have no billing downloaded. Download billing first before you can request for special collection.");
//				}
//			} else {
//				startActivity(intent);
//			}
		} else if (itemId.equals("remit")) {
			Intent intent = new Intent(this, RemitRouteCollectionActivity.class);
			startActivity(intent);
			
		}
	} 	
	
	private boolean hasPreviousBillings(String date) {
		boolean flag = false;
//		synchronized (MainDB.LOCK) {
			DBContext ctx = new DBContext("clfc.db");
			DBCollectionGroup colGroup = new DBCollectionGroup();
			colGroup.setDBContext(ctx);
			try {
				flag = colGroup.hasPreviousBilling(date);
			} catch (Throwable t) {
				t.printStackTrace();
			}
//		}
		
		return flag;
	}	

	private Handler errorExportHandler = new Handler() {  
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
	
	private Handler successExportHandler = new Handler() {
		public void handleMessage(Message msg) {
			getHandler().post(new Runnable() {
				public void run() {
					if (progressDialog.isShowing()) progressDialog.dismiss();
				}
			});
			ApplicationUtil.showShortMsg("Successfully export previous billings.");
			
			try {
				new DownloadRoutesController(ControlPanelActivity.this, progressDialog).execute();
			} catch (Throwable t) {
				UIDialog.showMessage(t, ControlPanelActivity.this); 
			}
		}
	};
	
	private void exportPreviousBillings() throws Exception {
		final String date = (new SimpleDateFormat("yyyy-MM-dd")).format(Platform.getApplication().getServerDate());
		if (hasPreviousBillings(date)) {
			UIDialog dialog = new UIDialog() {
				public void onApprove() {
					try { 
//						exportPreviousBillingsImpl(date);
						
						Platform.runAsync(new ExportPreviousBillingProcess(date));
						
					} catch (Throwable t) {
						UIDialog.showMessage(t, ControlPanelActivity.this); 
					}
				}
			};
			dialog.confirm("This devices has previous billings that were not remitted. Click yes to save previous billings to a file.");
		} else {			
			new DownloadRoutesController(ControlPanelActivity.this, progressDialog).execute();
		}
	}
	
	private class ExportPreviousBillingProcess implements Runnable
	{
		private String date;
		
		public ExportPreviousBillingProcess(String date) {
			this.date = date;
		}
		
		public void run() {

			progressDialog.setMessage("Exporting previous billings...");
			getHandler().post(new Runnable() {
				public void run() {
					if (!progressDialog.isShowing()) progressDialog.show();
				}
			});
			
			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			try {
				runImpl();
				
				data.putString("response", "success");
				handler = successExportHandler;
				message = handler.obtainMessage();
			} catch(Throwable t) { 
				data.putSerializable("response", t);
				handler = errorExportHandler;
				message = handler.obtainMessage();				
				t.printStackTrace();
			} 
			
			message.setData(data);
			handler.sendMessage(message);
		}
		
		
		private void runImpl() throws Exception {
			List<Map> list = new ArrayList<Map>();
//			synchronized (MainDB.LOCK) {
				DBContext ctx = new DBContext("clfc.db");
				DBCollectionGroup colGroup = new DBCollectionGroup();
				colGroup.setDBContext(ctx);
				try {
					list = colGroup.getPreviousBillings(date);
				} catch (Exception e) {
					throw e;
				}
//			}
			
			if (list != null && list.size() > 0) {
				DBPaymentService paymentSvc = new DBPaymentService();
				DBCollectionSheet colSheetSvc = new DBCollectionSheet();
				DBRemarksService remarksSvc = new DBRemarksService();
				
				int size = list.size();
				Map item;
				List<Map> forExport = new ArrayList<Map>();
//				synchronized (PaymentDB.LOCK) {
					ctx = new DBContext("clfcpayment.db");
					paymentSvc.setDBContext(ctx);
					paymentSvc.setCloseable(false);
					try {
						boolean hasPayments;
						for (int i=0; i<size; i++) {
							item = (Map) list.get(i);
							
							hasPayments = paymentSvc.hasPaymentsByItem(item.get("objid").toString());
							if (hasPayments) forExport.add(item);
						}
					} catch (Exception t) {
						throw t;
					} finally {
						ctx.close();
					}
//				}
				
				List<Map> collectionsheets = new ArrayList<Map>();
				
				size = forExport.size();
				List<Map> dummyList;
				Map map;
				boolean hasPayments, hasRemarks;
				for (int i=0; i<size; i++) {
					item = (Map) forExport.get(i);
					
					dummyList = new ArrayList<Map>();
//					synchronized (MainDB.LOCK) {
						ctx = new DBContext("clfc.db");
						colSheetSvc.setDBContext(ctx);
						try {
							dummyList = colSheetSvc.getCollectionSheetsByItem(item.get("objid").toString());
						} catch (Throwable t) {
							t.printStackTrace();
							UIDialog.showMessage(t, ControlPanelActivity.this);
						}
//					}
					
					if (dummyList != null && dummyList.size() > 0) {
						for (int j=0; j<dummyList.size(); j++) {
							map = (Map) dummyList.get(j);
							
							hasPayments = false;
//							synchronized (PaymentDB.LOCK) {
								ctx = new DBContext("clfcpayment.db");
								paymentSvc.setDBContext(ctx);
								try {
									hasPayments = paymentSvc.hasPayments(map.get("objid").toString());
								} catch (Exception t) {
									throw t;
								}
//							}
							
							if (hasPayments) {
								collectionsheets.add(map);
								continue;
							} 
							
							hasRemarks = false;
//							synchronized (RemarksDB.LOCK) {
								ctx = new DBContext("clfcremarks.db");
								remarksSvc.setDBContext(ctx);
								try {
									hasRemarks = remarksSvc.hasRemarksByItem(map.get("objid").toString());
								} catch (Exception t) {
									throw t;
								}
//							}
							
							if (hasRemarks) {
								collectionsheets.add(map);
							}
						}
					}
				}
				
				
				List<Map> payments = new ArrayList<Map>(), remarks = new ArrayList<Map>();

				if (collectionsheets != null && collectionsheets.size() > 0) {
					int arrsize = collectionsheets.size();
					for (int i=0; i<arrsize; i++) {
						item = (Map) collectionsheets.get(i);
						
						dummyList = new ArrayList<Map>();
//						synchronized (PaymentDB.LOCK) {
							ctx = new DBContext("clfcpayment.db");
							paymentSvc.setDBContext(ctx);
							try {
								dummyList = paymentSvc.getPayments(item.get("objid").toString());
							} catch (Exception t) {
								throw t;
							}
//						}
						
						if (dummyList != null && dummyList.size() > 0) {
							for (int j=0; j<dummyList.size(); j++) {
								payments.add(dummyList.get(j));
							}
						}
						
						map = new HashMap();
//						synchronized (RemarksDB.LOCK) {
							ctx = new DBContext("clfcremarks.db");
							remarksSvc.setDBContext(ctx);
							try {
								map = remarksSvc.findRemarksById(item.get("objid").toString());
							} catch (Exception t) {
								throw t;
							}
//						}
						
						if (map != null && !map.isEmpty()) {
							remarks.add(map);
						}
					}
				}
				
				List<Map> request = new ArrayList<Map>();
				DBVoidService voidSvc = new DBVoidService();
				
				if (payments != null && payments.size() > 0) {
					int arrsize = payments.size();
					for (int i=0; i<arrsize; i++) {
						item = (Map) payments.get(i);
						
						map = new HashMap();
//						synchronized (VoidRequestDB.LOCK) {
							ctx = new DBContext("clfcrequest.db");
							voidSvc.setDBContext(ctx);
							try {
								map = voidSvc.findVoidRequestByPaymentid(item.get("objid").toString());
							} catch (Exception t) {
								throw t;
							}
//						}
						
						if (map != null && !map.isEmpty()) {
							request.add(map);
						}
					}
				}
				
				List<Map> capturepayments = new ArrayList<Map>();
//				synchronized (CaptureDB.LOCK) {
					ctx = new DBContext("clfccapture.db");
					DBCapturePayment captureSvc = new DBCapturePayment();
					captureSvc.setDBContext(ctx);
					try {
						capturepayments = captureSvc.getPreviousPayments(date);
					} catch (Exception t) {
						throw t;
					}
//				}
				

				String path = Environment.getExternalStorageDirectory().getAbsolutePath();				
				CSVWriter writer = null;
				String[] entries;
				String str;
				MapProxy proxy;
				if (forExport.size() > 0) {
					try {
						size = forExport.size();
						writer = new CSVWriter(new FileWriter(path + "/billing_item.csv"), ',');
						
						entries = new String[] {"objid", "billingid", "billdate", "collectorid", "type"};
						writer.writeNext(entries);
						
						for (int i=0; i<size; i++) {
							proxy = new MapProxy((Map) forExport.get(i));
							
							str = proxy.getString("objid");
							str += "#" + proxy.getString("billingid");
							str += "#" + proxy.getString("billdate");
							str += "#" + proxy.getString("collectorid");
							str += "#" + proxy.getString("type");

							entries = str.split("#");
						    writer.writeNext(entries);  
						}
						
					    writer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw e;
//						UIDialog.showMessage(e, ControlPanelActivity.this);
					}
				}
				
				if (collectionsheets.size() > 0) {
					try {
						size = collectionsheets.size(); 
						writer = new CSVWriter(new FileWriter(path + "/billing_collectionsheets.csv"), ',');
						
						entries = new String[] {"objid", "billingid", "itemid", "borrower_objid", "borrower_name", "loanapp_objid", 
												"loanapp_appno", "routecode", "paymentmethod", "type", "isfirstbill"};
						writer.writeNext(entries);
						
						for (int i=0; i<size; i++) {
							proxy = new MapProxy((Map) collectionsheets.get(i));
							
							str = proxy.getString("objid");
							str += "#" + proxy.getString("billingid");
							str += "#" + proxy.getString("itemid");
							str += "#" + proxy.getString("borrower_objid");
							str += "#" + proxy.getString("borrower_name");
							str += "#" + proxy.getString("loanapp_objid");
							str += "#" + proxy.getString("loanapp_appno");
							str += "#" + proxy.getString("routecode");
							str += "#" + proxy.getString("paymentmethod");
							str += "#" + proxy.getString("type");
							str += "#" + proxy.getInteger("isfirstbill");
							
							entries = str.split("#");
							writer.writeNext(entries);
						}
						
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw e;
//						UIDialog.showMessage(e, ControlPanelActivity.this);
					}
				}
				
				if (payments.size() > 0) {
					try {
						size = payments.size(); 
						writer = new CSVWriter(new FileWriter(path + "/billing_payments.csv"), ',');
						
						entries = new String[] {"objid", "parentid", "itemid", "billingid", "trackerid", "collector_objid", 
												"collector_name", "borrower_objid", "borrower_name", "loanapp_objid", "loanapp_appno", 
												"routecode", "refno", "txndate", "paytype", "payoption", "amount", "bank_objid", "bank_name", 
												"check_no", "check_date", "isfirstbill", "lng", "lat", "type"};
						writer.writeNext(entries);
						
						for (int i=0; i<size; i++) {
							proxy = new MapProxy((Map) payments.get(i));
							
							str = proxy.getString("objid");
							str += "#" + proxy.getString("parentid");
							str += "#" + proxy.getString("itemid");
							str += "#" + proxy.getString("billingid");
							str += "#" + proxy.getString("trackerid");
							str += "#" + proxy.getString("collector_objid");
							str += "#" + proxy.getString("collector_name");
							str += "#" + proxy.getString("borrower_objid");
							str += "#" + proxy.getString("borrower_name");
							str += "#" + proxy.getString("loanapp_objid");
							str += "#" + proxy.getString("loanapp_appno");
							str += "#" + proxy.getString("routecode");
							str += "#" + proxy.getString("refno");
							str += "#" + proxy.getString("txndate");
							str += "#" + proxy.getString("paytype");
							str += "#" + proxy.getString("payoption");
							str += "#" + proxy.getDouble("amount");
							str += "#" + proxy.getString("bank_objid");
							str += "#" + proxy.getString("bank_name");
							str += "#" + proxy.getString("check_no");
							str += "#" + proxy.getString("check_date");
							str += "#" + proxy.getInteger("isfirstbill");
							str += "#" + proxy.getDouble("lng");
							str += "#" + proxy.getDouble("lat");
							str += "#" + proxy.getString("type");
							
							entries = str.split("#");
							writer.writeNext(entries);
						}
						
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw e;
//						UIDialog.showMessage(e, ControlPanelActivity.this);
					}
				}
				
				if (remarks.size() > 0) {
					try {
						size = remarks.size(); 
						writer = new CSVWriter(new FileWriter(path + "/billing_remarks.csv"), ',');

						entries = new String[] {"objid", "billingid", "itemid", "trackerid", "txndate", "borrower_objid", "borrower_name", 
												"loanapp_objid", "loanapp_appno", "collector_objid", "collector_name", "routecode", "remarks", 
												"lng", "lat", "type"};
						writer.writeNext(entries);
						
						for (int i=0; i<size; i++) {
							proxy = new MapProxy((Map) remarks.get(i));
							
							str = proxy.getString("objid");
							str += "#" + proxy.getString("billingid");
							str += "#" + proxy.getString("itemid");
							str += "#" + proxy.getString("trackerid");
							str += "#" + proxy.getString("txndate");
							str += "#" + proxy.getString("borrower_objid");
							str += "#" + proxy.getString("borrower_name");
							str += "#" + proxy.getString("loanapp_objid");
							str += "#" + proxy.getString("loanapp_appno");
							str += "#" + proxy.getString("collector_objid");
							str += "#" + proxy.getString("collector_name");
							str += "#" + proxy.getString("routecode");
							str += "#" + proxy.getString("remarks");
							str += "#" + proxy.getDouble("lng");
							str += "#" + proxy.getDouble("lat");
							str += "#" + proxy.getString("type");
							
							entries = str.split("#");
							writer.writeNext(entries);
						}
						
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw e;
//						UIDialog.showMessage(e, ControlPanelActivity.this);
					}
				}
				
				if (request.size() > 0) {
					try {
						size = request.size(); 
						writer = new CSVWriter(new FileWriter(path + "/billing_request.csv"), ',');

						entries = new String[] {"objid", "csid", "paymentid", "itemid", "collector_objid", "collector_name", "reason"};
						writer.writeNext(entries);
						
						for (int i=0; i<size; i++) {
							proxy = new MapProxy((Map) request.get(i));
							
							str = proxy.getString("objid");
							str += "#" + proxy.getString("csid");
							str += "#" + proxy.getString("paymentid");
							str += "#" + proxy.getString("itemid");
							str += "#" + proxy.getString("collector_objid");
							str += "#" + proxy.getString("collector_name");
							str += "#" + proxy.getString("reason");
							
							entries = str.split("#");
							writer.writeNext(entries);
						}
						
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw e;
//						UIDialog.showMessage(e, ControlPanelActivity.this);
					}
				}
				
				if (capturepayments.size() > 0) {
					try {
						size = capturepayments.size(); 
						writer = new CSVWriter(new FileWriter(path + "/billing_capturepayments.csv"), ',');
						
						entries = new String[] {"objid", "captureid", "billingid", "txndate", "borrowername", "amount", "payoption", "paidby", 
												"dtpaid", "bank_objid", "bank_name", "check_no", "check_date", "lng", "lat", "collector_objid", 
												"collector_name", "trackerid"};
						writer.writeNext(entries);
						
						for (int i=0; i<size; i++) {
							proxy = new MapProxy((Map) capturepayments.get(i));
							
							str = proxy.getString("objid");
							str += "#" + proxy.getString("captureid");
							str += "#" + proxy.getString("billingid");
							str += "#" + proxy.getString("txndate");
							str += "#" + proxy.getString("borrowername");
							str += "#" + proxy.getDouble("amount");
							str += "#" + proxy.getString("payoption");
							str += "#" + proxy.getString("paidby");
							str += "#" + proxy.getString("dtpaid");
							str += "#" + proxy.getString("bank_objid");
							str += "#" + proxy.getString("bank_name");
							str += "#" + proxy.getString("check_no");
							str += "#" + proxy.getString("check_date");
							str += "#" + proxy.getDouble("lng");
							str += "#" + proxy.getDouble("lat");
							str += "#" + proxy.getString("collector_objid");
							str += "#" + proxy.getString("collector_name");
							str += "#" + proxy.getString("trackerid");
							
							entries = str.split("#");
							writer.writeNext(entries);
						}
						
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw e;
//						UIDialog.showMessage(e, ControlPanelActivity.this);
					}
				}
				
				size = list.size();
				SQLTransaction clfcdb, paymentdb, remarksdb, requestdb, capturedb;
				String objid;
				for (int i=0; i<size; i++) {
					proxy = new MapProxy((Map) list.get(i));
					objid = proxy.getString("objid");
					
					clfcdb = new SQLTransaction("clfc.db");
					paymentdb = new SQLTransaction("clfcpayment.db");
					remarksdb = new SQLTransaction("clfcremarks.db");
					requestdb = new SQLTransaction("clfcrequest.db");
					try {
						clfcdb.beginTransaction();
						paymentdb.beginTransaction();
						remarksdb.beginTransaction();
						requestdb.beginTransaction();
						
						synchronized (MainDB.LOCK) {
							clfcdb.delete("collectionsheet", "itemid=?", new Object[]{objid});
							clfcdb.delete("collection_group", "objid=?", new Object[]{objid});
						}
						
						synchronized (PaymentDB.LOCK) {
							paymentdb.delete("payment", "itemid=?", new Object[]{objid});
						}
						
						synchronized (RemarksDB.LOCK) {
							remarksdb.delete("remarks", "itemid=?", new Object[]{objid});
						}
						
						synchronized (VoidRequestDB.LOCK) {
							requestdb.delete("void_request", "itemid=?", new Object[]{objid});
						}
						
						clfcdb.commit();
						paymentdb.commit();
						remarksdb.commit();
						requestdb.commit();
					} catch (Exception t) {
						t.printStackTrace();
						throw t;
//						UIDialog.showMessage(t,  ControlPanelActivity.this);
					} finally {
						clfcdb.endTransaction();;
						paymentdb.endTransaction();
						remarksdb.endTransaction();
						requestdb.endTransaction();
					}
				}
				
				capturedb = new SQLTransaction("clfccapture.db");
				clfcdb = new SQLTransaction("clfc.db");
				try {
					capturedb.beginTransaction();
					clfcdb.beginTransaction();
					
					synchronized (CaptureDB.LOCK) {
						capturedb.delete("capture_payment", "txndate < ?", new Object[]{date});
					}
					
					synchronized (MainDB.LOCK) {
						clfcdb.delete("specialcollection", "txndate < ?", new Object[]{date});
					}
					
					capturedb.commit();
					clfcdb.commit();
				} catch (Exception t) {
					t.printStackTrace();
					throw t; 
				} finally {
					capturedb.endTransaction();
					clfcdb.endTransaction();
				}
			}
		}
	}
	
}
