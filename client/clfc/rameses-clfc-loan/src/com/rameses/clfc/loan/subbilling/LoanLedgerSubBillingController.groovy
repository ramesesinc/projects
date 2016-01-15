package com.rameses.clfc.loan.subbilling;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class LoanLedgerSubBillingController extends CRUDController
{   
    @Caller
    def caller;
        
    String serviceName = 'LoanLedgerSubBillingService';
    String entityName = 'ledgersubbilling';

    def collectorLookupHandler = InvokerUtil.lookupOpener('route-collector:lookup', [
        onselect: {o->
            if (o.objid == entity.prevcollector.objid)
                throw new Exception("Cannot select previous collector as collector.")
            entity.collector = o;
        }
    ]);

    boolean allowApprove = false;
    boolean allowCreate = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def billing;
    
    Map createEntity() {
        if (entity) billing = entity;
        def routes = service.getBillingRoutes([billingid: billing.objid]);
        return [ 
            objid           : 'LSB'+new java.rmi.server.UID(),
            prevcollector   : (billing? billing.collector : [:]),
            routes          : routes,
            billdate        : (billing? billing.billdate : ''),
            prevbillingid   : (billing? billing.objid : '')
        ];
    }
    
    def open() {
        billing = entity;
        entity = [objid: billing.subbillingid];
        entity = service.open(entity);
        if (!entity.editable) allowEdit = false;
        
        return null;
    }
    
    /*def edit( data ) {
        allowEdit = true;
        super.edit();
    }

    void beforeCancel() {
        allowEdit = false;
    }*/

    void afterSave( data ) {
        if (!billing.subbillingid) {
            billing.subbillingid = data.objid;
            caller?.binding.refresh('formActions');
        }
    }

    def listHandler = [
        fetchList: { 
            if (!entity.routes) entity.routes = [];
            return entity.routes;
        }
    ] as BasicListModel;
}
