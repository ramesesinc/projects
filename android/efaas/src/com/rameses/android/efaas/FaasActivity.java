package com.rameses.android.efaas;

import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.FaasDB;
import com.rameses.android.efaas.dialog.AppraisalInfo;

public class FaasActivity extends ControlActivity{
	
	public boolean isCloseable() { return false; }
	
	private TextView tdno, pin, owner, address, year, classification, area;
	private TextView totalmv, totalav, cadastrallotno, blockno, surveyno;
	private TextView street, purok, north, south, west, east;
	private Button appraisal_add;
	
	private Activity activity;
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		
		setContentView(R.layout.activity_faas);
		
		String faasid = getIntent().getExtras().getString("faasid");
		Properties faas = null;
		
		try{
			FaasDB db = new FaasDB();
			faas = db.find(faasid);
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
		
		tdno = (TextView) findViewById(R.id.faas_tdno);
		pin = (TextView) findViewById(R.id.faas_pin);
		owner = (TextView) findViewById(R.id.faas_owner);
		address = (TextView) findViewById(R.id.faas_address);
		year = (TextView) findViewById(R.id.faas_ry);
		classification = (TextView) findViewById(R.id.faas_classification);
		area = (TextView) findViewById(R.id.faas_area);
		totalmv = (TextView) findViewById(R.id.faas_totalmv);
		totalav = (TextView) findViewById(R.id.faas_totalav);
		cadastrallotno = (TextView) findViewById(R.id.faas_cadastrallotno);
		blockno = (TextView) findViewById(R.id.faas_blockno);
		surveyno = (TextView) findViewById(R.id.faas_surveyno);
		street = (TextView) findViewById(R.id.faas_street);
		purok = (TextView) findViewById(R.id.faas_purok);
		north = (TextView) findViewById(R.id.faas_north);
		south = (TextView) findViewById(R.id.faas_south);
		west = (TextView) findViewById(R.id.faas_west);
		east = (TextView) findViewById(R.id.faas_east);
		
		if(faas != null){
			ApplicationUtil.changeTitle(this, faas.getProperty("owner_name"));
			
			tdno.setText(faas.getProperty("tdno"));
			pin.setText(faas.getProperty("fullpin"));
			owner.setText(faas.getProperty("owner_name"));
			address.setText(faas.getProperty("owner_address"));
			year.setText(faas.getProperty("rpu_ry"));
			classification.setText(faas.getProperty("rpu_classification_objid"));
			area.setText(faas.getProperty("rpu_totalareasqm"));
			totalmv.setText(faas.getProperty("rpu_totalmv"));
			totalav.setText(faas.getProperty("rpu_totalav"));
			cadastrallotno.setText(faas.getProperty("rp_cadastrallotno"));
			blockno.setText(faas.getProperty("rp_blockno"));
			surveyno.setText(faas.getProperty("rp_surveyno"));
			street.setText(faas.getProperty("rp_street"));
			purok.setText(faas.getProperty("rp_purok"));
			north.setText(faas.getProperty("rp_north"));
			south.setText(faas.getProperty("rp_south"));
			west.setText(faas.getProperty("rp_west"));
			east.setText(faas.getProperty("rp_east"));
		}else{
			ApplicationUtil.showShortMsg("Record not found!");
		}
		
		appraisal_add = (Button) findViewById(R.id.appraisal_add);
		appraisal_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AppraisalInfo appraisal = new AppraisalInfo(activity);
                appraisal.show();
            }
        });

	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}

}
