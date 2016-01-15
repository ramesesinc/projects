package com.rameses.clfc.android.db;

import java.util.List;

import com.rameses.db.android.DBContext;


public class DBCollectorRemarks extends AbstractDBMapper
{

	public String getTableName() { return "collector_remarks"; }
	
	public List getRemarks(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE parentid=? ORDER BY txndate DESC";
			return ctx.getList(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
