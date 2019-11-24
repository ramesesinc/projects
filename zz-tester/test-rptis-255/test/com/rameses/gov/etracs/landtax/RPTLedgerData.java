/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.landtax;

import com.rameses.gov.etracs.services.RPTLedgerService;
import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.Map;


public class RPTLedgerData {
    public static RPTLedgerService svc;
    public static final String objid = "L-0001";
    public static final String tdno = "L-0001";
    public static final int effectivityYear = 2015; 
    public static final int effectivityQtr = 1; 
    public static final double totalAv = 10000.0;
    
    static {
        svc = ServiceUtil.create(RPTLedgerService.class);
    }
    
    private RPTLedgerData(){}
    
    public static Map createApprovedLedger(){
        Map entity = createLedger();
        return approveLedger(entity);
    }    
    
    public static Map createLedger(){
        Map entity = createEntity();
        ServiceUtil.persistence().create(entity);
        entity.putAll(ServiceUtil.persistence().read(entity));
        return entity;
    }
    
    public static Map approveLedger(Map entity){
        svc.approve(entity);
        return ServiceUtil.persistence().read(entity);
    }
    
    public static void cleanup(){
        Map m = new HashMap();
        m.put("objid", objid);
        cleanup(m);
    }
    public static void cleanup(Map entity){
        deleteLedgerItems(entity);
        deleteLedgerFaases(entity);
        deleteLedger(entity);
    }
    
    
    public static Map createEntity(){
        Map m = new HashMap();
        m.put("txntype", Data.getTxnType("GR"));
        m.put("classification", Data.getClassification());
        m.put("barangay", Data.getBarangay());
        m.put("taxpayer", Data.getEntity());
        
        String fullpin = ((Map)m.get("barangay")).get("pin") + "-" + "001-01";
        
        m.put("_schemaname", "rptledger");
        m.put("objid", objid);
        m.put("state", "PENDING");
        m.put("lastyearpaid", effectivityYear - 1);
        m.put("lastqtrpaid", 4);
        m.put("fullpin", fullpin);
        m.put("tdno", tdno);
        m.put("cadastrallotno", "1");
        m.put("blockno", "1");
        m.put("rputype", "land");
        m.put("totalmv", 0);
        m.put("totalav", totalAv);
        m.put("totalareaha", 0);
        m.put("taxable", true);
        m.put("idleland", false);
        m.put("prevtdno", null);
        m.put("titleno", null);
        m.put("totalareasqm", 0);
        m.put("effectivityyear", effectivityYear);
        m.put("effectivityqtr", effectivityQtr);
        return m;
    }
    
    
    public static void deleteLedgerItems(Map entity) {
        Map findby = new HashMap();
        findby.put("parentid", entity.get("objid"));
        
        Map m = new HashMap();
        m.put("_schemaname", "rptledger_item");
        m.put("findBy", findby);
        ServiceUtil.persistence().removeEntity(m);
    }
    
    public static void deleteLedgerFaases(Map entity) {
        Map findby = new HashMap();
        findby.put("rptledgerid", entity.get("objid"));
        
        Map m = new HashMap();
        m.put("_schemaname", "rptledger_faas");
        m.put("findBy", findby);
        ServiceUtil.persistence().removeEntity(m);
    }
    
    public static void deleteLedger(Map entity) {
        Map findby = new HashMap();
        findby.put("objid", entity.get("objid"));
        
        Map m = new HashMap();
        m.put("_schemaname", "rptledger");
        m.put("findBy", findby);
        ServiceUtil.persistence().removeEntity(m);
    }
    
}
