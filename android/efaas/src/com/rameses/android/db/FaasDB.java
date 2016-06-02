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

}
