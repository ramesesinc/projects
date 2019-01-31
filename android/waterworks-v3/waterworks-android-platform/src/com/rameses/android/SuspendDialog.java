package com.rameses.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.util.Encoder;

public final class SuspendDialog 
{
	private final static Object LOCKED = new Object();
	private static boolean active;
	
	private SuspendDialog() {}
	
	public static boolean isVisible() {
		return active;
	}
	 
	public static void show(String content) {
		show(content, null);
	}
	
	public static void show(String content, Context context) {
		synchronized (LOCKED) {
			if (active) {
				System.out.println("[SuspendDialog] suspend dialog is still active");
				return;
			}
//			if (context == null) context = Platform.getCurrentActivity();
//			if (context == null) context = Platform.getMainActivity();
			if (context == null) context = Platform.getCurrentActionBarActivity();
			if (context == null) context = Platform.getActionBarMainActivity();


			DialogImpl dialog = new DialogImpl(context);
			dialog.show(content);
		}
	}
	
	
	private static class DialogImpl implements View.OnClickListener  
	{
		private Context context;
		private AlertDialog dialog;
		
		DialogImpl(Context context) {
			this.context = context;
		}
		
		private void show(String content) {			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			View view = inflater.inflate(R.layout.dialog_suspend, null);
			((TextView) view.findViewById(R.id.idle_text)).setText(content);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context); 
			builder.setTitle("Session Timeout"); 
			builder.setView(view);
			builder.setPositiveButton("Resume", null);
			dialog = builder.create();
			dialog.setCancelable(false);
			dialog.show();
			
			Button btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
			btn.setOnClickListener(this);
			SuspendDialog.active = true; 
		}
		
		@Override
		public void onClick(View v) {
			
			EditText et_password = (EditText) dialog.findViewById(R.id.login_password);
			String input = et_password.getText().toString();
			if (input == null || input.length() == 0) {
				ApplicationUtil.showShortMsg("Please enter your password");
				et_password.requestFocus();
				return;
			}

			String username = SessionContext.getProfile().getUserName(); 			
			String encinput = Encoder.MD5.encode(input, username);
			if (!encinput.equals(SessionContext.getPassword())) {
				ApplicationUtil.showShortMsg("Password is invalid. Please try again");
				et_password.requestFocus();
				return;
			}
			
			dialog.dismiss(); 
			SuspendDialog.active = false;
			Platform.getApplication().resumeSuspendTimer(); 
		}		
	}
}
