package com.rameses.android;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.rameses.db.android.DBContext;

public abstract class AbstractDBMapper 
{
	private String DB_NAME;
	private String TABLE_NAME;
	private DBContext dbContext;	
	private DBContext localdbContext;
	private boolean autoCloseConnection;
	
	public AbstractDBMapper() {
		this.DB_NAME = getDatabaseName();
		this.TABLE_NAME = getTableName(); 
		setAutoCloseConnection(true);
	}
	
	public abstract String getTableName();
	
	public String getDatabaseName() { 
		return "main.db"; 
	} 
	
	public boolean isAutoCloseConnection() {
		return autoCloseConnection;
	}
	public void setAutoCloseConnection(boolean autoCloseConnection) {
		this.autoCloseConnection = autoCloseConnection;
	}
	
	private DBContext getLocalDBContext() {
		if (localdbContext == null) {
			localdbContext = new DBContext(DB_NAME); 
		}
		return localdbContext; 
	}
	
	public DBContext getDBContext() { 
		if (dbContext == null) {
			return getLocalDBContext(); 
		} else { 
			return dbContext; 
		} 
	}
	
	public void setDBContext(DBContext dbContext) {
		this.dbContext = dbContext;
	}
	
	public void close() {
		try { localdbContext.close(); } catch(Throwable t){;}
		try { dbContext.close(); } catch(Throwable t){;} 		
	}
	
	
	public Properties find(String objid) throws Exception {
		String sql = "select * from "+ TABLE_NAME +" where objid=?";
		DBContext ctx = getDBContext();
		try {
			return (Properties) ctx.find(sql, new Object[]{objid});
		} catch(Exception e) {
			throw e; 
		} finally {
			if (isAutoCloseConnection()) ctx.close(); 
		}
	} 

	public Properties find(Map params) throws Exception {
		StringBuffer filter = new StringBuffer();
		if (params != null) {
			Iterator keys = params.keySet().iterator(); 
			while (keys.hasNext()) {
				String key = keys.next()+"";
				if (filter.length() > 0) filter.append(" and ");
				
				filter.append(key + "=$P{"+key+"}");
			}
		}
		
		String sql = "select * from "+ TABLE_NAME;	
		if (filter.length() > 0) sql += " where " + filter.toString();
		
		DBContext ctx = getDBContext();
		try {
			return (Properties) ctx.find(sql, params);
		} catch(Exception e) {
			throw e; 
		} finally {
			if (isAutoCloseConnection()) ctx.close();
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
		
		DBContext ctx = getDBContext();
		try {
			return ctx.getList(sql, params);
		} catch(Exception e) {
			throw e; 
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}	
	
	public void create(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			ctx.insert(TABLE_NAME, params); 
		} catch(Exception e) {
			throw e; 
		} finally { 
			if (isAutoCloseConnection()) ctx.close();
		}
	}	
	
	public int update(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			return ctx.update(TABLE_NAME, params, "objid=$P{objid}");  
		} catch(Exception e) { 
			throw e; 
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		}
	}	
	
	public int delete(Map params) throws Exception {
		DBContext ctx = getDBContext();
		try {
			return ctx.delete(TABLE_NAME, params, "objid=$P{objid}");  
		} catch(Exception e) { 
			throw e; 
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		} 
	} 
	
	public void clearAll() throws Exception{
		DBContext ctx = getDBContext();
		try {
			ctx.execute("DELETE FROM " + getTableName());
		} catch(Exception e) { 
			throw e;
		} finally {
			if (isAutoCloseConnection()) ctx.close();
		} 
	}
	
} 
