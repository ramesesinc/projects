package com.rameses.clfc.loan.fieldcollections;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class FieldCollectionsController 
{
    @Binding
    def binding;

    @Service("DateService")
    def dateSvc;

    String title = 'Field Collections';
    def fieldColSvc;

    def page = 'init';
    def entity;
    def route;
    def routeList;
    def collector;
    def collectorList;
    def selectedCollectionSheet;
    def billdate;
    def state;
    def selectedPayment;
    def searchtext;
        
    FieldCollectionsController() {
        try {
            fieldColSvc = InvokerProxy.instance.create("LoanFieldCollectionService");
            if (!getIsCollector()) {
                collectorList = fieldColSvc.getCollectors();
            }
        } catch(ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }   
    }

    def getIsCollector() {
        return (ClientContext.currentContext.headers.ROLES.containsKey("LOAN.FIELD_COLLECTOR")? true : false);
    }

    def getCollectorid() {
        return ClientContext.currentContext.headers.USERID;
    }

    def getRouteList() {
        def params = [ billdate: billdate ]
        if (getIsCollector()) {
            params.collectorid = getCollectorid();
        
        } else {
            if (!collector) return [];
            params.collectorid = collector.objid;

        }

        def list = fieldColSvc.getRoutes(params);
        list.sort{ it.description };
        return list;
    }

    void init() {
        page = 'init';
        entity = [:];
        billdate = dateSvc.getServerDateAsString().split(" ")[0];
    }

    public void setSelectedCollectionSheet( selectedCollectionSheet ) {
        this.selectedCollectionSheet = selectedCollectionSheet;
        binding.refresh('formActions');
    }

    def close() {
        return '_close';
    }

    def next() {
        page = 'main'
        searchtext = null;
        
        selectedCollectionSheet = [:];
        def collectorid;
        if (getIsCollector()) 
            collectorid = ClientContext.currentContext.headers.USERID;
        else 
            collectorid = collector.objid;

        def params = [ 
            routecode   : route.code,
            collectionid: route.objid,
            billdate    : billdate,
            collectorid : collectorid,
            type        : route.type
        ];
        
        entity = fieldColSvc.getCollections(params);
        state = entity.state;
        if (!entity.collectionsheets) {
            throw new Exception("No collections to display.");
        }
        collectionSheetsHandler.reloadAll();
        return page;
    }

    def back() {
        page = 'init'
        return 'default';
    }

    def getColumns() {
        def cols = [];
        if (route.type != 'route') cols.add([name: 'route.name', caption: 'Route']);
        def l = [
            [name: 'loanapp.appno', caption: 'App. No.'],
            [name: 'loanapp.loanamount', caption: 'Loan Amount', type: 'decimal', format: '#,##0.00'],
            [name: 'borrower.name', caption: 'Borrower'],
            [name: 'total', caption: 'Amount', type: 'decimal', format: '#,##0.00'],
            [name: 'remarks', caption: 'Remarks'],
        ]
        cols.addAll(l);
        return cols;
    };

    def collectionSheetsHandler = [
        getColumnList: { return getColumns(); },
        fetchList: {o->
            if (!entity.collectionsheets) entity.collectionsheets = [];
            return entity.collectionsheets;
        }
    ] as BasicListModel;

    def paymentsHandler = [
        fetchList: {o->
            def payments = [];
            if (selectedCollectionSheet?.payments) payments = selectedCollectionSheet.payments;
            return payments;
        },
        onOpenItem: { itm, colName->
            if (getAllowRemit() && itm.version == 1) {
                if (itm.voidid) {
                    return openVoidRequest(itm);
                } else {
                    return createVoidRequest(itm);
                }
            }
        }
    ] as BasicListModel;

    void search() {
        entity.collectionsheets = fieldColSvc.getCollectionList([searchtext: searchtext, collectionid: route.objid]);
        collectionSheetsHandler.reload();
    }

    private def getVoidRequestParameters() {
        def params = [
            txncode                 : 'FIELD',
            collectionid            : route.objid,
            afterSaveHandler        : {o->
                selectedPayment.voidid = o.objid;
                selectedPayment.pending = 1;
                paymentsHandler.reload();
            },
            afterApproveHandler     : {o->
                //println 'calling after approve handler';
                selectedPayment.voided = 1;
                selectedPayment.pending = 0;
                selectedCollectionSheet.total = 0;
                def payments = selectedCollectionSheet.payments.findAll{ it.voided == 0 };
                if (payments.size() > 0) selectedCollectionSheet.total = payments.payamount.sum();
                collectionSheetsHandler.reload();
                paymentsHandler.reload();
                binding.refresh('totalcollection');
            },
            afterDisapproveHandler  : {o->
                selectedPayment.pending = 0;
                paymentsHandler.reload();
            }
        ]
    }

    def createVoidRequest( payment ) {
        def params  = getVoidRequestParameters();
        params.route = selectedCollectionSheet.route;//route;
        params.collectionsheet = selectedCollectionSheet;
        params.payment = payment;
        params.collector = [
            objid   : ClientContext.currentContext.headers.USERID,
            name    : ClientContext.currentContext.headers.NAME
        ];
        return Inv.lookupOpener('voidrequest:create', params);
    }

    def openVoidRequest( payment ) {
        def params = getVoidRequestParameters();
        params.payment = payment;
        return InvokerUtil.lookupOpener('voidrequest:open', params);
    }

    boolean getAllowRemit() {
        if (page != 'main' || !entity.remittanceid || entity.remitted || !getIsCollector()) return false;
        return true;
    }

    boolean getAllowViewSendBack() {
        if (page != 'main' || !getIsCollector() || entity.state != 'SEND_BACK') return false;
        return true;
    }

    boolean getAllowCapturePayment() {
        if (page != 'main' || !entity.remittanceid || entity.remitted || !getIsCollector()) return false;
        return true;
    }

    def remit() {
        if (MsgBox.confirm("You are about to remit this collection. Continue?")) {
            entity = fieldColSvc.remit([entity: entity]);
            state = entity.state;
            MsgBox.alert("Successfully remitted collection.");
            binding.refresh('formActions');
        }
    }

    def viewSendBack() {
        return Inv.lookupOpener("sendback:open", [remittanceid: entity.remittanceid, action: 'remit']);
    }

    def capturePayment() {
        def handler = { o->
            def params = [ 
                routecode   : route.code,
                collectionid: route.objid,
                billdate    : billdate,
                collectorid : collectorid,
                type        : route.type
            ];
            entity = fieldColSvc.getCollections(params);
            collectionSheetsHandler.reload();
        }
        return Inv.lookupOpener('capturepayment:new', [ remittanceid: entity.remittanceid, refreshHandler: handler ]);
        //println 'capture payment';
    }
}