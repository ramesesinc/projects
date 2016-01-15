package com.rameses.clfc.android.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;


public class DBCapturePayment extends AbstractDBMapper
{

	public String getTableName() { return "capture_payment"; }

	public List<Map> getPaymentsByCollector(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE collector_objid=? ORDER BY borrowername";
			return ctx.getList(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getPendingPayments() throws Exception {
		return getPendingPayments(0);
	}
	
	public List<Map> getPendingPayments(int limit) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE state = 'PENDING'";
			if (limit > 0) sql += " LIMIT " + limit;
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getPreviousPayments(String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE txndate < ?";
			return ctx.getList(sql, new Object[]{date});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasUnpostedPayments() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE state = 'PENDING' LIMIT 1";
			return (ctx.getCount(sql, new Object[]{}) > 0);
		} catch(Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPayments(String collectorid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE collector_objid=? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{collectorid}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPaymentS(String collectorid, String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			Map params = new HashMap();
			params.put("collectorid", collectorid);
			params.put("date", date);
			
			String sql =  "SELECT objid FROM " + getTableName()
						+ " WHERE collector_objid = $P{collectorid}"
						+ " AND txndate = $P{date}"
						+ " LIMIT 1";
			return (ctx.getCount(sql, params) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPendingPayments() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE state = 'PENDING' LIMIT 1";
			return (ctx.getCount(sql, new HashMap()) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPendingPayments(String collectorid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE state = 'PENDING' AND collector_objid = ? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{collectorid}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
