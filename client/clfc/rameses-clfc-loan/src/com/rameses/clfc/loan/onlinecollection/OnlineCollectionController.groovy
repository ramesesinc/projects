package com.rameses.clfc.loan.onlinecollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import java.rmi.server.UID;

class OnlineCollectionController
{
    @Binding
    def binding;
    
    @Service('LoanOnlineCollectionService')
    def service;
    
    @Service('DateService')
    def dateSvc;
    
    @PropertyChangeListener
    def listener = [
        'txndate': { o->
            binding?.refresh('route|collector');
        },
        'collector': { o->
            binding?.refresh('route');
        }
    ];
    
    String title = 'Post Online Collection';
    
    def txndate, route, collector;
    def action = 'init', mode = 'read';
    def entity, totalbreakdown, prevcashbreakdown;
    def selectedPayment;
    
    def init() {
        resetDate();
        return initImpl();
    }
    
    def initImpl() {
        action = 'init';
        mode = 'read';
        binding?.refresh();
        return 'default';
    }
    
    void resetDate() {
        txndate = dateSvc.getServerDateAsString().split(' ')[0];
    }
    
    def getCollectorList() {
        def list = service.getCollectorList([txndate: txndate]);
        if (!list) return [];
        return list;
    }
    
    def getRouteList() {
        def list = service.getRouteList([collectorid: collector?.objid, txndate: txndate]);
        if (!list) return [];
        return list;
    }
    
    def close() {
        return '_close';
    }
    
    def next() {
        action = 'main';
        mode = 'read';
        getCollection();
        
        return 'main';
    }
    
    void setSelectedPayment( selectedPayment ) {
        this.selectedPayment = selectedPayment;
        binding?.refresh('formActions');
    }
    
    private void getCollection() {
        entity = service.getCollection([collector: collector, collection: route]);
        entity.collector = collector;
        
        listHandler?.reload();
        if (entity.remittance) {            
            if (entity.hasCash) {
                if (!entity.cashbreakdown) {
                    entity.cashbreakdown = createCashBreakdown();
                }
                totalbreakdown = entity.cashbreakdown?.items?.amount?.sum();
                if (!totalbreakdown) totalbreakdown = 0;
                
            }
        } else {
            entity.remittance = [objid: 'REM' + new UID(), state: 'DRAFT'];
        }
    }
    
    def createCashBreakdown() {
        def item = [
            objid   : 'CB' + new UID(),
            items   : []
        ]
        mode = 'create';
        totalbreakdown = 0;
        return item;
        
    }
    
    def back() {
        action = 'init';
        mode = 'read';
        
        return 'default';
    }
    
    def listHandler = [
        fetchList: { o->
            if (!entity.payments) entity.payments = [];
            return entity.payments;
        }
    ] as BasicListModel;
    
    def getCashbreakdown() {
        if (!entity.cashbreakdown?.items) {
            entity.cashbreakdown.items = [];
        }
        def params = [
            entries         : entity.cashbreakdown.items,
            totalbreakdown  : totalbreakdown,
            editable        : ((mode != 'read' && entity.hasCash)? true: false),
            onupdate        : { o->
                totalbreakdown = o;
            }
        ]
        def op = InvokerUtil.lookupOpener('clfc:denomination', params);
        if (!op) return null;
        return op;
    }
    
    def getTotalcollection() {
        if (!entity.payments || !entity.payments.find{ !it.state }) return 0;
        def list = entity.payments.findAll{ !it.state };
        def amt = list?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def getState() {
        if (!entity.remittance?.state) {
            entity.remittance.state = 'DRAFT';
        }
        return entity?.remittance?.state;
    }
    
    def getTotalcash() {
        if (!entity.payments || !entity.payments.find{ !it.state && it.iscash==true }) return 0;
        def list = entity.payments.findAll{ !it.state && it.iscash==true }
        def amt = list?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def getTotalnoncash() {
        if (!entity.payments || !entity.payments.find{ !it.state && it.isnoncash==true }) return 0;
        def list = entity.payments.findAll{ !it.state && it.isnoncash==true }
        def amt = list?.amount?.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    void save() {
        if (totalbreakdown != getTotalcash()) {
            throw new Exception('Total for denomination does not match with total cash collected.');
        }
        
        if (mode != 'read') {
            entity.cashbreakdown = service.updateCashBreakdown(entity.cashbreakdown);
        }
        mode = 'read';
    }
    
    void edit() {
        prevcashbreakdown = [];
        
        def item;
        entity?.cashbreakdown?.items?.each{ o->
            item = [:];
            item.putAll(o);
            prevcashbreakdown << item;
        }
        
        mode = 'edit';
    }
    
    void cancel() {
        entity?.cashbreakdown?.items = [];
        entity?.cashbreakdown?.items.addAll(prevcashbreakdown);
        totalbreakdown = entity?.cashbreakdown?.items?.amount?.sum();
        if (!totalbreakdown) totalbreakdown = 0;
        
        mode = 'read';
    }
    
    void remit() {
        if (!MsgBox.confirm('You are about to remit this collection. Continue?')) return;
        
        entity.objid = route?.objid;
        entity = service.remit(entity);
        
        totalbreakdown = entity?.cashbreakdown?.items?.amount?.sum();
        if (!totalbreakdown) totalbreakdown = 0;
        
        binding?.refresh('formActions');
    }
    
    def post() {
        if (!MsgBox.confirm('You are about to post this collection. Continue?')) return;
        
        service.post(entity);
        MsgBox.alert("Collection successfully posted!");
        return initImpl();
    }
    
    void submitCbsForVerification() {
        if (!MsgBox.confirm("You are about to submit CBS for this collection for verification. Continue?")) return;
        
        entity.cashbreakdown = service.submitCbsForVerification(entity);
        getCollection();
        //entity.cashbreakdown = service.submitCbsForVerification(entity);
        //getFieldCollection();
    }
    
    def viewCbsSendbackRemarks() {
        def params = [
            title   : 'Reason for Send Back',
            remarks : entity?.cashbreakdown?.sendbackremarks
        ];
        
        def op = Inv.lookupOpener('remarks:open', params);
        if (!op) return null;
        return op;
    }
    
    def voidPayment() {
        if (!selectedPayment) return;
        
        if (selectedPayment?.isproceedcollection == 1) {
            MsgBox.alert("Cannot void this payment.");
            return;
        }
        
        def op;
        if (selectedPayment.voidid) {
            op = openVoidRequest(selectedPayment);
        } else {
            op = createVoidRequest(selectedPayment);
        }
        
        if (!op) return null;
        return op;
    }
    
    def createVoidRequest( payment ) {
        def params = getVoidRequestParameters(payment);
        params.route = payment.route;
        params.collectionsheet = [
            loanapp : payment.loanapp,
            borrower: payment.borrower
        ];
        params.payment = payment;
        params.collector = [
            objid   : ClientContext.currentContext.headers.USERID,
            name    : ClientContext.currentContext.headers.NAME
        ];
        //println 'params ' + params;
        return InvokerUtil.lookupOpener('voidrequest:create', params);
    }

    def openVoidRequest( payment ) {
        def params = getVoidRequestParameters(payment);        
        params.payment = payment;
        return InvokerUtil.lookupOpener('voidrequest:open', params);
    }
    
    private def getVoidRequestParameters( payment ) {
        def handler = { o->
            getCollection();
            binding?.refresh();
        }
        
        def params = [
            txncode                 : 'ONLINE',
            collectionid            : payment.parentid,
            afterSaveHandler        : handler,
            afterApproveHandler     : handler,
            afterDisapproveHandler  : handler
        ];
        
        return params;
        /*
        def params = [
            txncode                 : 'ONLINE',
            collectionid            : payment.parentid,
            afterSaveHandler        : { o->
                selectedPayment.voidid = o.objid;
                selectedPayment.pending = 1;
            },
            afterApproveHandler     : { o->
                selectedPayment.voided = 1;
                selectedPayment.pending = 0;
                binding.refresh('totalcollection');
            },
            afterDisapproveHandler  : { o->
                selectedPayment.pending = 0;
                binding.refresh('totalcollection');
            }
        ];
        */
    }
}