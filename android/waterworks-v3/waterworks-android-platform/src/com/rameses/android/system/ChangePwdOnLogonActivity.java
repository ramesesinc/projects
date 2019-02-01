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

public class ChangePwdOnLogonActivity extends SettingsMenuActivity 
{
	private ProgressDialog progressDialog;
	
	public boolean isCloseable() { return false; }	
	
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.template_footer);
		
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_changepwdonlogon, rl_container, true);
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false); 
	}
	
	protected void afterBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}
	
	protected void onStartProcess() {
		super.onStartProcess();

		setValue(R.id.et_password_new, "");
		setValue(R.id.et_password_confirm, "");
		requestFocus(R.id.et_password_new);
		
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
		String newpassword = getValueAsString(R.id.et_password_new);
		String confirmpassword = getValueAsString(R.id.et_password_confirm);
		if (newpassword == null || newpassword.trim().length() == 0) {
			requestFocus(R.id.et_password_new); 
			ApplicationUtil.showShortMsg("New Password is required.");
			
		} else if (confirmpassword == null || confirmpassword.trim().length() == 0) {
			requestFocus(R.id.et_password_confirm);
			ApplicationUtil.showShortMsg("Confirm Password is required.");
			
		} else {
			progressDialog.setMessage("Processing...");
			if (!progressDialog.isShowing()) progressDialog.show();
						
			Platform.runAsync(new ActionProcess(newpassword, confirmpassword));
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
			Platform.getApplication().resumeAppLoader();  
		}
	};
	
	private class ActionProcess implements Runnable 
	{
		private String newpassword;
		private String confirmpassword;
		
		public ActionProcess(String newpassword, String confirmpassword) {
			this.newpassword = newpassword;
			this.confirmpassword = confirmpassword;
		}
		
		public void run() { 
			boolean success = false;			
			Bundle data = new Bundle();
			Message msg = errorhandler.obtainMessage();
			
			try {
				new PasswordService().changePassword(null, newpassword, confirmpassword);
				msg = successhandler.obtainMessage();
				data.putString("response", "Password successfully changed");
				success = true;
				
			} catch (Throwable e) {
				data.putSerializable("response", e);
				e.printStackTrace();
				
			} finally { 
				msg.setData(data);
				if (success) {
					successhandler.sendMessage(msg);
				} else {
					errorhandler.sendMessage(msg);
				}
			}
		}
	}
}