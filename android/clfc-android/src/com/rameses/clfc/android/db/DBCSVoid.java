package com.rameses.clfc.android.db;

import java.util.HashMap;
import java.util.Map;

import com.rameses.db.android.DBContext;


public class DBCSVoid extends AbstractDBMapper
{

	public String getTableName() { return "void_request"; }

	public boolean hasPendingVoidRequest(String itemid, String csid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() 
						 + " WHERE itemid = $P{itemid}"
						 + " AND csid = $P{csid} "
						 + "AND state = 'PENDING'"
						 + " LIMIT 1";
			Map params = new HashMap();
			params.put("itemid", itemid);
			params.put("csid", csid);
			return (ctx.getCount(sql, params) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public Map findVoidRequestByPaymentid(String paymentid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName()
						 + " WHERE paymentid =?";
			return ctx.find(sql, new Object[]{paymentid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
