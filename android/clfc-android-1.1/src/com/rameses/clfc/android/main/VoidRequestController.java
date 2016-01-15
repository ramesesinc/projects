package com.rameses.clfc.android.main;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.R;
import com.rameses.clfc.android.ApplicationImpl;
import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.VoidRequestDB;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class VoidRequestController 
{
//	private UIActivity activity;
	private UIActionBarActivity activity;
	private ProgressDialog progressDialog;
	private Map params;
	private View view;
//	private Fragment fragment;
	private LayoutInflater inflater;
	private AlertDialog dialog;
	
	VoidRequestController(UIActionBarActivity activity, ProgressDialog progressDialog, Map params, AlertDialog dialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.params = params;
		this.dialog = dialog;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	VoidRequestController(UIActionBarActivity activity, ProgressDialog progressDialog, Map params, View view, AlertDialog dialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.params = params;
		this.view = view;
//		this.fragment = fragment;
		this.dialog = dialog;
		
//		String index = "";
//		Object obj = view.getTag(PaymentsAdapter.INDEX);
//		if (obj != null) {
//			index = obj.toString();
//		}
//		println("view index " + index);
//		
//		if (1==1) {
//			throw new RuntimeException("stopping");
//		}
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void execute() throws Exception {
		progressDialog.setMessage("processing.. ");
		if (!progressDialog.isShowing()) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					progressDialog.show();
				}
			});
		}
		Platform.runAsync(new VoidRequestActionProcess());
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
			try {
				saveVoidRequest(msg);
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, activity);
			}
		}
		
		private void saveVoidRequest(Message msg) throws Exception {
			SQLTransaction txn = new SQLTransaction("clfcrequest.db");
			SQLTransaction clfcdb = new SQLTransaction("clfc.db");
			try {
				String sql = "";
				txn.beginTransaction();
				clfcdb.beginTransaction();

				MapProxy proxy = new MapProxy(params);
				
				String objid = proxy.getString("objid");
				
				Map prm = new HashMap();
				synchronized (VoidRequestDB.LOCK) {
					sql = "SELECT objid FROM void_request WHERE objid=?";
					prm = txn.find(sql, new Object[]{objid});
				}
				
				if (prm == null || prm.isEmpty()) {
					prm = new HashMap();
					prm.put("objid", objid);
					prm.put("state", proxy.getString("state"));
					prm.put("csid", proxy.getString("csid"));
					prm.put("paymentid", proxy.getString("paymentid"));
					prm.put("itemid", proxy.getString("itemid"));
					prm.put("txndate", proxy.getString("txndate"));
					prm.put("reason", proxy.getString("reason"));
					
					Map collector = (Map) proxy.get("collector");
					prm.put("collector_objid", MapProxy.getString(collector, "objid"));
					prm.put("collector_name", MapProxy.getString(collector, "name"));

					synchronized (VoidRequestDB.LOCK) {
						txn.insert("void_request", prm);
					} 
				}
				
//				prm.put("collector_objid", collector.get("objid").toString());
//				prm.put("collector_name", collector.get("name").toString());
//				Map mParams = new HashMap();
//				mParams.put("objid", params.get("objid").toString());
//				mParams.put("state", params.get("state").toString());
//				mParams.put("csid", params.get("csid").toString());
//				mParams.put("paymentid", params.get("paymentid").toString());
//				mParams.put("itemid", params.get("itemid").toString());
//				mParams.put("collector_, value)
//				mParams.put("reason", params.get("reason").toString());
//				
//				Map map = (Map) params.get("collector");
//				mParams.put("collectorid", map.get("objid").toString());
//				mParams.put("collectorname", map.get("name").toString());
				
				
				Map prm2 = new HashMap();
				
				synchronized (MainDB.LOCK) {
					sql = "SELECT objid FROM void_request WHERE objid=?";
					prm2 = clfcdb.find(sql, new Object[]{objid});
				}
				
				if (prm2 == null || prm2.isEmpty()) {
					prm2 = new HashMap();
					prm2.put("objid", objid);
					prm2.put("state", MapProxy.getString(prm, "state"));
					prm2.put("csid", MapProxy.getString(prm, "csid"));
					prm2.put("paymentid", MapProxy.getString(prm, "paymentid"));
					prm2.put("itemid", MapProxy.getString(prm, "itemid"));
					prm2.put("collector_objid", MapProxy.getString(prm, "collector_objid"));
					synchronized (MainDB.LOCK) {
						clfcdb.insert("void_request", prm2);
					}
				}

				if (view != null) {
					View overlay = inflater.inflate(R.layout.overlay_void_text, null);

					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
					((TextView) overlay).setTextColor(activity.getResources().getColor(R.color.red));
					((TextView) overlay).setText("VOID REQUEST PENDING");
					overlay.setLayoutParams(layoutParams);
					((RelativeLayout) view).addView(overlay);
					view.setClickable(false);
					view.setOnClickListener(null);
					view.setOnLongClickListener(null);
				}
				
//				String index = "";
//				Object obj = view.getTag(PaymentsAdapter.INDEX);
//				if (obj != null) {
//					index = obj.toString();
//				}
//				
//				println("index " + index);
//				
//				String paymentid = "";
//				obj = view.getTag(R.id.paymentid);
//				if (obj != null) {
//					paymentid = obj.toString();
//				}
//				println("paymentid " + paymentid);
				
				
				if (dialog.isShowing()) dialog.dismiss();
				activity.getHandler().post(new Runnable() {
					public void run() {
						((ApplicationImpl) Platform.getApplication()).voidRequestSvc.start();						
					}
				});
				
				txn.commit();
				clfcdb.commit();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				txn.endTransaction();
				clfcdb.endTransaction();
			}
		}
	};	
	
	private class VoidRequestActionProcess implements Runnable {
		public void run() {
			Bundle data = new Bundle();
			Message message = null;
			Handler handler = null;
			try {
				println("app.host " + ApplicationUtil.getAppHost());
				LoanPostingService svc = new LoanPostingService();
				Map map = svc.voidPayment(params);
				
				data.putString("response", map.get("response").toString());
				message = successhandler.obtainMessage();
				handler = successhandler;
			} catch (Throwable t) {
				data.putSerializable("response", t);
				message = errorhandler.obtainMessage();
				handler = errorhandler;
				t.printStackTrace();
			}
			
			message.setData(data);
			handler.sendMessage(message);
		}
	}
	
	private void println(String msg) {
		Log.i("VoidRequestController", msg);
	}
}
