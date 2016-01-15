package com.rameses.clfc.android.main;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.rameses.clfc.android.AppSettingsImpl;
import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.RemarksDB;
import com.rameses.clfc.android.db.DBCSRemarks;
import com.rameses.clfc.android.db.DBCSVoid;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPrevLocation;
import com.rameses.client.android.NetworkLocationProvider;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class CollectionSheetInfoMainActivity extends ControlActivity {

	private ViewPager tab;
	private CollectionSheetInfoTabPagerAdapter tabAdapter;
	private ActionBar actionBar;
	private MapProxy collectionsheet;
	private String objid;
	
	private DBCSVoid voidcs = new DBCSVoid();
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);

		setTitle("CLFC Collection - ILS");
		setContentView(R.layout.template_footer);
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_collectionsheetinfo_main, rl_container, true);
		
		tabAdapter = new CollectionSheetInfoTabPagerAdapter(getSupportFragmentManager());
		
		Map<String, Object> params = new HashMap();
		objid = getIntent().getStringExtra("objid");
		params.put("objid", objid);
		tabAdapter.setParams(params);
		
		tab = (ViewPager) findViewById(R.id.pager);
		tab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			public void onPageSelected(int position) {
				actionBar = getSupportActionBar();
				actionBar.setSelectedNavigationItem(position);
				 
				String tag = actionBar.getTabAt(position).getTag().toString();
				if ("PAYMENTS".equals(tag)) {
					reloadPayments();
				} else if ("COL_REMARKS".equals(tag)) {
					reloadRemarks();
				} 
			}  
		});
		tab.setAdapter(tabAdapter);
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {

			public void onTabReselected(Tab t, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}

			public void onTabSelected(Tab t, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				tab.setCurrentItem(t.getPosition());
			}

			public void onTabUnselected(Tab t, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				
			}
		};
		
		actionBar.addTab(actionBar.newTab().setTag("GEN_INFO").setText("General Info").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setTag("PAYMENTS").setText("Payments").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setTag("COL_REMARKS").setText("Collector Remarks").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setTag("NOTES").setText("Notes").setTabListener(tabListener));
		actionBar.addTab(actionBar.newTab().setTag("FOL_REMARKS").setText("Follow-up Remarks").setTabListener(tabListener));
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		DBContext ctx = new DBContext("clfc.db");
		DBCollectionSheet collectionsheetdb = new DBCollectionSheet();
		collectionsheetdb.setDBContext(ctx);
		
		try {
			collectionsheet = new MapProxy(collectionsheetdb.findCollectionSheet(objid));
		} catch (Throwable t) {
			t.printStackTrace();
			UIDialog.showMessage(t, CollectionSheetInfoMainActivity.this);
		}
		
	}
	
	private void reloadPayments() {
		Fragment fragment = tabAdapter.getCurrentFragment();
		if (fragment instanceof PaymentsFragment) {
			PaymentsFragment paymentsFrag = (PaymentsFragment) fragment;
			int currentNoOfPayments = paymentsFrag.getCurrentNoOfPayments();
			int oldNoOfPayments = paymentsFrag.getOldNoOfPayments();
			if (currentNoOfPayments > oldNoOfPayments) {
				paymentsFrag.reloadPayments();
			}
		}
	}
	
	private void reloadRemarks() {
		Fragment fragment = tabAdapter.getCurrentFragment();
		if (fragment instanceof CollectorRemarksFragment) {
			CollectorRemarksFragment remarksFrag = (CollectorRemarksFragment) fragment;
			remarksFrag.reloadRemarks();
		}
	}
	

	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.payment, menu);
		
		boolean hasremarks = false;
		DBContext ctx = new DBContext("clfc.db");
		DBCSRemarks csremarks = new DBCSRemarks();
		csremarks.setDBContext(ctx);
		csremarks.setCloseable(false);
		
		try {
			hasremarks = csremarks.hasRemarksById(objid);
		} catch(Throwable t) {;}
		finally {
			ctx.close();
		}
		
		if (hasremarks) {
			((MenuItem) menu.findItem(R.id.payment_addremarks)).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.payment_addpayment) {
			DBContext ctx = new DBContext("clfc.db");
			try { 
//				paymentdb.beginTransaction();
//				requestdb.beginTransaction();
				addPaymentImpl(ctx);
//				paymentdb.commit();
//				requestdb.commit();
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, CollectionSheetInfoMainActivity.this); 
			} finally {
				ctx.close();
//				paymentdb.close();
//				requestdb.close();
			}
		} else if (item.getItemId() == R.id.payment_addremarks) {
			addRemarks();
//			showRemarksDialog("create");
		}
		return true;
	}
	
	private void addPaymentImpl(DBContext ctx) throws Exception {
		if (collectionsheet == null || collectionsheet.isEmpty()) return;
		
		voidcs.setDBContext(ctx);
		voidcs.setCloseable(false);
		String itemid = collectionsheet.getString("itemid");//collectionsheet.get("itemid").toString();
		String csid = collectionsheet.getString("objid");//collectionsheet.get("objid").toString();
//		System.out.println("itemid " + itemid + " csid " + csid);
		if (voidcs.hasPendingVoidRequest(itemid, csid)) {
			ApplicationUtil.showShortMsg("[ERROR] Cannot add payment. No confirmation for void requested at the moment.");
			
		} else {
			Intent intent = new Intent(this, PaymentActivity.class);
			intent.putExtra("itemid", collectionsheet.get("objid").toString());
			startActivity(intent);
		}
	}
	
	private void addRemarks() {
		UIDialog dialog = new UIDialog(CollectionSheetInfoMainActivity.this) {
			public boolean onApprove(Object value) {
				if (value == null || "".equals(value.toString())) {
					ApplicationUtil.showShortMsg("Remarks is required.");
					return false;
				}
				
				boolean flag = saveRemarks(value.toString());
				
				return flag;
			}
			
			private boolean saveRemarks(String remarks) {
				boolean flag = false;
				
				SQLTransaction clfcdb = new SQLTransaction("clfc.db");
				SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
		 		DBContext ctx = new DBContext("clfctracker.db");
		 		DBPrevLocation prevLocationSvc = new DBPrevLocation();
		 		prevLocationSvc.setDBContext(ctx);
		 		prevLocationSvc.setCloseable(false);
		 		
				try {
					clfcdb.beginTransaction();
					remarksdb.beginTransaction();
					
					saveRemarksImpl(clfcdb, remarksdb, prevLocationSvc, remarks);
					flag = true;						
					clfcdb.commit();
					remarksdb.commit();	
		 			getHandler().post(new Runnable() {
						public void run() {
							getApp().remarksSvc.start();
						}
					});				
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, CollectionSheetInfoMainActivity.this); 	
					flag = false;
				} finally {
					clfcdb.endTransaction();
					remarksdb.endTransaction();
					ctx.close();
				}
				if (flag == true) supportInvalidateOptionsMenu();
				
				return flag;
			}
			
			private void saveRemarksImpl(SQLTransaction clfcdb, SQLTransaction remarksdb,
					DBPrevLocation prevLocationSvc, String remarks) throws Exception {
				Location location = NetworkLocationProvider.getLocation();
				double lng = 0.00;
				double lat = 0.00;
				
				if (location != null) {
					lng = location.getLongitude();
					lat = location.getLatitude();
					
				} else {
					Map prevLocation = prevLocationSvc.getPrevLocation();
					if (prevLocation != null) {
						lng = MapProxy.getDouble(prevLocation, "longitude");
						lat = MapProxy.getDouble(prevLocation, "latitude");
					}
					
				}

				String trackerid = ((AppSettingsImpl) Platform.getApplication().getAppSettings()).getTrackerid();
				
				Map params = new HashMap();	
				params.put("objid", objid);
				params.put("billingid", collectionsheet.getString("billingid"));
				params.put("itemid", collectionsheet.getString("itemid"));
				params.put("state", "PENDING");
				params.put("trackerid", trackerid);
				params.put("txndate", Platform.getApplication().getServerDate().toString());
				params.put("borrower_objid", collectionsheet.getString("borrower_objid"));
				params.put("borrower_name", collectionsheet.getString("borrower_name"));
				params.put("loanapp_objid", collectionsheet.getString("loanapp_objid"));
				params.put("loanapp_appno", collectionsheet.getString("loanapp_appno"));
				params.put("collector_objid", SessionContext.getProfile().getUserId());
				params.put("collector_name", SessionContext.getProfile().getFullName());
				params.put("routecode", collectionsheet.getString("routecode"));
				params.put("remarks", remarks);
				params.put("lng", lng);
				params.put("lat", lat);
				params.put("type", collectionsheet.getString("type"));					
				    			 
				synchronized (RemarksDB.LOCK) {
					remarksdb.insert("remarks", params);
				}
				
				Map prm = new HashMap();
				prm.put("objid", params.get("objid").toString());
				prm.put("billingid", params.get("billingid").toString());
				prm.put("itemid", params.get("itemid").toString());
				prm.put("remarks", remarks);
				prm.put("collector_objid", params.get("collector_objid").toString());
				prm.put("collector_name", params.get("collector_name").toString());
				
				synchronized (MainDB.LOCK) {
					clfcdb.insert("remarks", prm);
				}
				
				ApplicationUtil.showShortMsg("Successfully added remark.");
			}
			
		};
		dialog.input(null, "Remarks");
	}
}
