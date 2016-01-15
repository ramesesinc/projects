package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.services.LoanBillingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.db.android.SQLTransaction;

class DownloadRoutesController 
{
//	private UIActivity activity;
	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	
	DownloadRoutesController(UIActionBarActivity activity, ProgressDialog progressDialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
	}
	
	void execute() throws Exception {
		progressDialog.setMessage("processing...");
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
			Bundle data = msg.getData();
			if (progressDialog.isShowing()) progressDialog.dismiss();
			Intent intent = new Intent(activity, RouteListActivity.class);
			intent.putExtra("routes", data.getSerializable("routes"));
			intent.putExtra("followups", data.getSerializable("followups"));
			intent.putExtra("specials", data.getSerializable("specials"));
			activity.startActivity(intent);
		}
	};	
	
	private class ActionProcess implements Runnable 
	{
		public void run() {
			Bundle data = new Bundle();			
			Handler handler = null;
			Message message = null;
			try {				
				processDB();
				//new RouteFetcher().run();

				String userid = SessionContext.getProfile().getUserId();
				Map params = new HashMap();
				params.put("collectorid", userid);
				LoanBillingService svc = new LoanBillingService();
//				ArrayList routes = (ArrayList) svc.getRoutes(params);
				System.out.println("passing");
				Map result = svc.getForDownloadBilling(params);

				data.putSerializable("routes", ((ArrayList) result.get("routes")));
				data.putSerializable("followups",((ArrayList) result.get("followups")));
				data.putSerializable("specials", ((ArrayList) result.get("specials")));
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
		
		private void processDB() throws Exception {
			SQLTransaction clfcdb = new SQLTransaction("clfc.db");
			SQLTransaction paymentdb = new SQLTransaction("clfcpayment.db");
			SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
			try {
				clfcdb.beginTransaction();
				paymentdb.beginTransaction();
				remarksdb.beginTransaction();
				execute(clfcdb, paymentdb, remarksdb);
				clfcdb.commit();
				paymentdb.commit();
				remarksdb.commit();
			} catch(Exception e) {
				throw e; 
			} finally { 
				clfcdb.endTransaction();  
				paymentdb.endTransaction();  
				remarksdb.endTransaction();  
			} 
		}
				
		private void execute(SQLTransaction clfcdb, SQLTransaction paymentdb, SQLTransaction remarksdb) throws Exception {
			String msg = "There are still collection sheets to upload. Please upload the collection sheets before downloading current collection sheets.";
//			String sql = "" +
//			" SELECT p.* FROM payment p " +
//			"    INNER JOIN collectionsheet cs ON p.loanappid=cs.loanappid " +
//			" WHERE p.state='PENDING' LIMIT 1 ";
//			String sql = "SELECT * FROM collectionsheet";
//			List<Map> list = clfcdb.getList(sql);//txn.getList(sql);
//			if (!list.isEmpty()) {
//				boolean flag = flag;
//
//				DBPaymentService dbPs = new DBPaymentService();
//				dbPs.setDBContext(paymentdb.getContext());
//				
//				DBRemarksService dbRs = new DBRemarksService();
//				dbRs.setDBContext(remarksdb.getContext());
//				Map map;
//				for (int i=0; i<list.size(); i++) {
//					map = (Map) list.get(i);
//					
//				}
//				
//				if (flag == true) {
//					throw new Exception(msg);
//				}
//			}
			
			DBPaymentService dbPs = new DBPaymentService();
			dbPs.setDBContext(paymentdb.getContext());
			
			if (dbPs.hasUnpostedPayments()) {
				throw new Exception(msg);
			}
			
			DBRemarksService dbRs = new DBRemarksService();
			dbRs.setDBContext(remarksdb.getContext());
			
			if (dbRs.hasUnpostedRemarks()) {
				throw new Exception(msg);
			}
			
//			if (!pendings.isEmpty()) {
//				pendings.clear();
//				throw new Exception(msg);
//			} 
			
//			sql = "" + 
//			" SELECT n.* FROM notes n " +
//			"    INNER JOIN collectionsheet cs ON n.loanappid=cs.loanappid " +
//			" WHERE n.state='PENDING' ";			
//			pendings = txn.getList(sql);
//			if (!pendings.isEmpty()) {
//				pendings.clear();
//				throw new Exception(msg);
//			} 
//			
//			sql = "" + 
//			" SELECT r.* FROM remarks r " +
//			"    INNER JOIN collectionsheet cs ON r.loanappid=cs.loanappid" +
//			" WHERE r.state='PENDING' ";	
//			pendings = txn.getList(sql);
//			if (!pendings.isEmpty()) {
//				pendings.clear();
//				throw new Exception(msg);
//			} 
		}
		
		private void getRoutes() {
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("collectorid", SessionContext.getProfile().getUserId());
//			//SQLiteDatabase db = getDbHelper().getReadableDatabase();
//			//params.put("collectorid", getDbHelper().getCollectorid(db));
//			//db.close();
//			Bundle bundle = new Bundle();
//			Message msg = responseHandler.obtainMessage();
//			boolean status = false;
//			ServiceProxy svcProxy = ApplicationUtil.getServiceProxy(context, "DeviceLoanBillingService");
//			try {
//				Object response = svcProxy.invoke("getRoutes", new Object[]{params});
//				Map<String, Object> result = (Map<String, Object>) response;
//				bundle.putParcelableArrayList("routes", ((ArrayList<RouteParcelable>) result.get("routes")));
//				msg = routesHandler.obtainMessage();
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
//				if(status == true) routesHandler.sendMessage(msg);
//				else responseHandler.sendMessage(msg);
//			}
		} 
	}		
}
