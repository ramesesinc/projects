package com.rameses.gov.etracs.landtax;

import com.rameses.gov.etracs.services.RPTLedgerService;
import test.utils.Data;
import test.utils.ServiceUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;


public class RPTLedgerTest {
    private static RPTLedgerService svc;
    private static Map entity;
    
    private int effectivityYear = 2015; 
    private int effectivityQtr = 1; 
    private double totalAv = 10000.0;
    
    static {
        svc = ServiceUtil.create(RPTLedgerService.class);
    }


    @BeforeClass
    public static void setUpClass() {
        entity = RPTLedgerData.createLedger();
        assertNotNull(entity);
        entity.putAll(RPTLedgerData.approveLedger(entity));
        assertNotNull(entity);
    }
    
    @AfterClass
    public static void tearDownClass() {
        RPTLedgerData.cleanup(entity);
        svc = null;
    }
    
    
    @Test
    public void stateIsApproved() {
        assertEquals("APPROVED", entity.get("state"));
    }
    
    @Test
    public void lastYearPaidIsEffectivityYearLessOne() {
        assertEquals(effectivityYear - 1, entity.get("lastyearpaid"));
    }
    
    @Test
    public void lastQtrPaidIsFour() {
        assertEquals(4, entity.get("lastqtrpaid"));       
    }
    
    @Test
    public void ledgerFaasesCountIsOne() {
        List<Map> faases = (List<Map>) entity.get("faases");
        assertEquals(1, faases.size());
    }
    
    @Test
    public void ledgerFaasStateIsApproved() {
        List<Map> faases = (List<Map>) entity.get("faases");
        assertEquals(1, faases.size());
        if (faases.size() > 0){
            Map first = faases.get(0);
            assertEquals("APPROVED", first.get("state"));
        }
    }
    
    @Test
    public void ledgerFaasFromYearIsEffectivityYear() {
        List<Map> faases = (List<Map>) entity.get("faases");
        assertEquals(1, faases.size());
        if (faases.size() > 0){
            Map first = faases.get(0);
            assertEquals(effectivityYear, first.get("fromyear"));
        }
    }
    
    @Test
    public void ledgerFaasFromQtrIsEffectivityQtr() {
        List<Map> faases = (List<Map>) entity.get("faases");
        assertEquals(1, faases.size());
        if (faases.size() > 0){
            Map first = faases.get(0);
            assertEquals(effectivityQtr, first.get("fromqtr"));
        }
    }
    
    @Test
    public void testLedgerToYearAndToQtrIsZero() {
        List<Map> faases = (List<Map>) entity.get("faases");
        assertEquals(1, faases.size());
        if (faases.size() > 0){
            Map first = faases.get(0);
            assertEquals(0, first.get("toyear"));
            assertEquals(0, first.get("toqtr"));
        }
    }
    
    @Test
    public void ledgerItemsCountIsNumYearsTimesRevCountOf2() {
        int revtypeCount = 2; //default: basic and sef 
        int cy = Data.getCurrentYear();
        int numYears = cy - effectivityYear + 1;
        int itemSize = numYears * revtypeCount;
        
        List<Map> items = svc.getLedgerEntries(Data.toMapObjid(entity));
        assertEquals(itemSize, items.size());
    }
    
    @Test
    public void ledgerItemsTaxIsOnePercentOfAV() {
        int cy = Data.getCurrentYear();
        double rate = 0.01;
        double tax = totalAv * rate;
        
        List<Map> items = svc.getLedgerEntries(Data.toMapObjid(entity));
        Map firstItem = items.get(0);
        double computedTax = new BigDecimal(firstItem.get("amount")+"").doubleValue();
        assertEquals(Data.toDecimal(tax), Data.toDecimal(computedTax));
    }
    
    @Test
    public void ledgerItemsFirstItemYearIsEffectivityYear() {
        List<Map> items = svc.getLedgerEntries(Data.toMapObjid(entity));
        Map firstItem = items.get(0);
        assertEquals(firstItem.get("year"), effectivityYear);
    }
    
    @Test
    public void ledgerItemsLastItemYearIsCurrentYear() {
        int cy = Data.getCurrentYear();
        List<Map> items = svc.getLedgerEntries(Data.toMapObjid(entity));
        Map lastItem = items.get(items.size()-1);
        assertEquals(lastItem.get("year"), cy);
    }

}

