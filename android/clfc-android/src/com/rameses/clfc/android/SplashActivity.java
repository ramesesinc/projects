package com.rameses.clfc.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.rameses.client.android.Platform;
import com.rameses.client.android.UIActionBarMain;


public class SplashActivity extends UIActionBarMain  
{

	protected void onCreateProcess(Bundle savedInstanceState) {
//		try {
    	setContentView(R.layout.activity_splash);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    				WindowManager.LayoutParams.FLAG_FULLSCREEN);	
    	getSupportActionBar().hide();
		if (!isTaskRoot()) {
			Intent intent = getIntent();
			String action = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
				Platform.getLogger().log("This is not the main activity. Exiting.");
				disposeMe();
				return;
			}
		}
//		} catch (Throwable t) {
//			UIDialog.showMessage(t, SplashActivity.this);
//		}
    } 
    
	protected void afterRegister() { 
		
		Platform.getTaskManager().schedule(new Runnable() {

			public void run() { 
				try {
					SplashActivity.this.load();
				} catch(Throwable t) {
					t.printStackTrace();
				}
			}
			
		}, 1000);
	}

	protected void afterDestroy() {  
    }     

	private void load() {
		System.out.println("splash activity loaded");
    	ApplicationImpl app = (ApplicationImpl) Platform.getApplication();
    	app.getAppSettings();
    	app.load(); 
    }
}