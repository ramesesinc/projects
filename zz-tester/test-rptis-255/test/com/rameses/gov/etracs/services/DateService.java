/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.services;

import java.util.Date;
import java.util.Map;


public interface DateService {
    public Map parseCurrentDate();
    public Map parseDate(Object dt, Object object);
    public String format(String pattern, Object date);
    public Object getServerDate();
}
