package com.rameses.clfc.android.db;

import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBPrevLocation extends AbstractDBMapper 
{
	public String getTableName() { return "prev_location"; }
	
	public Map getPrevLocation() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM "+getTableName()+" LIMIT 1";
			return ctx.find(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
