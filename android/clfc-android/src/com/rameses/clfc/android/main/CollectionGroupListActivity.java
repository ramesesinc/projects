package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBCollectionGroup;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class CollectionGroupListActivity extends ControlActivity 
{	
	private ListView lv_route;
	private LayoutInflater inflater;
	private DBContext clfcdb;
	private DBCollectionGroup collectionGroup = new DBCollectionGroup();
//	private DBRouteService routeSvc = new DBRouteService();
	private List<Map> list;
	private List<Map> colGroups = new ArrayList<Map>();
	private TextView tv_billdate;
	private String str;
	private int size;
	private Map colGroup;
	private String billdate;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.template_footer);
		setTitle("Collection Sheet");

		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_collectionsheet_route, rl_container, true);

		tv_billdate = (TextView) findViewById(R.id.tv_billdate);
		lv_route = (ListView) findViewById(R.id.lv_route);
		View header = (View) getLayoutInflater().inflate(R.layout.header_route, null);
		((TextView) header.findViewById(R.id.tv_info_remarks)).setText("Select collection:");
		lv_route.addHeaderView(header, null, false);
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		
		getHandler().post(new Runnable() {
			public void run() {
//				synchronized (MainDB.LOCK) {
					clfcdb = new DBContext("clfc.db"); 
					collectionGroup.setDBContext(clfcdb);
					collectionGroup.setCloseable(false);
					try {
						runImpl(clfcdb);
//						onStartImpl();
					} catch(Throwable e) {
						Platform.getLogger().log(e);
						System.out.println("[CollectionRouteListActivity] error caused by "+e.getClass().getName() + ": " + e.getMessage()); 
					} finally {
						clfcdb.close();
					}
//				}
			}
			
			private void runImpl(DBContext clfcdb) throws Exception {
				billdate = "Collection Date: ";
				str = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "MMM dd, yyyy");
				billdate += str;
//				
				list = collectionGroup.getCollectionGroupsByCollector(SessionContext.getProfile().getUserId());
				colGroups.clear();
				if (!list.isEmpty()) {
					size = list.size();
					MapProxy proxy;
					Map item;
					for (int i=0; i<size; i++) {
						proxy = new MapProxy((Map) list.get(i));
						
						item = new HashMap();
						item.put("objid", proxy.getString("objid"));
						item.put("description", proxy.getString("description"));
						item.put("state", proxy.getString("state"));
						item.put("type", proxy.getString("type"));
						item.put("billdate", proxy.getString("billdate"));
						if ("route".equals(item.get("type").toString())) {
							item.put("area", proxy.getString("area"));
						}
						colGroups.add(item);
					}
				}
				tv_billdate.setText(billdate);
				lv_route.setAdapter(new CollectionGroupAdapter(CollectionGroupListActivity.this, colGroups));
//				list = routeSvc.getRoutesByCollectorid(SessionContext.getProfile().getUserId());
//				routes.clear();
////				System.out.println("billdate -> "+billdate);
//				if (!list.isEmpty()) {
//					str = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "MMM dd, yyyy");//new java.text.SimpleDateFormat("MMM dd, yyyy").format(Platform.getApplication().getServerDate());
////					System.out.println("str -> "+str);
//					billdate += str;
//					listSize = list.size();
//					for (int i=0; i<listSize; i++) {
//						itm = (Map) list.get(i);
//						
//						params = new HashMap();
//						params.put("code", itm.get("routecode").toString());
//						params.put("description", itm.get("routedescription").toString());
//						params.put("area", itm.get("routearea").toString());
//						params.put("state", itm.get("state").toString());
//						routes.add(params);
//					}
//				}
//				tv_billdate.setText(billdate);
//				lv_route.setAdapter(new RouteAdapter(CollectionRouteListActivity.this, routes));	
			}
		});
		lv_route.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedItem(parent, view, position, id);
			}
		}); 
	}
	
	private void selectedItem(AdapterView<?> parent, View view, int position, long id) {
		colGroup = (Map) parent.getItemAtPosition(position);
		if (!MapProxy.getString(colGroup, "state").equals("REMITTED")) {
			Intent intent = new Intent(this, CollectionSheetListActivity.class);
			intent.putExtra("itemid", MapProxy.getString(colGroup, "objid"));
			startActivity(intent);
		}
	}
}
