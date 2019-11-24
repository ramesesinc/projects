package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import test.utils.Data;
import test.utils.Lookup;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FaasTransactionWithUnpaidLedgerTest  {
    protected static FaasModel faasModel;
    protected static Map faas;
    protected static Map newFaas;
    protected static Map ledger;
    
    static {
        faasModel = new FaasModel();
        faas = new HashMap();
        newFaas = new HashMap();
        ledger = new HashMap();
    }
    
    
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.initAndCapture();
        faas = faasModel.approve(faas);
        ledger = faasModel.approveLedger(faas);
        transferFaas();
    }
    
    @AfterClass
    public static void tearDownClass() {
        Data.cleanUp();
        Data.cleanUp(true);
    }

    private static void simulateUnpaidLeger() {
        // set lastyearpaid to lastyearpaid - 1 to simulate non-payment
        Map ledger = Data.getLedgerByFaas(faas);
        assertNotNull(ledger);
        int lastYearPaid = (Integer)ledger.get("lastyearpaid");
        ledger.put("_schemaname", "rptledger");
        ledger.put("lastyearpaid", lastYearPaid - 1);
        ServiceUtil.persistence().update(ledger);
    }

    private static void transferFaas() {
        simulateUnpaidLeger();
        
        try{
            Map info = createTransferInfo();
            faas = faasModel.initOnlineTransaction(info);
            fail("should check not fully paid ledger.");
        } catch(Exception ex){
            //
        }
    }

    private static Map createTransferInfo() {
        Map refFaas = Lookup.faas(faas.get("tdno"));
        Map info = new HashMap();
        info.put("txntype", Data.getTxnType("TR"));
        info.put("datacapture", true);
        info.put("faas", refFaas);
        return info;
    }
    
    @Test
    public void shouldNotCreateFaas() {
        Map data = Lookup.newFaas(faas);
        assertNull(data);
    }

}
