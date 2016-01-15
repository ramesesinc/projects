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
    
    @Service('DateService')
    def dateSvc;
    
    @Service('LoanFieldCollectionService')
    def service;
    
    String title = 'Field Collections';
    
    def action = 'init';
    def billdate, collector, route, entity;
    def searchtext, state, selectedPayment;
    
    void init() {
        action = 'init';
        entity = [:];
        billdate = dateSvc.getServerDateAsString().split(' ')[0];
    }
    
    def close() {
        return '_close';
    }
    
    void setSelectedPayment( selectedPayment ) {
        this.selectedPayment = selectedPayment;
        binding?.refresh('formActions');
    }
    
    def getIsCollector() {
        return (ClientContext.currentContext.headers.ROLES.containsKey("LOAN.FIELD_COLLECTOR")? true : false);
    }
    
    def getCollectorid() {
        return ClientContext.currentContext.headers.USERID;
    }
    
    def getCollectorList() {
        return service.getCollectors();
    }
    
    def getRouteList() {
        def params = [billdate: billdate];
        
        if (getIsCollector()) {
            params.collectorid = getCollectorid();
        
        } else {
            if (!collector) return [];
            params.collectorid = collector.objid;
        }
        
        def list = service.getRoutes(params);
        
        if (!list) return [];
        return list;
    }
    
    def next() {
        action = 'view';
        searchtext = null;
        //selectedCollectionSheet = null;
        
        def collectorid;
        if (getIsCollector()) {
            collectorid = getCollectorid();
        } else {
            collectorid = collector.objid;
        }

        def params = [ 
            routecode   : route.code,
            collectionid: route.objid,
            billdate    : billdate,
            collectorid : collectorid,
            type        : route.type
        ];
        
        entity = service.getFieldCollectionList(params);
        state = entity.state;
        
        if (!entity.payments) {
            throw new Exception("No collections to display.");
        }
        
        collectionSheetsHandler.reloadAll();
        return 'main';
    }
    
    def back() {
        action = 'init';
        entity = [:];
        return 'default';
    }
    
    void search() {
        def params = [
            searchtext  : searchtext,
            collectionid: route?.objid
        ];
        entity.payments = service.getCollectionPaymentList(params);
        //entity.payment = fieldColSvc.getCollectionList([searchtext: searchtext, collectionid: route.objid]);
        collectionSheetsHandler.reload();
    }
    
    def collectionSheetsHandler = [
        getColumnList: { return getColumns(); },
        fetchList: {o->
            if (!entity?.payments) entity.payments = [];
            return entity.payments;
            //if (!entity.collectionsheets) entity.collectionsheets = [];
            //return entity.collectionsheets;
        }
    ] as BasicListModel;
    
    def getColumns() {
        def cols = [];
        if (route.type != 'route') cols << [name: 'route.name', caption: 'Route'];
        cols << [name: 'payment.state', caption: 'Status'];
        cols << [name: 'loanapp.appno', caption: 'App. No.'];
        cols << [name: 'loanapp.loanamount', caption: 'Loan Amount', type: 'decimal', format: '#,##0.00'];
        cols << [name: 'borrower.name', caption: 'Borrower'];
        cols << [name: 'payment.refno', caption: 'Ref. No.'];
        cols << [name: 'payment.amount', caption: 'Amount', type: 'decimal', format: '#,##0.00'];
        cols << [name: 'payment.dtpaid', caption: 'Date Paid'];
        cols << [name: 'payment.paidby', caption: 'Paid By'];
        cols << [name: 'payment.author.name', caption: 'Collected By'];
        cols << [name: 'remarks', caption: 'Remarks'];
        
        return cols;
    };
    
    void remit() {
        if (!MsgBox.confirm("You are about to remit this collection. Continue?")) return;
        
        entity = service.remit([entity: entity]);
        state = entity.state;
        MsgBox.alert("Successfully remitted collection.");
        binding.refresh('formActions');
    }
    
    def viewSendBack() {
        return Inv.lookupOpener("sendback:open", [remittanceid: entity.remittanceid, action: 'remit']);
    }
    
    def voidPayment() {
        if (!selectedPayment) return null;
        
        def op;
        if (!selectedPayment?.voidid) {
            op = createVoidRequest();
        } else if (selectedPayment?.voidid) {
            op = openVoidRequest();
        }
        /*
        println 'payment';
        for (i in selectedPayment) {
            println i;
        }
        */
        if (!op) return null;
        return op;
    }
        
    def createVoidRequest() {
        def params  = getVoidRequestParameters();
        //params.route = selectedCollectionSheet.route;//route;
        params.route = selectedPayment.route;
        //params.collectionsheet = selectedCollectionSheet;
        params.collectionsheet = selectedPayment;
        //params.payment = payment;
        params.payment = selectedPayment.payment;
        params.collector = [
            objid   : ClientContext.currentContext.headers.USERID,
            name    : ClientContext.currentContext.headers.NAME
        ];
        
        return Inv.lookupOpener('voidrequest:create', params);
    }

    def openVoidRequest() {
        def params = getVoidRequestParameters();
        params.payment = selectedPayment.payment;
        params.payment.voidid = selectedPayment.voidid;
        return Inv.lookupOpener('voidrequest:open', params);
    }
    
    private def getVoidRequestParameters() {
        def handler = { o->
            //println 'before search';
            search();
            //println 'after search';
        }
        
        def params = [
            txncode                 : 'FIELD',
            collectionid            : route.objid,
            afterSaveHandler        : handler,
            afterApproveHandler     : handler,
            afterDisapproveHandler  : handler
        ];
        return params;
    }

    def capturePayment() {
        def handler = { o->
            /*
            def params = [ 
                routecode   : route.code,
                collectionid: route.objid,
                billdate    : billdate,
                collectorid : collectorid,
                type        : route.type
            ];
            entity = fieldColSvc.getCollections(params);
            collectionSheetsHandler.reload();
            */
           search();
        }
        return Inv.lookupOpener('capturepayment:new', [remittanceid: entity.remittanceid, refreshHandler: handler]);
        //println 'capture payment';
    }
}