package com.rameses.android.efaas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.ControlActivity;
import com.rameses.android.R;
import com.rameses.android.db.BldgRpuDB;
import com.rameses.android.db.FaasDB;
import com.rameses.android.efaas.bean.AppraisalListItem;
import com.rameses.android.efaas.bean.BldgStructure;
import com.rameses.android.efaas.bean.ExaminationListItem;
import com.rameses.android.efaas.bean.ImageItem;
import com.rameses.android.efaas.dialog.InfoDialog;

public class FaasBuildingGeneralActivity extends ControlActivity {
	
	private static TextView tdno, pin, owner, address;
	private static TextView bldgage, effectiveage, depreciationpercentage, depreciationvalue;
	private static TextView floorcount, percentcompleted, datecompleted, dateoccupied;
	private static TextView permitno, issuancedate;
	
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
		setContentView(R.layout.activity_faas_bldg_general);
		faasid = getIntent().getExtras().getString("faasid");
	    
		tdno = (TextView) findViewById(R.id.faas_tdno);
		pin = (TextView) findViewById(R.id.faas_pin);
		owner = (TextView) findViewById(R.id.faas_owner);
		address = (TextView) findViewById(R.id.faas_address);
		bldgage = (TextView) findViewById(R.id.faas_buildingage);
		effectiveage = (TextView) findViewById(R.id.faas_effectivegage);
		depreciationpercentage = (TextView) findViewById(R.id.faas_depreciationpercentage);
		depreciationvalue = (TextView) findViewById(R.id.faas_depreciationvalue);
		floorcount = (TextView) findViewById(R.id.faas_floorcount);
		percentcompleted = (TextView) findViewById(R.id.faas_percentcompleted);
		datecompleted = (TextView) findViewById(R.id.faas_datecompleted);
		dateoccupied = (TextView) findViewById(R.id.faas_dateoccupied);
		permitno = (TextView) findViewById(R.id.faas_permitno);
		issuancedate = (TextView) findViewById(R.id.faas_permitissuedate);
		
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
		}else{
			new InfoDialog(activity, "Record not found!").show();
		}
		
		try{
			Map params = new HashMap();
			params.put("objid", rpuid);
			
			BldgRpuDB db = new BldgRpuDB();
			bldgrpu = db.find(params);
		}catch(Exception e){
			ApplicationUtil.showShortMsg(e.toString());
		}
		
		if(bldgrpu != null){
			bldgage.setText(bldgrpu.getProperty("bldgage"));
			effectiveage.setText(bldgrpu.getProperty("effectiveage"));
			depreciationpercentage.setText(bldgrpu.getProperty("depreciation"));
			depreciationvalue.setText(bldgrpu.getProperty("depreciationvalue"));
			floorcount.setText(bldgrpu.getProperty("floorcount"));
			percentcompleted.setText(bldgrpu.getProperty("percentcompleted"));
			datecompleted.setText(bldgrpu.getProperty("dtcompleted"));
			dateoccupied.setText(bldgrpu.getProperty("dtoccupied"));
			permitno.setText(bldgrpu.getProperty("permitno"));
			issuancedate.setText(bldgrpu.getProperty("permitdate"));
		}
	}

}
