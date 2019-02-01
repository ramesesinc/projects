package com.rameses.android.main;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.controller.UploadReadingController;
import com.rameses.android.database.AccountDB;
import com.rameses.android.database.ReadingDB;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.DBContext;

public class UploadActivity extends UserInfoMenuActivity {
	
	private AccountDB accountdb = new AccountDB();
	private int noOfAccounts = 0;
	private TextView tv_records;
	private ProgressDialog progressDialog;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Upload Data");
		
		progressDialog = new ProgressDialog(this);
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_upload, rl_container, true);

		tv_records = (TextView) findViewById(R.id.tv_records);
			
		
		new UIAction(this, R.id.btn_upload) {
			protected void onClick() {
//				println("upload data");
				try { 
					UserProfile prof = SessionContext.getProfile();
					String userid = (prof != null? prof.getUserId() : "");
					
					ReadingDB readingdb = new ReadingDB();
					DBContext ctx = new DBContext("main.db");
					List list = readingdb.getReadingByAssigneeid(userid);
					new UploadReadingController(UploadActivity.this, progressDialog, list).execute();
				} catch (Throwable t) {
					UIDialog.showMessage(t, UploadActivity.this); 
				}
			}
		};
		
		reloadNoOfAccounts(); 
	}
	
	public void reloadNoOfAccounts() {

		UserProfile prof = SessionContext.getProfile();
		String userid = (prof != null? prof.getUserId() : "");
		
		DBContext ctx = new DBContext("main.db");
		accountdb.setDBContext(ctx);	
		
		try {
			noOfAccounts = accountdb.getNoOfTotalReadRecordsByAssigneeid(userid);
		} catch (Exception e) {
			UIDialog.showMessage(e, UploadActivity.this);
		}
		
		tv_records.setText(noOfAccounts + "");
	}
	
	void println(String msg) {
		Log.i("UploadActivity", msg);
	}
}
