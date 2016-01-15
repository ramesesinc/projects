package com.rameses.clfc.collection.shortage

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CollectionShortageListController extends ListController 
{
    @Service('LoanCollectionShortageService')
    def svc
    
    String serviceName = "LoanCollectionShortageService";
    String entityName = "shortage";

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

