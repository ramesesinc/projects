package com.rameses.android.efaas;

import java.util.List;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.BldgStructure;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.ImageItem;

public class FaasBuildingAppraisalActivity extends ControlActivity{
	
	private static Button appraisal_add;
	
	public static Activity activity;
	private static Properties faas = null;
	private static Properties bldgrpu = null;
	private static String faasid;
	private static String rpuid;
	private static Properties examination;
	private static List<ImageItem> data_image;
	private static List<AppraisalListItem> data_appraisal;
	private static List<ExaminationListItem> data_examination;
	private static List<BldgStructure> data_material;
	private static int ctxMenuId;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		setContentView(R.layout.activity_faas_bldg_appraisal);
		faasid = getIntent().getExtras().getString("faasid");
		appraisal_add = (Button) findViewById(R.id.appraisal_add);
		loadAppraisalInfo();
	}
	
	public static void loadAppraisalInfo(){
		appraisal_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(activity, BuildingAppraisalActivity.class);
  			  	activity.startActivity(myIntent);
            }
        });
	}

}
