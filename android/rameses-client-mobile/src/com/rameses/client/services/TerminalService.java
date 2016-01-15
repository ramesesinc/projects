/*
 * TerminalService.java
 *
 * Created on January 22, 2014, 11:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.client.services;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wflores
 */
public class TerminalService extends AbstractService 
{    
    public String getServiceName() { 
        return "MobileTerminalService"; 
    }
    
    public Map findTerminal(String terminalid) {
        Map params = new HashMap(); 
        params.put("terminalid", terminalid); 
        return (Map) invoke("findTerminal", params); 
    }
    
    public Map register(Map params) {
        return (Map) invoke("register", params); 
    }    
    
    public Map recover(String IMEI) {
        Map params = new HashMap(); 
        params.put("macaddress", IMEI); 
        return (Map) invoke("recover", params); 
    }
}
