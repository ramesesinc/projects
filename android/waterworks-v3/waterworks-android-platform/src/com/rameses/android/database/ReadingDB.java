package com.rameses.android.database;

import java.util.List;
import java.util.Map;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class ReadingDB extends AbstractDBMapper {

	public String getTableName() {
		return "reading";
	}

	public Map findReadingByAcctid(String acctid) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE acctid = ? AND state = 'OPEN'";
			return ctx.find(sql, new Object[] {acctid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public List<Map> getReadingByAssigneeid(String assigneeid) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.acctname, r.* FROM " + getTableName() + " r " +
					"INNER JOIN account a ON r.acctid = a.objid " +
					"WHERE a.assignee_objid = ?";
			return ctx.getList(sql, new Object[] {assigneeid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public int countReadingByAssigneeid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT r.objid FROM " + getTableName() + " r " +
					" INNER JOIN account a ON r.acctid = a.objid " +
					" WHERE a.assignee_objid = $P{assigneeid} ";
			
			return ctx.getCount(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
}
