package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBCollectionGroup;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class CollectionDateActivity extends ControlActivity 
{
	private LayoutInflater inflater;
	private ListView lv_dates;

	protected void onCreateProcess(Bundle savedInstanceState) {
		setTitle("CLFC Collection - ILS");
		setContentView(R.layout.template_footer);
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_collectiondate, rl_container, true);

		lv_dates = (ListView) findViewById(R.id.lv_collectiondates);

		View header = (View) getLayoutInflater().inflate(R.layout.header_route, null);
		((TextView) header.findViewById(R.id.tv_info_remarks)).setText("Select Collection:");
		lv_dates.addHeaderView(header, null, false);
//		println("inflater " + inflater);
//		println("on create process");
	}
	
	private void println(String msg) {
		Log.i("CollectionDateActivity", msg);
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		
		getHandler().post(new Runnable() {
			public void run() {
				DBContext clfcdb = new DBContext("clfc.db");
				DBCollectionGroup dbcg = new DBCollectionGroup();
				dbcg.setDBContext(clfcdb);
				dbcg.setCloseable(false);
				
				List list = new ArrayList();
				try {
					list = dbcg.getCollectionDates();
					println("list " +  list);
					populateListView(list);
				} catch (Throwable t) {
					t.printStackTrace();
				} finally {
					clfcdb.close();
				}
				
			}
		});
		lv_dates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedItem(parent, view, position, id);
			}
		}); 
	}
	
	private void populateListView(List list) {
		if (list != null && !list.isEmpty()) {
			if (lv_dates != null) {
				lv_dates.setAdapter(new CollectionDateAdapter(CollectionDateActivity.this, list));
			}
		}
	}

	private void selectedItem(AdapterView<?> parent, View view, int position, long id) {
		Map item = (Map) parent.getItemAtPosition(position);
		if (item != null && !item.isEmpty()) {
			Intent intent = new Intent(this, CollectionSheetListActivity.class);
			intent.putExtra("billdate", MapProxy.getString(item, "billdate"));
			startActivity(intent);
		}
//		if (!MapProxy.getString(colGroup, "state").equals("REMITTED")) {
//			Intent intent = new Intent(this, CollectionSheetListActivity.class);
//			intent.putExtra("itemid", MapProxy.getString(colGroup, "objid"));
//			startActivity(intent);
//		}
	}
	
}
