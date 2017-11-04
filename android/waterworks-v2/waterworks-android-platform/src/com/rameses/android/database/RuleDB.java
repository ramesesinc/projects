package com.rameses.android.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class RuleDB extends AbstractDBMapper {

	public String getTableName() {
		return "rule";
	}
	
	public List<Map> getRules() throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " ORDER BY salience DESC";
			return ctx.getList(sql, new HashMap());
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		} 
	}

}
