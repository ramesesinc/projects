package com.rameses.clfc.loan.fieldcollection

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class FieldCollectionController 
{
    @Binding
    def binding;
    
    @Service("DateService")
    def dateSvc;
    
    @Service('LoanFieldCollectionService')
    def service;
    
    String title = 'Post Field Collection';
    
    def action = 'init', billdate, collector, route;
    def collectorList, entity, totalbreakdown = 0;
    def mode = 'read', prevcashbreakdown;
    
    void init() {
        action = 'init';
        billdate = dateSvc.getServerDateAsString().split(' ')[0];
        collectorList = service.getCollectors();
    }
    
    def close() {
        return '_exit';
    }
    
    def next() {
        mode = 'read';
        action = 'view';
        def opener = new Opener(outcome: 'main');
        getFieldCollection();
        return opener;
    }
    
    private void getFieldCollection() {
        entity = service.getFieldCollection([itemid: route.itemid]);
        if (!entity) throw new Exception("No unposted collection for this collector.");

        entity.billdate = billdate;
        entity.collection = route.type;
        if (!entity.route) entity.route = route;

        totalbreakdown = 0;
        if (entity.cashbreakdown.items) totalbreakdown = entity.cashbreakdown.items.amount.sum();
        if (!totalbreakdown) totalbreakdown = 0;
        /*
        if (!entity.cashbreakdown) {
            entity.cashbreakdown = createCashBreakdown();
        } else {
            totalbreakdown = 0;
            if (entity.cashbreakdown.items) totalbreakdown = entity.cashbreakdown.items.amount.sum();
            if (!totalbreakdown) totalbreakdown = 0;
        }
        */
    }
    
    /*
    private def createCashBreakdown() {
        def m = [
            objid   : 'CB' + new UID(),
            items   : []
        ]
        mode = 'create';
        totalbreakdown = 0;
        return m;
    }
    */
    
    def getCashbreakdown() {
        def params = [
            entries         : entity.cashbreakdown.items,
            totalbreakdown  : totalbreakdown,
            editable        : (mode != 'read'? true: false),
            onupdate        : {o->
                totalbreakdown = o;
            }
        ]
        return InvokerUtil.lookupOpener('clfc:denomination', params);
    }
    
    void submitCbsForVerification() {
        if (!MsgBox.confirm("You are about to submit CBS for this collection for verification. Continue?")) return;
        
        entity.cashbreakdown = service.submitCbsForVerification(entity);
        getFieldCollection();
    }
    
    def viewCbsSendbackRemarks() {
        return Inv.lookupOpener('remarks:open', [title: 'Reason for Send Back', remarks: entity.cashbreakdown.sendbackremarks])
    }
    
    /*
    boolean getAllowPost() {
        if (mode != 'read' || entity?.remittance?.state != 'FOR_POSTING' || getTotalCollection() != totalbreakdown) return false;
        return true;
    }
    */
    
    def getTotalCollection() {
        //if (page == 'special') return entity.routes.total.sum();
        //return entity.route.total;
        return entity.totalamount;
    }
    
    def post() {
        if (!MsgBox.confirm('You are about to post this collection. Continue?')) return;
        
        service.post(entity);
        MsgBox.alert("Collection successfully posted!");
        entity = [:];
        mode = 'read';
        action = 'init';
        return 'default';
    }

    def viewCollectionSheets() {
        def title = "Field Collection: " + route.description + (route.area? " - " + route.area : "");
        def opener = InvokerUtil.lookupOpener('fcloan:open', [type: route.type, collectionid: route.itemid, title: title])
        opener.caption = title;
        return opener;
    }
 
    void save() {
        entity.cashbreakdown = service.updateCashBreakdown(entity.cashbreakdown);
        mode = 'read';
    }
    
    void edit() {
        prevcashbreakdown = [];
        def item;
        entity?.cashbreakdown?.items?.each{ o->
            item = [:];
            item.putAll(o);
            prevcashbreakdown.add(item);
        }
        mode = 'edit';
    }
    
    void cancel() {
        if (!MsgBox.confirm('Cancelling will undo changes made to cash breakdown. Continue?')) return;
        
        entity?.cashbreakdown?.items = [];
        entity?.cashbreakdown?.items.addAll(prevcashbreakdown);
        totalbreakdown = entity?.cashbreakdown?.items?.amount?.sum();
        if (!totalbreakdown) totalbreakdown = 0;
        mode = 'read';
    }
    
    def getRouteList() {
        if (!collector) return [];
        def params = [
            collectorid : collector.objid,
            billdate    : billdate
        ];
        return service.getRoutes(params);
    }
    
    def back() {
        action = 'init';
        return 'default';
    }
    
    def overage() {
        //println 'overage';
        def handler = { o->
            getFieldCollection();
            binding.refresh();
        }

        def allowCreate = false;
        if (totalbreakdown == null) totalbreakdown = 0;
        if (entity.totalamount < totalbreakdown && entity?.hassendback==true) {
            allowCreate = true;
        }

        def params = [
            remittanceid: entity.remittance?.objid,
            collector   : collector,
            txndate     : billdate,
            handler     : handler,
            allowCreate : allowCreate
        ];
        return Inv.lookupOpener('overage:list', params);
    }

    def shortage() {
        //println 'shortage';
        def handler = { o->
            getFieldCollection();
            binding.refresh();
        }

        def allowCreate = false;
        if (!totalbreakdown) totalbreakdown = 0;
        if (entity.totalamount > totalbreakdown && entity?.hassendback==true) {
            allowCreate = true;
        }

        def params = [
            remittanceid: entity.remittance?.objid,
            collector   : collector,
            txndate     : entity.billdate,
            handler     : handler,
            allowCreate : allowCreate
        ];
        return Inv.lookupOpener('shortage:list', params);
    }

    def sendback() {
        def handler = { o->
            def params = [
                remittance  : entity?.remittance,
                itemid      : route.itemid
            ]
            entity = service.sendBack(params);
            binding.refresh('formActions');
        }

        def params = [
            remittanceid    : entity.remittance?.objid,
            action          : 'sendback',
            afterSaveHandler: handler
        ];

        return Inv.lookupOpener('sendback:open', params);
    }
}

