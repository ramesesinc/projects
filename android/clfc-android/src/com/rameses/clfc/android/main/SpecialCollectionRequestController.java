package com.rameses.clfc.android.main;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.db.DBSpecialCollection;
import com.rameses.clfc.android.db.DBSystemService;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;

public class SpecialCollectionRequestController 
{
//	private UIActivity activity;
	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	private String  remarks;
//	private AlertDialog dialog;
	private String objid = "";
	
	SpecialCollectionRequestController(UIActionBarActivity activity, ProgressDialog progressDialog, String remarks, String objid) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.remarks = remarks;
		this.objid = objid;
//		this.dialog = dialog;
	}
	
	void execute() throws Exception {
//		if (remarks == null || remarks.equals("")) {
//			ApplicationUtil.showShortMsg("Remarks is required.");
//		} else {
//			objid = "SCR" + UUID.randomUUID();
//			Platform.runAsync(new ActionProcess());
//		}
//		objid = "SCR" + UUID.randomUUID();
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
			activity.runOnUiThread(new Runnable() {
				public void run() {
					((SpecialCollectionActivity) activity).loadRequests();
				}
			});
			if (progressDialog.isShowing()) progressDialog.dismiss();
//			if (dialog.isShowing()) dialog.dismiss();
			ApplicationUtil.showShortMsg("Special collection request successfuly posted.", activity);
		}
	};	
	
	private class ActionProcess implements Runnable {
		public void run() {
			Bundle data = new Bundle();
			Message message = null;
			Handler handler = null;
			try {
				Map params = getParameters();
				LoanPostingService svc = new LoanPostingService();
				Map response = svc.postSpecialCollectionRequest(params);
				
				if (response.containsKey("response") && response.get("response").toString().toLowerCase().equals("success")) {
					saveRequest(params);
				}
				
				data.putString("response", response.get("response").toString());
				message = successhandler.obtainMessage();
				handler = successhandler;
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("objid", "SCR"+UUID.randomUUID());
//				SQLiteDatabase db = getDbHelper().getWritableDatabase();
//				params.put("collectorid", SessionContext.getProfile().getUserId());
//				params.put("state", "PENDING");
//				params.put("remarks", remarks);
//				ServiceProxy postingProxy = ApplicationUtil.getServiceProxy(context, "DevicePostingService");
//				try {
//					postingProxy.invoke("postSpecialCollectionRequest", new Object[]{params});
//					getDbHelper().insertSpecialCollection(db, params);
//				} catch (Exception e) {
//					ApplicationUtil.showShortMsg(context, "Error requesting for special collection.");
//				}
//				db.close();
//				loadRequests();
//				if (dialog.isShowing()) dialog.dismiss();
			} catch (Throwable t) {
				t.printStackTrace();
				data.putSerializable("response", t);
				message = errorhandler.obtainMessage(); 
				handler = errorhandler;
			}
			
			message.setData(data);
			handler.sendMessage(message);
		}
		
		private void saveRequest(Map map) throws Exception {
			System.out.println("map " + map);
			Map params = new HashMap();
			params.put("objid", map.get("objid").toString());
			
			int count = 0;
//			synchronized (MainDB.LOCK) {
				DBContext ctx = new DBContext("clfc.db");
				DBSpecialCollection specialCollection = new DBSpecialCollection();
				specialCollection.setDBContext(ctx);
				specialCollection.setCloseable(false);
				try {
					count = specialCollection.noOfSpecialCollectionByCollector(SessionContext.getProfile().getUserId());
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, activity);
				} finally {
					ctx.close();
				}
//			}
			params.put("name", "Request " + (count+1));
			params.put("state", "FOR_DOWNLOAD");
			params.put("remarks", map.get("remarks").toString());
			params.put("collector_objid", SessionContext.getProfile().getUserId());
			params.put("collector_name", SessionContext.getProfile().getFullName());
			params.put("txndate", Platform.getApplication().getServerDate().toString());
			synchronized (MainDB.LOCK) {
				SQLTransaction txn = new SQLTransaction("clfc.db");
				try {
					txn.beginTransaction();
					txn.insert("specialcollection", params);
					txn.commit();
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, activity);
				} finally {
					txn.endTransaction();
				}
			}
		}
		
		private Map getParameters() throws Exception {
			Map params = new HashMap();
//			SQLTransaction txn = new SQLTransaction("clfc.db");
//			synchronized (MainDB.LOCK) {
				DBContext ctx = new DBContext("clfc.db");
				try {
					params = getParametersImpl(ctx);
				} catch (Exception e) {
					throw e;
				} finally {
					ctx.close();
				}
//			}
			return params;
		}
		
		private Map getParametersImpl(DBContext ctx) throws Exception {
			DBSystemService systemSvc = new DBSystemService();
			systemSvc.setDBContext(ctx);
			systemSvc.setCloseable(false);
			
			String collectorid = SessionContext.getProfile().getUserId();
			String date = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyyy-MM-dd");
			
			Map params = new HashMap();
			params.put("objid", objid);
			params.put("specialcollectionid", objid);
			params.put("billingid", systemSvc.getBillingid(collectorid, date));
			params.put("state", "PENDING");
			params.put("remarks", remarks);
			params.put("orgid", SessionContext.getProfile().get("ORGID"));
			
			Map collector = new HashMap();
			collector.put("objid", SessionContext.getProfile().getUserId());
			collector.put("name", SessionContext.getProfile().getFullName());
			params.put("collector", collector);
			return params;
		}
	}
}
