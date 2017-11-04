package com.rameses.android.database;

import java.util.List;
import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class StuboutDB extends AbstractDBMapper {

	public String getTableName() {
		return "stubout";
	}
	
	public int countStuboutByZoneid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT s.objid FROM " + getTableName() + " s " +
					"WHERE s.zoneid = $P{zoneid} ";
			
			return ctx.getCount(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public List<Map> getStuboutByZoneid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT s.* FROM " + getTableName() + " s " +
					" WHERE s.code LIKE $P{searchtext} " +
					" AND s.zoneid = $P{zoneid} " +
					" ORDER BY s.code";
			
			if (params.containsKey("start") && params.containsKey("limit")) {
				sql += " LIMIT " + Integer.parseInt(params.get("start").toString()) + ", " + Integer.parseInt(params.get("limit").toString()) + " ";
			}
//			String sql = "SELECT * FROM stubout WHERE code LIKE ?  AND zoneid = ? ORDER BY code";
			return ctx.getList(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}

}
