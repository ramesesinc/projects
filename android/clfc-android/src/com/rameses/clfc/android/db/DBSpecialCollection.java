package com.rameses.clfc.android.db;

import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBSpecialCollection extends AbstractDBMapper
{
	public String getTableName() { return "specialcollection"; }
	
	public int noOfSpecialCollectionByCollector(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE collector_objid=?";
			return ctx.getCount(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getSpecialCollectionRequestsByCollector(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName();
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getSpecialCollectionRequestsByCollectorid(String collectorid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM "+getTableName()+" WHERE collectorid=?";
			return ctx.getList(sql, new Object[]{collectorid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public void changeStateById(Map params) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "UPDATE " + getTableName() + " SET state=$P{state} WHERE objid = $P{objid}";
			ctx.execute(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
