package com.rameses.android.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rameses.android.MenuAdapter;
import com.rameses.android.R;
import com.rameses.android.UserInfoMenuActivity;
import com.rameses.android.controller.DownloadSectorController;
import com.rameses.android.database.AccountDB;
import com.rameses.android.database.ReadingDB;
import com.rameses.android.system.ChangePasswordActivity;
import com.rameses.android.system.LogoutController;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.db.android.DBContext;


public class DefaultHomeActivity extends UserInfoMenuActivity {
	private ListView lv_menu;
	private ProgressDialog progressDialog;	
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	private TextView tv_read, tv_unread;
	private ImageView iv_read, iv_unread;
	
	public boolean isCloseable() { return false; }	
		
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		setContentView(R.layout.template_footer);
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_menu_panel, rl_container, true);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		
		tv_read = (TextView) findViewById(R.id.tv_read);
		tv_unread = (TextView) findViewById(R.id.tv_unread);
		
		lv_menu = (ListView) findViewById(R.id.lv_menu);
		lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try { 
					selectionChanged(parent, view, position, id); 
				} catch (Throwable t) {
					UIDialog.showMessage(t, DefaultHomeActivity.this); 
				}
			} 
		}); 
		
		iv_read = (ImageView) findViewById(R.id.iv_read);
		iv_read.setImageResource(R.drawable.read);
		
		iv_unread = (ImageView) findViewById(R.id.iv_unread);
		iv_unread.setImageResource(R.drawable.unread);
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();

		list.clear();
		loadMenuItems(list);
		lv_menu.setAdapter(new MenuAdapter(this, list));
		loadReading();
		
//		list.add(createMenuItem("changepassword", "Change Password", "description", R.drawable.changepassword));
//		list.add(createMenuItem("logout", "Logout", "description", R.drawable.logout));
		
	} 
	
	void loadReading() {
		DBContext ctx = new DBContext("main.db");
		AccountDB accountdb = new AccountDB();
		accountdb.setDBContext(ctx);
		accountdb.setAutoCloseConnection(false);
		
		ReadingDB readingdb = new ReadingDB();
		readingdb.setDBContext(ctx);
		readingdb.setAutoCloseConnection(false);
		
		try {
			UserProfile prof = SessionContext.getProfile();
			String userid = (prof != null? prof.getUserId() : "");
			
			Map params = new HashMap();
			params.put("assigneeid", userid);
			int noofread = readingdb.countReadingByAssigneeid(params);
			int totalrecords = accountdb.countAccountByAssigneeid(params);
			int noofunread = totalrecords - noofread;
			
			tv_read.setText("READ: " + noofread);
			tv_unread.setText("UNREAD: " + noofunread);
			
		} catch (Throwable t) {
			t.printStackTrace();
			UIDialog.showMessage(t, DefaultHomeActivity.this);
		} finally {
			ctx.close();
		}
	}
	
	protected Map<String, Object> createMenuItem(String name, String title, String description, int iconid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("title", title);
		map.put("description", description);
		map.put("iconid", iconid);
		return map;
	} 
	
	protected void afterActivityChanged() {
		Platform.getInstance().disposeAllActionBarExcept(this);
	}
	
	protected void afterBackPressed() {
		if (SessionContext.getSessionId() != null) {
			Platform.getApplication().suspendSuspendTimer();
		} 
	}

	protected void loadMenuItems(List<Map<String, Object>> items) {
		items.add(createMenuItem("accounts", "Accounts", "View the list of account information including it's meter and application information.", R.drawable.database));
		items.add(createMenuItem("reading", "Reading Sheet", "Update the account's meter reading history by capturing it's new meter reading.", R.drawable.meter));
		items.add(createMenuItem("download", "Download", "Download account and meter information from the server database.", R.drawable.download));
		items.add(createMenuItem("upload", "Upload", "Upload meter reading information to the server database.", R.drawable.upload));
		items.add(createMenuItem("rates", "Water Rates", "View the list of water rates.", R.drawable.rates));
		items.add(createMenuItem("setting", "Setting", "Manage the system settings", R.drawable.settings));
	}
	
	protected void onItemClick(String name, Map<String, Object> item) throws Exception {
		Intent intent = null;
		if ("accounts".equals(name)) {
			println("Accounts selected"); 
			intent = new Intent(this, AccountsActivity.class);
			
		} else if ("reading".equals(name)) {
			println("Reading selected");
			intent = new Intent(this, AccountSearchActivity.class);
			
		} else if ("download".equals(name)) {
			println("Download selected");
//			intent = new Intent(this, DownloadListActivity.class);
			new DownloadSectorController((UIActionBarActivity) DefaultHomeActivity.this, progressDialog).execute();
			
		} else if ("upload".equals(name)) {
			println("Upload selected");
			intent = new Intent(this, UploadActivity.class);
			
		} else if ("rates".equals(name)) {
			println("Rates selected");
			intent = new Intent(this, RateListActivity.class);
			
		} else if ("setting".equals(name)) {
			println("Setting selected");
			intent = new Intent(this, PrinterSettingActivity.class);
			
		}
		
		if (intent != null) {
			startActivity(intent);
		}
	}
	
	protected void doLogout() throws Exception {
		new LogoutController(this, progressDialog).logout();
	}
	
	protected void doChangePassword() {
		Intent intent = new Intent(this, ChangePasswordActivity.class);  
		startActivity(intent); 
	}
	
	private void selectionChanged(AdapterView<?> parent, View view, int position, long id) throws Exception {
		Map item = (Map) parent.getItemAtPosition(position);
		Object name = item.get("name");
		if ("logout".equals(name)) {
			doLogout(); 
			
		} else if ("changepassword".equals(name)) {
			doChangePassword();
			
		} else {
			String sname = (name == null? null: name.toString());
			onItemClick(sname, item);
		}
	} 
	
	void println(String msg) {
		Log.i("DefaultHomeActivity", msg);
	}
}
