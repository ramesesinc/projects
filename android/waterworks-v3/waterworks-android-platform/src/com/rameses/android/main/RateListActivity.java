package com.rameses.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.controller.DownloadRuleController;
import com.rameses.android.database.RuleDB;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;

public class RateListActivity extends UserInfoMenuActivity {

	private RuleDB rule = new RuleDB();
	private ListView lv_list;
	private ProgressDialog progressDialog;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Water Rates");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_rate, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		
		lv_list = (ListView) findViewById(R.id.lv_list);
		
		new UIAction(this, R.id.btn_sync) {
			public void onClick() {

				try { 
					new DownloadRuleController(RateListActivity.this, progressDialog).execute();
				} catch (Throwable t) {
					UIDialog.showMessage(t, RateListActivity.this); 
				}
			}
		};
		loadList();
//		Button btn_sync = (Button) findViewById(R.id.btn_sync);
//		btn_sync.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//			}
//		});
	}
	
	public void loadList() {
		DBContext ctx = new DBContext("main.db");
		List<Map> list = new ArrayList<Map>();
		rule.setDBContext(ctx);
		 
		try {
			list = rule.getRules();
		} catch (Exception e) {
			e.printStackTrace();
			UIDialog.showMessage(e, RateListActivity.this);
		}
		
		lv_list.setAdapter(new RateListAdapter(this, list));
	}
	
	void println(String msg) {
		Log.i("RateListActivity", msg);
	}
}
