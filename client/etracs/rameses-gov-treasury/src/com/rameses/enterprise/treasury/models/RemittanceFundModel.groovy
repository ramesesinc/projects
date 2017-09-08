package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;
        
public class RemittanceFundModel extends CashBreakdownModel  {
    
    @Service("RemittanceService")
    def service;
    
    String schemaName = "remittance_fund";
    
    public def getChecks() {
        return entity.payments.findAll{ it.reftype == 'CHECK' };
    }
    
    public def getCreditMemos() {
        return entity.payments.findAll{ it.reftype == 'CREDITMEMO' };
    }
    
    public void afterOpen() {
        super.afterOpen();
        editable = entity.remittance?.state == 'DRAFT';
    }
    
    public void afterUpdate() {
        service.updateCashBreakdown([objid:entity.objid, cashbreakdown: entity.cashbreakdown ] );
        caller?.caller?.reload();
    }
    
}       