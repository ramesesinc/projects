package com.rameses.android.main;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.rameses.android.AppSettingsImpl;
import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.bean.Account;
import com.rameses.android.bean.ItemAccount;
import com.rameses.android.controller.BluetoothPrinterController;
import com.rameses.android.database.AccountDB;
import com.rameses.android.database.ReadingDB;
import com.rameses.android.database.SettingDB;
import com.rameses.android.handler.PrinterHandler;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;
import com.rameses.util.ObjectDeserializer;

public class AccountSearchActivity extends UserInfoMenuActivity {
	    
	private AccountDB accountdb = new AccountDB();
	private ReadingDB readingdb = new ReadingDB();
//	private RuleDB ruledb = new RuleDB();
	private SettingDB settingdb = new SettingDB();
	
	private MapProxy account = new MapProxy(new HashMap());
	private MapProxy reading = new MapProxy(new HashMap());
	private AutoCompleteTextView act_search;
	private DecimalFormat decFormat = new DecimalFormat("#,##0.00");
	private ProgressDialog progressDialog;
	private Context context;
	private double amtdue, totaldue;
	private int presentreading, lastreading, consumption;
	
	private String acctid;
////	private TextView tv_reading1, tv_reading2, tv_reading3, tv_reading4, tv_reading5, tv_reading6, currentTextView;
////	private Button btn_save, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_clear, btn_backspace;
////	private GestureDetector gestureDetector;
////	private View.OnTouchListener gestureListener = new View.OnTouchListener() {
//		
//		@Override
//		public boolean onTouch(View v, MotionEvent ev) {
//			// TODO Auto-generated method stub
//			currentTextView = (TextView) v;
//			currentTextView.getParent().requestDisallowInterceptTouchEvent(true);
//			return gestureDetector.onTouchEvent(ev);
//		}
//	};
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Account Search");
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_account_search, rl_container, true);	
		
		context = this;
		progressDialog = new ProgressDialog(this);
		
//		ImageButton ib_search = (ImageButton) findViewById(R.id.ib_search);
//		ib_search.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				println("search");
//			}
//		});
		
//		btn_save = (Button) findViewById(R.id.btn_save);
		
		act_search = (AutoCompleteTextView) findViewById(R.id.act_search);
		act_search.setThreshold(1);
		act_search.setAdapter(new AccountSearchAdapter(this, R.layout.item_account_search));
		act_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int idx, long pos) {
				println("on item click");
				acctid = view.getTag(R.string.id_tag).toString();
				
//				DBContext ctx = new DBContext("main.db");
//				accountdb.setDBContext(ctx);
//				accountdb.setAutoCloseConnection(false);
//				
//				readingdb.setDBContext(ctx);
//				readingdb.setAutoCloseConnection(false);
//				
//				try {
//					account = new MapProxy(accountdb.findByObjid(objid));
//					reading = new MapProxy(readingdb.findReadingByAcctid(objid));
//				} catch (Exception e) {
//					UIDialog.showMessage(e, AccountSearchActivity.this);
//				} finally {
//					ctx.close();
//				}
				
//				act_search.setText(account.get("acctname").toString());
				
				setValue(R.id.act_search, "");
				println("before clear focus");
				act_search.clearFocus();
				println("after clear focus");
				
				setAccountInfo();
				setReadingInfo();
//				setValue(R.id.tv_acctno, account.getString("acctno"));
//				setValue(R.id.tv_name, account.getString("acctname"));
//				setValue(R.id.tv_address, account.getString("address"));
//				setValue(R.id.tv_serialno, account.getString("serialno"));
//				setValue(R.id.tv_classification, account.getString("classificationid"));
//				setValue(R.id.tv_prevbalance, getPrevBalance(account));
								
//				act_search.setText("");
//				act_search.clearFocus();
				
//				
			}
		});
		act_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				println("has focus " + hasFocus);
			}
		});

		setButtonClickListener();
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		if (!account.isEmpty()) {
			setReadingInfo();
		}
	}

	private Map getAccount() {

		DBContext ctx = new DBContext("main.db");
		
		accountdb.setDBContext(ctx);

		Map account = new HashMap();
		try {
			account = accountdb.findByObjid(acctid);
		} catch (Exception e) {
			UIDialog.showMessage(e, AccountSearchActivity.this);
		}
		
		return account;
	}
	
	private Map getReading() {
		DBContext ctx = new DBContext("main.db");

		readingdb.setDBContext(ctx);

		Map reading = new HashMap();
		try {
			reading = readingdb.findReadingByAcctid(acctid);
		} catch (Exception e) {
			UIDialog.showMessage(e, AccountSearchActivity.this);
		}
		
		return reading;
	}
	
	void setAccountInfo() {	
		account = new MapProxy(getAccount());
		
		setValue(R.id.tv_acctno, account.getString("acctno"));
		setValue(R.id.tv_name, account.getString("acctname"));
		setValue(R.id.tv_address, account.getString("address"));
		setValue(R.id.tv_serialno, account.getString("serialno"));
		setValue(R.id.tv_classification, account.getString("classificationid"));
		setValue(R.id.tv_prevbalance, getPrevBalance(account));
	}
	
	void setReadingInfo() {
		reading = new MapProxy(getReading());
		
		Object val = reading.get("amtdue");
		if (val == null) {
			val = 0;
		}
		amtdue = Double.parseDouble(String.valueOf(val));
		setValue(R.id.tv_amtdue, decFormat.format(amtdue));
		
		val = reading.get("totaldue");
		if (val == null) {
			val = 0;
		}
		totaldue = Double.parseDouble(String.valueOf(val));
		setValue(R.id.tv_totaldue, decFormat.format(totaldue));

		val = account.get("lastreading");
		if (val == null) {
			val = 0;
		}
		lastreading = Integer.parseInt(String.valueOf(val));
		setValue(R.id.tv_reading_prev, String.valueOf(lastreading));
		
		val = reading.get("reading");
		if (val == null) {
			val = 0;
		}
		presentreading = Integer.parseInt(String.valueOf(val));
		setValue(R.id.tv_reading_present, String.valueOf(presentreading));
		
		consumption = presentreading - lastreading;
		if (consumption < 0) consumption = 0;
		setValue(R.id.tv_reading_consumption, String.valueOf(consumption));
	}

	private String getPrevBalance(MapProxy account) {
		double val = 0.00;
		if (account.containsKey("items")) {
			String items = account.getString("items");
			if (items != null) {
				List<Map> list = (List<Map>) ObjectDeserializer.getInstance().read(items);
				Map data = new HashMap();
				for (int i = 0; i < list.size(); i++) {
					data = (Map) list.get(i);
					if (data.containsKey("amount")) {
						val += Double.parseDouble(data.get("amount").toString());
					}
				}
			}
		}
		
		return decFormat.format(val);
	}
	
	void setButtonClickListener() {
		
		new UIAction(this, R.id.btn_print) {
		    Set<BluetoothDevice> pairedDevices;
			
			public void onClick() {
				println("print receipt");
				if (account.isEmpty()) return;
				if (reading == null) return;

				runOnUiThread(new Runnable() {
					public void run() {
						progressDialog.setMessage("processing...");
						if (!progressDialog.isShowing()) progressDialog.show();	
					}
				});
				
				try {
					onClickImpl();
				} catch (Exception e) {
					UIDialog.showMessage(e, AccountSearchActivity.this);
				}
				
				if (progressDialog.isShowing()) progressDialog.dismiss();
			}
			
			private void onClickImpl() throws Exception {
				DBContext ctx = new DBContext("main.db");
				settingdb.setDBContext(ctx);
				
				List<Map> list = settingdb.getSettings();
				AppSettingsImpl sets = (AppSettingsImpl) Platform.getApplication().getAppSettings();
								

				Account acc = new Account(account);
				acc.setPresReading(presentreading);
				acc.setAmtDue(String.valueOf(amtdue));
				acc.setTotalDue(String.valueOf(totaldue));
				acc.setConsumption(String.valueOf(consumption));
				
				String printername = sets.getPrinterName();
				PrinterHandler printerhandler = sets.getPrinterHandler(AccountSearchActivity.this);
				
//				println("handler name: " + printerhandler.getName());
				
				BluetoothPrinterController btcontroller = new BluetoothPrinterController(AccountSearchActivity.this);
				btcontroller.setPrinter(printername);
				btcontroller.open();
				
				Map params = new HashMap();
				params.put("account", acc);
				
				btcontroller.print(printerhandler.getData(params));
				btcontroller.close();
			}
		}; 
		
		new UIAction(this, R.id.btn_capture) {
			public void onClick() {
				if (account.isEmpty()) return;
//				println("capture reading");
				Intent intent = new Intent(AccountSearchActivity.this, ReadingSheetActivity.class);
				intent.putExtra("acctid", acctid);
//				intent.putExtra("stuboutcode", stuboutcode);
				startActivity(intent);
			}
		};
	}
	
	void println(String str) {
		Log.i("AccountSearchActivity", str);
	}
	
	
}
