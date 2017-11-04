package com.rameses.android.main;

import java.util.ArrayList;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.controller.DownloadSectorAccountsController;
import com.rameses.client.android.UIDialog;

public class DownloadListActivity extends ControlActivity {

	private ListView lv_list;
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private ProgressDialog progressDialog;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Download");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_download, rl_container, true);
				 
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		lv_list = (ListView) findViewById(R.id.lv_list);
		lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				println("tag " + view.getTag(R.string.id_tag));
				String sectorid = String.valueOf(view.getTag(R.string.id_tag));
				try { 
					new DownloadSectorAccountsController(DownloadListActivity.this, progressDialog, sectorid).execute();
				} catch (Throwable t) {
					UIDialog.showMessage(t, DownloadListActivity.this); 
				}
			}
		});
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		list = (ArrayList<Map<String, Object>>) bundle.getSerializable("list");
		lv_list.setAdapter(new DownloadListAdapter(this, list));
	}
	
	void println(String msg) {
		Log.i("DownloadListActivity", msg);
	}
}
