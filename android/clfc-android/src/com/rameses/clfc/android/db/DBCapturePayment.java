package com.rameses.clfc.android.db;

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
}
