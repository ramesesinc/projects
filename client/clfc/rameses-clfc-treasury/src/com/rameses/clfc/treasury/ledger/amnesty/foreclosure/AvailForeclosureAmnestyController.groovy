package com.rameses.clfc.treasury.ledger.amnesty.foreclosure

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class AvailForeclosureController extends CRUDController
{
    @Binding
    def binding;
    
    def amnestytype = 'FORECLOSURE';
    
    String serviceName = 'AvailFixAmnestyService';
    String entityName = 'availforeclosureamnesty';
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def amnestyLookup = Inv.lookupOpener('ledgeramnesty:lookup', [
         onselect   : { o->
             //println 'selected amnesty ' + o;
             def am = service.getAmnestyInfo(o);
             if (am) {
                 entity.amnesty = am;
                 entity.amnestyid = am.objid;
                 entity.borrower = am.borrower;
                 entity.loanapp = am.loanapp;
                 entity.ledger = am.ledger;
             }
             binding?.refresh();
         },
         type       : amnestytype,
         foravail   : true
    ]);
    
    Map createEntity() {
        return [
            objid   : 'AF' + new UID(),
            txnstate: 'DRAFT'
        ];
    }
}

