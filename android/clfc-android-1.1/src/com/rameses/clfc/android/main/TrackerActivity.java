package com.rameses.clfc.android.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.TrackerDB;
import com.rameses.clfc.android.db.DBLocationTracker;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;

public class TrackerActivity extends ControlActivity {
	private LayoutInflater inflater;
	private RelativeLayout rl_container;
	private ProgressDialog progressDialog;
	private DBLocationTracker trackerSvc = new DBLocationTracker();
	private TextView tv_tracker;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		setTitle("CLFC Collection - ILS");
		setContentView(R.layout.template_footer);
		
		rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_tracker, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		tv_tracker = (TextView) findViewById(R.id.tv_tracker);
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		getUnpostedTracker();
		new UIAction(this, R.id.btn_refresh) {
			protected void onClick() {
				try {
					getUnpostedTracker();
				} catch(Throwable t) {
					UIDialog.showMessage("[ERROR] " + t.getMessage());
				}  
			} 
		};
	}
	
	private void getUnpostedTracker() {
		progressDialog.setMessage("Processing..");
		if (!progressDialog.isShowing()) progressDialog.show();
//		synchronized (TrackerDB.LOCK) {
			DBContext ctx = new DBContext("clfctracker.db");
			trackerSvc.setDBContext(ctx);
			int noOfTracker = 0;
			try {
				noOfTracker = trackerSvc.getNoOfTrackersByCollectorid(SessionContext.getProfile().getUserId());
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, TrackerActivity.this);
			} finally {
				if (progressDialog.isShowing()) progressDialog.dismiss();
			}
			tv_tracker.setText("No. of unposted tracker: " + noOfTracker);
//		}
	}
}
