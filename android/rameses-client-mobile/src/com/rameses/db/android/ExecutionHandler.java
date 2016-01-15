/*
 * ExecutionHandler.java
 *
 * Created on February 17, 2014, 10:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.db.android;

/**
 *
 * @author wflores
 */
public interface ExecutionHandler 
{
    public void execute(SQLExecutor sqlexec) throws Exception; 
}
