package com.rameses.android.efaas;

import android.os.Bundle;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;

public class BuildingAppraisalActivity extends ControlActivity{
	
	public boolean isCloseable() { return false; }
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.activity_appraisal_bldg);
		ApplicationUtil.changeTitle(this, "Building Appraisal");
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}

}
