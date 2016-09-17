package com.rameses.android.efaas;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class TabFaasBuildingActivity extends TabActivity {
	
	Activity activity;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        activity = this;
        
        ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3485c7")));
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        
        String faasid = getIntent().getExtras().getString("faasid");
         
        TabHost tabHost = getTabHost();
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){
        	@Override
        	public void onTabChanged(String tabId) {
        	    ApplicationUtil.changeTitle(activity,tabId + " Information");
        	}
        });
         
        TabSpec general = tabHost.newTabSpec("General");
        general.setIndicator("General");
        Intent generalIntent = new Intent(this, FaasBuildingGeneralActivity.class);
        generalIntent.putExtra("faasid", faasid);
        general.setContent(generalIntent);
        
        TabSpec general1 = tabHost.newTabSpec("Material");
        general1.setIndicator("Material");
        Intent generalIntent1 = new Intent(this, FaasBuildingMaterialActivity.class);
        generalIntent1.putExtra("faasid", faasid);
        general1.setContent(generalIntent1);
        
        TabSpec general2 = tabHost.newTabSpec("Appraisal");
        general2.setIndicator("Appraisal");
        Intent generalIntent2 = new Intent(this, FaasBuildingAppraisalActivity.class);
        generalIntent2.putExtra("faasid", faasid);
        general2.setContent(generalIntent2);
        
        TabSpec general3 = tabHost.newTabSpec("Examination");
        general3.setIndicator("Examination");
        Intent generalIntent3 = new Intent(this, FaasLandExaminationActivity.class);
        generalIntent3.putExtra("faasid", faasid);
        general3.setContent(generalIntent3);

        tabHost.addTab(general);
        tabHost.addTab(general1);
        tabHost.addTab(general2);
        tabHost.addTab(general3);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.finish();
	    return true; 
	}
	
}
