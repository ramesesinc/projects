package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.Task;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class CollectionSheetListActivity extends ControlActivity 
{
	private final int SIZE = 11;
	private final int POSITION_KEY = "position".hashCode();
	
//	private ListView lv_collectionsheet;
	private LinearLayout ll_collectionsheet;
	private String itemid = "";
	private String type = "";
	private int isfirstbill;
	private DBPaymentService paymentSvc = new DBPaymentService();
	private DBVoidService voidSvc = new DBVoidService();
	private LayoutInflater inflater;
	private MapProxy proxy;
	private EditText et_search;
	private int addSize = 0;
	private List<Map> collectionSheets;
//	private LinearLayout ll_collectionsheet;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.template_footer);
		setTitle("Collection Sheet");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_collectionsheet, rl_container, true);
		
		Intent intent = getIntent();
		itemid = intent.getStringExtra("itemid");
		et_search = (EditText) findViewById(R.id.et_search);
//		lv_collectionsheet = (ListView) findViewById(R.id.lv_collectionsheet);
		ll_collectionsheet = (LinearLayout) findViewById(R.id.ll_collectionsheet);
		
		et_search.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				addSize = 0;
				loadCollectionSheets(s+"");
			}

			public void afterTextChanged(Editable s) {}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
		});
		
		addSize = 0; 
		
		ll_collectionsheet.removeAllViews();
		ll_collectionsheet.removeAllViewsInLayout();

		View child = inflater.inflate(R.layout.item_collectionsheet, null);
		((TextView) child.findViewById(R.id.tv_item_collectionsheet)).setText("Loading...");
		((ImageView) child.findViewById(R.id.iv_item_collectionsheet)).setVisibility(View.GONE);
		ll_collectionsheet.addView(child);
	}
	
	protected void onStartProcess() {
		super.onStartProcess();

		loadCollectionSheets(et_search.getText().toString());
	}
	
	private void loadCollectionSheets(String searchtext) {
		Platform.getTaskManager().schedule(new LoadCollectionSheetTask(searchtext, (SIZE+addSize), itemid, this), 500);
		
	}	
//	
	private void loadCollectionSheetsImpl(List<Map> list, int listSize, boolean eof) {
//		Log.w("Loading collection sheets", "loading....");
		collectionSheets = list;
		ll_collectionsheet.removeAllViews();
		ll_collectionsheet.removeAllViewsInLayout();

		View child;
		TextView tv_info_name;
		ImageView iv_info_paid;
		int noOfPayments = 0;
		int noOfVoids = 0;
		for (int i=0; i<listSize; i++) {
			child = inflater.inflate(R.layout.item_collectionsheet, null);
			tv_info_name = (TextView) child.findViewById(R.id.tv_item_collectionsheet);
			iv_info_paid = (ImageView) child.findViewById(R.id.iv_item_collectionsheet);
			
			child.setClickable(true);
			child.setOnClickListener(collectionSheetOnClickListener);
			child.setOnLongClickListener(collectionSheetOnLongClickListener);
			child.setTag(POSITION_KEY, i);
			
			proxy = new MapProxy((Map) list.get(i));
			tv_info_name.setText(proxy.getString("borrower_name"));
			

			iv_info_paid.setVisibility(View.GONE);
			noOfPayments = proxy.getInteger("noofpayments");
			noOfVoids = proxy.getInteger("noofvoids");
			if (noOfPayments > 0 && noOfPayments > noOfVoids) {
				if (proxy.getInteger("isfirstbill") == 1) {
					proxy.put("isfirstbill", 0); 
				}
				iv_info_paid.setVisibility(View.VISIBLE);
			}
			ll_collectionsheet.addView(child);
		} 
		
		if (eof == false) {
			child = inflater.inflate(R.layout.item_string, null);
			
			child.setClickable(true);
			child.setOnClickListener(viewMoreOnClickListener);
			child.setOnLongClickListener(collectionSheetOnLongClickListener);
			
			TextView tv_str = (TextView) child.findViewById(R.id.tv_item_str);
			tv_str.setText("View more..");
			tv_str.setTextColor(getResources().getColor(R.color.blue));
			ll_collectionsheet.addView(child);
		}
	}
	
	private View.OnClickListener viewMoreOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			v.setBackgroundResource(android.R.drawable.list_selector_background);
			addSize += 5;
			loadCollectionSheets(et_search.getText().toString());
		}
	};
	
	private View.OnClickListener collectionSheetOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			v.setBackgroundResource(android.R.drawable.list_selector_background);
			int idx = Integer.parseInt(v.getTag(POSITION_KEY).toString());
			
			final Map cs = (Map) collectionSheets.get(idx);
			isfirstbill = MapProxy.getInteger(cs, "isfirstbill");
			System.out.println("cs " + cs);
			if (isfirstbill != 1) {
//				Intent intent = new Intent(CollectionSheetListActivity.this, XCollectionSheetInfoActivity.class);
				Intent intent = new Intent(CollectionSheetListActivity.this, CollectionSheetInfoMainActivity.class);
				intent.putExtra("objid", MapProxy.getString(cs, "objid"));
				startActivity(intent);
			} else if (isfirstbill == 1) {
				final String[] items = {"Schedule", "Overpayment"};
				UIDialog dialog = new UIDialog(CollectionSheetListActivity.this) {
					public void onSelectItem(int index) {
						String type = "schedule";
						if (index == 1) type = "over";
						synchronized (MainDB.LOCK) {
							SQLTransaction txn = new SQLTransaction("clfc.db");
							Map params = new HashMap();
							params.put("objid", MapProxy.getString(cs, "objid"));
							params.put("type", type);
							try {
								txn.beginTransaction();
								String sql = "UPDATE collectionsheet SET paymentmethod = $P{type} WHERE objid = $P{objid}";
								txn.execute(sql, params);
								txn.commit();
							} catch (Throwable t) {
								t.printStackTrace();
							} finally {
								txn.endTransaction();
							}
						}
						
//						Intent intent = new Intent(CollectionSheetListActivity.this, XCollectionSheetInfoActivity.class);
						Intent intent = new Intent(CollectionSheetListActivity.this, CollectionSheetInfoMainActivity.class);
						intent.putExtra("objid", MapProxy.getString(cs, "objid"));
						startActivity(intent);
					}
				};
				
				dialog.select(items);
			}
		}
	};
	
	private View.OnLongClickListener collectionSheetOnLongClickListener = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			v.setBackgroundResource(android.R.drawable.list_selector_background);
			return false;
		}
	};
	
	private void showPaymentTypeDialog(final Map map) {
		CharSequence[] items = {"Schedule", "Overpayment"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Payment Method");
		builder.setPositiveButton("OK", null);
		builder.setNegativeButton("Cancel", null);
		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
					case 0: type = "schedule"; break;
					case 1: type = "over"; break;
				}
			}
		});
		
		final AlertDialog dialog = builder.create();
		System.out.println("payment type dialog = "+dialog);
		dialog.show();	
		Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		b.setOnClickListener(new View.OnClickListener() {					
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				showCollectionSheetInfo(map);
			}
		});
	}
	
	
	private class LoadCollectionSheetTask extends Task
	{
		private String queryString;
		private int size;
		private String itemid;
//		private UIActivity activity;
		private UIActionBarActivity activity;
		
		public LoadCollectionSheetTask(String queryString, int size, String itemid, UIActionBarActivity activity) {
			this.queryString = queryString.trim();
			this.size = size;
			this.itemid = itemid;
			this.activity = activity;
		}
		
		public void run() {
			
			String searchtext = et_search.getText().toString().trim();
			if (!queryString.equals(searchtext)) return;
			
			try {
				runImpl();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		private void runImpl() throws Exception {

			DBCollectionSheet collectionSheet = new DBCollectionSheet();
			int totalcs = 0;
			DBContext ctx = new DBContext("clfc.db");
			collectionSheet.setDBContext(ctx);
			try {
				totalcs = collectionSheet.getCountByGroup(itemid);
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, CollectionSheetListActivity.this);
			}
		
			queryString = (!queryString.equals("")? queryString : "") + "%";
			Map params = new HashMap();
			params.put("searchtext", queryString);
			params.put("itemid", itemid);
			
			List collectionSheets = new ArrayList<Map>();
			DBContext clfcdb = new DBContext("clfc.db");
			collectionSheet.setDBContext(clfcdb);
			try {
				collectionSheets = collectionSheet.getCollectionSheetsByItemWithSearchtext(params, size);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
			int listSize = totalcs;
			boolean eof = true;
			
			if (collectionSheets.size() == size && collectionSheets.size() < totalcs) {
				listSize = collectionSheets.size()-1;
				eof = false;
			} else if (collectionSheets.size() < size) {
				listSize = collectionSheets.size();
			}
			
			final List<Map> list = collectionSheets;
			final int s = listSize;
			final boolean bool = eof;
			activity.getHandler().post(new Runnable() {
				public void run() {
					loadCollectionSheetsImpl(list, s, bool);
				}
			});			
		}		
	}
	
	private void showCollectionSheetInfo(Map map) {
		Intent intent = new Intent(CollectionSheetListActivity.this, XCollectionSheetInfoActivity.class);
		intent.putExtra("objid", MapProxy.getString(map, "objid"));
		startActivity(intent);
	}
}
