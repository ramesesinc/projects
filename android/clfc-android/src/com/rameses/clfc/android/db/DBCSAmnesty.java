package com.rameses.clfc.android.db;

import java.util.Map;

import com.rameses.db.android.DBContext;


public class DBCSAmnesty extends AbstractDBMapper
{

	public String getTableName() { return "amnesty"; }

	public Map findByParentid(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE parentid=? LIMIT 1";
			return ctx.find(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
