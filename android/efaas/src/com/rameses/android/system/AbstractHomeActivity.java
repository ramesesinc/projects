package com.rameses.android.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.rameses.android.ControlActivity;
import com.rameses.android.MenuAdapter;
import com.rameses.android.R;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIDialog;

public abstract class AbstractHomeActivity extends ControlActivity 
{
	private String txndate;
	private GridView gv_menu;
	private ProgressDialog progressDialog;	
	private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	
	public boolean isCloseable() { return false; }	
		
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		
		setContentView(R.layout.template_footer);
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_control_panel, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);

		gv_menu = (GridView) findViewById(R.id.gv_menu);	
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
				
		list.clear();
		loadMenuItems(list);
		list.add(createMenuItem("masterfile", "Master Files", null, R.drawable.masterfile));
		list.add(createMenuItem("settings", "Revision Settings", null, R.drawable.settings));
		list.add(createMenuItem("faas", "FAAS", null, R.drawable.faas));
		list.add(createMenuItem("download", "Download", null, R.drawable.download));
		list.add(createMenuItem("upload", "Upload", null, R.drawable.upload));
		list.add(createMenuItem("changepassword", "Change Password", null, R.drawable.changepassword));
		list.add(createMenuItem("logout", "Logout", null, R.drawable.logout));
		
		gv_menu.setAdapter(new MenuAdapter(this, list));
		gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				try { 
					selectionChanged(parent, view, position, id); 
				} catch (Throwable t) {
					UIDialog.showMessage(t, AbstractHomeActivity.this); 
				}
			} 
		}); 
	} 
	
	protected void afterActivityChanged() {
		Platform.getInstance().disposeAllExcept(this);
	}
	
	protected void afterBackPressed() {
		if (SessionContext.getSessionId() != null) {
			Platform.getApplication().suspendSuspendTimer();
		} 
	}
	
	protected Map<String, Object> createMenuItem(String name, String text, String subtext, int iconid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("text", text);
		map.put("subtext", subtext);
		map.put("iconid", iconid);
		return map;
	}
	
	protected abstract void loadMenuItems(List<Map<String, Object>> items);
	protected abstract void onItemClick(String name, Map<String, Object> item);
	
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
}
