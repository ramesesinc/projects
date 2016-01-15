/*
 * SQLExecutor.java
 *
 * Created on February 17, 2014, 9:54 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.db.android;

import java.util.List;
import java.util.Map;

/**
 *
 * @author wflores
 */
public interface SQLExecutor 
{
    public DBContext getContext();
        
    public void execute(String sql);
    public void execute(String sql, Object[] params);
    public void execute(String sql, Map params);
    
    public void insert(String tablename, Map params); 
    
    public int update(String tablename, String whereClause);
    public int update(String tablename, String whereClause, Object[] params);
    public int update(String tablename, String whereClause, Map params);
    
    public int delete(String tablename, String whereClause);
    public int delete(String tablename, String whereClause, Object[] params);
    public int delete(String tablename, String whereClause, Map params);
    
    public List<Map> getList(String sql);
    public List<Map> getList(String sql, Object[] params);
    public List<Map> getList(String sql, Map params);
    
    public Map find(String sql);
    public Map find(String sql, Object[] params);
    public Map find(String sql, Map params);
}
