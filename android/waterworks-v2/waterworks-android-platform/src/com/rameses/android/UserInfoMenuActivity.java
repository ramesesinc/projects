package com.rameses.android;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rameses.android.system.LogoutController;
import com.rameses.android.system.SettingsActivity;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIActionBarActivity;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserProfile;

public class UserInfoMenuActivity extends ControlActivity {
	
	private Context context;
	
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		context = this;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_menu, menu);
        
        return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_userinfo:
//				Intent intent = new Intent(this, SettingsActivity.class);
//				startActivity(intent);
				println("show user info");
				showUserInfoDialog();
				break;
			case R.id.menu_settings:
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
		}
		return true;
	}
	
	void showUserInfoDialog() {
		
		UserProfile prof = SessionContext.getProfile();
		
		String username = (prof != null? prof.getUserName() : "");
		String name = (prof != null? prof.getFullName() : "");
		
		//println("username " + username + " name " + name); 
		
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_userinfo);
		dialog.setTitle("User Account");
		
		TextView tv_username = (TextView) dialog.findViewById(R.id.tv_username);
		SpannableString content = new SpannableString(username.toLowerCase());
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		tv_username.setText(content);
		
		TextView tv_name = (TextView) dialog.findViewById(R.id.tv_name);
		tv_name.setText(name);
		
		ImageView icon = (ImageView) dialog.findViewById(R.id.iv_icon);
		icon.setImageResource(R.drawable.profile);
		
		Button btn_logout = (Button) dialog.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				println("logout");
				ProgressDialog progressDialog = new ProgressDialog(context);
				try {
					dialog.dismiss();
					new LogoutController((UIActionBarActivity) context, progressDialog).logout();
				} catch (Throwable t) {
					UIDialog.showMessage(t, UserInfoMenuActivity.this);
				}
			}
		});
		
		dialog.show();
	}
	
	void println(String str) {
		Log.i("UserInfoMenuActivity", str);
	}
}
