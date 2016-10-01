package com.rameses.android.efaas;

import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.FaasDB;
import com.rameses.android.efaas.dialog.InfoDialog;

public class FaasLandGeneralActivity extends ControlActivity {
	
	private static TextView tdno, pin, owner, address, year, classification;
	private static TextView cadastrallotno, blockno, surveyno, totalarea;
	private static TextView street, purok, north, south, west, east;
	
	public static Activity activity;
	private static Properties faas = null;
	private static String faasid;
	private static String rpuid;
	
	public boolean isCloseable() { return false; }
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		activity = this; 
		setContentView(R.layout.activity_faas_land_general);
		faasid = getIntent().getExtras().getString("faasid");
		
		tdno = (TextView) findViewById(R.id.faas_tdno);
		pin = (TextView) findViewById(R.id.faas_pin);
		owner = (TextView) findViewById(R.id.faas_owner);
		address = (TextView) findViewById(R.id.faas_address);
		year = (TextView) findViewById(R.id.faas_ry);
		classification = (TextView) findViewById(R.id.faas_classification);
		cadastrallotno = (TextView) findViewById(R.id.faas_cadastrallotno);
		blockno = (TextView) findViewById(R.id.faas_blockno);
		surveyno = (TextView) findViewById(R.id.faas_surveyno);
		street = (TextView) findViewById(R.id.faas_street);
		purok = (TextView) findViewById(R.id.faas_purok);
		north = (TextView) findViewById(R.id.faas_north);
		south = (TextView) findViewById(R.id.faas_south);
		west = (TextView) findViewById(R.id.faas_west);
		east = (TextView) findViewById(R.id.faas_east);
		
		try{
			FaasDB db = new FaasDB();
			faas = db.find(faasid);
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
		
		if(faas != null){
			rpuid = faas.getProperty("rpuid");
			
			tdno.setText(faas.getProperty("tdno"));
			pin.setText(faas.getProperty("fullpin"));
			owner.setText(faas.getProperty("owner_name"));
			address.setText(faas.getProperty("owner_address"));
			year.setText(faas.getProperty("rpu_ry"));
			classification.setText(faas.getProperty("rpu_classification_objid"));
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
			new InfoDialog(activity, "Record not found!").show();
		}
	}

}
