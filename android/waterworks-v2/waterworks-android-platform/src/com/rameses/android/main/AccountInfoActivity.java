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
import android.widget.RelativeLayout;

import com.rameses.android.AppSettingsImpl;
import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.bean.Account;
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

public class AccountInfoActivity extends UserInfoMenuActivity {

	private AccountDB accountdb = new AccountDB();
	private ReadingDB readingdb = new ReadingDB();
	private SettingDB settingdb = new SettingDB();
	private DecimalFormat decFormat = new DecimalFormat("#,##0.00");
	private String stuboutcode, acctid;
//	private String[] list;
	private int index;
	private ProgressDialog progressDialog;
	private double amtdue, totaldue;
	private int presentreading, lastreading, consumption;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		setTitle("Reading Sheet");

		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_account_info, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		Bundle extras = getIntent().getExtras();
		acctid = extras.get("acctid").toString();
		
		Object obj = extras.get("stuboutcode");
		if (obj != null) {
			stuboutcode = obj.toString();
		}
		
		index = extras.getInt("index");
//		list = extras.getStringArray("list");
//		Object obj = extras.get("previd");
//		if (obj != null) {
//			previd = obj.toString();
//		}
//	
//		obj = extras.get("nextid");
//		if (obj != null) {
//			nextid = obj.toString();
//		}

		setAccountInfo();
		setButtonClickListener();
	}
	
	protected void onStartProcess() {
		super.onStartProcess();
		setReadingInfo();
	}
	
	private Map getAccount() {

		DBContext ctx = new DBContext("main.db");
		
		accountdb.setDBContext(ctx);

		Map account = new HashMap();
		try {
			account = accountdb.findByObjid(acctid);
		} catch (Exception e) {
			UIDialog.showMessage(e, AccountInfoActivity.this);
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
			UIDialog.showMessage(e, AccountInfoActivity.this);
		}
		
		return reading;
	}
	
	void setAccountInfo() {
		MapProxy account = new MapProxy(getAccount());
		
		if (stuboutcode != null) {
			setTitle(stuboutcode + " ( " + account.getInteger("sortorder") + " ) ");
		}
		
		setValue(R.id.tv_acctno, account.getString("acctno"));
		setValue(R.id.tv_name, account.getString("acctname"));
		setValue(R.id.tv_address, account.getString("address"));
		setValue(R.id.tv_serialno, account.getString("serialno"));
		setValue(R.id.tv_classification, account.getString("classificationid"));
		setValue(R.id.tv_prevbalance, getPrevBalance(account));
	}
	
	void setReadingInfo() {
		MapProxy account = new MapProxy(getAccount());
		MapProxy reading = new MapProxy(getReading());
		
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
		new UIAction(this, R.id.btn_prev) {
			public void onClick() {
//				println("go to prev " + index);
				Map account = getAccount();
				DBContext ctx = new DBContext("main.db");
				accountdb.setDBContext(ctx);
				try {
					Map params = new HashMap();
					params.put("stuboutid", account.get("stuboutid").toString());
					params.put("sortorder", MapProxy.getInteger(account, "sortorder"));
					
					Map data = accountdb.findPreviousAccountByStuboutid(params);
					if (data != null) {
						acctid = data.get("objid").toString();
						setAccountInfo();
						setReadingInfo();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				if ((index - 1) >= 0) {
//					index--;
//					acctid = list[index];
//					setAccountInfo();
//					setReadingInfo();
//				}
			}
		};
		
		new UIAction(this, R.id.btn_next){
			public void onClick() {
//				println("go to next" + index);
				Map account = getAccount();
				DBContext ctx = new DBContext("main.db");
				accountdb.setDBContext(ctx);
				try {
					Map params = new HashMap();
					params.put("stuboutid", account.get("stuboutid").toString());
					params.put("sortorder", MapProxy.getInteger(account, "sortorder"));
					
					Map data = accountdb.findNextAccountByStuboutid(params);
					if (data != null) {
						acctid = data.get("objid").toString();
						setAccountInfo();
						setReadingInfo();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				if ((index + 1) < list.length) {
//					index++;
//					acctid = list[index];
//					setAccountInfo();
//					setReadingInfo();
//				}
			}
		};
		
		new UIAction(this, R.id.btn_print) {
		    Set<BluetoothDevice> pairedDevices;
			
			public void onClick() {
				println("print receipt");
//				
//				BluetoothDevice device;
				progressDialog.setMessage("processing...");
				if (!progressDialog.isShowing()) progressDialog.show();
				
				try {
					onClickImpl();
				} catch (Exception e) {
					UIDialog.showMessage(e, AccountInfoActivity.this);
				}

				if (progressDialog.isShowing()) progressDialog.dismiss();		
			}
			
			private void onClickImpl() throws Exception {
				DBContext ctx = new DBContext("main.db");
				settingdb.setDBContext(ctx);
				
				List<Map> list = settingdb.getSettings();
				AppSettingsImpl sets = (AppSettingsImpl) Platform.getApplication().getAppSettings();

				Account acc = new Account(getAccount());
				acc.setPresReading(presentreading);
				acc.setAmtDue(String.valueOf(amtdue));
				acc.setTotalDue(String.valueOf(totaldue));
				acc.setConsumption(String.valueOf(consumption));
				
				String printername = sets.getPrinterName();
				PrinterHandler printerhandler = sets.getPrinterHandler(AccountInfoActivity.this);
				
				println("handler name: " + printerhandler.getName() + " " + printerhandler.getClass().getName());
				
				BluetoothPrinterController btcontroller = new BluetoothPrinterController(AccountInfoActivity.this);
				btcontroller.setPrinter(printername);
				btcontroller.open();
				
				Map params = new HashMap();
				params.put("account", acc);
				
				println("data-> " + printerhandler.getData(params));
				
				btcontroller.print(printerhandler.getData(params));
//				while (btcontroller.getIsPrinting()) {
//					println("printing");
//				}
				btcontroller.close();
			}
		}; 
		
		new UIAction(this, R.id.btn_capture) {
			public void onClick() {
//				println("capture reading");
				Intent intent = new Intent(AccountInfoActivity.this, ReadingSheetActivity.class);
				intent.putExtra("acctid", acctid);
//				intent.putExtra("stuboutcode", stuboutcode);
				startActivity(intent);
			}
		};
	}
	
	void println(String msg) {
		Log.i("AccountInfoActivity", msg);
	}
}
