package com.rameses.gov.etracs.landtax;

import com.rameses.gov.etracs.services.CashReceiptService;
import com.rameses.gov.etracs.services.RPTLedgerService;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class RPTReceiptTest {
    private RPTLedgerService ledgerSvc;
    private CashReceiptService receiptSvc;
    private Map receipt;
    private Map ledger;
    
    
    @Before
    public void setUp(){
        ledger = RPTLedgerData.createApprovedLedger();
        receipt = RPTReceiptData.createReceipt(ledger);
    }
    
    @Test()
    public void testReceipt(){
        System.out.println("NOT YET IMPLEMENTED");
    }
}
