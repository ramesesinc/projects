package com.rameses.clfc.android.main;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.android.UIApplication;
import com.rameses.client.android.UIDialog;
import com.rameses.client.services.LoginService;
import com.rameses.client.services.LogoutService;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

class LogoutController 
{
//	private UIActivity activity;
	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	
	LogoutController(UIActionBarActivity activity, ProgressDialog progressDialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
	}
	
	void execute() throws Exception {
		if (hasUnpostedTransactions()) { 
			ApplicationUtil.showShortMsg("Cannot logout. There are still unposted transactions");
			
		} else {
			UIDialog dialog = new UIDialog(activity) {				
				public void onApprove() {
					progressDialog.setMessage("Logging out...");
					if (!progressDialog.isShowing()) progressDialog.show();
					
					Platform.runAsync(new LogoutActionProcess()); 
				}
			};
			dialog.confirm("Are you sure you want to logout?");
		}	
	}
	
	private boolean hasUnpostedTransactions() throws Exception {
		DBContext paymentdb = new DBContext("clfcpayment.db");
		DBContext remarksdb = new DBContext("clfcremarks.db");
		DBContext clfcdb = new DBContext("clfc.db");
		try {
			boolean flag = hasUnpostedTransactionsImpl(paymentdb, remarksdb, clfcdb);
			return flag;
		} catch (Exception e) {
			throw e;
		} finally {
			paymentdb.close();
			remarksdb.close();
			clfcdb.close();
		}
	}
	
	private boolean hasUnpostedTransactionsImpl(DBContext paymentdb, DBContext remarksdb, DBContext clfcdb) 
			throws Exception {
		DBPaymentService paymentSvc = new DBPaymentService();
		paymentSvc.setDBContext(paymentdb);
		paymentSvc.setCloseable(false);
		
		String collectorid = SessionContext.getProfile().getUserId();
		if (paymentSvc.hasUnpostedPaymentsByCollector(collectorid)) return true;
		
		DBRemarksService remarksSvc = new DBRemarksService();
		remarksSvc.setDBContext(remarksdb);
		remarksSvc.setCloseable(false);
		
		if (remarksSvc.hasUnpostedRemarksByCollector(collectorid)) return true;
		
		DBCollectionSheet collectionSheet = new DBCollectionSheet();
		collectionSheet.setDBContext(clfcdb);
		collectionSheet.setCloseable(false);
		
		boolean flag = false;
		List<Map> list = collectionSheet.getUnremittedCollectionSheetsByCollector(collectorid);
		if (!list.isEmpty()) {
			String sql = "";
			String objid = "";
			Map map;
			for (int i=0; i<list.size(); i++) {
				map = (Map) list.get(i);
				
				objid = map.get("objid").toString();
				sql = "SELECT objid FROM payment WHERE parentid=? LIMIT 1";
				if (!paymentdb.getList(sql, new Object[]{objid}).isEmpty()) {
					flag = true;
					break;
				}
				
				sql = "SELECT objid FROM remarks WHERE objid=? LIMIT 1";
				if (!remarksdb.getList(sql, new Object[]{objid}).isEmpty()) {
					flag = true;
					break;
				}
			}
		}
		return flag;
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

			UIApplication uiapp = Platform.getApplication();
			uiapp.getAppSettings().put("collector_state", "logout");
			uiapp.getAppSettings().remove("trackerid");
			uiapp.getAppSettings().remove("tracker_owner");
			uiapp.getAppSettings().remove("captureid");			
			uiapp.logout(); 
		}
	};	
	
	private class LogoutActionProcess implements Runnable 
	{
		public void run() {
			Bundle data = new Bundle();
			Message msg = errorhandler.obtainMessage();
			Boolean success = false;
			try {
				runImpl();
				data.putString("response", "success");
				msg = successhandler.obtainMessage();
				success = true;
				
			} catch(Throwable t) { 
				data.putSerializable("response", t);			
				t.printStackTrace();
			} finally { 
				msg.setData(data);
				if (success) {
					successhandler.sendMessage(msg);
				} else {
					errorhandler.sendMessage(msg);
				}
			}
		}
		
		private void runImpl() throws Exception {
			SQLTransaction clfcdb = new SQLTransaction("clfc.db");
			SQLTransaction capturedb = new SQLTransaction("clfccapture.db");
			SQLTransaction paymentdb = new SQLTransaction("clfcpayment.db");
			SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
			SQLTransaction remarksremoveddb = new SQLTransaction("clfcremarksremoved.db");
			SQLTransaction requestdb = new SQLTransaction("clfcrequest.db");
			//SQLTransaction trackerdb = new SQLTransaction("clfctracker.db");
			try {
				clfcdb.beginTransaction();
				capturedb.beginTransaction();
				paymentdb.beginTransaction();
				remarksdb.beginTransaction();
				remarksremoveddb.beginTransaction();
				requestdb.beginTransaction();
				//trackerdb.beginTransaction();
				execute(clfcdb, capturedb, paymentdb, remarksdb, remarksremoveddb, requestdb);
				
				clfcdb.commit();
				capturedb.commit();
				paymentdb.commit();
				remarksdb.commit();
				remarksremoveddb.commit();
				requestdb.commit();
				//trackerdb.commit();
			} catch(Exception e) {
				throw e; 
			} finally { 
				clfcdb.endTransaction();  
				capturedb.endTransaction();
				paymentdb.endTransaction();
				remarksdb.endTransaction();
				remarksremoveddb.endTransaction();
				requestdb.endTransaction();
				//trackerdb.endTransaction();
			} 
		}
				
		private void execute(SQLTransaction clfcdb, SQLTransaction capturedb, 
				SQLTransaction paymentdb, SQLTransaction remarksdb, SQLTransaction remarksremoveddb,
				SQLTransaction requestdb) throws Exception {
			
			String collectorid = SessionContext.getProfile().getUserId();
			List<Map> collectionGroups = clfcdb.getList("SELECT * FROM collection_group WHERE collectorid=?", new Object[]{collectorid});			
			String objid;
			Map data;
			while (!collectionGroups.isEmpty()) {
				data = collectionGroups.remove(0);
				objid = MapProxy.getString(data, "objid");
				
				paymentdb.delete("payment", "itemid=?", new Object[]{objid});
				clfcdb.delete("payment", "itemid=?", new Object[]{objid});
				
				remarksdb.delete("remarks", "itemid=?", new Object[]{objid});
				clfcdb.delete("remarks", "itemid=?", new Object[]{objid});

				remarksremoveddb.delete("remarks_removed", "itemid=?", new Object[]{objid});
				requestdb.delete("void_request", "itemid=?", new Object[]{objid});
				processCollection(clfcdb, objid);
				clfcdb.delete("collection_group", "objid=?", new Object[]{objid});
			}
			clfcdb.delete("specialcollection", "collector_objid=?", new Object[]{collectorid});
			capturedb.delete("capture_payment", "collector_objid=?", new Object[]{collectorid});

			clfcdb.delete("bank", null);
			String date = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyyy-MM-dd");
			clfcdb.delete("sys_var", "name=?", new Object[]{collectorid+"-"+date});		
			try { 
				new LogoutService().logout(); 
			} catch (Exception e) { 
				e.printStackTrace(); 
				throw e;
			}			
		} 
		
		private void processCollection(SQLTransaction clfcdb, String itemid) throws Exception {
			String sql = "SELECT * FROM collectionsheet WHERE itemid=?";
			List<Map> sheets = clfcdb.getList(sql, new Object[]{itemid});
			String objid;
			Map data;
			int count = 0;
			while (!sheets.isEmpty()) {
				data = sheets.remove(0);
				objid = MapProxy.getString(data, "objid");
				count = clfcdb.delete("notes", "parentid=?", new Object[]{objid});
				System.out.println("notes deleted " + count);
				
				count = clfcdb.delete("amnesty", "parentid=?", new Object[]{objid});
				System.out.println("amnesty deleted " + count);
				
				count = clfcdb.delete("collector_remarks", "parentid=?", new Object[]{objid});
				System.out.println("collector remarks deleted " + count);
				
				count = clfcdb.delete("followup_remarks", "parentid=?", new Object[]{objid});
				System.out.println("followup remarks deleted " + count);
				
				count = clfcdb.delete("collectionsheet", "objid=?", new Object[]{objid});
				System.out.println("collectionsheet deleted " + count);
			} 
		}
	}	
}
