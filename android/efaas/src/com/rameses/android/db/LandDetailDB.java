package com.rameses.android.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.android.AbstractDBMapper;

public class LandDetailDB extends AbstractDBMapper{

	@Override
	public String getTableName() {
		return "landdetail";
	}
	
	public List<Map> getLandDetails(String landrpuid) throws Exception {
		List<Map> data = new ArrayList<Map>();
		Map param1 = new HashMap();
		param1.put("landrpuid", landrpuid);
		List<Map> landdetails = getList(param1);
		for(Map m : landdetails){
			System.err.println("Land Detail : " + m);
			System.err.println("******************************************************");
			Map params = new HashMap();
			params.put("objid", m.get("objid"));
			params.put("landrpuid", m.get("landrpuid"));
			params.put("subclass_objid", m.get("subclass_objid"));
			params.put("specificclass_objid", m.get("specificclass_objid"));
			params.put("specificclass_areatype", m.get("specificclass_areatype"));
			params.put("specificclass_classification_objid", m.get("specificclass_classification_objid"));
			params.put("actualuse_objid", m.get("actualuse_objid"));
			params.put("actualuse_code", m.get("actualuse_code"));
			params.put("actualuse_name", m.get("actualuse_name"));
			params.put("actualuse_classification_objid", m.get("actualuse_classification_objid"));
			params.put("stripping_objid", m.get("stripping_objid"));
			params.put("stripping_rate", Integer.parseInt(m.get("stripping_rate") != null ? m.get("stripping_rate").toString() : "0"));
			params.put("striprate", m.get("striprate") != null ? Integer.parseInt(m.get("striprate").toString().replaceAll("\\.", "")) : 0);
			params.put("areatype", m.get("areatype"));
			params.put("area", Double.parseDouble(m.get("area").toString()));
			params.put("areasqm", Double.parseDouble(m.get("areasqm").toString()));
			params.put("areaha", Double.parseDouble(m.get("areaha").toString()));
			params.put("basevalue", Double.parseDouble(m.get("basevalue").toString()));
			params.put("unitvalue", Double.parseDouble(m.get("unitvalue").toString()));
			params.put("subclass_unitvalue", params.get("unitvalue"));
			params.put("taxable", m.get("taxable"));
			params.put("basemarketvalue", Double.parseDouble(m.get("basemarketvalue").toString()));
			params.put("adjustment", Double.parseDouble(m.get("adjustment").toString()));
			params.put("landvalueadjustment", Double.parseDouble(m.get("landvalueadjustment").toString()));
			params.put("actualuseadjustment", Double.parseDouble(m.get("actualuseadjustment").toString()));
			params.put("marketvalue", Double.parseDouble(m.get("marketvalue").toString()));
			params.put("assesslevel", Double.parseDouble(m.get("assesslevel").toString()));
			params.put("assessedvalue", Double.parseDouble(m.get("assessedvalue").toString()));
			data.add(params);
		}
		return data;
	}

}
