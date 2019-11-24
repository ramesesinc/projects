/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.services;

import java.util.Map;


public interface PersistenceService {
    public Map create(Map entity);
    public Map read(Map entity);
    public Map update(Map entity);
    public void  removeEntity(Map entity);
}
