package com.rameses.clfc.android.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;

public class DBRouteService extends AbstractDBMapper 
{
	public String getTableName() { return "route"; }
	
//	public List<Map> getRoutesByCollectorid(String collectorid) throws Exception {
//		DBContext ctx = createDBContext();
//		try {
//			String sql = "SELECT * FROM "+getTableName()+" WHERE collectorid=? ORDER BY routedescription";
//			return ctx.getList(sql, new Object[]{collectorid});
//		} catch(Exception e) {
//			throw e; 
//		} finally {
//			if (isCloseable()) ctx.close(); 
//		}	
//	}
	
	public boolean hasRoutesByCollectorid(String collectorid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT routecode FROM "+getTableName()+" WHERE collectorid=? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{collectorid}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public void remitRouteByRoutecode(String routecode) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "UPDATE "+getTableName()+" SET state='REMITTED' WHERE routecode=?";
			ctx.execute(sql, new Object[]{routecode});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
