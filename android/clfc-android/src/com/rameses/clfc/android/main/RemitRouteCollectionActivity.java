package com.rameses.clfc.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.VoidRequestDB;
import com.rameses.clfc.android.db.DBCSPayment;
import com.rameses.clfc.android.db.DBCollectionGroup;
import com.rameses.clfc.android.db.DBVoidService;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class RemitRouteCollectionActivity extends ControlActivity 
{
	private final int OBJID_KEY = "objid".hashCode();
	
	private ProgressDialog progressDialog;
	private LayoutInflater inflater;
//	private List<Map> routes;
//	private List<Map> routes;
//	private List<Map> followups;
//	private List<Map> specials;
	private SQLTransaction txn;
	private DBCollectionGroup colGroup = new DBCollectionGroup();
//	private DBRouteService routeSvc = new DBRouteService();
//	private ListView lv_routes;
//	private LinearLayout ll_routes;
//	private LinearLayout ll_followups;
	private RelativeLayout rl_routes;
	private RelativeLayout rl_followups;
	private RelativeLayout rl_specials;
//	private List<Map> list;
//	private Map params;
//	private Map itm;
//	private int listSize;
//	private Map item;
//	private Map route;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setTitle("CLFC Collection - ILS");
		setContentView(R.layout.template_footer);
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		inflater.inflate(R.layout.activity_routelist, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		((TextView) findViewById(R.id.tv_header_route)).setText("Routes:");
		rl_routes = (RelativeLayout) findViewById(R.id.rl_routes);
		rl_routes.setVisibility(View.GONE);

		((TextView) findViewById(R.id.tv_header_followup)).setText("Follow-up Collections:");
		rl_followups = (RelativeLayout) findViewById(R.id.rl_followups);
		rl_followups.setVisibility(View.GONE);

		((TextView) findViewById(R.id.tv_header_special)).setText("Special Collections:");
		rl_specials = (RelativeLayout) findViewById(R.id.rl_specials);
		rl_specials.setVisibility(View.GONE);
//		lv_routes = (ListView) findViewById(R.id.lv_route);
//		((TextView) findViewById(R.id.tv_header_route)).setText("Route:");
//		ll_routes = (LinearLayout) findViewById(R.id.ll_routes);
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		loadRoutes();
		loadFollowups();
		loadSpecials();
	}
	
	public void loadRoutes() {
		getHandler().post(new Runnable() {
			public void run() {
				List<Map> list = new ArrayList<Map>();
//				synchronized (MainDB.LOCK) {
					DBContext ctx = new DBContext("clfc.db");
					colGroup.setDBContext(ctx);
					colGroup.setCloseable(false);
					try {
						list = colGroup.getCollectionGroupsByCollector(SessionContext.getProfile().getUserId(), "route");
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, RemitRouteCollectionActivity.this);
					} finally {
						ctx.close();
					}
//				}
				
				if (list != null && list.size() > 0) {
					rl_routes.setVisibility(View.VISIBLE);
					LinearLayout layout = (LinearLayout) findViewById(R.id.ll_routes);
					populate(list, layout);
				}				
			}
		});
	}	
	
	public void loadFollowups() {
		getHandler().post(new Runnable() {
			public void run() {
				List<Map> list = new ArrayList<Map>();
//				synchronized (MainDB.LOCK) {
					DBContext ctx = new DBContext("clfc.db");
					colGroup.setDBContext(ctx);
					colGroup.setCloseable(false);
					try {
						list = colGroup.getCollectionGroupsByCollector(SessionContext.getProfile().getUserId(), "followup");
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, RemitRouteCollectionActivity.this);
					} finally {
						ctx.close();
					}
//				}
				
				if (list != null && list.size() > 0) {
					rl_followups.setVisibility(View.VISIBLE);
					LinearLayout layout = (LinearLayout) findViewById(R.id.ll_followups);
					populate(list, layout);
				}				
			}
		});
	}	
	
	public void loadSpecials() {
		getHandler().post(new Runnable() {
			public void run() {
				List<Map> list = new ArrayList<Map>();
//				synchronized (MainDB.LOCK) {
					DBContext ctx = new DBContext("clfc.db");
					colGroup.setDBContext(ctx);
					colGroup.setCloseable(false);
					try {
						list = colGroup.getCollectionGroupsByCollector(SessionContext.getProfile().getUserId(), "special");
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, RemitRouteCollectionActivity.this);
					} finally {
						ctx.close();
					}
//				}
				
				if (list != null && list.size() > 0) {
					rl_specials.setVisibility(View.VISIBLE);
					LinearLayout layout = (LinearLayout) findViewById(R.id.ll_specials);
					populate(list, layout);
				}				
			}
		});
	}

	private void populate(List<Map> list, LinearLayout layout) {
		try {
			layout.removeAllViews();
			layout.removeAllViewsInLayout();
			int size = list.size();
			View child;
			MapProxy item;
			TextView tv_description;
			TextView tv_area;
			RelativeLayout.LayoutParams layoutParams;
			String type;
			for (int i=0; i<size; i++) {
				item = new MapProxy((Map) list.get(i));
				
				if ("route".equals(item.getString("type"))) {
					child = inflater.inflate(R.layout.item_route, null);
					
					tv_description = (TextView) child.findViewById(R.id.tv_route_description);
					tv_area = (TextView) child.findViewById(R.id.tv_route_area);
					((TextView) child.findViewById(R.id.tv_route_billdate)).setVisibility(View.GONE);
				} else {
					child = inflater.inflate(R.layout.item_string, null);
					
					tv_description = (TextView) child.findViewById(R.id.tv_item_str);
					layoutParams = (RelativeLayout.LayoutParams) tv_description.getLayoutParams();
					layoutParams.bottomMargin = 5;
					layoutParams.leftMargin = 5;
//					layoutParams.addRule(RelativeLayout)
					tv_description.setLayoutParams(layoutParams);
					tv_description.setTypeface(Typeface.DEFAULT_BOLD);
					tv_description.setText(item.getString("name"));
					tv_area = null;
				}
				
				if (!"REMITTED".equals(item.getString("state"))) {
					child.setClickable(true);
					child.setOnClickListener(collectionOnClickListener);
					child.setOnLongClickListener(collectionOnLongClickListener);
					child.setTag(OBJID_KEY, item.getString("objid"));
				} else {
					addRemittedOverlay(child);
				}
				
				layoutParams = (RelativeLayout.LayoutParams) tv_description.getLayoutParams();
				layoutParams.leftMargin = 5;
				tv_description.setLayoutParams(layoutParams);
				tv_description.setText(item.getString("description"));
				
				if (tv_area != null) {
					layoutParams = (RelativeLayout.LayoutParams) tv_area.getLayoutParams();
					layoutParams.leftMargin = 5;
					tv_area.setLayoutParams(layoutParams);
					
					tv_area.setText(item.getString("area"));
				}
				layout.addView(child);
			}
		} catch (Throwable t) {
			UIDialog.showMessage(t, RemitRouteCollectionActivity.this);
		}
	}
	
	private View.OnClickListener collectionOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			view.setBackgroundResource(android.R.drawable.list_selector_background);
//			view.setPadding(5, 0, 0, 0);
			String objid = view.getTag(OBJID_KEY).toString();
			Map item = new HashMap();
//			synchronized (MainDB.LOCK) {
				DBContext ctx = new DBContext("clfc.db");
				DBCollectionGroup colGroup = new DBCollectionGroup();
				colGroup.setDBContext(ctx);
				colGroup.setCloseable(false);
				try {
					item = colGroup.findCollectionGroup(objid);
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, RemitRouteCollectionActivity.this);
				} finally {
					ctx.close();
				}
//			}
			
			final Map collection = item;
			if ("REMITTED".equals(collection.get("state").toString())) return;			
			
			boolean flag = false;
			ctx = new DBContext("clfc.db");
			DBCSPayment cspayment = new DBCSPayment();
			cspayment.setDBContext(ctx);;
			try {
				flag = cspayment.hasPaymentByItemid(item.get("objid").toString());
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, RemitRouteCollectionActivity.this);
			}
			 
			collection.put("haspayment", flag);
			if (flag == true) {
				UIDialog dialog = new UIDialog(RemitRouteCollectionActivity.this) {
					public boolean onApprove(Object value) {
						if (value == null) {
							ApplicationUtil.showShortMsg("CBS no. is required.");
							return false;
						}
						collection.put("cbsno", value.toString());
						remit(collection);
						return true;
//						System.out.println("value " + value);
					}
//					public void onApprove() {
//						remit(collection);
//					}
				};
							
				dialog.input(null, "Enter CBS No.");
			} else {
				remit(collection);
			}
			
//			String msg = "You are about to remit collections for " + collection.get("description").toString();
//			if ("route".equals(collection.get("type").toString())) msg += "-" + collection.get("area").toString();
//			msg += ". Continue?";
//			dialog.confirm(msg);
//			int idx = Integer.parseInt(view.getTag(POSITION_KEY).toString());
//			String type = view.getTag(TYPE_KEY).toString();
//			Map item = new HashMap();
//			
//			if ("route".equals(type)) {
//				item = (Map) routes.get(idx);
//			} else if 
//			System.out.println("index-> "+idx);
//			route = (Map) routes.get(idx);
//			
//			if (!"REMITTED".equals(route.get("state")+"")) {
//				UIDialog dialog = new UIDialog() {
//					public void onApprove() {
//						remit(route);
//					}
//				};
//				dialog.confirm("You are about to remit collections for this route. Continue?");
//			}
		}
	};
//	
	private View.OnLongClickListener collectionOnLongClickListener = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View view) {
			// TODO Auto-generated method stub
			view.setBackgroundResource(android.R.drawable.list_selector_background);
			return false;
		}
	};

	private void addRemittedOverlay(View child) {
		View overlay = inflater.inflate(R.layout.overlay_void_text, null);
		((TextView) overlay).setTextColor(this.getResources().getColor(R.color.red));
		((TextView) overlay).setText("REMITTED");
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
		overlay.setLayoutParams(layoutParams);
		((RelativeLayout) child).addView(overlay);
	}
	
	private void remit(Map collection) {
		try {
			new RemitRouteCollectionController(this, progressDialog, collection).execute();
		} catch (Throwable t) {
			UIDialog.showMessage(t, RemitRouteCollectionActivity.this);
		}
	}
//	
}
