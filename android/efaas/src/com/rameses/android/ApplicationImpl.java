package com.rameses.android;

import java.io.File;
import java.util.Properties;

import android.os.Environment;
import android.os.Handler;

import com.rameses.client.android.AbstractActivity;
import com.rameses.client.android.AppSettings;
import com.rameses.client.android.Logger;
import com.rameses.client.android.NetworkLocationProvider;
import com.rameses.client.android.Platform;
import com.rameses.client.android.SessionContext;
import com.rameses.client.android.UIApplication;

public class ApplicationImpl extends UIApplication 
{
	private MainDB maindb;
	private AppSettingsImpl appSettings;
	private int networkStatus;
	
	public File getLogFile() {
		// TODO Auto-generated method stub
		File dir = Environment.getExternalStorageDirectory();
		return new File(dir, "android-platform-log.txt");
	}
	
	protected void init() {
		super.init();
	}

	protected void onCreateProcess() {
		super.onCreateProcess();
		
		Platform.setDebug(true);
		NetworkLocationProvider.setEnabled(false);
		try {
			maindb = new MainDB(this, "main.db", 1);
			maindb.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Runnable services = new Runnable() {
			public void run() {
				new NetworkCheckerService(ApplicationImpl.this).start();
			} 
		};
		new Handler().postDelayed(services, 1);		
	}
	
	protected AppSettings createAppSettings() {
		return new AppSettingsImpl(); 
	}

	public Logger createLogger() {
		return Logger.create("android-platform-log.txt");
	} 	

	protected void beforeLoad(Properties appenv) {
		super.beforeLoad(appenv);
		appenv.put("app.context", "etracs25");
		appenv.put("app.cluster", "osiris3");
		appenv.put("app.host", ApplicationUtil.getAppHost(networkStatus));
	}

	protected void onTerminateProcess() { 
		super.onTerminateProcess(); 
		NetworkLocationProvider.setEnabled(false); 
	} 	
	
	public int getNetworkStatus() { return networkStatus; }
	void setNetworkStatus(int networkStatus) { 
		this.networkStatus = networkStatus; 
		 
		AppSettingsImpl impl = (AppSettingsImpl) getAppSettings(); 
		getAppEnv().put("app.host", impl.getAppHost(networkStatus)); 
	}

	public void suspend() {
		if (SuspendDialog.isVisible()) return;

		AbstractActivity aa = Platform.getCurrentActivity();
		if (aa == null) aa = Platform.getMainActivity();
		
		final AbstractActivity current = aa;
		current.getHandler().postAtFrontOfQueue(new Runnable() {
			@Override
			public void run() {
				String content = "User: "+ SessionContext.getProfile().getFullName()+"\n\nTo resume your session, please enter your password";
				SuspendDialog.show(content);
			}
		});
	}
	
}
