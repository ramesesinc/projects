/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.waterworks.database;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Rameses
 */
public interface DBContext {
    public boolean isClosed();
    public void close();
    
    public String showCreateTable(String name);
    
    public Map findBy( String tableName, Map params );
    public Map findBy( String tableName, Map params, String filter );
    public Map find(String sql, Map params);
    public Map find(String sql, Object[] params);
    public List<Map> getList(String sql, Map params);
    public List<Map> getList(String sql, Object[] params);
    public int getCount(String sql, Object[] params);
    public int getCount(String sql, Map params);
    public void execute(String sql);
    public void execute(String sql, Object[] params);
    public void execute(String sql, Map params);
    public long insert(String tableName, Map params);
    public int update(String tableName, Map params, String whereClause);
    public int update(String tableName, Object[] args, String whereClause);
    public int delete(String tableName, Map params, String whereClause);
    public int delete(String tableName, Object[] args, String whereClause);
    
}
