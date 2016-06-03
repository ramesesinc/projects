package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.db.FaasDB;
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
			Log.v("DOWNLOAD DATA", data.toString());
			createFaasData(data);
		}catch(Throwable t){
			error = true;
			ApplicationUtil.showShortMsg(t.getMessage());
		}
		
		if(!error){
			ApplicationUtil.showShortMsg("Download Finish");
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
		params.put("rp_cadastrallotno", rp.get("cadastrallotno") != null ? rp.get("cadastrallotno").toString() : "");
		params.put("rp_blockno", rp.get("blockno") != null ? rp.get("blockno").toString() : "");
		params.put("rp_surveyno", rp.get("surveyno") != null ? rp.get("surveyno").toString() : "");
		params.put("rp_street", rp.get("street") != null ? rp.get("street").toString() : "");
		params.put("rp_purok", rp.get("purok") != null ? rp.get("purok").toString() : "");
		params.put("rp_north", rp.get("north") != null ? rp.get("north").toString() : "");
		params.put("rp_south", rp.get("south") != null ? rp.get("south").toString() : "");
		params.put("rp_east", rp.get("east") != null ? rp.get("east").toString() : "");
		params.put("rp_west", rp.get("west") != null ? rp.get("west").toString() : "");
		
		FaasDB db = new FaasDB();
		try{
			db.create(params);
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
