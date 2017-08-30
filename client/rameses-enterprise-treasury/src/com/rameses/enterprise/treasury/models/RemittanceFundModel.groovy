package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
        
public class RemittanceFundModel extends CashBreakdownModel2  {
    
    String schemaName = "remittance_fund";
    
    public def getChecks() {
        return entity.checks;
    }
    
    public def getCreditMemos() {
        return entity.creditmemos;
    }
    
    
}       