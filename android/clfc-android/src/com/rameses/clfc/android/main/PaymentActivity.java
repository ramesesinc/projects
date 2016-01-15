package com.rameses.clfc.android.main;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rameses.clfc.android.AppSettingsImpl;
import com.rameses.clfc.android.ApplicationImpl;
import com.rameses.clfc.android.ApplicationUtil;
import com.rameses.clfc.android.ControlActivity;
import com.rameses.clfc.android.MainDB;
import com.rameses.clfc.android.PaymentDB;
import com.rameses.clfc.android.R;
import com.rameses.clfc.android.db.DBBankService;
import com.rameses.clfc.android.db.DBCollectionSheet;
import com.rameses.clfc.android.db.DBPaymentService;
import com.rameses.client.android.NetworkLocationProvider;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.db.android.SQLTransaction;
import com.rameses.util.MapProxy;

public class PaymentActivity extends ControlActivity 
{
	private ApplicationImpl app;
	private AppSettingsImpl settings;
	
	private MapProxy collectionSheet = new MapProxy(new HashMap());
	
	private String refno = "", type = "", objid = "";
	private EditText et_amount, et_overpayment, et_paidby;
	private String routecode = "";
	private int isfirstbill = 0, totaldays;
	private BigDecimal overpayment = new BigDecimal("0").setScale(2);
	private BigDecimal defaultAmount = new BigDecimal("0").setScale(2);
	private BigDecimal amountdue = new BigDecimal("0").setScale(2);
	private BigDecimal dailydue = new BigDecimal("0").setScale(2);
	private BigDecimal totaldue = new BigDecimal("0").setScale(2);
	private double lng = 0.0, lat = 0.0;
	
	private RelativeLayout rl_overpayment, rl_container, rl_check;
	
	private LayoutInflater inflater;
	private SQLTransaction paymentdb;
	private DBCollectionSheet colSheetSvc = new DBCollectionSheet();
	private DBPaymentService paymentSvc = new DBPaymentService();
	private Location location;
	private String message;	
	
	private String itemid, txndate, option = "cash";
	private Spinner optionSpinner, bankSpinner;
	private List<String> options = new ArrayList<String>(), banks = new ArrayList<String>();
	
	private EditText et_checkno, et_checkdate;
	private int year, month, day;
	
	private List bankSrc;
	private Map bank;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		
		setContentView(R.layout.template_footer);
		rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_payment, rl_container, true);

		rl_overpayment = (RelativeLayout) findViewById(R.id.rl_overpayment);
		
		et_overpayment = (EditText) findViewById(R.id.et_overpayment);
		et_overpayment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					String t = et_overpayment.getText().toString();
					et_overpayment.setSelection(0, t.length());
				}
			}
		});		
		
		et_paidby = (EditText) findViewById(R.id.et_payment_paidby);
		et_amount = (EditText) findViewById(R.id.et_payment_amount);
		et_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					String t = et_amount.getText().toString();
					et_amount.setSelection(0, t.length());
				}
			}
		});
		
		rl_check = (RelativeLayout) findViewById(R.id.rl_check);	
		optionSpinner = (Spinner) findViewById(R.id.payment_option);
		options.add("cash");
		options.add("check");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
         
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		         
		optionSpinner.setAdapter(dataAdapter);
		optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
//				System.out.println("item " + parent.getItemAtPosition(position).toString());
				option = parent.getItemAtPosition(position).toString();
				loadCheckUI();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		synchronized (MainDB.LOCK) {
			DBContext ctx = new DBContext("clfc.db");
			DBBankService bankSvc = new DBBankService();
			bankSvc.setDBContext(ctx);
			bankSvc.setCloseable(false);
			try {
				bankSrc = bankSvc.getBanks();
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {
				ctx.close();
			}
//		} 
		
		if (bankSrc != null && !bankSrc.isEmpty()) {
			int size = bankSrc.size();
			Map item;
			for (int i=0; i<size; i++) {
				item = (Map) bankSrc.get(i);
				
				banks.add(MapProxy.getString(item, "name"));				
			} 
		}	
		bankSpinner = (Spinner) findViewById(R.id.spinner_bank);
//		ArrayAdapter<Map> bankAdapter = new ArrayAdapter<Map>(this, android.R.layout.simple_spinner_item, bankSrc);
		ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, banks);
         
		bankAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		         
		bankSpinner.setAdapter(bankAdapter);
		bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				bank = (Map) bankSrc.get(position);
//				System.out.println("item " + parent.getItemAtPosition(position).toString());
//				option = parent.getItemAtPosition(position).toString();
//				loadCheckUI();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		et_checkno = (EditText) findViewById(R.id.et_payment_checkno);
		final Calendar c = Calendar.getInstance();
		c.setTime(Platform.getApplication().getServerDate());
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		et_checkdate = (EditText) findViewById(R.id.et_payment_checkdate);
		et_checkdate.setFocusable(false);
		et_checkdate.setEnabled(false);
		
		Button btn_checkdate = (Button) findViewById(R.id.btn_checkdate);
		btn_checkdate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(PaymentActivity.this, datePickerListener, year, month, day).show();
			}
		});
//		et_checkdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if (hasFocus) {
//					showDateDialog();
//				}
//			}
//		});
//		et_checkdate.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				showDateDialog();
//			}
//		});
		
		Intent intent = getIntent();	
		itemid = intent.getStringExtra("itemid");
		if (itemid != null) {
//			synchronized (MainDB.LOCK) {
				ctx = new DBContext("clfc.db");
				colSheetSvc.setDBContext(ctx);
				try {
					collectionSheet = new MapProxy(colSheetSvc.findCollectionSheet(itemid));
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, PaymentActivity.this);
				}
//			}
		}
		
		if (!collectionSheet.isEmpty()) {
			isfirstbill = collectionSheet.getInteger("isfirstbill");
			totaldays = collectionSheet.getInteger("totaldays");
			type = collectionSheet.getString("paymentmethod");
			refno = collectionSheet.getString("refno");
			dailydue = new BigDecimal(collectionSheet.getDouble("dailydue")+"").setScale(2);
			overpayment = new BigDecimal(collectionSheet.getDouble("overpaymentamount")+"").setScale(2);
			defaultAmount = dailydue;
			if ("over".equals(type)) {
				if (isfirstbill == 0) {
					defaultAmount = overpayment;
				}
			}
			amountdue = new BigDecimal(collectionSheet.getDouble("amountdue")+"").setScale(2);
			
			totaldue = dailydue.multiply(new BigDecimal(totaldays + "").setScale(2)).setScale(2, BigDecimal.ROUND_HALF_UP);
//			synchronized (PaymentDB.LOCK) {
				ctx = new DBContext("clfcpayment.db");
				paymentSvc.setDBContext(ctx);
				paymentSvc.setCloseable(false);
				try {
					if (paymentSvc.hasPayments(itemid)) {
						refno += paymentSvc.noOfPayments(itemid);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, PaymentActivity.this);
				} finally {
					ctx.close();
				}
//			}
		}
		et_amount.setText(defaultAmount.toString());
		
		objid = "PT" + UUID.randomUUID().toString();
		app = (ApplicationImpl) Platform.getApplication();
		settings = (AppSettingsImpl) Platform.getApplication().getAppSettings();
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			
			String m = (((month+1)+"").length() < 2? "0" : "") + (month+1);
			StringBuilder sb = new StringBuilder();
			sb.append(year + "-" + m + "-" + day);
			et_checkdate.setText(sb);
		}
	};
	
	protected void onStartProcess() {
		super.onStartProcess();
		
		rl_overpayment.setVisibility(View.GONE);
		if (type.equals("over")) rl_overpayment.setVisibility(View.VISIBLE);
		
		et_overpayment.setEnabled(false);
		if (isfirstbill == 1) et_overpayment.setEnabled(true);
		//if (overpayment.compareTo(new BigDecimal("0").setScale(2)) > 0) {
			//decf.format(overpayment, sb, fp);
			et_overpayment.setText(overpayment.toString());
		//}
		
		try {
			txndate = new SimpleDateFormat("MMM-dd-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(Platform.getApplication().getServerDate().toString()));
		} catch (Throwable t) { ;}
		
		((TextView) findViewById(R.id.tv_payment_refno)).setText(refno);
		((TextView) findViewById(R.id.tv_payment_txndate)).setText(txndate);	
		
		new UIAction(this, R.id.btn_ok) {
			protected void onClick() {
				try {
					doSavePayment();
				} catch(Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage("[ERROR] " + t.getMessage()); 
				}
			}
		};
		
		loadCheckUI();
	}
	
	private void loadCheckUI() {
		rl_check.setVisibility(View.GONE);
		if ("check".equals(option)) {
			rl_check.setVisibility(View.VISIBLE);
			et_checkno.setText("");
			et_checkdate.setText("");
			bank = new HashMap();
			bankSpinner.setSelection(0);
			
			et_checkno.requestFocus();
		} else {
			et_amount.requestFocus();
		}
	}
	
	private void doSavePayment() {		
		if (option.trim().equals("")) {
			ApplicationUtil.showShortMsg("Option is required.");
			optionSpinner.requestFocus();
			return;
			
		}
		
		if ("check".equals(option)) {
			if (bank == null || bank.isEmpty()) {
				ApplicationUtil.showShortMsg("Bank is required.");
				bankSpinner.requestFocus();
				return;
				
			}
			
			String checkno = et_checkno.getText().toString();
			if (checkno.trim().equals("")) {
				ApplicationUtil.showShortMsg("Check no. is required.");
				et_checkno.requestFocus();
				return;
				
			}
			
			String checkdate = et_checkdate.getText().toString();			
			if (checkdate.trim().equals("")) {
				ApplicationUtil.showShortMsg("Check date is required.");
				et_checkdate.requestFocus();
				return;
				
			}
		}
		
		
		String amount = et_amount.getText().toString();
		if (amount.trim().equals("")) {
			ApplicationUtil.showShortMsg("Amount is required.");
			et_amount.requestFocus();
			return;
			
		}
		BigDecimal amt = new BigDecimal(amount).setScale(2);
		BigDecimal amt2 = new BigDecimal("0");
		if ("over".equals(type) && isfirstbill == 1) {
			String amount2 = et_overpayment.getText().toString();
			if (amount2.trim().equals("")) {
				ApplicationUtil.showShortMsg("Overpayment is required.");
				et_overpayment.requestFocus();
				return;
			}
			
			BigDecimal defaultamount = new BigDecimal(collectionSheet.getDouble("dailydue")+"").setScale(2);
			BigDecimal overamount = new BigDecimal(amount2).setScale(2);
			if (overamount.compareTo(defaultamount) <= 0) {
				ApplicationUtil.showShortMsg("Overpayment must be greater than schedule of payment of " + defaultamount.toString());
				et_overpayment.requestFocus();
				return;
			}
			
			amt2 = new BigDecimal(amount2).setScale(2);
//			int totaldays = amountdue.divide(defaultAmount, 2, BigDecimal.ROUND_HALF_UP).intValue();
			int td = amt.divide(amt2, 2, BigDecimal.ROUND_HALF_UP).intValue();
			if (td < totaldays) {
				ApplicationUtil.showShortMsg("Amount paid could not cover up to current date based on overpayment amount.");
				et_amount.requestFocus();
				return;
				
			}
		}
		
		String paidby = et_paidby.getText().toString();
		if (paidby.trim().equals("")) {
			ApplicationUtil.showShortMsg("Paid by is required.");
			et_paidby.requestFocus();
			return;
			
		}
		
		UIDialog dialog = new UIDialog(this) {
			public void onApprove() {
				onApproveImpl();
			}
		};
		message = amt.toString();
//		message = "Amount Paid: "+amt.toString();
//		if (type.equals("over") && isfirstbill == 1) message += "\nOverpayment: "+amt2.toString();
//		message += "\n\nEnsure that all information are correct. Continue?";
		
		dialog.confirm(message, collectionSheet.get("borrower_name").toString(), 65);
	}
	
	private void onApproveImpl() {
		getHandler().post(new Runnable() {
			public void run() {
				paymentdb = new SQLTransaction("clfcpayment.db");
				SQLTransaction clfcdb = new SQLTransaction("clfc.db");
				try {
					paymentdb.beginTransaction();
					clfcdb.beginTransaction();
					runImpl(paymentdb, clfcdb);
					paymentdb.commit();
					clfcdb.commit();
					app.paymentSvc.start();
					finish();
				} catch (Throwable t) {
					t.printStackTrace();
					UIDialog.showMessage(t, PaymentActivity.this); 
				} finally {
					paymentdb.endTransaction();
					clfcdb.endTransaction();
				}
			}
			
			private void runImpl(SQLTransaction paymentdb, SQLTransaction clfcdb) throws Exception {
				String trackerid = settings.getTrackerid();
//				synchronized (PaymentDB.LOCK) {
				execPayment(paymentdb, clfcdb, trackerid);
//				}
				
				if (isfirstbill == 1) {
					synchronized (MainDB.LOCK) {
						Map params = new HashMap();
						params.put("objid", collectionSheet.getString("objid"));
						params.put("isfirstbill", 0);
						String sql = "UPDATE collectionsheet SET isfirstbill = $P{isfirstbill} WHERE objid = $P{objid}";
						clfcdb.execute(sql, params);
					}
				}
			}
			
			private void execPayment(SQLTransaction paymentdb, SQLTransaction clfcdb, String trackerid) throws Exception {
				
				location = NetworkLocationProvider.getLocation();
				lng = (location == null? 0.0 : location.getLongitude());
				lat = (location == null? 0.0 : location.getLatitude());
//				System.out.println("location-> "+location);
				Map params = new HashMap();
				params.put("objid", objid);
				params.put("parentid", collectionSheet.getString("objid"));
				params.put("state", "PENDING");
				params.put("itemid", collectionSheet.getString("itemid"));
				params.put("billingid", collectionSheet.getString("billingid"));
				params.put("trackerid", trackerid);
				params.put("collector_objid", SessionContext.getProfile().getUserId());
				params.put("collector_name", SessionContext.getProfile().getFullName());
				params.put("borrower_objid", collectionSheet.getString("borrower_objid"));
				params.put("borrower_name", collectionSheet.getString("borrower_name"));
				params.put("loanapp_objid", collectionSheet.getString("loanapp_objid"));
				params.put("loanapp_appno", collectionSheet.getString("loanapp_appno"));
				params.put("routecode", collectionSheet.getString("routecode"));
				params.put("refno", refno);
				params.put("txndate", Platform.getApplication().getServerDate().toString());
				params.put("paytype", type);
				params.put("payoption", option);
				params.put("amount", Double.parseDouble(et_amount.getText().toString()));
				params.put("paidby", et_paidby.getText().toString());
				params.put("isfirstbill", isfirstbill);
				params.put("lng", lng);
				params.put("lat", lat);
				params.put("type", collectionSheet.getString("type"));
				
				if ("check".equals(option)) {
					params.put("bank_objid", bank.get("objid").toString());
					params.put("bank_name", bank.get("name").toString());
					params.put("check_no", et_checkno.getText().toString());
					params.put("check_date", java.sql.Date.valueOf(et_checkdate.getText().toString()));
				}		
//				System.out.println("params-> "+params);
				synchronized (PaymentDB.LOCK) {
					paymentdb.insert("payment", params);
				}
				
				String posttype = "Schedule";
				BigDecimal amountpaid = new BigDecimal(params.get("amount").toString()).setScale(2);
				if (amountpaid.compareTo(totaldue) > 0) {
					posttype = "Overpayment";
				} else if (amountpaid.compareTo(totaldue) < 0) {
					posttype = "Underpayment";
				}
				
				Map prm = new HashMap();
				prm.put("objid", params.get("objid").toString());
				prm.put("parentid", params.get("parentid").toString());
				prm.put("itemid", params.get("itemid").toString());
				prm.put("billingid", params.get("billingid").toString());
				prm.put("txndate", params.get("txndate").toString());
				prm.put("refno", params.get("refno").toString());
				prm.put("posttype", posttype);
				prm.put("paytype", params.get("paytype").toString());
				prm.put("payoption", params.get("payoption").toString());
				prm.put("amount", params.get("amount").toString());
				prm.put("paidby", params.get("paidby").toString());
				prm.put("collector_objid", params.get("collector_objid").toString());
				
				if ("check".equals(option)) {		
					prm.put("bank_objid", params.get("bank_objid").toString());
					prm.put("bank_name", params.get("bank_name").toString());
					prm.put("check_no", params.get("check_no").toString());
					prm.put("check_date", params.get("check_date").toString());
				}
				
				synchronized (MainDB.LOCK) {
					clfcdb.insert("payment", prm);
				}
//				System.out.println("done insert");
			}
			
		});
	}		
}
