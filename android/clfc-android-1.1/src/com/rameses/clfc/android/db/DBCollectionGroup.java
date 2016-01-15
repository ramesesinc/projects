package com.rameses.clfc.android.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBCollectionGroup extends AbstractDBMapper
{

	public String getTableName() {
		return "collection_group";
	}
	
	public List<Map> getCollectionDates() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT DISTINCT billdate FROM " + getTableName() + " ORDER BY billdate DESC";
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean isCollectionCreatedByCollectionid(String collectionid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE objid=? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{collectionid}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPreviousBilling(String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE billdate < ? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{date}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}

	public boolean hasCollectionGroupByCollector(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE collectorid = ? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{id}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasCollectionGroupByCollectorAndDate(String id, String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			Map params = new HashMap();
			params.put("collectorid", id);
			params.put("billdate", date);
			
			String sql = "SELECT objid FROM " + getTableName() + 
					" WHERE collectorid = $P{collectorid} " +
					" AND billdate = $P{billdate} " +
					" LIMIT 1";
			/*
			if (id != null || date != null) {
				sql += "WHERE ";
				if (id != null) {
					sql += "collectorid = '" + id + "' ";
					if (date != null) sql += "AND ";
				}
				if (date != null) {
					sql += "billdate = '" + date + "' ";
				}
			}
			*/
//			sql += "LIMIT 1";
			return (ctx.getCount(sql, params) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getPreviousBillings(String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE billdate < ?";
			return ctx.getList(sql, new Object[]{date});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getCollectionGroupsByCollector(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE collectorid=? ORDER BY billdate, description";
			return ctx.getList(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getCollectionGroupsByCollector(String id, String type) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE collectorid = $P{collectorid} AND type = $P{type} ORDER BY billdate, description";
			Map params = new HashMap();
			params.put("collectorid", id);
			params.put("type", type);
			return ctx.getList(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public Map findCollectionGroup(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE objid=?";
			return ctx.find(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
}
