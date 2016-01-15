/*
 * CommonService.java
 *
 * Created on January 22, 2014, 11:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.services;

import java.util.Date;

/**
 *
 * @author wflores
 */
public class CommonService extends AbstractService 
{
    public String getServiceName() { return "MobileCommonService"; }

    public Date getServerDate() { 
        Object result = invoke("getServerDateAsString", null);
        try {
            return java.sql.Timestamp.valueOf(result.toString()); 
        } catch(Throwable t) {
            t.printStackTrace();
            return null; 
        }
    } 
    
    public long getServerTime() { 
        Date dt = getServerDate();
        return (dt == null? 0L: dt.getTime()); 
    } 
    
}
