package com.rameses.clfc.cashbreakdownchecking;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CashBreakdownCheckingController
{
    @Service("CashBreakdownCheckingService")
    def breakdownSvc;

    def type;
    def service;
    def dateSvc;
    def collectorList;

    def collector;
    def route;
    def billdate;
    def mode;
    def cashbreakdown;
    def totalbreakdown;

    String title = "Cash Breakdown Checking";

    @PropertyChangeListener
    def listener = [
        "type": {o->
            if (o == 'consolidated') collector = null;
        }
    ]

    CashBreakdownCheckingController() {
        try {
            service = InvokerProxy.instance.create("UsergroupMemberLookupService");
            collectorList = service.getList([_tag: 'FIELD_COLLECTOR,CASHIER']);
            collectorList.unique{ it.objid }.sort{ it.lastname };

            dateSvc = InvokerProxy.instance.create("DateService");
            billdate = dateSvc.getServerDateAsString().split(" ")[0];
        } catch(ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }   
    }

    void init() {
        type = 'consolidated';
        mode = 'init';
    }

    def getRouteList() {
        return breakdownSvc.getRoutes([collectorid: collector.objid, billdate: billdate]);
    }

    def getCashbreakdown() {
        def params = [
            entries         : cashbreakdown,
            totalbreakdown  : totalbreakdown,
            editable        : false
        ]
        return InvokerUtil.lookupOpener('clfc:denomination', params);
    }

    def close() {
        return "_close";
    }

    def next() {
        mode = 'main'
        
        def params = [ billdate: billdate ];
        
        def page = 'consolidated'
        cashbreakdown = [];
        if (type == 'consolidated') {
            title = "Consolidated Cash Breakdown";
            cashbreakdown = breakdownSvc.getConsolidatedBreakdown(params);
        } else if (type == 'percollector') {
            title = "Per Collection Cash Breakdown";
            //params.collectorid = collector.objid;
            //params.routecode = route.code;
            //params.collectionid = route.objid;
            //params.type = route.type;
            params.objid = route.objid
            params.grouptype = route.group.type;
            cashbreakdown = breakdownSvc.getPercollectorBreakdown(params);
            
            page = 'percollector';
            //if (params.type != 'route') page = 'consolidated';
            //route.name = (route.code? route.description + ' - ' + route.area : route.description);
        }

        totalbreakdown = 0
        if (cashbreakdown) totalbreakdown = cashbreakdown.amount.sum();

        return page;
    }

    def back() {
        mode = 'init';
        title = "Cash Breakdown Checking";
        return 'default';
    }
}