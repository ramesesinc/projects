package com.rameses.clfc.android.main;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ApplicationImpl;
import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.VoidRequestDB;
import com.rameses.clfc.android.services.LoanPostingService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.android.UIActivity;
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
	private LayoutInflater inflater;
	private AlertDialog dialog;
	
	VoidRequestController(UIActionBarActivity activity, ProgressDialog progressDialog, Map params, View view, AlertDialog dialog) {
		this.activity = activity;
		this.progressDialog = progressDialog;
		this.params = params;
		this.view = view;
		this.dialog = dialog;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void execute() throws Exception {
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
				txn.beginTransaction();
				clfcdb.beginTransaction();
				
				MapProxy proxy = new MapProxy(params);
				Map prm = new HashMap();
				prm.put("objid", proxy.getString("objid"));
				prm.put("state", proxy.getString("state"));
				prm.put("csid", proxy.getString("csid"));
				prm.put("paymentid", proxy.getString("paymentid"));
				prm.put("itemid", proxy.getString("itemid"));
				prm.put("txndate", proxy.getString("txndate"));
				prm.put("reason", proxy.getString("reason"));
				
				Map collector = (Map) proxy.get("collector");
				prm.put("collector_objid", collector.get("objid").toString());
				prm.put("collector_name", collector.get("name").toString());
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
				
				synchronized (VoidRequestDB.LOCK) {
					txn.insert("void_request", prm);
				} 
				
				Map prm2 = new HashMap();
				prm2.put("objid", prm.get("objid").toString());
				prm2.put("state", prm.get("state").toString());
				prm2.put("csid", prm.get("csid").toString());
				prm2.put("paymentid", prm.get("paymentid").toString());
				prm2.put("itemid", prm.get("itemid").toString());
				prm2.put("collector_objid", prm.get("collector_objid").toString());
				synchronized (MainDB.LOCK) {
					clfcdb.insert("void_request", prm2);
				}
				
				txn.commit();
				clfcdb.commit();
				
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
				if (dialog.isShowing()) dialog.dismiss();
				activity.getHandler().post(new Runnable() {
					public void run() {
						((ApplicationImpl) Platform.getApplication()).voidRequestSvc.start();						
					}
				});
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
				progressDialog.setMessage("processing.. ");
				if (!progressDialog.isShowing()) {
					activity.runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.show();
						}
					});
				}
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
}
