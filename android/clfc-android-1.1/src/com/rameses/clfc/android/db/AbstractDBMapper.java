package com.rameses.clfc.android.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.rameses.db.android.DBContext;

public abstract class AbstractDBMapper 
{
	private DBContext dbContext;
	private String DB_NAME;
	private String TABLE_NAME;
	private boolean closeable = true;
	
	public AbstractDBMapper() {
		this.DB_NAME = getDatabaseName();
		this.TABLE_NAME = getTableName(); 
	}
	
	public String getDatabaseName() { return "clfc.db"; } 
	public abstract String getTableName();

	public void setDBContext(DBContext dbContext) {
		this.dbContext = dbContext;
	}
	
	protected DBContext createDBContext() {
		if (dbContext == null) {
			return new DBContext(DB_NAME); 
		} else {
			return dbContext; 
		} 
	} 
	
	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}
	
	public boolean isCloseable() { return closeable; }
	
	public Properties find(String objid) throws Exception {
		String sql = "select * from "+ TABLE_NAME +" where objid=?";
		DBContext ctx = new DBContext(DB_NAME);
		try {
			return (Properties) ctx.find(sql, new Object[]{objid});
		} catch(Exception e) {
			throw e; 
		} finally {
			ctx.close(); 
		}
	}	
	
	public List<Map> getList(Map params) throws Exception {
		StringBuffer buffer = new StringBuffer();
		if (params == null) params = new HashMap();
		Iterator keys = params.keySet().iterator(); 
		while (keys.hasNext()) {
			String key = keys.next()+"";
			if (buffer.length() > 0) buffer.append(" and ");
			
			buffer.append(key + "=$P{"+key+"}");
		}
		
		String sql = "select * from "+ TABLE_NAME;
		if (buffer.length() > 0) sql = sql + " where " + buffer;
		
		DBContext ctx = new DBContext(DB_NAME);
		try {
			return ctx.getList(sql, params);
		} catch(Exception e) {
			throw e; 
		} finally {
			ctx.close(); 
		}
	}	
		
	public void create(Map params) throws Exception {
		DBContext ctx = new DBContext(DB_NAME);
		try {
			ctx.insert(TABLE_NAME, params); 
		} catch(Exception e) {
			throw e; 
		} finally { 
			ctx.close(); 
		}
	}	
	
	public int update(Map params) throws Exception {
		DBContext ctx = new DBContext(DB_NAME);
		try {
			return ctx.update(TABLE_NAME, params, "objid=$P{objid}");  
		} catch(Exception e) { 
			throw e; 
		} finally {
			ctx.close(); 
		}
	}	
	
	public int delete(Map params) throws Exception {
		DBContext ctx = new DBContext(DB_NAME);
		try {
			return ctx.delete(TABLE_NAME, params, "objid=$P{objid}");  
		} catch(Exception e) { 
			throw e; 
		} finally {
			ctx.close(); 
		} 
	} 
} 
