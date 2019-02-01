package com.rameses.android.system;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.client.android.AppContext;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIAction;
import com.rameses.client.android.UIApplication;
import com.rameses.client.android.UIDialog;
import com.rameses.client.interfaces.UserProfile;
import com.rameses.client.services.LoginService;
import com.rameses.client.services.SessionProviderImpl;
import com.rameses.util.Base64Cipher;
import com.rameses.util.Encoder;

public class LoginActivity extends SettingsMenuActivity 
{
	private ProgressDialog progressDialog;
	private CheckBox cb_offline;
	
	public boolean isCloseable() { return false; }

	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		super.onCreateProcess(savedInstanceState);
		
		setContentView(R.layout.template_footer);
		RelativeLayout rl_container = (RelativeLayout) findViewById(R.id.rl_container);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.activity_login, rl_container, true);
		
		RelativeLayout login_container = (RelativeLayout) findViewById(R.id.login_container);
		inflater.inflate(R.layout.dialog_login, login_container, true);
		
		cb_offline = (CheckBox) findViewById(R.id.cb_offline);
		
//		setValue(R.id.login_username, "estrada-f");
//		setValue(R.id.login_password, "1234");
		
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

//		AppSettingsImpl sets = (AppSettingsImpl) Platform.getApplication().getAppSettings();
//		boolean flag = false;
//		if ("true".equals(sets.getDebugEnabled())) {
//			 flag = true;
//		}
//		Platform.setDebug(flag);
		//Platform.setDebug((Boolean) sets.get("debug_enabled")); 
//	     File dir = Environment.getExternalStorageDirectory();
//	     File file = new File(dir, "clfclog.txt");
//	     new UIDialog().showMessage("clfclog.txt file is exist -> "+file.exists());
//		setValue(R.id.login_username, "quesa");
//		setValue(R.id.login_password, "quesaamor");
		requestFocus(R.id.login_username);		
		new UIAction(this, R.id.btn_login) {
			protected void onClick() {
				try {
					doLogin();
				} catch(Throwable t) {
					UIDialog.showMessage("[ERROR] " + t.getMessage()); 
				} 
//				finally {
//					if (progressDialog.isShowing()) progressDialog.dismiss();
//				}
			}
		};
	}
	
	private void doLogin() throws Exception {
		String username = getValueAsString(R.id.login_username);
		String password = getValueAsString(R.id.login_password);
		if (isEmpty(username)) {
			requestFocus(R.id.login_username);
			ApplicationUtil.showShortMsg("Username is required");
			
		} else if (isEmpty(password)) {
			requestFocus(R.id.login_password);
			ApplicationUtil.showShortMsg("Password is required");
			
		} else {
			progressDialog.setMessage("Logging in...");
			if (!progressDialog.isShowing()) progressDialog.show();

			Boolean isOffline = cb_offline.isChecked();
			
			
			Runnable runnable = new OnlineLogin(username, password);
			if (isOffline == true) {
				runnable = new OfflineLogin(username, password);
//				offlineLogin(username, password);
				
			} 
//			else if (isOffline == false) {
//				Platform.runAsync(new OnlineLogin(username, password));
//				
//			} 
			
			Platform.runAsync(runnable);
		}		
	}
	
//	private void offlineLogin(String username, String password) {
//		UIApplication app = Platform.getApplication();
//		
//		Map settings = app.getAppSettings().getAll();
//		 
//		String encprof = "";
//		if (settings.containsKey("user_profile")) {
//			encprof = settings.get("user_profile").toString();
//		}
//				
//		if (encprof == null || encprof.equals("")) {
//			throw new RuntimeException("Offline account cannot be found!");
//		}
//		
////		println("offline " + encprof);s
////		Map result = (Map) new Base64Cipher().decode( encprof );
//
//		Map param = (Map) new Base64Cipher().decode( encprof );
//
//		Map result = (Map) param.get("data");
//		
//		String encpwd = Encoder.MD5.encode(password, username);
//		
////		println("env " + result.get("env")); 
//		
//        SessionProviderImpl sessImpl = new SessionProviderImpl(result);
//        SessionContext sess = AppContext.getSession();
//        sess.setProvider(sessImpl); 
//        sess.set("encpwd", encpwd);
//        
//        UserProfile prof = SessionContext.getProfile();
//		if (prof == null) {
//			throw new RuntimeException("Offline account cannot be found!");
//		}
//
//		if (!prof.getUserName().toLowerCase().equals(username)) {
//			throw new RuntimeException("Offline account cannot be found!");
//		}
//		
//		String p = "";
//		if (settings.containsKey("user_password")) {
//			p = settings.get("user_password").toString();
//		}
//
//		if (!p.equals(encpwd)) {
//			throw new RuntimeException("Access denied. Please check username and password.");
//		}
////		
////		Map source = new HashMap();
////		Map env = new HashMap();
////		
////		env.put("USERID", prof.getUserId());
////		env.put("USER", prof.getUserName());
////		env.put("FULLNAME", prof.getFullName());
////		env.put("NAME", prof.getName());
////		env.put("JOBTITLE", prof.getJobTitle());
////		env.put("ROLES", prof.getRoles());
////		
////		source.put("env", env);
//		
////		SessionProviderImpl sessImpl = new SessionProviderImpl(source);
////		SessionContext sess = AppContext.getSession();
////		sess.setProvider(sessImpl);
////		sess.set("encpwd", encpwd);
//
//		if (progressDialog.isShowing()) progressDialog.dismiss();
//		
//		app.restartSuspendTimer();
//		app.resumeAppLoader();
////		String u = prof.get
//		
//	} 
	
	private Handler errorhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {	
			try { 
				Bundle data = msg.getData();			
				Object o = data.getSerializable("response"); 
				if (o instanceof Throwable) {
					Throwable t = (Throwable)o;
					ApplicationUtil.showShortMsg("[ERROR] " + t.getMessage());
				} else {
					ApplicationUtil.showShortMsg("[ERROR] " + o);	
				}
			} finally { 
				if (progressDialog.isShowing()) progressDialog.dismiss(); 
			} 
		}
	}; 
	
	private Handler successhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressDialog.isShowing()) progressDialog.dismiss();

			Bundle data = msg.getData();			
			Map map = (Map) data.getSerializable("response");
			
//			UserProfile prof = SessionContext.getProfile();
//			map.put("user_profile", prof);
			
			
			UIApplication app = Platform.getApplication();
			app.getAppSettings().putAll(map);
			app.restartSuspendTimer();
			app.resumeAppLoader();
		}
	};
	
	private void println(String str) {
		Log.i("LoginActivity", str);
	}
	
	private class OnlineLogin implements Runnable {
		
		private String username;
		private String password;
		
		public OnlineLogin(String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public void run() { 
			boolean success = false;			
			Bundle data = new Bundle();
			Message msg = errorhandler.obtainMessage();
			
			try {				
				LoginService loginSvc = new LoginService();
				loginSvc.login(username, password);
				
				Map result = loginSvc.getResult();
				
				HashMap<String, Object> map = new HashMap<String, Object>();

				String encpwd = Encoder.MD5.encode(password, username); 
				
				
//				println("result " + result);
				
				Map param = new HashMap();
				param.put("username", username);
				param.put("password", encpwd);
				param.put("data", result);
				String encprof = new Base64Cipher().encode( param );
				
//				println("online encprof " + encprof);
				
				map.put("user_profile", encprof);
				map.put("user_password", Encoder.MD5.encode(password, username));
				 
//				UserSetting userSets = SessionContext.getSettings();
//				
//				println("user profile " + userSets.get("user_profile"));
//				
//				String onlinehost 	= userSets.getOnlineHost();
//				String offlinehost 	= userSets.getOfflineHost(); 
//				int sessiontimeout 	= userSets.getSessionTimeout();
//				int uploadtimeout 	= userSets.getUploadDelay();
//				int trackertimeout 	= userSets.getTrackerDelay();
//				int port 			= userSets.getPort();
//				
//				HashMap<String,Object> map = new HashMap();
//				if (port > 0) map.put("host_port", port);				
//				if (onlinehost != null) map.put("host_online", onlinehost);
//				if (offlinehost != null) map.put("host_offline", offlinehost);
//				if (uploadtimeout > 0) map.put("timeout_upload", uploadtimeout);				
//				if (sessiontimeout > 0) map.put("timeout_session", sessiontimeout);
//				if (trackertimeout > 0) map.put("timeout_tracker", trackertimeout);
//				
//				println("map " + map);
				
				data.putSerializable("response", map);
				msg = successhandler.obtainMessage();				
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
	
	private class OfflineLogin implements Runnable {

		private String username;
		private String password;
		
		public OfflineLogin (String username, String password) {
			this.username = username;
			this.password = password;
		}
		
		public void run() { 
			boolean success = false;			
			Bundle data = new Bundle();
			Message msg = errorhandler.obtainMessage();
			
			try {				
				UIApplication app = Platform.getApplication();
				
				Map settings = app.getAppSettings().getAll();
				 
				String encprof = "";
				if (settings.containsKey("user_profile")) {
					encprof = settings.get("user_profile").toString();
				}
						
				if (encprof == null || encprof.equals("")) {
					throw new RuntimeException("Offline account cannot be found!");
				}
				
//				println("offline " + encprof);s
//				Map result = (Map) new Base64Cipher().decode( encprof );

				Map param = (Map) new Base64Cipher().decode( encprof );

				Map result = (Map) param.get("data");
				
				String encpwd = Encoder.MD5.encode(password, username);
				
//				println("env " + result.get("env")); 
				
		        SessionProviderImpl sessImpl = new SessionProviderImpl(result);
		        SessionContext sess = AppContext.getSession();
		        sess.setProvider(sessImpl); 
		        sess.set("encpwd", encpwd);
		        
		        UserProfile prof = SessionContext.getProfile();
				if (prof == null) {
					throw new RuntimeException("Offline account cannot be found!");
				}

				if (!prof.getUserName().toLowerCase().equals(username)) {
					throw new RuntimeException("Offline account cannot be found!");
				}
				
				String p = "";
				if (settings.containsKey("user_password")) {
					p = settings.get("user_password").toString();
				}

				if (!p.equals(encpwd)) {
					throw new RuntimeException("Access denied. Please check username and password.");
				}

//				param = new HashMap();
//				param.put("username", username);
//				param.put("password", encpwd);
//				param.put("data", result);
//				encprof = new Base64Cipher().encode( param );
				
//				println("online encprof " + encprof);

//				HashMap<String, Object> map = new HashMap<String, Object>();
//				map.put("user_profile", encprof);
//				map.put("user_password", Encoder.MD5.encode(password, username));
				 
				data.putSerializable("response", new HashMap<String, Object>());
				msg = successhandler.obtainMessage();				
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
