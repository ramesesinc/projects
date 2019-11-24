/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.services;

import java.util.Map;

public interface CrudService {
    public Map create(Map entity);
    public Map open(Map entity);
    public Map update(Map entity);
    public void delete(Map entity);
}
