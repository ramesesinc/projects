package com.rameses.android.efaas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rameses.android.ApplicationUtil;
import com.rameses.android.R;
import com.rameses.android.SettingsMenuActivity;
import com.rameses.android.db.BldgFloorAdditionalDB;
import com.rameses.android.db.BldgFloorAdditionalParamDB;
import com.rameses.android.db.BldgRpuDB;
import com.rameses.android.db.BldgStructureDB;
import com.rameses.android.db.BldgUseDB;
import com.rameses.android.db.FaasDB;
import com.rameses.android.db.LandDetailDB;
import com.rameses.android.db.StructuralTypeDB;
import com.rameses.android.db.StructureDB;
import com.rameses.android.efaas.dialog.ErrorDialog;
import com.rameses.android.efaas.dialog.InfoDialog;
import com.rameses.android.service.DownloadService;

public class DownloadActivity  extends SettingsMenuActivity{
	
	private EditText tdno;
	private Button download;
	private List<String> errorList;
	private boolean error = false;
	
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
		
		errorList = new ArrayList<String>();
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
		error = false;
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
		error = false;
		if(data == null) return 0;
		//check if the record already exists
		Properties faas = null;
		try{
			Map param = new HashMap();
			param.put("objid",  data.get("objid"));
			
			FaasDB faasdb = new FaasDB();
			faas = faasdb.find(param);
		}catch(Exception e){
			new ErrorDialog(this, e).show();
		}
		if(faas != null){
			new InfoDialog(this,"FAAS is already downloaded!").show();
			error = true;
			errorList.add("FAAS is already downloaded!");
			return 0;
		}
		
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
		params.put("rpu_objid", rpu.get("objid") != null ? rpu.get("objid").toString() : "");
		params.put("rpu_type", rpu.get("type") != null ? rpu.get("type").toString() : "");
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
		
		List<Map> landdetails = (List<Map>) data.get("landdetails");
		Map bldgrpu = (Map) data.get("bldgrpu");
		List<Map> bldgstructures = (List<Map>) data.get("bldgstructures");
		List<Map> bldgrpustructuraltypes = (List<Map>) data.get("bldgrpustructuraltypes");
		List<Map> bldguses = (List<Map>) data.get("bldguses");
		List<Map> bldgflooradditionals = (List<Map>) data.get("bldgflooradditionals");
		List<Map> bldgflooradditionalparams = (List<Map>) data.get("bldgflooradditionalparams");
		
		try{
			FaasDB db = new FaasDB();
			db.create(params);
			
			if(landdetails != null){
				for(Map m : landdetails){
					Map subclass = (Map) m.get("subclass");
					Map specificclass = (Map) m.get("specificclass");
					Map actualuse = (Map) m.get("actualuse");
					Map stripping = (Map) m.get("stripping");
					Map actualuse_classification = (Map) actualuse.get("classification");
					Map specificclass_classification = (Map) specificclass.get("classification");
					
					Map param = new HashMap();
					param.put("objid", m.get("objid") != null ? m.get("objid").toString() : null);
					param.put("landrpuid", m.get("landrpuid") != null ? m.get("landrpuid").toString() : null);
					param.put("subclass_objid", subclass != null ? subclass.get("objid") : "");
					param.put("specificclass_objid", specificclass != null ? specificclass.get("objid") : null);
					param.put("specificclass_areatype", specificclass != null ? specificclass.get("areatype") : null);
					param.put("specificclass_classification_objid", specificclass_classification != null ? specificclass_classification.get("objid") : null);
					param.put("actualuse_objid", actualuse != null ? actualuse.get("objid") : null);
					param.put("actualuse_code", actualuse != null ? actualuse.get("code") : "");
					param.put("actualuse_name", actualuse != null ? actualuse.get("name") : "");
					param.put("actualuse_fixrate", actualuse != null ? actualuse.get("fixrate") : 0);
					param.put("actualuse_classification_objid", actualuse_classification != null ? actualuse_classification.get("objid") : null);
					param.put("stripping_objid", stripping != null ? stripping.get("objid") : null);
					param.put("stripping_rate", stripping != null ? stripping.get("rate") : 0);
					param.put("striprate", m.get("striprate") != null ? m.get("striprate").toString() : "0");
					param.put("areatype", m.get("areatype") != null ? m.get("areatype").toString() : "");
					param.put("area", m.get("area") != null ? m.get("area").toString() : "0.00");
					param.put("areasqm", m.get("areasqm") != null ? m.get("areasqm").toString() : "0.00");
					param.put("areaha", m.get("areaha") != null ? m.get("areaha").toString() : "0.00");
					param.put("basevalue", m.get("basevalue") != null ? m.get("basevalue").toString() : "0.00");
					param.put("unitvalue", m.get("unitvalue") != null ? m.get("unitvalue").toString() : "0.00");
					param.put("taxable", m.get("taxable") != null ? m.get("taxable").toString() : "");
					param.put("basemarketvalue", m.get("basemarketvalue") != null ? m.get("basemarketvalue").toString() : "0.00");
					param.put("adjustment", m.get("adjustment") != null ? m.get("adjustment").toString() : "0.00");
					param.put("landvalueadjustment", m.get("landvalueadjustment") != null ? m.get("landvalueadjustment").toString() : "0.00");
					param.put("actualuseadjustment", m.get("actualuseadjustment") != null ? m.get("actualuseadjustment").toString() : "0.00");
					param.put("marketvalue", m.get("marketvalue") != null ? m.get("marketvalue").toString() : "0.00");
					param.put("assesslevel", m.get("assesslevel") != null ? m.get("assesslevel").toString() : "0.00");
					param.put("assessedvalue", m.get("assessedvalue") != null ? m.get("assessedvalue").toString() : "0.00");
					
					LandDetailDB ldb = new LandDetailDB();
					ldb.create(param);
				}
			}
			
			if(bldgrpu != null){
				Map bldgtype = (Map) bldgrpu.get("bldgtype");
				Map bldgkindbucc = (Map) bldgrpu.get("bldgkindbucc");
				Map bldgassesslevel = (Map) bldgrpu.get("bldgassesslevel");
				
				Map param = new HashMap();
				param.put("objid", bldgrpu.get("objid") != null ? bldgrpu.get("objid").toString() : null);
				param.put("landrpuid", bldgrpu.get("landrpuid") != null ? bldgrpu.get("landrpuid").toString() : null);
				param.put("houseno", bldgrpu.get("houseno") != null ? bldgrpu.get("houseno").toString() : null);
				param.put("psic", bldgrpu.get("psic") != null ? bldgrpu.get("psic").toString() : null);
				param.put("permitno", bldgrpu.get("permitno") != null ? bldgrpu.get("permitno").toString() : null);
				param.put("permitdate", bldgrpu.get("permitdate") != null ? bldgrpu.get("permitdate").toString() : null);
				param.put("permitissuedby", bldgrpu.get("permitissuedby") != null ? bldgrpu.get("permitissuedby").toString() : null);
				param.put("bldgtype_objid", bldgtype.get("objid") != null ? bldgtype.get("objid").toString() : null);
				param.put("bldgkindbucc_objid", bldgkindbucc.get("objid") != null ? bldgkindbucc.get("objid").toString() : null);
				param.put("basevalue", bldgrpu.get("basevalue") != null ? bldgrpu.get("basevalue").toString() : null);
				param.put("dtcompleted", bldgrpu.get("dtcompleted") != null ? bldgrpu.get("dtcompleted").toString() : null);
				param.put("dtoccupied", bldgrpu.get("dtoccupied") != null ? bldgrpu.get("dtoccupied").toString() : null);
				param.put("floorcount", bldgrpu.get("floorcount") != null ? bldgrpu.get("floorcount").toString() : null);
				param.put("depreciation", bldgrpu.get("depreciation") != null ? bldgrpu.get("depreciation").toString() : null);
				param.put("depreciationvalue", bldgrpu.get("depreciationvalue") != null ? bldgrpu.get("depreciationvalue").toString() : null);
				param.put("totaladjustment", bldgrpu.get("totaladjustment") != null ? bldgrpu.get("totaladjustment").toString() : null);
				param.put("additionalinfo", bldgrpu.get("additionalinfo") != null ? bldgrpu.get("additionalinfo").toString() : null);
				param.put("bldgage", bldgrpu.get("bldgage") != null ? bldgrpu.get("bldgage").toString() : null);
				param.put("percentcompleted", bldgrpu.get("percentcompleted") != null ? bldgrpu.get("percentcompleted").toString() : null);
				param.put("bldgassesslevel_objid", bldgassesslevel.get("objid") != null ? bldgassesslevel.get("objid").toString() : null);
				param.put("assesslevel", bldgrpu.get("assesslevel") != null ? bldgrpu.get("assesslevel").toString() : null);
				param.put("condominium", bldgrpu.get("condominium") != null ? bldgrpu.get("condominium").toString() : null);
				param.put("bldgclass", bldgrpu.get("bldgclass") != null ? bldgrpu.get("bldgclass").toString() : null);
				param.put("predominant", bldgrpu.get("predominant") != null ? bldgrpu.get("predominant").toString() : null);
				param.put("effectiveage", bldgrpu.get("effectiveage") != null ? bldgrpu.get("effectiveage").toString() : null);
				param.put("condocerttitle", bldgrpu.get("condocerttitle") != null ? bldgrpu.get("condocerttitle").toString() : null);
				param.put("dtcertcompletion", bldgrpu.get("dtcertcompletion") != null ? bldgrpu.get("dtcertcompletion").toString() : null);
				param.put("dtcertoccupancy", bldgrpu.get("dtcertoccupancy") != null ? bldgrpu.get("dtcertoccupancy").toString() : null);
				
				BldgRpuDB bldgrpudb = new BldgRpuDB();
				bldgrpudb.create(param);	
			}
			
			if(bldgstructures != null){
				for(Map m : bldgstructures){
					Map structure = (Map) m.get("structure");
					Map material = (Map) m.get("material");
					
					Map param = new HashMap();
					param.put("objid", m.get("objid") != null ? m.get("objid").toString() : null);
					param.put("bldgrpuid", m.get("bldgrpuid") != null ? m.get("bldgrpuid").toString() : null);
					param.put("structure_objid", structure.get("objid") != null ? structure.get("objid").toString() : null);
					param.put("material_objid", material.get("objid") != null ? material.get("objid").toString() : null);
					param.put("floor", m.get("floor") != null ? m.get("floor").toString() : null);
					
					BldgStructureDB structuredb = new BldgStructureDB();
					structuredb.create(param);
				}
			}
			
			if(bldgrpustructuraltypes != null){
				for(Map m : bldgrpustructuraltypes){
					Map bldgtype = (Map) m.get("bldgtype");
					Map bldgkindbucc = (Map) m.get("bldgkindbucc");
					Map classification1 = (Map) m.get("classification");
					
					Map param = new HashMap();
					param.put("objid", m.get("objid") != null ? m.get("objid").toString() : null);
					param.put("bldgrpuid", m.get("bldgrpuid") != null ? m.get("bldgrpuid").toString() : null);
					param.put("bldgtype_objid", bldgtype.get("objid") != null ? bldgtype.get("objid").toString() : null);
					param.put("bldgkindbucc_objid", bldgkindbucc.get("objid") != null ? bldgkindbucc.get("objid").toString() : null);
					param.put("floorcount", m.get("floorcount") != null ? m.get("floorcount").toString() : null);
					param.put("basefloorarea", m.get("basefloorarea") != null ? m.get("basefloorarea").toString() : null);
					param.put("totalfloorarea", m.get("totalfloorarea") != null ? m.get("totalfloorarea").toString() : null);
					param.put("basevalue", m.get("basevalue") != null ? m.get("basevalue").toString() : null);
					param.put("unitvalue", m.get("unitvalue") != null ? m.get("unitvalue").toString() : null);
					param.put("classification_objid", classification1.get("objid") != null ? classification1.get("objid").toString() : null);
					
					StructuralTypeDB structuraltypedb = new StructuralTypeDB();
					structuraltypedb.create(param);
				}
			}
			
			if(bldguses != null){
				for(Map m : bldguses){
					Map structuraltype = (Map) m.get("structuraltype");
					Map actualuse = (Map) m.get("actualuse");
					
					Map param = new HashMap();
					param.put("objid", m.get("objid") != null ? m.get("objid").toString() : null);
					param.put("bldgrpuid", m.get("bldgrpuid") != null ? m.get("bldgrpuid").toString() : null);
					param.put("structuraltype_objid", structuraltype.get("objid") != null ? structuraltype.get("objid").toString() : null);
					param.put("actualuse_objid", actualuse.get("objid") != null ? actualuse.get("objid").toString() : null);
					param.put("basevalue", m.get("basevalue") != null ? m.get("basevalue").toString() : null);
					param.put("area", m.get("area") != null ? m.get("area").toString() : null);
					param.put("basemarketvalue", m.get("basemarketvalue") != null ? m.get("basemarketvalue").toString() : null);
					param.put("depreciationvalue", m.get("depreciationvalue") != null ? m.get("depreciationvalue").toString() : null);
					param.put("adjustment", m.get("adjustment") != null ? m.get("adjustment").toString() : null);
					param.put("marketvalue", m.get("marketvalue") != null ? m.get("marketvalue").toString() : null);
					param.put("assesslevel", m.get("assesslevel") != null ? m.get("assesslevel").toString() : null);
					param.put("assessedvalue", m.get("assessedvalue") != null ? m.get("assessedvalue").toString() : null);
					param.put("addlinfo", m.get("addlinfo") != null ? m.get("addlinfo").toString() : null);
					
					BldgUseDB actualusedb = new BldgUseDB();
					actualusedb.create(param);
				}
			}
			
			if(bldgflooradditionals != null){
				for(Map m : bldgflooradditionals){
					Map additionalitem = (Map) m.get("additionalitem");
					
					Map param = new HashMap();
					param.put("objid", m.get("objid") != null ? m.get("objid").toString() : null);
					param.put("bldgfloorid", m.get("bldgfloorid") != null ? m.get("bldgfloorid").toString() : null);
					param.put("bldgrpuid", m.get("bldgrpuid") != null ? m.get("bldgrpuid").toString() : null);
					param.put("additionalitem_objid", additionalitem.get("objid") != null ? additionalitem.get("objid").toString() : null);
					param.put("amount", m.get("amount") != null ? m.get("amount").toString() : null);
					param.put("expr", m.get("expr") != null ? m.get("expr").toString() : null);
					
					BldgFloorAdditionalDB bldgflooradditionaldb = new BldgFloorAdditionalDB();
					bldgflooradditionaldb.create(param);
				}
			}
			
			if(bldgflooradditionalparams != null){
				for(Map m : bldgflooradditionalparams){
					Map prm = (Map) m.get("param");
					
					Map param = new HashMap();
					param.put("objid", m.get("objid") != null ? m.get("objid").toString() : null);
					param.put("bldgflooradditionalid", m.get("bldgflooradditionalid") != null ? m.get("bldgflooradditionalid").toString() : null);
					param.put("bldgrpuid", m.get("bldgrpuid") != null ? m.get("bldgrpuid").toString() : null);
					param.put("param_objid", prm.get("objid") != null ? prm.get("objid").toString() : null);
					param.put("intvalue", m.get("intvalue") != null ? m.get("intvalue").toString() : null);
					param.put("decimalvalue", m.get("decimalvalue") != null ? m.get("decimalvalue").toString() : null);
					
					BldgFloorAdditionalParamDB paramdb = new BldgFloorAdditionalParamDB();
					paramdb.create(param);
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
