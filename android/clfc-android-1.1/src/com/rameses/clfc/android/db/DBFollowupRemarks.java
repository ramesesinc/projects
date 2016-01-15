package com.rameses.clfc.android.db;

import java.util.List;

import com.rameses.db.android.DBContext;


public class DBFollowupRemarks extends AbstractDBMapper
{

	public String getTableName() { return "followup_remarks"; }
	
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
