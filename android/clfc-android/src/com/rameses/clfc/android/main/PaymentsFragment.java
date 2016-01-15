package com.rameses.clfc.android.main;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

public class PaymentsFragment extends Fragment {

	private ListView listview;
	private Handler handler = new Handler();
	private int oldNoOfPayments = 0;
	private AlertDialog dialog = null;
	private ProgressDialog progressDialog;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_linear, container, false);
		
		listview = (ListView) view.findViewById(R.id.listview);
		progressDialog = new ProgressDialog((CollectionSheetInfoMainActivity) getActivity());
		
		return view;
	}
	
	public void onStart() {
		super.onStart();
		try {
			loadPayments();
		} catch (Throwable t) {
			t.printStackTrace();
			UIDialog.showMessage(t, ((CollectionSheetInfoMainActivity) getActivity()));
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
							
							voidrequest = voidcs.findVoidRequestByPaymentid(item.get("objid").toString());
							if (voidrequest != null && !voidrequest.isEmpty()) {
								hasrequest = true;
								requeststate = voidrequest.get("state").toString();
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
						String requeststate = item.get("requeststate").toString();
						if (!"".equals(requeststate) && requeststate != null) return true;
						
						DBContext ctx = new DBContext("cfcpayment.db");
						DBPaymentService paymentsvc = new DBPaymentService();
						paymentsvc.setDBContext(ctx);
						paymentsvc.setCloseable(false);
						
						Map payment = new HashMap();
						try {
							payment = paymentsvc.findPaymentById(item.get("objid").toString());
						} catch (Throwable t) {;}
						finally {
							ctx.close();
						}
						
						if (payment == null || payment.isEmpty()) return true;
						
						CharSequence[] items = {"Void"};
						final View v = view;
						UIDialog dialog = new UIDialog((CollectionSheetInfoMainActivity) getActivity()) {
							public void onSelectItem(int index) {
								switch(index) {
									case 0:
										showVoidDialog(v);
										break;
								}	
							}
						};
						dialog.select(items);
						
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
	

	private void showVoidDialog() {
		showVoidDialog(null);
	}

	private void showVoidDialog(View child) {
		final View payment = child;
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
		btn_positive.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				SQLTransaction txn = new SQLTransaction("clfc.db");
				
				try {
					txn.beginTransaction();
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
				try {
					new VoidRequestController((CollectionSheetInfoMainActivity) getActivity(), progressDialog, params, payment, dialog).execute();
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, (CollectionSheetInfoMainActivity) getActivity()); 
				}
				
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
}
