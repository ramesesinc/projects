package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import java.rmi.server.*;

class SubCollectorRemittanceModel {
    
    @Binding
    def binding

    @Service("SubCollectorRemittanceService")
    def service;

    def entity;    
    def collectorList;
    def mode = "read";
    String title = "SubCollector Remittance";
    
    def total = 0.0;
    def breakdown = 0.0;
    def cashremainingbalance = 0.0;
    def cashBreakdownHandler;
    def cashbreakdown = [];
    
    @PropertyChangeListener
    def listener = [
        "entity.collector": { o->
            loadCollectionRemittance();                    
        }
    ];

    void create() {
        collectorList = service.getCollectorList();
        entity = [totalcash:0.0, totalnoncash:0.0, items:[]];
        entity.objid =  "SUBREM"+new UID();
        cashBreakdownHandler = InvokerUtil.lookupOpener("cash:breakdown", [
            entries: cashbreakdown, 
            onupdate: { o->
                breakdown = o;
                total = entity.totalnoncash+breakdown;
                binding.refresh("breakdown|cashremainingbalance");
            }] );      
        mode = "create";
    }

    void open() {
        collectorList = [ entity.collector ]
        entity = service.open(entity);
        listModel.reload()
        cashbreakdown = entity.cashbreakdown
        cashBreakdownHandler = InvokerUtil.lookupOpener("cash:breakdown", [entries: cashbreakdown] );    
        total = entity.totalcash + entity.totalnoncash;
        breakdown = entity.cashbreakdown.sum{ it.amount } 
        mode = 'read';
    }

    def getCashremainingbalance() {
        return (entity.totalcash - breakdown); 
    } 

    void loadCollectionRemittance(){
        entity.putAll(service.getUremittedCollections(entity));
        listModel.reload();
    }

    def listModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;

    void submit() {
        if( breakdown != entity.totalcash )
            throw new Exception("Cash breakdown must equal total cash");
        if(MsgBox.confirm("You are about to remit this transaction. Proceed?")) {
            entity.cashbreakdown = cashbreakdown
            entity = service.post(entity);
            MsgBox.alert("Remittance successful. Remittance no is " + entity.txnno );
            mode='read';
        }
    }

    def viewCheckPayment() {
        if(! entity.collector) throw new Exception("Please select a collector.  ");

        return InvokerUtil.lookupOpener('subcollector_remittance:noncash', [mode:mode, remittanceid: entity.objid, collectorid:entity.collector.objid] )
    }

    def popupReports(def inv) {
        def popupMenu = new PopupMenuOpener();
        def list = InvokerUtil.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }
}   
