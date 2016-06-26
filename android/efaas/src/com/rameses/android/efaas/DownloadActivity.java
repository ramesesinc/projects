package com.rameses.android.efaas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.db.FaasDB;
import com.rameses.android.db.LandDetailDB;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.service.DownloadService;

public class DownloadActivity  extends SettingsMenuActivity{
	
	private EditText tdno;
	private Button download;
	private List<String> errorList;
	
	public boolean isCloseable() { return false; }
	
	@Override
	protected void onCreateProcess(Bundle savedInstanceState) {
		setContentView(R.layout.activity_download);
		
		ApplicationUtil.changeTitle(this, "Download");
		
		tdno = (EditText) findViewById(R.id.download_text);
		
		download = (Button) findViewById(R.id.download_button);
		download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	doDownload();
            }
        });
	}
	
	protected void afterBackPressed() {
		disposeMe(); 
	} 
	
	protected void onStartProcess() {
		super.onStartProcess();
	}
	
	private void doDownload(){
		String tdno_text = tdno.getText().toString();
		if(tdno_text.trim().length() <= 0){
			ApplicationUtil.showShortMsg("TD No. is required!");
			return;
		}
		boolean error = false;
		DownloadService svc = new DownloadService();
		try{
			Map data = svc.getFaas(tdno_text);
			createFaasData(data);
		}catch(Throwable t){
			error = true;
			new ErrorDialog(this, t).show();
		}
		
		if(!error){
			new InfoDialog(this,"Download Finish!").show();
			download.setText("New");
			download.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	tdno.getText().clear();
	            	download.setText("Download");
	            	download.setOnClickListener(new View.OnClickListener() {
	                    public void onClick(View v) {
	                    	doDownload();
	                    }
	                });
	            }
	        });
		}
	}
	
	private int createFaasData(Map data){
		boolean error = false;
		if(data == null) return 0;
		Map owner = (Map) data.get("owner");
		Map rpu = (Map) data.get("rpu");
		List<Map> landdetails = (List<Map>) data.get("landdetails");
		Map classification = (Map) rpu.get("classification");
		Map rp = (Map) data.get("rp");
		
		Map params = new HashMap();
		params.put("objid", data.get("objid") != null ? data.get("objid").toString() : "");
		params.put("state", data.get("state") != null ? data.get("state").toString() : "");
		params.put("rpuid", data.get("rpuid") != null ? data.get("rpuid").toString() : "");
		params.put("realpropertyid", data.get("realpropertyid") != null ? data.get("realpropertyid").toString() : "");
		params.put("owner_name", owner.get("name") != null ? owner.get("name").toString() : "");
		params.put("owner_address", owner.get("address") != null ? owner.get("address").toString() : "");
		params.put("tdno", data.get("tdno") != null ? data.get("tdno").toString() : "");
		params.put("fullpin", data.get("fullpin") != null ? data.get("fullpin").toString() : "");
		params.put("rpu_objid", rpu.get("objid") != null ? rpu.get("objid").toString() : "");
		params.put("rpu_ry", rpu.get("ry") != null ? rpu.get("ry").toString() : "");
		params.put("rpu_suffix", rpu.get("suffix") != null ? rpu.get("suffix").toString() : "");
		params.put("rpu_subsuffix", rpu.get("subsuffix") != null ? rpu.get("subsuffix").toString() : "");
		params.put("rpu_classification_objid", classification.get("objid") != null ? classification.get("objid").toString() : "");
		params.put("rpu_taxable", rpu.get("taxable") != null ? rpu.get("taxable").toString() : "");
		params.put("rpu_totalareaha", rpu.get("totalareaha") != null ? rpu.get("totalareaha").toString() : "");
		params.put("rpu_totalareasqm", rpu.get("totalareasqm") != null ? rpu.get("totalareasqm").toString() : "");
		params.put("rpu_totalbmv", rpu.get("totalbmv") != null ? rpu.get("totalbmv").toString() : "");
		params.put("rpu_totalmv", rpu.get("totalmv") != null ? rpu.get("totalmv").toString() : "");
		params.put("rpu_totalav", rpu.get("totalav") != null ? rpu.get("totalav").toString() : "");
		params.put("rp_objid", rp.get("objid") != null ? rp.get("objid").toString() : "");
		params.put("rp_cadastrallotno", rp.get("cadastrallotno") != null ? rp.get("cadastrallotno").toString() : "");
		params.put("rp_blockno", rp.get("blockno") != null ? rp.get("blockno").toString() : "");
		params.put("rp_surveyno", rp.get("surveyno") != null ? rp.get("surveyno").toString() : "");
		params.put("rp_street", rp.get("street") != null ? rp.get("street").toString() : "");
		params.put("rp_purok", rp.get("purok") != null ? rp.get("purok").toString() : "");
		params.put("rp_north", rp.get("north") != null ? rp.get("north").toString() : "");
		params.put("rp_south", rp.get("south") != null ? rp.get("south").toString() : "");
		params.put("rp_east", rp.get("east") != null ? rp.get("east").toString() : "");
		params.put("rp_west", rp.get("west") != null ? rp.get("west").toString() : "");
		
		try{
			FaasDB db = new FaasDB();
			db.create(params);
			
			LandDetailDB ldb = new LandDetailDB();
			if(landdetails != null){
				for(Map m : landdetails){
					Map subclass = (Map) m.get("subclass");
					Map specificclass = (Map) m.get("specificclass");
					Map actualuse = (Map) m.get("actualuse");
					Map stripping = (Map) m.get("stripping");
					Map actualuse_classification = (Map) actualuse.get("classification");
					Map specificclass_classification = (Map) specificclass.get("classification");
					
					Map param1 = new HashMap();
					param1.put("objid", m.get("objid") != null ? m.get("objid").toString() : "");
					param1.put("landrpuid", m.get("landrpuid") != null ? m.get("landrpuid").toString() : "");
					param1.put("subclass_objid", subclass != null ? subclass.get("objid") : "");
					param1.put("specificclass_objid", specificclass != null ? specificclass.get("objid") : "");
					param1.put("specificclass_classification_objid", specificclass_classification != null ? specificclass_classification.get("objid") : null);
					param1.put("actualuse_objid", actualuse != null ? actualuse.get("objid") : "");
					param1.put("actualuse_code", actualuse != null ? actualuse.get("code") : "");
					param1.put("actualuse_name", actualuse != null ? actualuse.get("name") : "");
					param1.put("actualuse_classification_objid", actualuse_classification != null ? actualuse_classification.get("objid") : null);
					param1.put("stripping_objid", stripping != null ? stripping.get("objid") : "");
					param1.put("striprate", m.get("striprate") != null ? m.get("striprate").toString() : "0");
					param1.put("areatype", m.get("areatype") != null ? m.get("areatype").toString() : "");
					param1.put("area", m.get("area") != null ? m.get("area").toString() : "0.00");
					param1.put("areasqm", m.get("areasqm") != null ? m.get("areasqm").toString() : "0.00");
					param1.put("areaha", m.get("areaha") != null ? m.get("areaha").toString() : "0.00");
					param1.put("basevalue", m.get("basevalue") != null ? m.get("basevalue").toString() : "0.00");
					param1.put("unitvalue", m.get("unitvalue") != null ? m.get("unitvalue").toString() : "0.00");
					param1.put("taxable", m.get("taxable") != null ? m.get("taxable").toString() : "");
					param1.put("basemarketvalue", m.get("basemarketvalue") != null ? m.get("basemarketvalue").toString() : "0.00");
					param1.put("adjustment", m.get("adjustment") != null ? m.get("adjustment").toString() : "0.00");
					param1.put("landvalueadjustment", m.get("landvalueadjustment") != null ? m.get("landvalueadjustment").toString() : "0.00");
					param1.put("actualuseadjustment", m.get("actualuseadjustment") != null ? m.get("actualuseadjustment").toString() : "0.00");
					param1.put("marketvalue", m.get("marketvalue") != null ? m.get("marketvalue").toString() : "0.00");
					param1.put("assesslevel", m.get("assesslevel") != null ? m.get("assesslevel").toString() : "0.00");
					param1.put("assessedvalue", m.get("assessedvalue") != null ? m.get("assessedvalue").toString() : "0.00");
					
					ldb.create(param1);
				}
			}
		}catch(Throwable t){
			error = true;
			errorList.add(t.getMessage());
		}
		if(error){
			return 1;
		}else{
			return 0;
		}
	}

}
