/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.landtax;

import com.rameses.gov.etracs.services.RPTLedgerService;
import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;



public class RPTLedgerFixTest {
    private static RPTLedgerService svc;
    private static Map entity;
    
    private static String objid = "L-0001";
    private static int effectivityYear = 2015; 
    private static int effectivityQtr = 1; 
    private static double totalAv = 10000.0;
    private static double firstItemDue = 0.0;
    
    private static int lastYearPaid;
    private static int lastQtrPaid;
    private static double partial = 0;
    

    static {
        svc = ServiceUtil.create(RPTLedgerService.class);
    }
    
    
    @BeforeClass
    public static void setUp() {
        createApprovedLedger();
    }
    
    @AfterClass
    public static void tearDown() {
        cleanup();
        svc = null;
    }
    
    
    @Test
    public void testFixFullYear(){
        lastYearPaid = effectivityYear;
        lastQtrPaid = 4;
        partial = 0.0;
        firstItemDue = 100.0;
        fixLedger();
        testLedger();
        testLedgerItems();
    }
    
    //============================================
    // lastyearpaid = 2015 
    // lastqtrpaid  = 1
    //
    // Qtr   tax  paid  balance 
    // 1      25    25    0
    // 2      25    0     25
    // 3      25    0     25
    // 4      25    0     25
    //============================================
    //       100    10    75
    //============================================
    @Test
    public void testFixFullQtr(){
        lastYearPaid = effectivityYear;
        lastQtrPaid = effectivityQtr;
        partial = 0.0;
        firstItemDue = 75;
        fixLedger();
        testLedger();
        testLedgerItems();
    }
    
    
    //============================================
    // Partial is applied on the 1Q, 2016
    // lastyearpaid = 2015 (effectivityyear)
    // lastqtrpaid  = 4
    //
    // Qtr   tax  paid  balance 
    // 1      25    10    15
    // 2      25    0     25
    // 3      25    0     25
    // 4      25    0     25
    //============================================
    //       100    10    90
    //============================================
    @Test
    public void testFixPartialQ1(){
        lastYearPaid = effectivityYear;
        lastQtrPaid = 4;
        partial = 10;
        firstItemDue = 90;
        fixLedger();
        testLedger();
        testLedgerItems();
    }
    
    //============================================
    // Partial is applied on the 1Q, 2016
    // lastyearpaid = 2015 (effectivityyear)
    // lastqtrpaid  = 1
    //
    // Qtr   tax  paid  balance 
    // 1      25    25    0
    // 2      25    10    15
    // 3      25    0     25
    // 4      25    0     25
    //============================================
    //       100    35    65
    //============================================
    @Test
    public void testFixPartialQ2(){
        lastYearPaid = effectivityYear;
        lastQtrPaid = 1;
        partial = 10;
        firstItemDue = 65;
        fixLedger();
        testLedger();
        testLedgerItems();
    }
    
    
    //============================================
    // Partial is applied on the 1Q, 2016
    // lastyearpaid = 2015 (effectivityyear)
    // lastqtrpaid  = 2
    //
    // Qtr   tax  paid  balance 
    // 1      25    25    0
    // 2      25    25    0
    // 3      25    10    15
    // 4      25    0     25
    //============================================
    //       100    60    40
    //============================================
    @Test
    public void testFixPartialQ3(){
        lastYearPaid = effectivityYear;
        lastQtrPaid = 2;
        partial = 10;
        firstItemDue = 40;
        fixLedger();
        testLedger();
        testLedgerItems();
    }
    
    
    //============================================
    // Partial is applied on the 1Q, 2016
    // lastyearpaid = 2015 (effectivityyear)
    // lastqtrpaid  = 3
    //
    // Qtr   tax  paid  balance 
    // 1      25    25    0
    // 2      25    25    0
    // 3      25    25    0
    // 4      25    10    15
    //============================================
    //       100    60    15
    //============================================
    @Test
    public void testFixPartialQ4(){
        lastYearPaid = effectivityYear;
        lastQtrPaid = 3;
        partial = 10;
        firstItemDue = 15;
        fixLedger();
        testLedger();
        testLedgerItems();
    }
    
    
    
    private void testLedger() {
        assertNotNull(entity);
        assertEquals("APPROVED", entity.get("state"));
        assertEquals(lastYearPaid, entity.get("lastyearpaid"));
        assertEquals(lastQtrPaid, entity.get("lastqtrpaid"));       
    }
        
    private void testLedgerItems() {
        int revtypeCount = 2; //default: basic and sef 
        int cy = Data.getCurrentYear();
        int numYears = cy - lastYearPaid;
        if (lastQtrPaid != 4){
            numYears += 1;
        }
        int itemSize = numYears * revtypeCount;
        
        double rate = 0.01;
        double tax = totalAv * rate;
        int paidQtr = (lastQtrPaid == 4 ? 0 : lastQtrPaid);
        if (partial > 0){
            if (paidQtr == 4){
                paidQtr = 0;
            }
        }
        
        double amtPaid = (tax / 4) * paidQtr + partial;
        int fromYear = lastYearPaid + (lastQtrPaid == 4 ? 1 : 0);
        
        List<Map> items = svc.getLedgerEntries(Data.toMapObjid(entity));
        assertEquals(itemSize, items.size());
        
        Map firstItem = items.get(0);
        Map lastItem = items.get(items.size()-1);
        
        assertEquals(Data.toDecimal(amtPaid), Data.toDecimal(firstItem.get("amtpaid")));
        assertEquals(Data.toDecimal(firstItemDue), Data.toDecimal(firstItem.get("amtdue")));
        
        assertEquals(firstItem.get("year"), fromYear);
        assertEquals(lastItem.get("year"), cy);
        
        for (int i=revtypeCount; i < items.size(); i++){
            Map m = items.get(i);
            assertEquals(Data.toDecimal(m.get("amtpaid")), Data.toDecimal(0));
        }
    }
    
    private static void createApprovedLedger(){
        entity = RPTLedgerData.createEntity();
        entity.put("_schemaname", "rptledger");
        entity.put("objid", objid);
        entity.put("effectivityyear", effectivityYear);
        entity.put("effectivityqtr", effectivityQtr);
        entity.put("totalav", totalAv);
        ServiceUtil.persistence().create(entity);
        entity.putAll(ServiceUtil.persistence().read(entity));
        entity.putAll(svc.approve(entity));
        entity.putAll(ServiceUtil.persistence().read(entity));
    }
    
    private void fixLedger(){
        Map info = new HashMap();
        info.put("objid", entity.get("objid"));
        info.put("tdno", entity.get("tdno"));
        info.put("fullpin", entity.get("fullpin"));
        info.put("taxpayer", entity.get("taxpayer"));
        info.put("lastyearpaid", lastYearPaid);
        info.put("lastqtrpaid", lastQtrPaid);
        info.put("taxable", entity.get("taxable"));
        info.put("basicpaid", partial);
        info.put("sefpaid", partial);
        svc.fixLedger(info);
        entity.putAll(ServiceUtil.persistence().read(entity));
    }
    
    private static void cleanup(){
        RPTLedgerData.deleteLedgerItems(entity);
        RPTLedgerData.deleteLedgerFaases(entity);
        RPTLedgerData.deleteLedger(entity);
    }
    

}

