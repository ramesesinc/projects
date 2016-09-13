package com.rameses.android.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class FaasDB extends AbstractDBMapper{

	@Override
	public String getTableName() {
		return "faas";
	}
	
	public List<Map> getSearchList(String searchtext) throws Exception{
		List<Map> data = new ArrayList<Map>();
		DBContext ctx = getDBContext();
		try {
			ctx.getList("SELECT * FROM faas", new HashMap());
		} catch(Exception e) {
			throw e; 
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
		return data;
	}
	
	public void deleteFaas(String faasid) throws Exception{
		Map param1 = new HashMap();
		param1.put("parent_objid", faasid);
		
		ExaminationDB examdb = new ExaminationDB();
		List<Map> examinations =  examdb.getList(param1);
		for(Map exam : examinations){
			//DELETE IMAGES
			Map param = new HashMap();
			param.put("examinationid", (String) exam.get("objid"));
			
			ImageDB imgdb = new ImageDB();
			List<Map> images = imgdb.getList(param);
			for(Map image : images){
				param = new HashMap();
				param.put("objid", param.get("objid"));
				
				imgdb = new ImageDB();
				imgdb.delete(param);
			}
		}
		
		//DELETE EXAMINATIONS
		for(Map exam : examinations){
			Map param = new HashMap();
			param.put("objid", exam.get("objid"));
			
			examdb = new ExaminationDB();
			examdb.delete(param);
		}
		
		Map param2 = new HashMap();
		param2.put("objid", faasid);
		
		Map faas = new FaasDB().find(param2);
		Map param3 = new HashMap();
		param3.put("landrpuid", faas.get("rpu_objid"));
		
		//DELETE LANDDETAILS
		LandDetailDB ldb = new LandDetailDB();
		List<Map> landdetails = ldb.getList(param3);
		for(Map landdetail : landdetails){
			Map param = new HashMap();
			param.put("objid", landdetail.get("objid"));
			
			ldb = new LandDetailDB();
			ldb.delete(param);
		}
		
		//DELETE FAAS
		FaasDB faasdb = new FaasDB();
		faasdb.delete(param2);
	}

}
