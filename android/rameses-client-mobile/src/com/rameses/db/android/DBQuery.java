/*
 * DBQuery.java
 *
 * Created on January 28, 2014, 12:02 PM
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
public class DBQuery 
{
    private AbstractDB adb;
    private String sql;
    private Map parameters;
    
    DBQuery(AbstractDB adb, String sql) {
        this.adb = adb;
        this.sql = sql; 
    }
    
    public void setParameters(Map parameters) {
        this.parameters = parameters; 
    }
    
    public List getResultList() {
        return null;
    }
    
    public Map getSingleResult() {
        return null; 
    }
}
