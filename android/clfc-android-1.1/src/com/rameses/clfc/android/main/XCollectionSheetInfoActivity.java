package com.rameses.clfc.android.main;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.AppSettingsImpl;
import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.RemarksDB;
import com.rameses.clfc.android.RemarksRemovedDB;
import com.rameses.clfc.android.db.DBCSPayment;
import com.rameses.clfc.android.db.DBCSRemarks;
import com.rameses.clfc.android.db.DBCSVoid;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBNoteService;
import com.rameses.clfc.android.db.DBPrevLocation;
import com.rameses.client.android.NetworkLocationProvider;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class XCollectionSheetInfoActivity extends ControlActivity 
{
	private AppSettingsImpl settings;
	private String objid = "";
	private String billingid = "";
	private String itemid = "";
	private String appno = "";
	private String acctid = "";
	private String acctname = "";
	private String cstype = "";
	private String loanappid = "";

	
	private BigDecimal overpayment = new BigDecimal("0").setScale(2);
	private BigDecimal dailydue = new BigDecimal("0").setScale(2);
	private String routecode = "";
	private String refno = "";
	private String paymenttype = "";
	private int totaldays = 0;
	private int isfirstbill = 0;
	private RelativeLayout rl_payment = null;
	private RelativeLayout rl_remarks = null;
	private RelativeLayout rl_notes = null;
	private AlertDialog dialog = null;
	private SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
	private LayoutInflater inflater;
	private Map remarks;
	private ProgressDialog progressDialog;
	private RelativeLayout rl_container;
	
	private DBCollectionSheet dbCollectionSheet = new DBCollectionSheet();	
//	private DBPaymentService paymentSvc = new DBPaymentService();
	private DBCSPayment cspayment = new DBCSPayment();
//	private DBVoidService voidSvc = new DBVoidService();
	private DBCSVoid csvoid = new DBCSVoid();
//	private DBRemarksService remarksSvc = new DBRemarksService();
	private DBCSRemarks csremarks = new DBCSRemarks();

	private Map collectionSheet = new HashMap();
	private BigDecimal amountdue = new BigDecimal("0").setScale(2);
	private BigDecimal loanamount = new BigDecimal("0").setScale(2);
	private BigDecimal balance = new BigDecimal("0").setScale(2);
	private BigDecimal interest = new BigDecimal("0").setScale(2);
	private BigDecimal penalty = new BigDecimal("0").setScale(2);
	private BigDecimal others = new BigDecimal("0").setScale(2);
	private int term = 0;
	private int size;
	private String duedate = "";
	private String homeaddress = "";
	private String collectionaddress = "";
	private List<Map> payments;
	private LinearLayout ll_info_payments;
	private LinearLayout ll_info_notes;

	private String type = "";
	private String voidType = "";
	private RelativeLayout child = null;
	private View overlay = null;
	private BigDecimal amount;
	private Map payment;
	private Map voidRequest;
	private RelativeLayout.LayoutParams layoutParams;
	private RelativeLayout remarks_child;
	private CharSequence[] remarks_items = {"Edit Remarks", "Remove Remarks"};
	private boolean noteDialogShowed = false;

	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.template_footer);
		setTitle("Collection Sheet Info");

		rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_collectionsheetinfo, rl_container, true);

		Intent intent = getIntent();
//		loanappid = intent.getStringExtra("loanappid");
//		detailid = intent.getStringExtra("detailid");
//		routecode = intent.getStringExtra("routecode");
//		paymenttype = intent.getStringExtra("paymenttype");
//		isfirstbill = intent.getIntExtra("isfirstbill", 0);
		
		objid = intent.getStringExtra("objid");		

		rl_payment = (RelativeLayout) findViewById(R.id.layout_info_payment);
		rl_remarks = (RelativeLayout) findViewById(R.id.layout_info_remarks);
		rl_notes = (RelativeLayout) findViewById(R.id.layout_info_notes);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
		ll_info_payments = (LinearLayout) findViewById(R.id.ll_info_payments);
		ll_info_notes = (LinearLayout) findViewById(R.id.ll_info_notes);		
	}

	protected void onStartProcess() {
		super.onStartProcess();
		onStartPocessImpl();
//		try {
//			
//		}  catch (Throwable t) {
//			t.printStackTrace();
//			UIDialog.showMessage(t, CollectionSheetInfoActivity.this); 
//		} finally {
//			clfcdb.close();
//			paymentdb.close();
//			remarksdb.close();
//			requestdb.close();
//		}
	}
	
	protected void onResumeProcess() {
		super.onResumeProcess();
		boolean hasNotes = false;
//		synchronized (MainDB.LOCK) {
			DBContext ctx = new DBContext("clfc.db");
			DBNoteService noteSvc = new DBNoteService();
			try {
				hasNotes = noteSvc.hasNotes(objid);
			} catch (Throwable t) {
				t.printStackTrace();
			}
//		}
		
		if (hasNotes && !noteDialogShowed) {
			showNotesDialog();
		}
	}

	private void onStartPocessImpl() {
		System.out.println("on start process collection sheet info");
		getHandler().post(new Runnable() {
			public void run() {
//				synchronized (MainDB.LOCK) {
					DBContext clfcdb = new DBContext("clfc.db");
					try {
						runImpl(clfcdb);
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, XCollectionSheetInfoActivity.this); 
					} finally {
						clfcdb.close();
					}
//				}		
			}
			
			private void runImpl(DBContext clfcdb) throws Exception {
				dbCollectionSheet.setDBContext(clfcdb);
				
//				System.out.println("loanappid-> "+loanappid);
//				System.out.println("clfcdb -> "+clfcdb);
//				collectionSheet = dbCollectionSheet.findCollectionSheetByLoanappid(loanappid);	
				collectionSheet = dbCollectionSheet.findCollectionSheet(objid);
				MapProxy proxy = new MapProxy(collectionSheet);
				
				if (collectionSheet != null && !collectionSheet.isEmpty()) {
//					acctid = proxy.getString("borrower_objid");
//					sessionid
//					
//					acctid = collectionSheet.get("acctid").toString();
//					sessionid = collectionSheet.get("sessionid").toString();
					
					itemid = proxy.getString("itemid");
					billingid = proxy.getString("billingid");
					acctid = proxy.getString("borrower_objid");
					acctname = proxy.getString("borrower_name");
					loanappid = proxy.getString("loanapp_objid");
					appno = proxy.getString("loanapp_appno");
					cstype = proxy.getString("type");
					amountdue = new BigDecimal(proxy.getDouble("amountdue")+"");
					loanamount = new BigDecimal(proxy.getDouble("loanapp_loanamount")+"");
					balance = new BigDecimal(proxy.getDouble("balance")+"");
					dailydue = new BigDecimal(proxy.getDouble("dailydue")+"");
					overpayment = new BigDecimal(proxy.getDouble("overpaymentamount")+"");
					interest = new BigDecimal(proxy.getDouble("interest")+"");
					penalty = new BigDecimal(proxy.getDouble("penalty")+"");
					others = new BigDecimal(proxy.getDouble("others")+"");
					term = proxy.getInteger("term");
					refno = proxy.getString("refno");
					homeaddress = proxy.getString("homeaddress");
					collectionaddress = proxy.getString("collectionaddress");
					try {
						duedate = df.format(new SimpleDateFormat("yyyy-MM-dd").parse(proxy.getString("maturitydate")));	
					} catch (Exception e) { e.printStackTrace();;}
					paymenttype = proxy.getString("paymentmethod");
					
//					acctname = collectionSheet.get("acctname").toString();
//					appno = collectionSheet.get("appno").toString();
//					cstype = collectionSheet.get("type").toString();
//					amountdue = new BigDecimal(collectionSheet.get("amountdue").toString());
//					loanamount = new BigDecimal(collectionSheet.get("loanamount").toString());
//					balance = new BigDecimal(collectionSheet.get("balance").toString());
//					dailydue = new BigDecimal(collectionSheet.get("dailydue").toString());
//					overpayment = new BigDecimal(collectionSheet.get("overpaymentamount").toString());
//					interest = new BigDecimal(collectionSheet.get("interest").toString());
//					penalty = new BigDecimal(collectionSheet.get("penalty").toString());
//					others = new BigDecimal(collectionSheet.get("others").toString());
//					term = Integer.parseInt(collectionSheet.get("term").toString());
//					refno = collectionSheet.get("refno").toString();
//					homeaddress = collectionSheet.get("homeaddress").toString();
//					collectionaddress = collectionSheet.get("collectionaddress").toString();
//					try {
//						duedate = df.format(new SimpleDateFormat("yyyy-MM-dd").parse(collectionSheet.get("duedate").toString()));	
//					} catch (Exception e) {;}
//					totaldays = amountdue.divide(dailydue, 2, BigDecimal.ROUND_HALF_UP).intValue();
//					if (paymenttype == null || paymenttype.equals("")) {
//						paymenttype = collectionSheet.get("paymentmethod").toString();
//					}
				}
				
				((TextView) findViewById(R.id.tv_info_acctname)).setText(acctname);
				((TextView) findViewById(R.id.tv_info_appno)).setText(appno);
				((TextView) findViewById(R.id.tv_info_loanamount)).setText(formatValue(loanamount));
				((TextView) findViewById(R.id.tv_info_balance)).setText(formatValue(balance));
				((TextView) findViewById(R.id.tv_info_dailydue)).setText(formatValue(dailydue));
				((TextView) findViewById(R.id.tv_info_amountdue)).setText(formatValue(amountdue));
				((TextView) findViewById(R.id.tv_info_overpayment)).setText(formatValue(overpayment));
				((TextView) findViewById(R.id.tv_info_duedate)).setText(duedate);
				((TextView) findViewById(R.id.tv_info_homeaddress)).setText(homeaddress);
				((TextView) findViewById(R.id.tv_info_collectionaddress)).setText(collectionaddress);
				((TextView) findViewById(R.id.tv_info_interest)).setText(formatValue(interest));
				((TextView) findViewById(R.id.tv_info_penalty)).setText(formatValue(penalty));
				((TextView) findViewById(R.id.tv_info_others)).setText(formatValue(others));
				((TextView) findViewById(R.id.tv_info_term)).setText(term+" days");
			}
		});

		rl_notes.setVisibility(View.GONE);
		rl_payment.setVisibility(View.GONE);
		rl_remarks.setVisibility(View.GONE);
		getHandler().post(new Runnable() {
			public void run() {
				payments = new ArrayList<Map>();
//				synchronized (PaymentDB.LOCK) {
//					DBContext paymentdb = new DBContext("clfcpayment.db");
					DBContext ctx = new DBContext("clfc.db");
					cspayment.setDBContext(ctx);
					cspayment.setCloseable(false);
					try {
						payments = cspayment.getPayments(objid);
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, XCollectionSheetInfoActivity.this); 
					} finally {
						ctx.close();
					}
//				}
;				
				if (payments != null && !payments.isEmpty()) {
					rl_payment.setVisibility(View.VISIBLE);

					size = payments.size();
//					synchronized (VoidRequestDB.LOCK) { 
//						DBContext requestdb = new DBContext("clfcrequest.db");
//						voidSvc.setDBContext(requestdb);
//						voidSvc.setCloseable(false);
						ctx = new DBContext("clfc.db");
						csvoid.setDBContext(ctx);
						csvoid.setCloseable(false);
						
						try {
							
							for (int i=0; i<size; i++) {
								payment = (Map) payments.get(i);
								payment.put("hasrequest", false);
								
								voidRequest = csvoid.findVoidRequestByPaymentid(MapProxy.getString(payment, "objid"));
								if (voidRequest != null && !voidRequest.isEmpty()) {
									//System.out.println("void request " + voidRequest);
									payment.put("hasrequest", true);
									payment.put("requeststate", MapProxy.getString(voidRequest, "state"));
								}
							}
						} catch (Throwable t) {
							t.printStackTrace();
							UIDialog.showMessage(t, XCollectionSheetInfoActivity.this); 
						} finally {
							ctx.close();
						}	
//					}
					
					ll_info_payments.removeAllViews();
					ll_info_payments.removeAllViewsInLayout();
					boolean hasrequest = false;
					String option;
					RelativeLayout rl_check;
					for (int i=0; i<size; i++) {
						child = (RelativeLayout) inflater.inflate(R.layout.item_payment, null);
						rl_check = (RelativeLayout) child.findViewById(R.id.rl_check);
						rl_check.setVisibility(View.GONE);
						payment = (Map) payments.get(i);

						((TextView) child.findViewById(R.id.tv_txndate)).setText(payment.get("refno").toString());
						((TextView) child.findViewById(R.id.tv_collector)).setText(payment.get("txndate").toString());
						((TextView) child.findViewById(R.id.tv_info_paidby)).setText(payment.get("paidby").toString());
						
						String ptype = payment.get("paytype").toString();
//						System.out.println("payment type " + ptype);
						if (payment.get("paytype").toString().equals("schedule")) {
							type = "Schedule/Advance";
						} else if (payment.get("paytype").toString().equals("over")) {
							type = "Overpayment";
						}
						((TextView) child.findViewById(R.id.tv_info_paymenttype)).setText(type);
						
						option = payment.get("payoption").toString();
						if ("check".equals(option)) {
							rl_check.setVisibility(View.VISIBLE);
							((TextView) child.findViewById(R.id.tv_info_bank)).setText(payment.get("bank_name").toString());
							((TextView) child.findViewById(R.id.tv_info_checkno)).setText(payment.get("check_no").toString());
							((TextView) child.findViewById(R.id.tv_info_checkdate)).setText(payment.get("check_date").toString());
						}
						
						amount = new BigDecimal(payment.get("amount").toString()).setScale(2);
						((TextView) child.findViewById(R.id.tv_info_paymentamount)).setText(formatValue(amount));
						child.setTag(R.id.paymentid, payment.get("objid").toString());
						
						hasrequest = MapProxy.getBoolean(payment, "hasrequest");
//						voidRequest = voidSvc.findVoidRequestByPaymentid(payment.get("objid").toString());
						if (hasrequest == false) {
							addPaymentProperties(child);
						} else if (hasrequest == true) {
							voidType = MapProxy.getString(payment, "requeststate");
//							System.out.println("void type " + voidType);
							layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
							layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
							overlay = inflater.inflate(R.layout.overlay_void_text, null);
							if (voidType.equals("PENDING")) {
								child.setOnClickListener(null);
								child.setOnLongClickListener(null);
								child.setClickable(false);
								((TextView) overlay).setTextColor(getResources().getColor(R.color.red));
								((TextView) overlay).setText("VOID REQUEST PENDING");
								overlay.setLayoutParams(layoutParams);
								child.addView(overlay); 
							} else if (voidType.equals("APPROVED")) {
								((TextView) overlay).setTextColor(getResources().getColor(R.color.green));
								((TextView) overlay).setText("VOID APPROVED");
								overlay.setLayoutParams(layoutParams);
								((RelativeLayout) child).addView(overlay);
								//addApprovedVoidPaymentProperies(child);
							}
						}
						ll_info_payments.addView(child);
					}
				}
			}
		});
		
		getHandler().post(new Runnable() {
			public void run() {
//				synchronized (RemarksDB.LOCK) {
//					DBContext remarksdb = new DBContext("clfcremarks.db");
					DBContext ctx = new DBContext("clfc.db");
					try {
						runImpl(ctx);
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, XCollectionSheetInfoActivity.this); 
					}
//				}
			}
			
			private void runImpl(DBContext ctx) throws Exception {
				csremarks.setDBContext(ctx);
				
				remarks = csremarks.findRemarksById(objid);
				
				if (remarks != null && !remarks.isEmpty()) {
					rl_remarks.setVisibility(View.VISIBLE);
					((TextView) findViewById(R.id.tv_info_remarks)).setText(remarks.get("remarks").toString());
					remarks_child = (RelativeLayout) findViewById(R.id.rl_info_remarks);
					addRemarksProperties(remarks_child);
				}
			}
		});
		
		getHandler().post(new Runnable() {
			public void run() {
				List<Map> notes = new ArrayList<Map>();
//				synchronized (MainDB.LOCK) {
					DBContext ctx = new DBContext("clfc.db");
					DBNoteService noteSvc = new DBNoteService();
					noteSvc.setDBContext(ctx);
					try {
						notes = noteSvc.getNotes(objid);
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, XCollectionSheetInfoActivity.this); 
					}
//				}
				
//				System.out.println("notes " + notes);
				if (notes != null && !notes.isEmpty()) {
					rl_notes.setVisibility(View.VISIBLE);
					
					ll_info_notes.removeAllViews();
					ll_info_notes.removeAllViewsInLayout();
					
					int size = notes.size();
					MapProxy proxy;
					View child;
					for (int i=0; i<size; i++) {
						proxy = new MapProxy((Map) notes.get(i));
						
						child = inflater.inflate(R.layout.item_string, null);
						((TextView) child.findViewById(R.id.tv_item_str)).setText((i+1) + ": " + proxy.getString("remarks"));
						ll_info_notes.addView(child);
					}
				}
			}
		});
	}
	
	private void showNotesDialog() {
		noteDialogShowed = true;
		UIDialog dialog = new UIDialog();
		String msg = "";
		
		List<Map> list = new ArrayList<Map>();
//		synchronized (MainDB.LOCK) {
			DBContext ctx = new DBContext("clfc.db");
			DBNoteService noteSvc = new DBNoteService();
			noteSvc.setDBContext(ctx); 
			try {
				list = noteSvc.getNotes(objid);
			} catch (Throwable t) {
				t.printStackTrace();
			}
//		}
		
		if (list != null && !list.isEmpty()) {
			int size = list.size();
			MapProxy proxy;
			for (int i=0; i<size; i++) {
				proxy = new MapProxy((Map) list.get(i));
				
				if (!msg.equals("")) msg += "\n\n";
				msg += (i+1) + ": " + proxy.getString("remarks");
			}
		}
		
		dialog.alert(msg, "Notes");
	}
	
	private void addRemarksProperties(View child) {
		child.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// TODO Auto-generated method stub
				view.setBackgroundResource(android.R.drawable.list_selector_background);
			}
		});
		child.setOnLongClickListener(new View.OnLongClickListener() {
			 @Override
			 public boolean onLongClick(View v) {
			 // TODO Auto-generated method stub
				 v.setBackgroundResource(android.R.drawable.list_selector_background);
				 UIDialog dialog = new UIDialog() {
					 public void onSelectItem(int index) {
						 switch(index) {
						 	case 0:
						 		showRemarksDialog("edit");
						 		break;
						 	case 1:
						 		removeRemarks();									 		
						 		break;
						 }
//						 SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
//						 SQLTransaction remarkremoveddb = new SQLTransaction("clfcremarksremoved.db");
//						 try {
//							 remarksdb.beginTransaction();
//							 remarkremoveddb.beginTransaction();
//							 onSelectedItemImpl(remarksdb, remarkremoveddb, index);
//							 remarksdb.commit();
//							 remarkremoveddb.commit();
//						 } catch (Throwable t) {
//							 UIDialog.showMessage(t, CollectionSheetInfoActivity.this);
//						 } finally {
//							 remarksdb.endTransaction();
//							 remarkremoveddb.endTransaction();
//						 }
					 }
					 
//					 private void onSelectedItemImpl(SQLTransaction remarksdb, SQLTransaction remarksremoveddb, int index) {
//						 switch(index) {
//						 	case 0:
//						 		showRemarksDialog("edit");
//						 		break;
//						 	case 1:
//						 		removeRemarks();									 		
//						 		break;
//						 }
//					 }
				 };
				 dialog.select(remarks_items);
				 return false;
			};
		});
	}
	
	private void removeRemarks() {
		synchronized (RemarksDB.LOCK) {
			SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
			try {
				remarksdb.beginTransaction();
				remarksdb.delete("remarks", "objid=?", new Object[]{objid});
				remarksdb.commit();
			} catch (Throwable t) {
				 UIDialog.showMessage(t, XCollectionSheetInfoActivity.this);
				
			} finally {
				remarksdb.endTransaction();
			}
		}
		
		synchronized (MainDB.LOCK) {
			SQLTransaction clfcdb = new SQLTransaction("clfc.db");
			try {
				clfcdb.beginTransaction();
				clfcdb.delete("remarks", "objid=?", new Object[]{objid});
				clfcdb.commit();
			} catch (Throwable t) {
				 UIDialog.showMessage(t, XCollectionSheetInfoActivity.this);
				
			} finally {
				clfcdb.endTransaction();
			}
		}
		
		synchronized (RemarksRemovedDB.LOCK) {
			SQLTransaction remarksremoveddb = new SQLTransaction("clfcremarksremoved.db");
			try {
				remarksremoveddb.beginTransaction();

		 		Map params = new HashMap();
		 		params.put("objid", objid);
		 		params.put("billingid", billingid);
		 		params.put("itemid", itemid);
		 		params.put("state", "PENDING");
		 		remarksremoveddb.insert("remarks_removed", params);
		 		
				remarksremoveddb.commit();
			} catch (Throwable t) {
				 UIDialog.showMessage(t, XCollectionSheetInfoActivity.this);
				
			} finally {
				remarksremoveddb.endTransaction();
			}
		}
		remarks = null;
		rl_remarks.setVisibility(View.GONE);
		ApplicationUtil.showShortMsg("Successfully removed remarks.");
		getHandler().post(new Runnable() {
			public void run() {
				getApp().remarksRemovedSvc.start();
			}
		});
	}

	private void addPaymentProperties(View child) {
		child.setClickable(true);
		child.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			// TODO Auto-generated method stub
				v.setBackgroundResource(android.R.drawable.list_selector_background);
			}
		});
		child.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
				final View view = v;
				v.setBackgroundResource(android.R.drawable.list_selector_background);
				CharSequence[] items = {"Void"};
				UIDialog dialog = new UIDialog() {
					public void onSelectItem(int index) {
						switch(index) {
							case 0:
								showVoidDialog(view);
								break;
						}	
					}
				};
				dialog.select(items);
				return false;
			}
		});
	}
	
	private void showVoidDialog() {
		showVoidDialog(null);
	}

	private void showVoidDialog(View child) {
		final View payment = child;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Reason for void");
		View view = inflater.inflate(R.layout.dialog_remarks, null);
		builder.setView(view);
		builder.setPositiveButton("Submit", null);
		builder.setNegativeButton("Cancel", null);
		dialog = builder.create();
		dialog.show();
		
		Button btn_positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn_positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// TODO Auto-generated method stub
				SQLTransaction txn = new SQLTransaction("clfc.db");
				try {
					txn.beginTransaction();
					onClickImpl(txn, payment);
					txn.commit();
				} catch (Throwable t) {
					UIDialog.showMessage(t, XCollectionSheetInfoActivity.this);
				} finally {
					txn.endTransaction();
				}
			}
			
			private void onClickImpl(SQLTransaction txn, View view) throws Exception {
				Map params = new HashMap();
				params.put("objid", "VOID" + UUID.randomUUID());
				params.put("state", "PENDING");
				params.put("csid", collectionSheet.get("objid").toString());
				params.put("paymentid", payment.getTag(R.id.paymentid));
				params.put("itemid", collectionSheet.get("itemid").toString());
				params.put("txndate", Platform.getApplication().getServerDate().toString());
				params.put("collectionid", collectionSheet.get("billingid").toString());
				params.put("routecode", collectionSheet.get("routecode").toString());
				
				Map borrower = new HashMap();
				borrower.put("objid", collectionSheet.get("borrower_objid").toString());
				borrower.put("name", collectionSheet.get("borrower_name").toString());
				params.put("borrower", borrower);
				
				Map collector = new HashMap(); 
				collector.put("objid", SessionContext.getProfile().getUserId());
				collector.put("name", SessionContext.getProfile().getFullName());
				params.put("collector", collector);
				
				Map loanapp = new HashMap();
				loanapp.put("objid", collectionSheet.get("loanapp_objid").toString());
				loanapp.put("appno", collectionSheet.get("loanapp_appno").toString());
				params.put("loanapp", loanapp);
				
				params.put("reason", ((EditText) dialog.findViewById(R.id.remarks_text)).getText().toString());
//				params.put("routecode", routecode);
//				
//				Map collector = new HashMap();
//				collector.put("objid", SessionContext.getProfile().getUserId());
//				collector.put("name", SessionContext.getProfile().getFullName());
//				params.put("collector", collector);
//				
//				DBCollectionSheet dbCs = new DBCollectionSheet();
//				dbCs.setDBContext(txn.getContext());
//
//				Map collectionSheet = new HashMap();
//				try {
//					collectionSheet = dbCs.findCollectionSheet(objid);
//				} catch (Exception e) {
//					throw e;
//				}
//				
//				MapProxy proxy = new MapProxy(collectionSheet);
//				Map loanapp = new HashMap();
//				loanapp.put("objid", proxy.getString("loanapp_objid"));
//				loanapp.put("appno", proxy.getString("loanapp_appno"));
//				params.put("loanapp", loanapp);
//				
//				Map borrower = new HashMap();
//				borrower.put("objid", proxy.getString("borrower_objid"));
//				borrower.put("name", proxy.getString("borrower_name"));
//				params.put("borrower", borrower);
//				
//				params.put("collectionid", collectionSheet.get("sessionid").toString());
//				
//				try {
//					new VoidRequestController(XCollectionSheetInfoActivity.this, progressDialog, params, view, dialog).execute();
//				} catch (Throwable t) {
//					UIDialog.showMessage(t, XCollectionSheetInfoActivity.this);
//				}
			}
		});
	}
	private String formatValue(Object number) {
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		StringBuffer sb = new StringBuffer();
		FieldPosition fp = new FieldPosition(0);
		df.format(number, sb, fp);
		return sb.toString();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.payment, menu);
		if (remarks != null && !remarks.isEmpty()) {
			((MenuItem) menu.findItem(R.id.payment_addremarks)).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
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
				UIDialog.showMessage(t, XCollectionSheetInfoActivity.this); 
			} finally {
				ctx.close();
//				paymentdb.close();
//				requestdb.close();
			}
		} else if (item.getItemId() == R.id.payment_addremarks) {
			showRemarksDialog("create");
		}
		return true;
	}
	
	private void addPaymentImpl(DBContext ctx) throws Exception {
		csvoid.setDBContext(ctx);
		csvoid.setCloseable(false);
		String itemid = collectionSheet.get("itemid").toString();
		String csid = collectionSheet.get("objid").toString();
		if (csvoid.hasPendingVoidRequest(itemid, csid)) {
			ApplicationUtil.showShortMsg("[ERROR] Cannot add payment. No confirmation for void requested at the moment.");
			
		} else {
			Intent intent = new Intent(this, PaymentActivity.class);
			intent.putExtra("itemid", objid);
			startActivity(intent);
		}
	}
	//
	 public void showRemarksDialog(String mode) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 builder.setTitle("Remarks");
		 View view = inflater.inflate(R.layout.dialog_remarks, null);
		 builder.setView(view);
		 builder.setPositiveButton("  Ok  ", null);
		 builder.setNegativeButton("Cancel", null);
		 dialog = builder.create();
		 dialog.show();
		 if (!mode.equals("create")) {
			 EditText et_remarks = (EditText) dialog.findViewById(R.id.remarks_text);
			 et_remarks.setText(remarks.get("remarks").toString());
			 et_remarks.setSelection(0, et_remarks.getText().toString().length());
		 }
		 Button btn_positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		 btn_positive.setOnClickListener(new RemarksValidationListener(dialog, mode));
	 }
	 
	 private class RemarksValidationListener implements View.OnClickListener 
	 {
		 private final Dialog dialog;
		 private final String mode;
		 private Location location;
		 private Map params = new HashMap();
		 private String mRemarks = null;
		 
		 public RemarksValidationListener(Dialog dialog, String mode) {
			 this.dialog = dialog;
			 this.mode = mode;
		 }

		 public void onClick(View v) {
			 // TODO Auto-generated method stub 
			 	mRemarks = ((EditText) dialog.findViewById(R.id.remarks_text)).getText().toString();
			 	if (mRemarks.trim().equals("")) {
					 ApplicationUtil.showShortMsg("Remarks is required.");
					 return;
			 	}
			 	
			 	String trackerid = settings.getTrackerid();
			 	
//			 	synchronized (RemarksDB.LOCK) {
			 		SQLTransaction remarksdb = new SQLTransaction("clfcremarks.db");
			 		SQLTransaction clfcdb = new SQLTransaction("clfc.db");
			 		DBContext ctx = new DBContext("clfctracker.db");
			 		DBPrevLocation prevLocationSvc = new DBPrevLocation();
			 		prevLocationSvc.setDBContext(ctx);
			 		try {
			 			remarksdb.beginTransaction();
			 			clfcdb.beginTransaction();
			 			execRemarks(remarksdb, clfcdb, mode, trackerid, prevLocationSvc);
			 			remarksdb.commit();
			 			clfcdb.commit();
			 			getHandler().post(new Runnable() {
							public void run() {
								getApp().remarksSvc.start();
							}
						});
			 		} catch (Throwable t) {
			 			t.printStackTrace();
						UIDialog.showMessage(t, XCollectionSheetInfoActivity.this);
			 			
			 		} finally {
			 			remarksdb.endTransaction();
			 			clfcdb.endTransaction();
			 		}
//			 	}
		 }
		 
		private void execRemarks(SQLTransaction remarksdb, SQLTransaction clfcdb, String mode, String trackerid, DBPrevLocation prevLocationSvc) throws Exception {
			location = NetworkLocationProvider.getLocation();
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
					 
			Map params = new HashMap();	
			params.put("objid", objid);
			params.put("billingid", billingid);
			params.put("itemid", itemid);
			params.put("state", "PENDING");
			params.put("trackerid", trackerid);
			params.put("txndate", Platform.getApplication().getServerDate().toString());
			params.put("borrower_objid", acctid);
			params.put("borrower_name", acctname);
			params.put("loanapp_objid", loanappid);
			params.put("loanapp_appno", appno);
			params.put("collector_objid", SessionContext.getProfile().getUserId());
			params.put("collector_name", SessionContext.getProfile().getFullName());
			params.put("routecode", collectionSheet.get("routecode").toString());
			params.put("remarks", mRemarks);
			params.put("lng", lng);
			params.put("lat", lat);
			params.put("type", cstype);
			
			    			 
			if (mode.equals("create")) {
				synchronized (RemarksDB.LOCK) {
					remarksdb.insert("remarks", params);
				}
				
				Map prm = new HashMap();
				prm.put("objid", params.get("objid").toString());
				prm.put("billingid", params.get("billingid").toString());
				prm.put("itemid", params.get("itemid").toString());
				prm.put("remarks", params.get("remarks").toString());
				prm.put("collector_objid", params.get("collector_objid").toString());
				
				synchronized (MainDB.LOCK) {
					clfcdb.insert("remarks", prm);
				}
				
				rl_remarks.setVisibility(View.VISIBLE);
				View remarks_child = (RelativeLayout) findViewById(R.id.rl_info_remarks); 
				addRemarksProperties(remarks_child);
				ApplicationUtil.showShortMsg("Successfully added remark.");
				
			} else if (!mode.equals("create")) {
				Map params2 = new HashMap();
				params2.put("remarks", mRemarks);
				synchronized (RemarksDB.LOCK) {
					remarksdb.update("remarks", "objid='"+objid+"'", params2);
				}
				synchronized (MainDB.LOCK) {
					clfcdb.update("remarks", "objid='"+objid+"'", params2);
				}
//				remarksdb.update("remarks", "loanappid='"+loanappid+"'", params);
				ApplicationUtil.showShortMsg("Successfully updated remark.");
				
			}
				
//			DBRemarksService remarksSvc = new DBRemarksService();
//			remarksSvc.setDBContext(remarksdb.getContext());
			DBCSRemarks csremarks = new DBCSRemarks();
			csremarks.setDBContext(clfcdb.getContext());
			remarks = csremarks.findRemarksById(objid);
			((TextView) findViewById(R.id.tv_info_remarks)).setText(mRemarks);
			dialog.dismiss();
		 }
	 }
}
