package com.rameses.clfc.android.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rameses.db.android.DBContext;


public class DBVoidService extends AbstractDBMapper
{
	public String getTableName() { return "void_request"; }
	
	public int noOfVoidPayments(String collectionsheetid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE csid=?";
			return ctx.getCount(sql, new Object[]{collectionsheetid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public int noOfVoidPaymentsByLoanappid(String loanappid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM "+getTableName()+" WHERE loanappid=?";
			return ctx.getCount(sql, new Object[]{loanappid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPendingVoidRequest() throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM "+getTableName()+" WHERE state='PENDING' LIMIT 1";
			return (ctx.getCount(sql, new Object[]{}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPendingVoidRequest(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE state = 'PENDING' AND itemid=? LIMIT 1";
			return (ctx.getCount(sql, new Object[]{id}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPendingVoidRequest(String itemid, String csid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM " + getTableName() + " WHERE state = 'PENDING' AND itemid = $P{itemid} AND csid = $P{csid} LIMIT 1";
			Map params = new HashMap();
			params.put("itemid", itemid);
			params.put("csid", csid);
			return (ctx.getCount(sql, params) > 1);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public boolean hasPendingVoidRequestByLoanappid(String loanappid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT objid FROM "+getTableName()+" WHERE state='PENDING'";
			return (ctx.getCount(sql, new Object[]{}) > 0);
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public Map findPendingVoidRequestByPaymentid(String paymentid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE paymentid=? AND state = 'PENDING'";
			return ctx.find(sql, new Object[]{paymentid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	
	public Map findVoidRequestByPaymentid(String paymentid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE paymentid=?";
			return ctx.find(sql, new Object[]{paymentid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public Map findVoidRequestByPaymentidAndState(String paymentid, String state) throws Exception {
		DBContext ctx = createDBContext();
		try {
			Map params = new HashMap();
			params.put("paymentid", paymentid);
			params.put("state", state);
			
			String sql = "SELECT * FROM " + getTableName() + " WHERE paymentid = $P{paymentid} AND state = $P{state}";
			return ctx.find(sql, params); 
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public Map findVoidRequestByItemidAndState(String itemid, String state) throws Exception {
		DBContext ctx = createDBContext();
		try {
			Map params = new HashMap();
			params.put("itemid", itemid);
			params.put("state", state);
			String sql = "SELECT * FROM " + getTableName() + " WHERE itemid = $P{itemid} AND state = $P{state}";
			return ctx.find(sql, params);
		} catch (RuntimeException re) {
			throw re; 
		} catch (Exception e) {
			throw e; 
		} catch (Throwable t) {
			throw new Exception(t.getMessage(), t); 
		} finally {
			if (isCloseable()) ctx.close(); 
		}
	}
	
	public List<Map> getPendingVoidRequests(int limit) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM "+getTableName()+" WHERE state='PENDING' ";
			if (limit > 0) sql += "LIMIT "+limit;
			return ctx.getList(sql, new Object[]{});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public List<Map> getPendingVoidRequests() throws Exception {
		return getPendingVoidRequests(0);
	}
	
	public List<Map> getVoidRequestsByItem(String itemid) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "SELECT * FROM " + getTableName() + " WHERE itemid = ?";
			return ctx.getList(sql, new Object[]{itemid});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public void approveVoidPaymentById(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "UPDATE "+getTableName()+" SET state='APPROVED' WHERE objid=?";
			ctx.execute(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
	
	public void disapproveVoidPaymentById(String id) throws Exception {
		DBContext ctx = createDBContext();
		try {
			String sql = "UPDATE "+getTableName()+" SET state='DISAPPROVED' WHERE objid=?";
			ctx.execute(sql, new Object[]{id});
		} catch (Exception e) {
			throw e;
		} finally {
			if (isCloseable()) ctx.close();
		}
	}
}
