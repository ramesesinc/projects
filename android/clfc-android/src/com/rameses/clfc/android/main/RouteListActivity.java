package com.rameses.clfc.android.main;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.clfc.android.db.DBRemarksService;
import com.rameses.clfc.android.db.DBSystemService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class RouteListActivity extends ControlActivity 
{
	private final int POSITION_KEY = "position".hashCode();  
	private ProgressDialog progressDialog;
	private List<Map> routes;
	private List<Map> followups;
	private List<Map> specials;
//	private ListView lv_route;
	private Map route;
	private LinearLayout ll_routes;
	private LinearLayout ll_followups;
	private LinearLayout ll_specials;
	
	private RelativeLayout rl_routes;
	private RelativeLayout rl_followups;
	private RelativeLayout rl_specials;
	private LayoutInflater inflater;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("CLFC Collection - ILS");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_routelist, rl_container, true);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		routes = (List<Map>) bundle.getSerializable("routes");
		followups = (List<Map>) bundle.getSerializable("followups");
		specials = (List<Map>) bundle.getSerializable("specials");
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
//		lv_route = (ListView) findViewById(R.id.lv_route);
		((TextView) findViewById(R.id.tv_header_route)).setText("Routes:");
		ll_routes = (LinearLayout) findViewById(R.id.ll_routes);
		rl_routes = (RelativeLayout) findViewById(R.id.rl_routes);
		if (routes.isEmpty()) {
			rl_routes.setVisibility(View.GONE);
		}
		
		((TextView) findViewById(R.id.tv_header_followup)).setText("Follow-up Collections:");
		ll_followups = (LinearLayout) findViewById(R.id.ll_followups);
		rl_followups = (RelativeLayout) findViewById(R.id.rl_followups);
		if (followups.isEmpty()) {
			rl_followups.setVisibility(View.GONE);
		}
		
		((TextView) findViewById(R.id.tv_header_special)).setText("Special Collections:");
		ll_specials = (LinearLayout) findViewById(R.id.ll_specials);
		rl_specials = (RelativeLayout) findViewById(R.id.rl_specials);
		if (specials.isEmpty()) {
			rl_specials.setVisibility(View.GONE);
		}
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		loadRoutes();
		loadFollowupCollections();
		loadSpecialCollections();
	}
	
	public void loadRoutes() {
		getHandler().post(new Runnable() {
			public void run() {
				try {
					ll_routes.removeAllViews();
					ll_routes.removeAllViewsInLayout();
					int size = routes.size();
					View child;
					MapProxy item;
					TextView tv_description;
					TextView tv_area;
					RelativeLayout.LayoutParams layoutParams;
					for (int i=0; i<size; i++) {
						item = new MapProxy((Map) routes.get(i));
						
						child = inflater.inflate(R.layout.item_route, null);
						if (!"1".equals(item.getString("downloaded"))) {
							child.setClickable(true);
							child.setOnClickListener(routeOnClickListener);
							child.setOnLongClickListener(routeOnLongClickListener);
							child.setTag(POSITION_KEY, i);
						} else {
							addDownloadedOverlay(child);
						}						

						tv_description = (TextView) child.findViewById(R.id.tv_route_description);
						tv_area = (TextView) child.findViewById(R.id.tv_route_area);
						((TextView) child.findViewById(R.id.tv_route_billdate)).setVisibility(View.GONE);;

						layoutParams = (RelativeLayout.LayoutParams) tv_description.getLayoutParams();
						layoutParams.leftMargin = 5;
						tv_description.setLayoutParams(layoutParams);
						
						layoutParams = (RelativeLayout.LayoutParams) tv_area.getLayoutParams();
						layoutParams.leftMargin = 5;
						tv_area.setLayoutParams(layoutParams);
//						layoutParams.leftMargin = 5;
//						tv_description.setLayoutParams(layoutParams);
//						tv_area.setLayoutParams(layoutParams);
						
						tv_description.setText(item.getString("description"));
						tv_area.setText(item.getString("area"));
						ll_routes.addView(child);
					}
//					lv_route.setAdapter(new RouteAdapter(RouteListActivity.this, routes));
				} catch (Throwable t) {
					UIDialog.showMessage(t, RouteListActivity.this);
				}
			}
		});
	}
	
	public void loadFollowupCollections() {
		getHandler().post(new Runnable() {
			public void run() {

				try {
					ll_followups.removeAllViews();
					ll_followups.removeAllViewsInLayout();
					int size = followups.size();
					View child;
					MapProxy item;
					TextView mTextView;
					RelativeLayout.LayoutParams layoutParams;// = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					for (int i=0; i<size; i++) {
						item = new MapProxy((Map) followups.get(i));
						
						child = inflater.inflate(R.layout.item_string, null);
						if (!"1".equals(item.getString("downloaded"))) {
							child.setClickable(true);
							child.setOnClickListener(followupOnClickListener);
							child.setOnLongClickListener(routeOnLongClickListener);
							child.setTag(POSITION_KEY, i);
						} else {
							addDownloadedOverlay(child);
						}
						
						mTextView = (TextView) child.findViewById(R.id.tv_item_str);
						layoutParams = (RelativeLayout.LayoutParams) mTextView.getLayoutParams();
						layoutParams.bottomMargin = 5;
						layoutParams.leftMargin = 5;
//						layoutParams.addRule(RelativeLayout)
						mTextView.setLayoutParams(layoutParams);
						mTextView.setTypeface(Typeface.DEFAULT_BOLD);
						mTextView.setText(item.getString("name"));
						ll_followups.addView(child);
					}
//					lv_route.setAdapter(new RouteAdapter(RouteListActivity.this, routes));
				} catch (Throwable t) {
					UIDialog.showMessage(t, RouteListActivity.this);
				}
			}
		});
	}
	

	public void loadSpecialCollections() {
		getHandler().post(new Runnable() {
			public void run() {

				try {
					ll_specials.removeAllViews();
					ll_specials.removeAllViewsInLayout();
					int size = specials.size();
					View child;
					MapProxy item;
					TextView mTextView;
					RelativeLayout.LayoutParams layoutParams;// = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					for (int i=0; i<size; i++) {
						item = new MapProxy((Map) specials.get(i));
						
						child = inflater.inflate(R.layout.item_string, null);
						if (!"1".equals(item.getString("downloaded"))) {
							child.setClickable(true);
							child.setOnClickListener(specialOnClickListener);
							child.setOnLongClickListener(routeOnLongClickListener);
							child.setTag(POSITION_KEY, i);
						} else {
							addDownloadedOverlay(child);
						}
						
						mTextView = (TextView) child.findViewById(R.id.tv_item_str);
						layoutParams = (RelativeLayout.LayoutParams) mTextView.getLayoutParams();
						layoutParams.bottomMargin = 5;
						layoutParams.leftMargin = 5;
//						layoutParams.addRule(RelativeLayout)
						mTextView.setLayoutParams(layoutParams);
						mTextView.setTypeface(Typeface.DEFAULT_BOLD);
						mTextView.setText(item.getString("name"));
						ll_specials.addView(child);
					}
//					lv_route.setAdapter(new RouteAdapter(RouteListActivity.this, routes));
				} catch (Throwable t) {
					UIDialog.showMessage(t, RouteListActivity.this);
				}
			}
		});
	}	
	
	private void addDownloadedOverlay(View child) {
		View overlay = inflater.inflate(R.layout.overlay_void_text, null);
		((TextView) overlay).setTextColor(this.getResources().getColor(R.color.red));
		((TextView) overlay).setText("DOWNLOADED");
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
		overlay.setLayoutParams(layoutParams);
		((RelativeLayout) child).addView(overlay);
	}
	
	private View.OnClickListener followupOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			view.setBackgroundResource(android.R.drawable.list_selector_background);
			
			int idx = Integer.parseInt(view.getTag(POSITION_KEY).toString());
			Map map = (Map) followups.get(idx);
//			System.out.println("follow-up collection -> "+map);
			try {
				downloadFolloupCollection(map);	
			} catch (Throwable t) {
				UIDialog.showMessage(t, RouteListActivity.this);
			}
		}
	};
	
	private View.OnClickListener specialOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			view.setBackgroundResource(android.R.drawable.list_selector_background);
			
			int idx = Integer.parseInt(view.getTag(POSITION_KEY).toString());
			Map map = (Map) specials.get(idx);
			try {
				downloadSpecialCollection(map);	
			} catch (Throwable t) {
				UIDialog.showMessage(t, RouteListActivity.this);
			}
		}
	};	
	
	private void downloadFolloupCollection(Map item) throws Exception {
		new DownloadFollowupCollectionController(this, progressDialog, item).execute();
	}
	
	private void downloadSpecialCollection(Map item) throws Exception {
		new DownloadSpecialCollectionController(this, progressDialog, item).execute();
	}
	
	private View.OnClickListener routeOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			view.setBackgroundResource(android.R.drawable.list_selector_background);
//			view.setPadding(5, 0, 0, 0);
			int idx = Integer.parseInt(view.getTag(POSITION_KEY).toString());
//			System.out.println("index-> "+idx);
			boolean flag = hasBilling();
			if (flag == true) {
				String msg = "You can only download 1 billing at a time.";
				//UIDialog.showMessage(msg, RouteListActivity.this);
				ApplicationUtil.showShortMsg(msg, RouteListActivity.this);
				return;
			}
			
			route = (Map) routes.get(idx);
			try {
				downloadBilling();	
			} catch (Throwable t) {
				UIDialog.showMessage(t, RouteListActivity.this);
			}
		}
	};
	
	private View.OnLongClickListener routeOnLongClickListener = new View.OnLongClickListener() {
		public boolean onLongClick(View view) {
			view.setBackgroundResource(android.R.drawable.list_selector_background);
			return false;
		}
	};
		
	private void downloadBilling() throws Exception {
		new DownloadBillingController(this, progressDialog, route).execute();
	}
	
	private boolean hasBilling() {
		boolean flag = false;
		
		DBContext ctx = new DBContext("clfc.db");
		DBSystemService sysSvc = new DBSystemService();
		sysSvc.setDBContext(ctx);
		
		String collectorid = SessionContext.getProfile().getUserId();
		String date = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyyy-MM-dd");
		
		try {
			flag = sysSvc.hasBillingid(collectorid, date);
		} catch (Throwable t) {
			UIDialog.showMessage(t, RouteListActivity.this);
		}
		
		return flag;
	}
	
//	private boolean hasUnremittedCollections(Map route) {
//		boolean flag = false;
//		String routecode = route.get("code").toString();
//		DBContext ctx = new DBContext("clfcpayment.db");
//		DBPaymentService paymentSvc = new DBPaymentService();
//		paymentSvc.setDBContext(ctx);
//		
//		try {
//			flag = paymentSvc.hasPaymentsByRoutecode(routecode);
//		} catch (Throwable t) {
//			UIDialog.showMessage(t, RouteListActivity.this);
//		}
//		if (flag == true) return true;
//		
//		ctx = new DBContext("clfcremarks.db");
//		DBRemarksService remarksSvc = new DBRemarksService();
//		remarksSvc.setDBContext(ctx);
//		
//		try {
//			flag = remarksSvc.hasRemarksByRoutecode(routecode);
//		} catch (Throwable t) {
//			UIDialog.showMessage(t, RouteListActivity.this);
//		}
//		if (flag == true) return true;
//		return false;
//	}
	
}