package com.rameses.clfc.loan.onlinecollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import java.rmi.server.UID;

class XXOnlineCollectionController
{
    @Binding
    def binding;    

    def cashbreakdown;
    def service;
    def entity;
    def totalbreakdown = 0;
    def mode = 'init'
    def collector;
    def route;
    def txndate;
    def prevcashbreakdown = [];
    def selectedPayment;
    def collection;
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);

    String title = "Post Online Collection";

    @PropertyChangeListener
    def listener = [
        "collection": { o->
            txndate = o.txndate;
        }
    ]

    XXOnlineCollectionController() {
        try {
            service = InvokerProxy.instance.create('LoanOnlineCollectionService');
        } catch (ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }
    }

    void init() {
        collector  = [:];
        binding?.refresh();
        mode = 'init';
    }

    def paymentsHandler = [
        fetchList: {o->
            if (!entity.payments) entity.payments = [];
            return entity.payments;
        },
        onOpenItem: {itm, colName->
            if (entity.state == 'REMITTED') return;
            
            if (itm.isproceedcollection == 1) {
                MsgBox.alert("Cannot void this payment.");
                return;
            }
            
            if (itm.voidid) {
                return openVoidRequest(itm);
            } else {
                return createVoidRequest(itm);
            }
        }
    ] as BasicListModel;


    private def getVoidRequestParameters(payment) {
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

    public def getTotalcollection() {
        if (!entity.payments || !entity.payments.find{ it.voided == 0 }) return 0;
        def list = entity.payments.findAll{ it.voided == 0 };
        return (list? list.amount.sum() : 0);
    }
    
    def getTotalcash() {
        /*
        if (!entity.payments || !entity.payments.find{ it.voided==0 && it.payoption=='cash' }) return 0;
        def list = entity.payments.findAll{ it.voided==0 && it.payoption=='cash' }
        return (list? list.amount.sum() : 0);
        */
       if (!entity.payments || !entity.payments.find{ it.voided==0 && it.iscash==true }) return 0;
       def list = entity.payments.findAll{ it.voided==0 && it.iscash==true }
       return (list? list.amount.sum() : 0);
    }
    
    def getTotalnoncash() {
        /*
        if (!entity.payments || !entity.payments.find{ it.voided==0 && it.payoption=='check' }) return 0;
        def list = entity.payments.findAll{ it.voided==0 && it.payoption=='check' }
        return (list? list.amount.sum() : 0);
        */
       if (!entity.payments || !entity.payments.find{ it.voided==0 && it.isnoncash==true }) return 0;
       def list = entity.payments.findAll{ it.voided==0 && it.isnoncash==true }
       return (list? list.amount.sum() : 0);
    }

    def getCollectorList() {
        return service.getCollectors();
    }

    def getCollectionDateList() {
        return collector.collectionDates;
    }

    def close() {
        return '_close';
    }

    def next() {
        mode = 'read';
        entity = service.getCollection([collector: collector, collection: collection]);
        entity.collector = collector;

        paymentsHandler.reload();
        if (entity.remittance) {
            if (entity.hasCash) {
                if (!entity.cashbreakdown) {
                    entity.cashbreakdown = createCashBreakdown();
                }
                totalbreakdown = entity.cashbreakdown.items.amount.sum();
                if (!totalbreakdown) totalbreakdown = 0;
            }
        } else {
            entity.remittance = [ objid: 'REM' + new UID() ];
        }
        return 'main';
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

    public def getRouteList() {
        def params = [
            collectorid     : collector.objid,
            txndate         : txndate
        ];
        return service.getRoutes(params);
    }

    public def getCashbreakdown() {
        def params = [
            entries         : entity.cashbreakdown.items,
            totalbreakdown  : totalbreakdown,
            editable        : ((mode != 'read' && entity.hasCash)? true: false),
            onupdate        : { o->
                totalbreakdown = o;
            }
        ]
        return InvokerUtil.lookupOpener('clfc:denomination', params);
    }

    public boolean getIsAllowSave() {
        def flag = true;
        if (flag==true && mode=='init') flag = false;
        if (flag==true && entity?.state=='DRAFT') flag = false;
        if (flag==true && !entity?.hasCash) flag = false;
        if (flag==true && mode=='read') flag = false;        
        return flag;
    }

    public boolean getIsAllowEdit() {
        def flag = true;
        if (flag==true && mode=='init') flag = false;
        if (flag==true && entity?.state=='DRAFT') flag = false;
        if (flag==true && !entity?.hasCash) flag = false;
        if (flag==true && mode!='read') flag = false;
        return flag;
    }

    public boolean getIsAllowPost() {
        //println 'has cash ' + entity.hasCash;
        def flag = true;
        if (flag==true && mode=='init') flag = false;
        if (flag==true && entity.state=='DRAFT') flag = false;
        if (flag==true && mode!='read') flag = false;
        if (flag==true && (entity?.hasCash==null || (entity?.hasCash==true && totalbreakdown != getTotalcash()))) flag = false;
        return flag;
    }

    def back() {
        mode = 'init';
        return 'default';
    }

    void remit() {
        if(MsgBox.confirm("You are about to remit collection. Continue?")) {
            entity = service.remit(entity);
            if (entity.hasCash==true) {
                if (!entity.cashbreakdown) entity.cashbreakdown = createCashBreakdown();
                totalbreakdown = entity.cashbreakdown.items.amount.sum();
            }
            if (!totalbreakdown) totalbreakdown = 0;
            binding.refresh('formActions');
        }
    }

    void save() {
        if (totalbreakdown != getTotalcash())
            throw new Exception('Total for denomination does not match with total cash collected.');

        if (mode == 'create') {
            entity.cashbreakdown = service.saveCashBreakdown(entity);
        } else if (mode == 'edit') {
            entity.cashbreakdown = service.updateCashBreakdown(entity);
        }
        mode = 'read';
    }

    void edit() {
        mode = 'edit';
        prevcashbreakdown.clear();
        def map;
        entity.cashbreakdown.items.each{
            map = [:];
            map.putAll(it);
            prevcashbreakdown.add(map);
        }
    }

    void cancel() {
        mode = 'read';
        entity.cashbreakdown.items.clear();
        entity.cashbreakdown.items.addAll(prevcashbreakdown);
        totalbreakdown = entity.cashbreakdown.items.amount.sum();
    }

    def post() {
        if (!MsgBox.confirm('You are about to post this collection. Continue?')) return;
        service.post(entity);
        MsgBox.alert("Collection successfully posted!");
        init();
        //return new Opener(outcome:'default')
        return 'default';
        /*
        def handler;
        
        if (!handler) {
            handler = [
                onMessage: { o->
                    //println 'EOF ' + AsyncHandler.EOF;
                    if (o == AsyncHandler.EOF) {
                        loadingOpener.handle.binding.fireNavigation("_close");
                        return;
                    }
                    MsgBox.alert("Collection successfully posted!");
                    init();
                    return 'default';
                },
                onTimeout: {
                    handler?.retry(); 
                },
                onCancel: {
                    println 'processing cancelled.';
                    //fires when cancel() method is executed 
                }, 
                onError: { o->
                    loadingOpener.handle.binding.fireNavigation("_close");
                    MsgBox.err(o.message);
                }
            ] as AbstractAsyncHandler;
        }
        service.postAsync(entity, handler);
        return loadingOpener;
        */
        /*
        if (MsgBox.confirm("You are about to post this collection. Continue?")) {
            service.post(entity);
            MsgBox.alert("Collection successfully posted!");
            init();
            //return new Opener(outcome:'default')
            return 'default';
        }
        */
    }
}