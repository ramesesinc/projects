package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
        
public class LiquidationFundModel extends CashBreakdownModel2  {
    
    public def getChecks() {
        return entity.checks;
    }
    
    public def getCreditMemos() {
        return entity.creditmemos;
    }
    
    
}       