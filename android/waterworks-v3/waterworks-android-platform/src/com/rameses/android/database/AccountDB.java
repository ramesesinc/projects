package com.rameses.android.database;

import java.util.List;
import java.util.Map;

import android.util.Log;

import com.rameses.android.AbstractDBMapper;
import com.rameses.db.android.DBContext;

public class AccountDB extends AbstractDBMapper {

	public String getTableName() {
		return "account";
	}

	public Map findByObjid(String objid) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.*, s.code AS stuboutcode " +
					" FROM " + getTableName() + " a " +
					" INNER JOIN stubout s ON a.stuboutid = s.objid " +
					" WHERE a.objid = ?";
			return ctx.find(sql, new Object[] {objid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public int getNoOfTotalReadRecordsByAssigneeid(String assigneeid) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.objid FROM " + getTableName() + " a " +
					"INNER JOIN reading r ON a.objid = r.acctid " +
					"WHERE a.assignee_objid = ?";
			return ctx.getCount(sql, new Object[] {assigneeid});		
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public int countAccountByAssigneeid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.objid FROM " + getTableName() + " a " +
					"WHERE a.assignee_objid = $P{assigneeid}";
			
			return ctx.getCount(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public List<Map> getAccountByAssigneeid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.*, r.objid AS readingid " +
					" FROM (" +
					"	SELECT objid FROM " + getTableName() +
					"   WHERE acctname LIKE $P{searchtext} " +
					"   AND assignee_objid = $P{assigneeid} " +
					"   UNION " +
					"   SELECT objid FROM " + getTableName() +
					"   WHERE acctno LIKE $P{searchtext} " +
					"   AND assignee_objid = $P{assigneeid} " +
					"   UNION " +
					"   SELECT objid FROM " + getTableName() +
					"   WHERE serialno LIKE $P{searchtext}" +
					"   AND assignee_objid = $P{assigneeid} " +
					" ) q INNER JOIN " + getTableName() + " a ON q.objid = a.objid " +
					" LEFT JOIN reading r ON a.objid = r.acctid";
			
			if (params.containsKey("start") && params.containsKey("limit")) {
				sql += " LIMIT " + Integer.parseInt(params.get("start").toString()) + ", " + Integer.parseInt(params.get("limit").toString()) + " ";
			}
//			Log.i("AccountDB", "sql " + sql);
//			String sql = "SELECT * FROM account WHERE (acctname LIKE ? OR acctno LIKE ? OR serialno LIKE ?) AND assignee_objid = ?";
			return ctx.getList(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public int countAccountByStuboutid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.objid FROM " + getTableName() + " a " +
					" WHERE a.stuboutid = $P{stuboutid} ";
			
			return ctx.getCount(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public List<Map> getAccountByStuboutid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.*, r.objid AS readingid " +
					" FROM (" +
					"	SELECT objid FROM " + getTableName() +
					"   WHERE acctname LIKE $P{searchtext} " +
					"   AND stuboutid = $P{stuboutid} " +
					"   UNION " +
					"   SELECT objid FROM " + getTableName() +
					"   WHERE acctno LIKE $P{searchtext} " +
					"   AND stuboutid = $P{stuboutid} " +
					"   UNION " +
					"   SELECT objid FROM " + getTableName() +
					"   WHERE serialno LIKE $P{searchtext}" +
					"   AND stuboutid = $P{stuboutid} " +
					" ) q INNER JOIN " + getTableName() + " a ON q.objid = a.objid " +
					" LEFT JOIN reading r ON a.objid = r.acctid " +
					" ORDER BY a.sortorder";

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
	
	public Map findPreviousAccountByStuboutid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.*, r.objid AS readingid " +
					" FROM " + getTableName() + " a " +
					" LEFT JOIN reading r ON a.objid = r.acctid " +
					" WHERE a.stuboutid = $P{stuboutid} " +
					" AND a.sortorder < $P{sortorder} " +
					" ORDER BY a.sortorder DESC";
			return ctx.find(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
	
	public Map findNextAccountByStuboutid(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			String sql = "SELECT a.*, r.objid AS readingid " +
					" FROM " + getTableName() + " a " +
					" LEFT JOIN reading r ON a.objid = r.acctid " +
					" WHERE a.stuboutid = $P{stuboutid} " +
					" AND a.sortorder > $P{sortorder} " +
					" ORDER BY a.sortorder";
			return ctx.find(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}
}
