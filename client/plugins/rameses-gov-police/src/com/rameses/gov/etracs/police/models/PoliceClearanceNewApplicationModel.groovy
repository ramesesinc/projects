package com.rameses.gov.police.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.reports.*;
import java.rmi.server.UID;
import com.rameses.rcp.framework.ClientContext;

public class PoliceClearanceNewApplicationModel extends CrudPageFlowModel   {
    
    @Service("PoliceClearanceBillingService")
    def billingService;
    
    def showTrackingno() {
        return Modal.show( "show_trackingno", [info: [trackingno: entity.barcode]] );
    }
    
    void loadFees() {
        def m = billingService.getBilling( [apptype: entity.apptype?.objid ] );
        entity.fees = m.items;
        entity.amount = m.amount;
    }
    
    def feeListModel = [
        fetchList: { o->
            return entity.fees;
        }
    ] as BasicListModel;
    
    
}