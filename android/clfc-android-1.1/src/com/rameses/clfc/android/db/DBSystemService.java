package com.rameses.clfc.android.db;

import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBSystemService extends AbstractDBMapper 
{
	public String getTableName() { return "sys_var"; }
	
	public boolean hasBillingid(String collectorid, String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT name FROM "+getTableName()+" WHERE name=? LIMIT 1";
			String name = collectorid + "-" + date;
			return (ctx.getCount(sql, new Object[]{name}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public String getBillingid(String collectorid, String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT value FROM "+getTableName()+" WHERE name=?";
			String name = collectorid + "-" + date;
			Map map = ctx.find(sql, new Object[]{name});

			if (map == null) return null;
			return map.get("value").toString();
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public Map getRecordByName(String name) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE name=?";
			return ctx.find(sql, new Object[]{name});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
		
	}
}
