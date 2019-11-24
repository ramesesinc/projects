/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.landtax;

import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;


public class RPTLedgerIncentiveTest {
    private static Map entity;
    private static Map incentive;
    private static Map incentiveItem;
    
    @BeforeClass
    public static void setUpClass() {
        RPTLedgerData.cleanup();
        entity = RPTLedgerData.createApprovedLedger();
    }
    
    @AfterClass
    public static void tearDownClass() {
        cleanupIncentives();
        RPTLedgerData.cleanup(entity);
    }
    
    
    /*===========================================
     * Effectivity year : 2015
     * Incentive years  : 2016 - 2017
     * tax   : 100
     ===========================================*/
    @Test
    public void test50PercentIncentive(){
        int basicRate = 50;
        int sefRate = 50;
        cleanupIncentives();
        createIncentive(basicRate, sefRate);
        testItemsAmount(basicRate, sefRate);
    }
    
    @Test
    public void test5075PercentIncentive(){
        int basicRate = 50;
        int sefRate = 75;
        cleanupIncentives();
        createIncentive(basicRate, sefRate);
        testItemsAmount(basicRate, sefRate);
    }
    
    
    
    private void testItemsAmount(int basicRate, int sefRate){
        Map parent = Data.toMapObjid(entity);
        List<Map> items = RPTLedgerData.svc.getLedgerEntries(parent);
        
        int fromYear = RPTLedgerData.effectivityYear + 1;
        int toYear = RPTLedgerData.effectivityYear + 2;
        
        double amount = 100.0;
        double basic = amount - (amount * basicRate / 100.0);
        double sef  = amount - (amount * sefRate / 100.0);
        
        for(Map item : items){
            int year = (Integer)item.get("year");
            if (year >= fromYear && year <= toYear){
                if ("basic".equalsIgnoreCase(item.get("revtype").toString())){
                    assertEquals(Data.toDecimal(basic), Data.toDecimal(item.get("amount")));
                }
                else if ("sef".equalsIgnoreCase(item.get("revtype").toString())){
                    assertEquals(Data.toDecimal(sef), Data.toDecimal(item.get("amount")));
                }
            }
            else {
                assertEquals(Data.toDecimal(amount), Data.toDecimal(item.get("amount")));
            }
        }
    }
    
    
    private void createIncentive(int basicRate, int sefRate){
        Map pdate = Data.parseCurrentDate();
        
        Map createdBy = new HashMap();
        createdBy.put("objid", "system");
        createdBy.put("name", "system");
        createdBy.put("title", "system");
        createdBy.put("date", pdate.get("date"));
        
        incentive = new HashMap();
        incentive.put("_schemaname", "rpttaxincentive");
        incentive.put("objid", entity.get("objid"));
        incentive.put("state", "APPROVED");
        incentive.put("txnno", "01");
        incentive.put("txndate", pdate.get("date"));
        
        Map taxpayer = (Map) entity.get("taxpayer");
        incentive.put("taxpayer_objid", taxpayer.get("objid"));
        incentive.put("taxpayer_name", taxpayer.get("name"));
        incentive.put("taxpayer_address", "LGU ADDRESS");
        incentive.put("name", "-");
        incentive.put("remarks", "-");
        
        Map user = Data.getAdminUser();
        incentive.put("createdby_objid", user.get("objid"));
        incentive.put("createdby_name", user.get("name"));
        incentive.put("createdby_title", user.get("jobtitle"));
        incentive.put("createdby_date", pdate.get("date"));
        
        incentiveItem = createIncentiveItem(incentive);
        incentiveItem.put("basicrate", basicRate);
        incentiveItem.put("sefrate", sefRate);
        
        ServiceUtil.persistence().create(incentive);
        ServiceUtil.persistence().create(incentiveItem);
        
        repostLedgerItems();
    }
    
    private Map createIncentiveItem(Map incentive){
        Map item = new HashMap();
        item.put("_schemaname", "rpttaxincentive_item");
        item.put("objid", incentive.get("objid"));
        item.put("rpttaxincentiveid", incentive.get("objid"));
        item.put("rptledgerid", entity.get("objid"));
        item.put("fromyear", RPTLedgerData.effectivityYear+1);
        item.put("toyear", RPTLedgerData.effectivityYear+2);
        item.put("basicrate", 0);
        item.put("sefrate", 0);
        return item;
    }
    
    private void repostLedgerItems(){
        //set nextbilldate to null 
        Map info = new HashMap();
        info.put("_schemaname", "rptledger");
        info.put("objid", entity.get("objid"));
        info.put("nextbilldate", null);
        ServiceUtil.persistence().update(info);
        
        RPTLedgerData.svc.postLedgerItems(entity, Data.getCurrentYear());
    }
    
    private static void cleanupIncentives(){
        if (incentiveItem != null){
            ServiceUtil.persistence().removeEntity(incentiveItem);
            ServiceUtil.persistence().removeEntity(incentive);
        }
    }
    
}

