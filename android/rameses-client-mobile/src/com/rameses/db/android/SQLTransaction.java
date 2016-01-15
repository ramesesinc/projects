/*
 * SQLTransaction.java
 *
 * Created on February 5, 2014, 11:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.db.android;

import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wflores
 */
public class SQLTransaction implements SQLExecutor  
{
    private final static Object LOCKED = new Object();
    
    private String dbname;
    private DBContext dbContext;
    private SQLiteDatabase readabledb;
    private SQLiteDatabase writabledb;
    private StringBuilder logbuffer;
    
    public SQLTransaction(String dbname) {
        this.dbname = dbname;
    }
    
    public void beginTransaction() {
        getContext();
        writabledb.beginTransaction();
        logbuffer = new StringBuilder();
    }
    
    public void endTransaction() {
        endTransactionImpl();
        logbuffer = null; 
    }
    
    public void commit() {
        writabledb.setTransactionSuccessful();
    }
    
    public void log(String type, String action, Map data) {
        Map map = new HashMap();
        map.put("type", type);
        map.put("action", action);
        map.put("data", data);
        DBManager.getLogger().write(data); 
    }
    
    public DBContext getContext() {
        return getContextImpl();
    }
    
    public void execute(String sql) {
        getContextImpl();
        writabledb.execSQL(sql);
    }
    
    public void execute(String sql, Object[] args) {
        getContextImpl();
        writabledb.execSQL(sql, args);
    }
    
    public void execute(String sql, Map params) {
        getContextImpl().execute(sql, params);
    }
    
    public List<Map> getList(String sql) {
        return getList(sql, new Object[]{});
    }
    
    public List<Map> getList(String sql, Object[] args) {
        return getContextImpl().getList(sql, args);
    }

    public List<Map> getList(String sql, Map params) {
        return getContextImpl().getList(sql, params);
    }

    public Map find(String sql) {
        return find(sql, new Object[]{});
    }
    
    public Map find(String sql, Object[] args) {
        return getContextImpl().find(sql, args); 
    }

    public Map find(String sql, Map params) {
        return getContextImpl().find(sql, params); 
    }

    public void insert(String tablename, Map params) {
        getContextImpl().insert(tablename, params); 
    }
    
    public int update(String tablename, String whereClause) {
        return update(tablename, whereClause, new Object[]{});
    }
    
    public int update(String tablename, String whereClause, Object[] args) {
        return getContextImpl().update(tablename, args, whereClause); 
    }

    public int update(String tablename, String whereClause, Map params) {
        return getContextImpl().update(tablename, params, whereClause); 
    }
    
    public int delete(String tablename, String whereClause) {
        return delete(tablename, whereClause, new Object[]{});
    }
    
    public int delete(String tablename, String whereClause, Object[] args) {
        return getContextImpl().delete(tablename, args, whereClause);
    }

    public int delete(String tablename, String whereClause, Map params) {
        return getContextImpl().delete(tablename, params, whereClause);
    }

    public final void execute(ExecutionHandler handler) {
        if (handler == null) throw new RuntimeException("Please provide an execution handler");
        
        try {
            beginTransaction();
            handler.execute(this); 
            commit();
        } catch(RuntimeException re) {
            throw re; 
        } catch(Throwable e) {
            throw new RuntimeException(e.getMessage(), e); 
        } finally {
            endTransaction(); 
        } 
    }

    public final void execute() {
        try {
            beginTransaction();
            onExecute(this); 
            commit();
        } catch(RuntimeException re) {
            throw re; 
        } catch(Throwable e) {
            throw new RuntimeException(e.getMessage(), e); 
        } finally {
            endTransaction(); 
        } 
    }
    
    protected void onExecute(SQLExecutor sqlexec) {
    }
    
    private DBContext getContextImpl() {
        synchronized (LOCKED) {
            if (dbContext == null) {
                AbstractDB adb = DBManager.get(dbname);
                if (adb == null) throw new RuntimeException("'"+dbname+"' database is not registered");
                
                writabledb = adb.getWritableDatabase();
                readabledb = adb.getReadableDatabase();
                dbContext = new DBContext(writabledb, readabledb);
            }
            return dbContext;
        }
    }
    
    private void endTransactionImpl() {
        try {
            if (writabledb == null) return;
            if (writabledb.isOpen()) { 
                writabledb.endTransaction(); 
            } 
        } catch(RuntimeException re) {
            throw re;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try { writabledb.close(); } catch(Throwable t){;}
            try { readabledb.close(); } catch(Throwable t){;}
            
            writabledb = null;
            readabledb = null;
        }
    }
}
