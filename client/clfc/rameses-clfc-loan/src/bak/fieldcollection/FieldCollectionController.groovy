package com.rameses.clfc.loan.fieldcollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class FieldCollectionController
{
    @Binding
    def binding;

    @Service("DateService")
    def dateSvc;

    @Service("RemittanceSendBackService")
    def sendbackSvc;
    
    @Service("LoanFieldCollectionService")
    def service;

    String title = "Post Field Collection";

    def mode;
    def entity;
    def billdate;
    def collector;
    def collectorList;
    def iscashier = false;
    def totalbreakdown;
    def totalCollection;
    def prevcashbreakdown = [];
    def selectedRoute;
    def route;
    def routeList;
    def page;

    void init() {
        entity = [:];
        billdate = dateSvc.getServerDateAsString().split(" ")[0];
        mode = 'init'
        collectorList = service.getCollectors();
    }

    def close() {
        return '_exit';
    }

    def next() {
        mode = 'read';
        page = "main";
       
        getFieldCollection();
        return new Opener(outcome: page);
    }

    private void getFieldCollection() {
         def params = [
            collectionid    : route.objid,
            collectorid     : collector.objid,
            billdate        : billdate,
            routecode       : route.code,
            type            : route.type
        ]

        entity = service.getFieldCollection(params);
        if (!entity) throw new Exception("No unposted collection for this collector.");

        entity.billdate = billdate;
        entity.collection = route.type;

        if (!entity.cashbreakdown) {
            entity.cashbreakdown = createCashBreakdown();
        } else {
            totalbreakdown = 0;
            if (entity.cashbreakdown.items) totalbreakdown = entity.cashbreakdown.items.amount.sum();
        }
    }

    private def createCashBreakdown() {
        def m = [
            objid   : 'CB' + new UID(),
            items   : []
        ]
        mode = 'create';
        totalbreakdown = 0;
        return m;
    }

    def getRouteList() {
        if (!collector) return [];
        def params = [
            collectorid : collector.objid,
            billdate    : billdate
        ];
        return service.getRoutes(params);
    }
    
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

    def viewCollectionSheets() {
        def title = "Field Collection: " + route.description + (route.area? " - " + route.area : "");
        def opener = InvokerUtil.lookupOpener('fcloan:open', [type: route.type, collectionid: entity.objid, title: title])
        opener.caption = title;
        return opener;
    }

    boolean getIsAllowEdit() {
        if (mode != 'read' || ((entity.remittance.state=='POSTED' && entity.cashbreakdown.state != 'SEND_BACK') && entity.remittance.state != 'FOR_POSTING')) return false;
        return true;
    }
    
    boolean getIsAllowSave() {
        if (mode == 'init' || mode == 'read' || ((entity.remittance.state=='POSTED' && entity.cashbreakdown.state != 'SEND_BACK') && entity.remittance.state != 'FOR_POSTING')) return false;
        return true;
    }

    boolean getIsAllowPost() {
        if (mode != 'read' || entity.remittance.state != 'FOR_POSTING' || getTotalCollection() != totalbreakdown) return false;
        return true;
    }
    
    boolean getIsAllowCbsForVerifcation() {
        if (mode!='read' || (entity.remittance.state!='POSTED' || (entity.remittance.state=='POSTED' && entity.cashbreakdown.state!='SEND_BACK' && entity.cashbreakdown.state!='DRAFT')) || getTotalCollection() != totalbreakdown) return false;
        return true;
    }

    def routesHandler = [
        fetchList: {o->
            if (!entity.routes) entity.routes = [];
            return entity.routes;
        },
        onOpenItem: {itm, colName->
            def params = [
                route               : itm.route,
                collectionid        : route.objid,
                type                : route.type,
                fieldcollectionid   : entity.objid
            ]
            def opener = InvokerUtil.lookupOpener('fcloan:open', params);
            opener.caption = "Field Collection: "+itm.route.description+" - "+itm.route.area;
            return opener;
        }
    ] as BasicListModel;

    def back() {
        mode = 'init';
        return 'default';
    }

    public def getTotalCollection() {
        //if (page == 'special') return entity.routes.total.sum();
        //return entity.route.total;
        return entity.totalamount;
    }
    
    void submitCbsForVerification() {
        if (!MsgBox.confirm("You are about to submit CBS for this collection for verification. Continue?")) return;
        
        entity.cashbreakdown = service.submitCbsForVerification(entity);
        binding.refresh();
    }
    
    def viewCbsSendbackRemarks() {
        return Inv.lookupOpener('remarks:open', [title: 'Reason for Send Back', remarks: entity.cashbreakdown.sendbackremarks])
    }

    def save() {
        /*if (getTotalCollection() != totalbreakdown) 
            throw new Exception('Total amount for denomination does not match with total amount collected.');*/
        
        /*if (page == 'main') {
            entity.routecode = route.code;
        } else if (page == 'special') {
            entity.collectionid = route.objid;
        }*/
        //entity.breakdowntype = route.type;
        if (mode == 'create') {
            entity.cashbreakdown = service.saveCashBreakdown(entity);
        } else if (mode == 'edit') {
            entity.cashbreakdown = service.updateCashBreakdown(entity);
        }
        mode = 'read';
        binding.refresh();
    }

    def edit() {
        mode = 'edit';
        prevcashbreakdown.clear()
        def map;
        entity.cashbreakdown.items.each{
            map = [:];
            map.putAll(it);
            prevcashbreakdown.add(map);
        }
        binding.refresh();
    }

    def cancel() {
        mode = 'read';
        /*println 'prev cash breakdown';
        prevcashbreakdown.each{
            'denomination = '+it;
        }*/
        entity.cashbreakdown.items.clear();
        entity.cashbreakdown.items.addAll(prevcashbreakdown);
        totalbreakdown = entity.cashbreakdown.items.amount.sum();
        binding.refresh();
    }

    def post() {
        if (MsgBox.confirm("You are about to post this collection. Continue?")) {
            //entity.posttype = route.type;
            //if (page == 'special') entity.collectionid = route.objid;
            service.post(entity);
            MsgBox.alert("Collection successfully posted!");
            entity = [:];
            mode = 'init';
            return 'default';
        }
    }

    def sendback() {
        def handler = { o->
            entity = service.sendBack([entity: entity]);
            binding.refresh('formActions');
        }

        def params = [
            remittanceid    : entity.remittance?.objid,
            action          : 'sendback',
            afterSaveHandler: handler
        ];

        return Inv.lookupOpener('sendback:open', params);
    }

    def shortage() {
        //println 'shortage';
        def handler = { o->
            getFieldCollection();
            binding.refresh();
        }

        def allowCreate = false;
        if (totalbreakdown && entity.totalamount > totalbreakdown) allowCreate = true;

        def params = [
            remittanceid: entity.remittance?.objid,
            collector   : collector,
            txndate     : entity.billdate,
            handler     : handler,
            allowCreate : allowCreate
        ];
        return Inv.lookupOpener('shortage:list', params);
    }

    def overage() {
        //println 'overage';
        def handler = { o->
            getFieldCollection();
            binding.refresh();
        }

        def allowCreate = false;
        if (totalbreakdown && entity.totalamount < totalbreakdown) allowCreate = true;

        def params = [
            remittanceid: entity.remittance?.objid,
            collector   : collector,
            txndate     : entity.billdate,
            handler     : handler,
            allowCreate : allowCreate
        ];
        return Inv.lookupOpener('overage:list', params);
    }

    def shortagefundrequest() {
        def allowCreate = false;
        if (totalbreakdown && entity.totalamount > totalbreakdown) allowCreate = true;
        
        if (allowCreate == false)
            throw new Exception("Cannot create shortage fund request. Collection does not have shortages.");

        def params = [
            remittanceid: entity.remittance?.objid,
            collector   : collector
        ]
        return Inv.lookupOpener("shortagefundrequest:create", params);
    }
}