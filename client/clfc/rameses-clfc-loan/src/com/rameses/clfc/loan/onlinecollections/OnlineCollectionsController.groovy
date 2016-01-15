package com.rameses.clfc.loan.onlinecollections;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class OnlineCollectionsController
{
    @Binding
    def binding;

    @Service('LoanOnlineCollectionService')
    def service;
    
    @Service("DateService")
    def dateSvc;

    String title = 'Online Collections';

    @PropertyChangeListener
    def listener = [
        'collector': { o->
            binding?.refresh('route');
        },
        'txndate': { o->
            binding?.refresh('route');
        }
    ]
    
    def page = 'init';
    def entity, collector, route;
    def collectorList, txndate;

    void init() {
        collector  = [:];
        page = 'init';
        txndate = dateSvc.getServerDateAsString().split(" ")[0];
    }
    
    def getRouteList() {
        def params = [txndate: txndate, collectorid: collector?.objid];
        def list = service.getRouteList(params);
        
        return list;
    }

    def paymentsHandler = [
        fetchList: {o->
            if (!entity.payments) entity.payments = [];
            return entity.payments;
        }
    ] as BasicListModel;

    def getCollectorList() {
        return service.getCollectors([txndate: txndate]);
    }

    def close() {
        return '_close';
    }

    def next() {
        page = 'main';
        
        /*
        def params = [
            txndate     : txndate,
            collectorid : collector?.objid,
            collectionid: route?.objid
        ];
        */
       def params = [collection: route, collector: collector];
        
        entity = service.getCollection(params);
        paymentsHandler.reload();        
        return page;
    }

    def back() {
        page = 'init';
        return 'default';
    }
}