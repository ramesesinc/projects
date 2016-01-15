package com.rameses.clfc.android.db;

import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBSpecialCollectionPendingService extends AbstractDBMapper
{
	public String getTableName() { return "specialcollection"; }
	
	public Boolean hasUnpostedRequest() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " LIMIT 1";
			
			return (ctx.getCount(sql, new Object[]{}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getPendingSpecialCollection() throws Exception {
		return getPendingSpecialCollection(0);
	}
	
	public List<Map> getPendingSpecialCollection(int limit) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " ";
			if (limit > 0) sql += "LIMIT " + limit;
			
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
