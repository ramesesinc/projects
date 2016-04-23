package com.rameses.android.system;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.client.android.Platform;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIDialog;
import com.rameses.client.services.PasswordService;

public class ChangePasswordActivity extends SettingsMenuActivity 
{
	private ProgressDialog progressDialog;
	
	public boolean isCloseable() { return false; }	
	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.template_footer);
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_changepwd, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();

		setValue(R.id.et_password_old, "");
		setValue(R.id.et_password_new, "");
		setValue(R.id.et_password_confirm, "");
		requestFocus(R.id.et_password_old);
		
		new UIAction(this, R.id.btn_ok) {
			protected void onClick() {
				try {
					doSubmit();
				} catch(Throwable t) {
					UIDialog.showMessage("[ERROR] " + t.getMessage()); 
				}
			}
		};
		
	}
	
	private void doSubmit() {
		String oldpassword = getValueAsString(R.id.et_password_old);
		String newpassword = getValueAsString(R.id.et_password_new);
		String confirmpassword = getValueAsString(R.id.et_password_confirm);
		if (isEmpty(oldpassword)) {
			requestFocus(R.id.et_password_old); 
			ApplicationUtil.showShortMsg("Old Password is required");
			
		} else if (isEmpty(newpassword)) {
			requestFocus(R.id.et_password_new); 
			ApplicationUtil.showShortMsg("New Password is required");
			
		} else if (isEmpty(confirmpassword)) {
			requestFocus(R.id.et_password_confirm);
			ApplicationUtil.showShortMsg("Confirm Password is required");
		} else { 
			progressDialog.setMessage("Processing...");
			if (!progressDialog.isShowing()) progressDialog.show();
			
			Platform.runAsync(new ActionProcess(oldpassword, newpassword, confirmpassword));
		}
	}
	
	private Handler errorhandler = new Handler() {  
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();
			
			Bundle data = msg.getData();			
			Object o = data.getSerializable("response"); 
			if (o instanceof Throwable) {
				Throwable t = (Throwable)o;
				ApplicationUtil.showShortMsg("[ERROR] " + t.getMessage());
			} else {
				ApplicationUtil.showShortMsg("[ERROR] " + o);	
			} 
		} 
	}; 
	
	private Handler successhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();

			Bundle data = msg.getData();			
			String response = data.getString("response");
			ApplicationUtil.showShortMsg(response); 
			ChangePasswordActivity.this.disposeMe(); 
		}
	};
	
	private class ActionProcess implements Runnable 
	{
		private String oldpassword;
		private String newpassword;
		private String confirmpassword;
		
		public ActionProcess(String oldpassword, String newpassword, String confirmpassword) {
			this.oldpassword = oldpassword;
			this.newpassword = newpassword;
			this.confirmpassword = confirmpassword;
		}
		
		public void run() { 
			boolean success = false;			
			Bundle data = new Bundle();
			Handler handler = null;
			Message message = null;
			
			try {
				new PasswordService().changePassword(oldpassword, newpassword, confirmpassword);
				data.putString("response", "Password successfully changed");
				handler = successhandler;
				message = handler.obtainMessage();
				
			} catch (Throwable e) {
				data.putSerializable("response", e);
				handler = successhandler;
				message = handler.obtainMessage();				
				e.printStackTrace();
				
			} 
			
			message.setData(data);
			handler.sendMessage(message);
		}
	}
}