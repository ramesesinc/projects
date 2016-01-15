package com.rameses.clfc.patch.cashbreakdown;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class UpdateCashBreakdownController
{
    @Binding
    def binding;

    @Service("DateService")
    def dateSvc;

    def service;

    def txndate;
    def entity;
    def collector;
    def collectorList;
    def route;
    def page;
    def mode;
    def breakdown;
    def totalbreakdown;
    def prevbreakdown;

    UpdateCashBreakdownController() {
        try {
            service = InvokerProxy.instance.create("UpdateCashBreakdownService");
            collectorList = service.getCollectors();
        } catch(ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }   
    }

    void init() {
        page = 'init';
        mode = 'read';
        entity = [:];
        txndate = dateSvc.getServerDate();
    }
    
    def getRouteList() {
        if (!collector) return [];

        return service.getRoutes([billdate: txndate, collectorid: collector.objid]);
    }

    def close() {
        return "_close";
    }

    def back() {
        page = 'init';
        mode = 'read';

        return "default";
    }

    def save() {
        
        if (mode == 'create') {
            entity.breakdown = service.saveCashBreakdown(entity);
        } else if (mode == 'edit') {
            entity.breakdown = service.updateCashBreakdown(entity);
        }

        mode = 'read';
        binding.refresh();
    }

    def edit() {
        mode = 'edit';
        prevbreakdown = [];
        
        def itm;
        entity.breakdown.items.each{
            itm = [:];
            itm.putAll(it);
            prevbreakdown.add(itm);
        }

        binding.refresh();
    }

    def cancel() {
        mode = 'read';
        entity.breakdown.items.clear();
        entity.breakdown.items.addAll(prevbreakdown);
        totalbreakdown = entity.breakdown.items.amount.sum();
        binding.refresh();
    }

    def next() {
        page = 'main'
        
        def params = [
            billdate    : txndate,
            collectorid : collector.objid,
            collectionid: route.objid,
            routecode   : route.code,
            type        : route.type
        ]
        entity.breakdown = service.getCashbreakdown(params);
        if (!entity.breakdown) {
            mode = 'create';
            entity.breakdown = [
                objid   : 'CB' + new UID(),
                items   : []
            ]
            totalbreakdown = 0;
        } else {
            totalbreakdown = entity.breakdown.items.amount.sum();
        }
        
        return "main";
    }

    def getCashbreakdown() {
        def params = [
            entries         : entity.breakdown.items,
            totalbreakdown  : totalbreakdown,
            editable        : (mode != 'read'? true: false),
            onupdate        : {o->
                totalbreakdown = o;
            }
        ]
        return InvokerUtil.lookupOpener('clfc:denomination', params);
    }
}