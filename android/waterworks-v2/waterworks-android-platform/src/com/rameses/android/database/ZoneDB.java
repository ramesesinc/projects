package com.rameses.android.database;

import java.util.List;
import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class ZoneDB extends AbstractDBMapper {

	public String getTableName() {
		return "zone";
	}
	
	public Map findByObjid(String objid) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE objid = ?";
			return ctx.find(sql, new Object[] {objid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public int countZoneByAssigneed(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT z.objid FROM " + getTableName() + " z " +
					" INNER JOIN sector_reader r ON z.readerid = r.objid " +
					" WHERE r.assigneeid = $P{assigneeid} ";
			
			return ctx.getCount(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}

	public List<Map> getZone(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT z.* FROM " + getTableName() + " z " +
					" INNER JOIN sector_reader r ON z.readerid = r.objid " +
					" WHERE z.code LIKE $P{searchtext} " +
					" AND r.assigneeid = $P{assigneeid} " +
					" GROUP BY z.code";
			
			if (params.containsKey("start") && params.containsKey("limit")) {
				sql += " LIMIT " + Integer.parseInt(params.get("start").toString()) + ", " + Integer.parseInt(params.get("limit").toString()) + " ";
			}
			return ctx.getList(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
}
