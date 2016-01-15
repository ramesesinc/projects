package com.rameses.clfc.loan.subbilling;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class xLoanLedgerSubBillingController extends CRUDController
{   
    @Binding
    def binding;
    def service;
    
    String serviceName = 'LoanLedgerSubBillingService';
    String entityName = 'ledgersubbilling';
    def collectorCallbackHandler = {o->
        entity.parentid = o.objid;
        entity.collector = o.collector;
        entity.billdate = o.billdate
        entity.routes = o.routes
        entity.specialcollectionid = null;
        if (o.specialcollectionid) entity.specialcollectionid = o.specialcollectionid;
       // println 'entity = '+o;
        binding.refresh('entity.collector|entity.billdate');
        listHandler.reload();
    }
    //def collectorLookupHandler = InvokerUtil.lookupOpener('draft-billing-collector:lookup', [onselect: collectorCallbackHandler]);
    def collectorLookupHandler = InvokerUtil.lookupOpener('route-collector:lookup', [:]);

    String preferredTitle = 'Sub Collection Sheet';    
    String createFocusComponent = 'entity.collector';
    String editFocusComponent = 'entity.collector';  
    boolean showConfirmOnSave = false;
    boolean allowApprove = false;
    boolean allowCreate = false;
    boolean allowDelete = false;
    
    def billing;

    xLoanLedgerSubBillingController() {
        try {
            service = InvokerProxy.instance.create('LoanLedgerSubBillingService');
        } catch (ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }
    }
    
    Map createEntity() {
        // println 'entity ' + entity;
        if (entity) billing = entity;
        return [ 
            objid   : 'LSB'+new java.rmi.server.UID(), 
            routes  : (billing? billing.routes : [])
        ];
    }

    def listHandler = [
        fetchList: { 
            if (!entity.routes) entity.routes = [];
            return entity.routes;
        }
    ] as BasicListModel;

    void afterOpen( data ) {
        data._parentid = data.parentid;
    }

    def reset() {
        if (MsgBox.confirm("You are about to reset the billing information. Continue?")) {
            service.resetBilling(entity);
            MsgBox.alert("Resetting has been successfully processed.", true);
        }
    }
}
