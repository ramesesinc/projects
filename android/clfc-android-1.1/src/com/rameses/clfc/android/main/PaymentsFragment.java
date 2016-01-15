package com.rameses.clfc.android.main;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBCSPayment;
import com.rameses.clfc.android.db.DBCSVoid;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class PaymentsFragment extends Fragment {

	private ListView listview;
	private Handler handler = new Handler();
	private int oldNoOfPayments = 0;
	private AlertDialog dialog = null;
	private ProgressDialog progressDialog;
	private String voidid = "";
	private LayoutInflater inflater;
	private LinearLayout layout;
	private int REQUEST_STATE = "REQUEST_STATE".hashCode();
	private View currentPaymentView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		View view = inflater.inflate(R.layout.fragment_linear, container, false);
		
//		listview = (ListView) view.findViewById(R.id.listview);
		layout = (LinearLayout) view.findViewById(R.id.ll_items);
		progressDialog = new ProgressDialog((CollectionSheetInfoMainActivity) getActivity());
		
		return view;
	}
	
	public void onStart() {
		super.onStart();
		try {
			loadPayments();
		} catch (Throwable t) {
			t.printStackTrace();
			UIDialog.showMessage(t, (CollectionSheetInfoMainActivity) getActivity());
		} 
//		
	}
	
	public int getCurrentNoOfPayments() { 
		Bundle args = getArguments();
		String objid = args.getString("objid");
		
		DBContext ctx = new DBContext("clfc.db");
		DBCSPayment cspayment = new DBCSPayment();
		cspayment.setDBContext(ctx);
		cspayment.setCloseable(false);
		
		int num = 0;
		
		try {
			num = cspayment.getNoOfPayments(objid);
		} catch (Throwable t) {;}
		finally {
			ctx.close();
		}
		
		return num;
	}
	public int getOldNoOfPayments() { return oldNoOfPayments; }	

	
	public void reloadPayments() {
		loadPayments();
	}
	
	private void loadPayments() {
		Bundle args = getArguments();
		final String objid = args.getString("objid");
		
		handler.post(new Runnable() {
			public void run() {
				List list = new ArrayList();
				
				DBContext ctx = new DBContext("clfc.db");

				DBCSPayment paymentcs = new DBCSPayment();
				paymentcs.setDBContext(ctx);
				paymentcs.setCloseable(false);
				
				try {
					list = paymentcs.getPayments(objid);

				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
					
				} finally {
					ctx.close();
				}
				
				if (list != null) {
					populate(list);
				}
			}
		});
	}
	
	private void populate(List<Map> list) {
		DBContext ctx = new DBContext("clfc.db");
		try {
			layout.removeAllViews();
			layout.removeAllViewsInLayout();
			int size = list.size();
			
			View child, overlay;
			MapProxy item;
			TextView tv_refno, tv_date, tv_paidby, tv_ptype, tv_amount;
			TextView tv_bank, tv_checkno, tv_checkdate;
			RelativeLayout rl_check;
			String option, requeststate, paymentid;
			Boolean hasrequest;
			BigDecimal amount = new BigDecimal("0").setScale(2);

			DBCSVoid voidcs = new DBCSVoid();
			voidcs.setDBContext(ctx);
			voidcs.setCloseable(false);
			
			Map voidrequest = new HashMap();
			RelativeLayout.LayoutParams layoutParams;
			
			for (int i=0; i<size; i++) {
				item = new MapProxy((Map) list.get(i));
				paymentid = item.getString("objid");
				
				child = inflater.inflate(R.layout.item_payment, null);
				child.setTag(R.id.paymentid, paymentid);
				
				tv_refno = (TextView) child.findViewById(R.id.tv_refno);
				tv_refno.setText(item.getString("refno"));
				
				tv_date = (TextView) child.findViewById(R.id.tv_txndate);
				tv_date.setText(item.getString("txndate"));
				
				tv_paidby = (TextView) child.findViewById(R.id.tv_info_paidby);
				tv_paidby.setText(item.getString("paidby"));
				
				tv_ptype = (TextView) child.findViewById(R.id.tv_info_paymenttype);
				tv_ptype.setText(item.getString("posttype"));
				
				amount = new BigDecimal(item.getString("amount")).setScale(2);
				tv_amount = (TextView) child.findViewById(R.id.tv_info_paymentamount);
				tv_amount.setText(formatValue(amount));
				
				rl_check = (RelativeLayout) child.findViewById(R.id.rl_check);
				rl_check.setVisibility(View.GONE);
				option = item.getString("payoption");
				if ("check".equals(option)) {
					tv_bank = (TextView) child.findViewById(R.id.tv_info_bank);
					tv_bank.setText(item.getString("bank_name"));
					
					tv_checkno = (TextView) child.findViewById(R.id.tv_info_checkno);
					tv_checkno.setText(item.getString("check_no"));
					
					tv_checkdate = (TextView) child.findViewById(R.id.tv_info_checkdate);
					tv_checkdate.setText(item.getString("check_date"));
				}
				

				hasrequest = false;
				requeststate = "";
				
				voidrequest = voidcs.findVoidRequestByPaymentid(paymentid);
				println("void request " + voidrequest);
//				System.out.println("void request " + voidrequest);
				if (voidrequest != null && !voidrequest.isEmpty()) {
					hasrequest = true;
//					requeststate = voidrequest.get("state").toString();
					requeststate = MapProxy.getString(voidrequest, "state");
				}
				item.put("requeststate", requeststate);
				item.put("hasrequest", hasrequest);
				
				child.setTag(REQUEST_STATE, requeststate);
				
				if (hasrequest == true) {
					child.setClickable(false);
//					String voidType = MapProxy.getString(payment, "requeststate");;
//					System.out.println("void type " + voidType);
					layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
					layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
					overlay = inflater.inflate(R.layout.overlay_void_text, null);
					if ("PENDING".equals(requeststate)) {
						((TextView) overlay).setTextColor(getActivity().getResources().getColor(R.color.red));
//						((TextView) overlay).setText(payment.get("refno").toString());
						((TextView) overlay).setText("VOID REQUEST PENDING");
						overlay.setLayoutParams(layoutParams);
						((RelativeLayout) child).addView(overlay); 
					} else if ("APPROVED".equals(requeststate)) {
						((TextView) overlay).setTextColor(getActivity().getResources().getColor(R.color.green));
						((TextView) overlay).setText("VOID APPROVED");
//						((TextView) overlay).setText(payment.get("refno").toString());
						overlay.setLayoutParams(layoutParams);
						((RelativeLayout) child).addView(overlay);
						//addApprovedVoidPaymentProperies(child);
					} else {
//						((TextView) overlay).setText(payment.get("refno").toString());
						((TextView) overlay).setText(item.getString("refno"));
						overlay.setLayoutParams(layoutParams);
						((RelativeLayout) child).addView(overlay);
					}
				} else {
					child.setClickable(true);
					child.setOnClickListener(paymentClickListener);
					child.setOnLongClickListener(paymentLongClickListener);
				}
				
				layout.addView(child);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			UIDialog.showMessage(t, (CollectionSheetInfoMainActivity) getActivity());
		} finally {
			ctx.close();
		}
	}
	
	private View.OnClickListener paymentClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			v.setBackgroundResource(android.R.drawable.list_selector_background);
			
		}
	};
	
	private View.OnLongClickListener paymentLongClickListener = new View.OnLongClickListener() {
		public boolean onLongClick(View v) {
			v.setBackgroundResource(android.R.drawable.list_selector_background);
						
			String rs = "";
			Object obj = v.getTag(REQUEST_STATE);
			if (obj != null) {
				rs = obj.toString();
			}
			
			if (!"".equals(rs) && rs != null) return true;
			
//			println("current payment view " + currentPaymentView);
			
			DBContext ctx = new DBContext("clfcpayment.db");
			DBPaymentService paymentsvc = new DBPaymentService();
			paymentsvc.setDBContext(ctx);
			paymentsvc.setCloseable(false);

			String paymentid = "";
			obj = v.getTag(R.id.paymentid);
			if (obj != null) {
				paymentid = obj.toString();
			}
			
			Map payment = new HashMap();
			try {
				payment = paymentsvc.findPaymentById(paymentid);
			} catch (Throwable t) { 
				t.printStackTrace(); 
			} finally {
				ctx.close();
			}
			
//			System.out.println("payment " + payment);
//			System.out.println("passing 1");
			if (payment == null || payment.isEmpty()) return true;

			currentPaymentView = v;
			CharSequence[] items = {"Void"};
			UIDialog dialog = new UIDialog((CollectionSheetInfoMainActivity) getActivity()) {
				public void onSelectItem(int index) {
					switch(index) {
						case 0:
																	
//							println("before show void dialog paymentid " + currentPaymentid);

							String dt = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyMMdd");
							
							voidid = "VOID" + dt + UUID.randomUUID();
							showVoidDialog();
							break;
					}	
				}
			};
			dialog.select(items);
			
			return false;
		}
	};

	private void showVoidDialog() {
		showVoidDialog(null);
	}

	private void showVoidDialog(View child) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Reason for void");
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.dialog_remarks, null);
		
		builder.setView(view);
		builder.setPositiveButton("Submit", null);
		builder.setNegativeButton("Cancel", null);
		dialog = builder.create();
		dialog.show();
		
		
		Button btn_positive = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn_positive.setOnClickListener(submitOnClickListener);
//		btn_positive.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View view) {
//			}
//			
//	
//		});
	}
	private View.OnClickListener submitOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			SQLTransaction txn = new SQLTransaction("clfc.db");
			
			try {
				txn.beginTransaction();
				
//				println("view " + currentPayment);
//				
//				String paymentid = "";
//				Object obj = v.getTag(R.id.paymentid);
//				if (obj != null) {
//					paymentid = obj.toString();
//				}
//				
				onClickImpl(txn);
				
				txn.commit();
			} catch (Throwable t) {
				UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
			} finally {
				txn.endTransaction();
			}
		}
		
		private void onClickImpl(SQLTransaction txn) throws Exception {
			DBCollectionSheet collectionsheetdb = new DBCollectionSheet();
			collectionsheetdb.setDBContext(txn.getContext());
			
			Bundle args = getArguments();
			String objid = args.getString("objid");
			
			Map collectionSheet = collectionsheetdb.findCollectionSheet(objid);
			String dt = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyMMdd");
			
			String paymentid = "";
			Object obj = currentPaymentView.getTag(R.id.paymentid);
			if (obj != null) {
				paymentid = obj.toString();
			}
			
			Map params = new HashMap();
//			params.put("objid", "VOID" + dt + UUID.randomUUID());
			params.put("objid", voidid);
			params.put("state", "PENDING");
//			params.put("csid", collectionSheet.get("objid").toString());
			params.put("csid", MapProxy.getString(collectionSheet, "objid"));
//			params.put("paymentid", payment.getTag(R.id.paymentid));
			params.put("paymentid", paymentid);
//			params.put("itemid", collectionSheet.get("itemid").toString());
			params.put("itemid", MapProxy.getString(collectionSheet, "itemid"));
			params.put("txndate", Platform.getApplication().getServerDate().toString());
//			params.put("collectionid", collectionSheet.get("billingid").toString());
			params.put("collectionid", MapProxy.getString(collectionSheet, "billingid"));
//			params.put("routecode", collectionSheet.get("routecode").toString());
			params.put("routecode", MapProxy.getString(collectionSheet, "routecode"));
			
			Map borrower = new HashMap();
//			borrower.put("objid", collectionSheet.get("borrower_objid").toString());
			borrower.put("objid", MapProxy.getString(collectionSheet, "borrower_objid"));
//			borrower.put("name", collectionSheet.get("borrower_name").toString());
			borrower.put("name", MapProxy.getString(collectionSheet, "borrower_name"));
			params.put("borrower", borrower);
			
			Map collector = new HashMap(); 
			collector.put("objid", SessionContext.getProfile().getUserId());
			collector.put("name", SessionContext.getProfile().getName());
			params.put("collector", collector);
			
			Map loanapp = new HashMap();
//			loanapp.put("objid", collectionSheet.get("loanapp_objid").toString());
			loanapp.put("objid", MapProxy.getString(collectionSheet, "loanapp_objid"));
//			loanapp.put("appno", collectionSheet.get("loanapp_appno").toString());
			loanapp.put("appno", MapProxy.getString(collectionSheet, "loanapp_appno"));
			params.put("loanapp", loanapp);
			
			params.put("reason", ((EditText) dialog.findViewById(R.id.remarks_text)).getText().toString());
			try {
//				String index = "";
//				Object obj = currentPaymentView.getTag(PaymentsAdapter.INDEX);
//				if (obj != null) {
//					index = obj.toString();
//				}
//				println("view index " + index);
								
				new VoidRequestController((CollectionSheetInfoMainActivity) getActivity(), progressDialog, params, currentPaymentView, dialog).execute();
				//reloadPayments();
			} catch (Throwable t) {
				t.printStackTrace();
				UIDialog.showMessage(t, (CollectionSheetInfoMainActivity) getActivity()); 
			}
	
		}
	};
	

//	private String formatValue(Object number) {
//		DecimalFormat df = new DecimalFormat("#,###,##0.00");
//		StringBuffer sb = new StringBuffer();
//		FieldPosition fp = new FieldPosition(0);
//		df.format(number, sb, fp);
//		return sb.toString();
//	}

	
	private void xloadPayments() {
		Bundle args = getArguments();
		final String objid = args.getString("objid");
		
		handler.post(new Runnable() {
			public void run() {
				DBContext ctx = new DBContext("clfc.db");				
				
				DBCSPayment paymentcs = new DBCSPayment();
				paymentcs.setDBContext(ctx);
				paymentcs.setCloseable(false);

				DBCSVoid voidcs = new DBCSVoid();
				voidcs.setDBContext(ctx);
				voidcs.setCloseable(false);
				
				List payments = new ArrayList();
				int size = 0;
				synchronized (MainDB.LOCK) {
					try {
						payments = paymentcs.getPayments(objid);
						
						size = payments.size();
						
						Map item, voidrequest;
						String requeststate;
						boolean hasrequest; 
						for (int i=0; i<size; i++) {
							item = (Map) payments.get(i);
							hasrequest = false;
							requeststate = "";
							
							voidrequest = voidcs.findVoidRequestByPaymentid(MapProxy.getString(item, "objid"));
//							System.out.println("void request " + voidrequest);
							if (voidrequest != null && !voidrequest.isEmpty()) {
								hasrequest = true;
//								requeststate = voidrequest.get("state").toString();
								requeststate = MapProxy.getString(voidrequest, "state");
							}
							item.put("requeststate", requeststate);
							item.put("hasrequest", hasrequest);
						}
//						runImpl(ctx);
					} catch (Throwable t) {
						t.printStackTrace();
						UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity())); 
					} finally {
						ctx.close();
					}
				}
				
				oldNoOfPayments = size;
				
				listview.setAdapter(new PaymentsAdapter(getActivity(), payments));
				final List list = payments; 
//				listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//					
//					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//						System.out.println("on item click");
//					}
//				});
				listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
						
						int index = Integer.parseInt(view.getTag(PaymentsAdapter.INDEX).toString());
						Map item = (Map) list.get(index);
						
//						String requeststate = item.get("requeststate").toString();
						String requeststate = MapProxy.getString(item, "requeststate");
						if (!"".equals(requeststate) && requeststate != null) return true;
						
						String currentPaymentid = "";
						View currentPaymentView;
						
						currentPaymentid = MapProxy.getString(item, "objid");
						if (item.containsKey("view")) {
							currentPaymentView = (View) item.get("view");
						}
//						println("current payment view " + currentPaymentView);
						
						DBContext ctx = new DBContext("clfcpayment.db");
						DBPaymentService paymentsvc = new DBPaymentService();
						paymentsvc.setDBContext(ctx);
						paymentsvc.setCloseable(false);
						
						Map payment = new HashMap();
						try {
							payment = paymentsvc.findPaymentById(currentPaymentid);
						} catch (Throwable t) { 
							t.printStackTrace(); 
						} finally {
							ctx.close();
						}
						
//						System.out.println("payment " + payment);
//						System.out.println("passing 1");
						if (payment == null || payment.isEmpty()) return true;
						
						CharSequence[] items = {"Void"};
//						final View v = view;
						UIDialog dialog = new UIDialog((CollectionSheetInfoMainActivity) getActivity()) {
							public void onSelectItem(int index) {
								switch(index) {
									case 0:
																				
//										println("before show void dialog paymentid " + currentPaymentid);

										String dt = ApplicationUtil.formatDate(Platform.getApplication().getServerDate(), "yyMMdd");
										
										voidid = "VOID" + dt + UUID.randomUUID();
										showVoidDialog();
										break;
								}	
							}
						};
						dialog.select(items);
//						System.out.println("passing 2");
						
						return false;
					}
				});
//				listview.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//				listview.setOnLongClickListener(new View.OnLongClickListener() {
//					@Override
//					public boolean onLongClick(View v) {
//						// TODO Auto-generated method stub
////						int index = Integer.parseInt(v.getTag(PaymentsAdapter.INDEX).toString());
////						Map item = (Map) list.get(index);
////						String requeststate = item.get("requeststate").toString();
//////						System.out.println("request state " + requeststate);
//////						if (!"".equals(requeststate) && requeststate != null) return true;
////
////						final View view = v;
////						CharSequence[] items = {"Void"};
////						UIDialog dialog = new UIDialog() {
////							public void onSelectItem(int index) {
////								switch(index) {
////									case 0:
////										showVoidDialog();
////										break;
////								}	
////							}
////						};
////						dialog.select(items);
//						
//						return false;
//					}
//				});
				
//				list.removeAllViews();
//				list.removeAllViewsInLayout();
//				boolean hasrequest = false;
//				String option, type = "", voidType;
//				BigDecimal amount;
//				RelativeLayout rl_check;
//				Map payment;
//				RelativeLayout.LayoutParams layoutParams;
//				LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				View overlay;
//				RelativeLayout child;
//				for (int i=0; i<size; i++) {
//					child = (RelativeLayout) inflater.inflate(R.layout.item_payment, null);
//					rl_check = (RelativeLayout) child.findViewById(R.id.rl_check);
//					rl_check.setVisibility(View.GONE);
//					payment = (Map) payments.get(i);
//
//					((TextView) child.findViewById(R.id.tv_info_refno)).setText(payment.get("refno").toString());
//					((TextView) child.findViewById(R.id.tv_info_txndate)).setText(payment.get("txndate").toString());
//					((TextView) child.findViewById(R.id.tv_info_paidby)).setText(payment.get("paidby").toString());
//					
//					String ptype = payment.get("paytype").toString();
////					System.out.println("payment type " + ptype);
//					if (payment.get("paytype").toString().equals("schedule")) {
//						type = "Schedule/Advance";
//					} else if (payment.get("paytype").toString().equals("over")) {
//						type = "Overpayment";
//					}
//					((TextView) child.findViewById(R.id.tv_info_paymenttype)).setText(type);
//					
//					option = payment.get("payoption").toString();
//					if ("check".equals(option)) {
//						rl_check.setVisibility(View.VISIBLE);
//						((TextView) child.findViewById(R.id.tv_info_bank)).setText(payment.get("bank_name").toString());
//						((TextView) child.findViewById(R.id.tv_info_checkno)).setText(payment.get("check_no").toString());
//						((TextView) child.findViewById(R.id.tv_info_checkdate)).setText(payment.get("check_date").toString());
//					}
//					
//					amount = new BigDecimal(payment.get("amount").toString()).setScale(2);
//					((TextView) child.findViewById(R.id.tv_info_paymentamount)).setText(formatValue(amount));
//					child.setTag(R.id.paymentid, payment.get("objid").toString());
//					
//					hasrequest = MapProxy.getBoolean(payment, "hasrequest");
////					voidRequest = voidSvc.findVoidRequestByPaymentid(payment.get("objid").toString());
//					if (hasrequest == false) {
//						addPaymentProperties(child);
//					} else if (hasrequest == true) {
//						voidType = MapProxy.getString(payment, "requeststate");
////						System.out.println("void type " + voidType);
//						layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//						layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, 1);
//						overlay = inflater.inflate(R.layout.overlay_void_text, null);
//						if (voidType.equals("PENDING")) {
//							child.setOnClickListener(null);
//							child.setOnLongClickListener(null);
//							child.setClickable(false);
//							((TextView) overlay).setTextColor(getResources().getColor(R.color.red));
//							((TextView) overlay).setText("VOID REQUEST PENDING");
//							overlay.setLayoutParams(layoutParams);
//							child.addView(overlay); 
//						} else if (voidType.equals("APPROVED")) {
//							((TextView) overlay).setTextColor(getResources().getColor(R.color.green));
//							((TextView) overlay).setText("VOID APPROVED");
//							overlay.setLayoutParams(layoutParams);
//							((RelativeLayout) child).addView(overlay);
//							//addApprovedVoidPaymentProperies(child);
//						}
//					}
//					list.addView(child);
//				}
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
	
	private void println(String msg) {
		Log.i("[PaymentsFragment]", msg);
	}
	
	private String formatValue(Object number) {
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		StringBuffer sb = new StringBuffer();
		FieldPosition fp = new FieldPosition(0);
		df.format(number, sb, fp);
		return sb.toString();
	}
}
