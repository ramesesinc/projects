package com.rameses.gov.etracs.rptis.models;

import test.utils.Data;
import test.utils.ServiceUtil;
import java.util.HashMap;
import java.util.Map;

public class LedgerModel {
    public LedgerModel() {
        
    }
    
    public void simulatePaidLedger(Map faas) {
        Map ledger = Data.getLedgerByFaas(faas);
        ledger.put("_schemaname", "rptledger");
        ledger.put("state", "APPROVED");
        ledger.put("lastyearpaid", Data.getCurrentYear());
        ledger.put("lastqtrpaid", Data.getCurrentYear());
        ServiceUtil.persistence().update(ledger);
    }
    
}
