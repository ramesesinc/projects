package com.rameses.clfc.collection.overage

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CollectionOverageListController extends ListController 
{
    @Service('LoanCollectionOverageService')
    def svc;
    
    String serviceName = "LoanCollectionOverageService";
    String entityName = "overage";

    boolean allowCreate = true;

    def remittanceid, collector, txndate, handler;
    def selectedOption;

    List fetchList(Map params) {
        params.remittanceid = remittanceid;
        return super.fetchList(params);
    }

    Map createOpenerParams() {
        def params = super.createOpenerParams();
        params.remittanceid = remittanceid
        params.collector = collector;
        params.txndate = txndate;
        params.afterApproveHandler = handler;
        params.state = selectedOption?.state;
        return params;
    }

    void viewonly() {
        allowCreate = false;
    }
    
    void beforeGetColumns(Map params) {
        params.state = selectedOption?.state;
    }

    def optionsModel = [
        getItems: {
            return svc.getStates();
        }, 
        onselect: { o->
            query.state = o.state;
            reloadAll();
        }
    ] as ListPaneModel;
}

