/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.services;

import java.util.List;
import java.util.Map;


public interface QueryService {
    public Map findFirst(Map param);
    public List getList(Map param);
}
