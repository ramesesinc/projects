package com.rameses.clfc.android.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;
import com.rameses.util.MapProxy;

public class DBLocationTracker extends AbstractDBMapper  
{
	public String getTableName() { return "location_tracker"; }

//	public int getCountByCollectorid(String id) throws Exception {
//		String sql = "select * from "+ getTableName() +" where collectorid=?";
//		DBContext ctx = createDBContext();
//		try {
//			return ctx.getCount(sql, new Object[]{id});
//		} catch(Exception e) {
//			throw e; 
//		} finally {
//			if (isCloseable()) ctx.close(); 
//		}
//	}
	
	public int getLastSeqnoByCollectorid(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM "+getTableName()+" WHERE collectorid=? ORDER BY seqno DESC LIMIT 1";
			List<Map> list = ctx.getList(sql, new Object[]{id});
			if (list == null || list.isEmpty()) return 0;
			else {
				Map item = list.get(0);
				return MapProxy.getInteger(item, "seqno");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getLocationTrackers(int limit) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " ORDER BY seqno ";
			if (limit > 0) sql += "LIMIT " + limit;
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getLocationTrackers() throws Exception {
		return getLocationTrackers(0);
	}
	
	public boolean hasLocationTrackers() throws Exception {
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
	
	public boolean hasLocationTrackerByCollectoridLessThanDate(String collectorid, String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			Map params = new HashMap();
			params.put("collectorid", collectorid);
			params.put("date", date);
			String sql = "SELECT objid FROM " + getTableName() + " WHERE collectorid = $P{collectorid} AND txndate <= $P{date} LIMIT 1";
			return (ctx.getCount(sql, params) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getLocationTrackersByCollectoridAndLessThanDate(String collectorid, String date) throws Exception {
		DBContext ctx = createDBContext();
		try {
			Map params = new HashMap();
			params.put("collectorid", collectorid);
			params.put("date", date);
			String sql = "SELECT * FROM " + getTableName() + " WHERE collectorid = $P{collectorid} AND txndate <= $P{date}";
			return ctx.getList(sql, params);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	/*public boolean hasTooManyLocationTrackerByCollectorid(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE collectorid=?";
			return (ctx.getCount(sql, new Object[]{id}) > 10);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}*/
	
	public int getNoOfTrackersByCollectorid(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE collectorid=?";
			return ctx.getCount(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
