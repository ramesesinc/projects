package com.rameses.clfc.android.db;

import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBRemarksRemoved extends AbstractDBMapper 
{
	public String getTableName() { return "remarks_removed"; }
	
	public List<Map> getPendingRemarksRemoved(int limit) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM "+getTableName()+" WHERE state='PENDING' ";
			if (limit > 0) sql += "LIMIT "+limit;
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getPendingRemarksRemoved() throws Exception {
		return getPendingRemarksRemoved(0);
	}
	
	public boolean hasPendingRemarksRemoved() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM "+getTableName()+" WHERE state='PENDING' LIMIT 1";
			return (ctx.getCount(sql, new Object[]{}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
