package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.Task;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class PostingListActivity extends ControlActivity 
{
	private Context context = this;
	private LayoutInflater inflater;
	private EditText et_search;
	private int size;
	private Map map;
	private MapProxy proxy;
	private String objid;
	private boolean haspayments = false;
	private boolean hasremarks = false;
	private boolean hasunpostedpayments = false;
	private boolean hasunpostedremarks;
	private ProgressDialog progressDialog;
	
	private TextView tv_posted;
	private TextView tv_unposted;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		setTitle("CLFC Collection - ILS");
		setContentView(R.layout.template_footer);
		
		RelativeLayout container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_posting, container, true);
		
		RelativeLayout layout_posted = (RelativeLayout) findViewById(R.id.layout_posted);
		inflater.inflate(R.layout.section_posted, layout_posted, true);
		
		RelativeLayout layout_unposted = (RelativeLayout) findViewById(R.id.layout_unposted);
		inflater.inflate(R.layout.section_unposted, layout_unposted, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		
		tv_posted  = (TextView) findViewById(R.id.tv_posted);
		tv_posted.setText("No. of Posted: 0");
		
		tv_unposted = (TextView) findViewById(R.id.tv_unposted);
		tv_unposted.setText("No. of Unposted: 0");
		
		et_search = (EditText) findViewById(R.id.et_search);
		ImageButton ib_refresh = (ImageButton) findViewById(R.id.ib_refresh);
		ib_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setBackgroundResource(android.R.drawable.list_selector_background);
				loadCollectionSheets();
			}
		});
		
		et_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {;}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {;}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				loadCollectionSheets();
			}
		});
		et_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					String s = et_search.getText().toString();
					et_search.setSelection(0, s.length());
				}
			}
		});
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
//		System.out.println("is showing " + progressDialog.isShowing());
		if (!progressDialog.isShowing()) progressDialog.show();
		loadCollectionSheets();
	}
	
	private void loadCollectionSheets() {
		Platform.getTaskManager().schedule(new LoadCollectionSheetTask(et_search.getText().toString(), this), 500);
//		String searchtext = et_search.getText().toString();
//		if (searchtext == null || searchtext.equals("")) {
//			searchtext = "%";
//		} else {
//			searchtext += "%";
//		}
		
	}
	
	private void loadCollectionSheetsImpl(List<Map> list) {
		int noOfPosted = 0;
		int noOfUnposted = 0;
		
		View child = null;
		LinearLayout ll_posted = (LinearLayout) findViewById(R.id.ll_posted);
		ll_posted.removeAllViewsInLayout();
		ll_posted.removeAllViews();
		
		LinearLayout ll_unposted = (LinearLayout) findViewById(R.id.ll_unposted);
		ll_unposted.removeAllViewsInLayout();
		ll_unposted.removeAllViews();
		
		boolean posted = false;
		for (int i=0; i<size; i++) {
			proxy = new MapProxy((Map) list.get(i));
			
			haspayments = proxy.getBoolean("haspayments");
			hasremarks = proxy.getBoolean("hasremarks");
			if (haspayments == true || hasremarks == true) {
				hasunpostedpayments = proxy.getBoolean("hasunpostedpayments");
				hasunpostedremarks = proxy.getBoolean("hasunpostedremarks");
				
				if (haspayments && !hasunpostedpayments) {
					posted = true;
				} else if (haspayments && hasunpostedpayments) {
					posted = false;
				}
				
				if (hasremarks && !hasunpostedremarks) {
					posted = true;
				} else if (hasremarks && hasunpostedremarks) {
					posted = false; 
				}
				
				child = inflater.inflate(R.layout.item_string, null);
				((TextView) child.findViewById(R.id.tv_item_str)).setText(proxy.getString("borrower_name"));
				if (posted == true) {
//					System.out.println("Posted = "+noOfPosted);
					noOfPosted++;
					ll_posted.addView(child);
				} else {
//					System.out.println("Unposted = "+noOfUnposted);
					noOfUnposted++;
					ll_unposted.addView(child);
				}
			}
		}
		tv_posted.setText("No. of Posted: "+noOfPosted);
		tv_unposted.setText("No. of Unposted: "+noOfUnposted);
		if (progressDialog.isShowing()) progressDialog.dismiss();
	}	
	
	private class LoadCollectionSheetTask extends Task 
	{
		private String queryString;
//		private UIActivity activity;
		private UIActionBarActivity activity;
		private DBCollectionSheet collectionSheet = new DBCollectionSheet();
		private DBPaymentService paymentSvc = new DBPaymentService();
		private DBRemarksService remarksSvc = new DBRemarksService();
		
		public LoadCollectionSheetTask(String queryString, UIActionBarActivity activity) {
			this.queryString = queryString;
			this.activity = activity;
		}
		
		public void run() {
			
			String searchtext = et_search.getText().toString();
			if (!queryString.equals(searchtext)) return;

			try {
				runImpl();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		private void runImpl() throws Exception {

			queryString = (!queryString.equals("")? queryString : "") + "%";
			List<Map> list = new ArrayList<Map>();
//			synchronized (MainDB.LOCK) {
				DBContext clfcdb = new DBContext("clfc.db");
				collectionSheet.setDBContext(clfcdb);
				
				try {
					list = collectionSheet.getCollectionSheetsByCollector(SessionContext.getProfile().getUserId(), queryString);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
//					UIDialog.showMessage(t, PostingListActivity.this);
				}
//			}
			
			size = list.size();
			DBContext paymentdb = new DBContext("clfcpayment.db");
			paymentSvc.setDBContext(paymentdb);
			paymentSvc.setCloseable(false);
			

			DBContext remarksdb = new DBContext("clfcremarks.db");
			remarksSvc.setDBContext(remarksdb);
			remarksSvc.setCloseable(false);
			
			try {
				for (int i=0; i<size; i++) {
					map = (Map) list.get(i);
					
					objid = map.get("objid").toString();
					
//					synchronized (PaymentDB.LOCK) {
						map.put("haspayments", paymentSvc.hasPayments(objid));
						map.put("hasunpostedpayments", paymentSvc.hasUnpostedPayments(objid));
//					}
					
//					synchronized (RemarksDB.LOCK) {
						map.put("hasremarks", remarksSvc.hasRemarksById(objid));
						map.put("hasunpostedremarks", remarksSvc.hasUnpostedRemarksById(objid));
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				paymentdb.close();
				remarksdb.close();
			}
			
			final List<Map> l = list;
			activity.getHandler().post(new Runnable() {
				public void run() {
					loadCollectionSheetsImpl(l);
				}
			});
			
		}
	}
}
