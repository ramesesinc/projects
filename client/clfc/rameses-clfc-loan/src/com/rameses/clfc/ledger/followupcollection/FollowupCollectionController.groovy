package com.rameses.clfc.ledger.followupcollection;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import java.rmi.server.UID;

class FollowupCollectionController //extends CRUDController
{
    @Binding
    def binding;
    
    @Service("DateService")
    def dateSvc;
    
    @Service("LoanFollowupCollectionService")
    def service;
    
    @Service("LoanBillingGroupService")
    def billingGroupSvc;

    @ChangeLog
    def changeLog;
    
    public String getTitle() {
        String text = "Follow-up Collection";

        if (mode == "create")
          return text + " (New)";
        if (mode == "edit") {
          return text + " (Edit)";
        }
        return text;
    }   
    
    //String serviceName = "LoanFollowupCollectionService";
    String entityName = "followupcollection";
    def mode = 'read';
    def entity, showconfirmation = true;
    /*
    boolean allowCreate = false;
    boolean allowDelete = false;
    boolean allowApprove = false;
    */
    /*def svc;
    FollowupCollectionController() {
        try {
            svc = InvokerProxy.instance.create("LoanFollowupCollectionService");
        } catch(ConnectException ce) {
            ce.printStackTrace();
            throw ce;
        }   
    }*/
    
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);

    def collectorLookupHandler = Inv.lookupOpener('route-collector:lookup', [:]);
    /*def collectorLookupHandler = InvokerUtil.lookupOpener('route-collector:lookup', [
        onselect : {o->
            def billing = svc.getCollectorBilling([collectorid: o.objid]);
            //if (!billing) throw new Exception("Collector has no billing for today. Create billing first before creating follow-up collections.");
            if (billing) entity.billingid = billing.objid;
        
            entity.collector = o;
            //for (i in entity) println i;
            binding.refresh('entity.collector');
        }
    ]);*/

    def selectedLedger;
    def listModelHandler;
    def prevledgers;
    
    void create() {
        entity = createEntity();
        mode = 'create';
        listHandler?.reload();
    }
    
    void open() {
        entity = service.open(entity);
        mode = 'read';
    }
    
    Map createEntity() {
        def map = [
            objid       : 'FC' + new UID(),
            state       : 'DRAFT',
            billingid   : 'LB' + new UID(),
            txntype     : 'ONLINE',
            ledgers     : [],
            routes      : [],
            txndate     : dateSvc.getServerDateAsString().split(" ")[0],
            //itemtype    : 'followup'
        ];
        showconfirmation = true;
        return map;
    }

    /*void afterCreate( data ) {
        if (data._specialcollectionrequest) {
            data.collector = data._specialcollectionrequest.collector;
            data.collector.username = data.collector.name;
            data.remarks = data._specialcollectionrequest.remarks ;
        }
    }*/

    def listHandler = [
        fetchList: {
            if (!entity.ledgers) entity.ledgers = [];
            return entity.ledgers;
        },
        onRemoveItem: { o->
            removeLedger();
        }
    ] as EditorListModel;

    
    void save() {
        if (listHandler.hasUncommittedData())
            throw new Exception("Please commit table data before saving.");
        
        if (!MsgBox.confirm("You are about to save this document. Continue?")) return;
        
        def msg;
        if (mode == 'create') {
            entity = service.create(entity);
            msg = "Follow-up collection created successfully.";
        } else if (mode == 'edit') {
            entity = service.update(entity);
            msg = "Follow-up collection updated successfully.";
        }
        changeLog?.clear();
        entity._addedledger = [];
        entity._removedledger = [];
        
        mode = 'read';
        MsgBox.alert(msg);
        binding?.refresh('formActions');
    }
    
    /*
    void beforeSave( data ) {
        //println 'data-> '+data;
        if (listHandler.hasUncommittedData())
            throw new Exception("Please commit table data before saving.");
    }
    */
   
    void edit() {
        mode = 'edit';
        prevledgers = [];
        def item;
        entity.ledgers.each{ o->
            item = [:];
            item.putAll(o);
            prevledgers.add(item);
        }
    }

     /*
    void beforeEdit( data ) {
        prevledgers = [];
        prevledgers.addAll(data.ledgers);
    }
    */
   
    def cancel() {
        if (mode == 'edit') {
            mode = 'read';
            
            if( changeLog.hasChanges() ) {
                changeLog.undoAll()
                changeLog.clear()
            }
            
            entity.ledgers = [];
            entity.ledgers.addAll(prevledgers);
            listHandler.reload();
            
            return null;
        }
        return close();
    }
    
    def close() {
        return "_close";
    }

    /*
    void afterCancel() {
        entity.ledgers = [];
        entity.ledgers.addAll(prevledgers);
        listHandler.reload();
    }
    */

    def getLookupLedgerHandler() {
        if (!entity.collector) throw new Exception("Please specify collector.");

        def handler = {o->
            if (entity.ledgers.find{ o.objid == it.objid }) 
                throw new Exception("Ledger has already been selected.");

            if (!entity.routes.find{ o.route.code == it.code }) {
                entity.routes.add(o.route);
            }
            
            if (!selectedLedger) {
                o.scdetailid = 'FCD' + new UID();
                selectedLedger = o;
                
                if (!entity._addedledger) entity._addedledger = [];
                entity._addedledger.add(o);
                
                if (!entity.ledgers) entity.ledgers = [];
                entity.ledgers.add(o);
            } else {
                o.scdetailid = selectedLedger.scdetailid;
                o._edited = true;
                selectedLedger.clear();
                selectedLedger.putAll(o);
            }
            listHandler.reload(); 
        }
        def params = [
            onselect    : handler,
            collectorid : entity.collector?.objid,
            billdate    : entity.txndate
        ];
        return InvokerUtil.lookupOpener('specialcollectionledger:lookup', params);
    }

    def addLedgersFromGroup() {
        if (!entity.txndate) throw new Exception("Please specify date.");
        
        def handler = { o->
            def list = billingGroupSvc.getDetailsWithLedgerInfo([objid: o.objid]);
            def m;
            list.each{ i->
                m = entity.ledgers.find{ i.objid == it.objid }
                if (!m) {
                    i.scdetailid = 'FCD' + new UID();
                    if (!entity._addedledger) entity._addedledger = [];
                    entity._addedledger.add(i);
                    
                    if (!entity.ledgers) entity.ledgers = [];
                    entity.ledgers.add(i);
                }
            }
            listHandler?.reload();
        }
        def params = [
            onselect: handler,
            state   : 'APPROVED',
            type    : 'FOLLOWUP',
            date    : entity.txndate
        ]
        return Inv.lookupOpener('billinggroup:lookup', params);
    }

    def removeLedger() {
        if (!selectedLedger) return;
        
        if (MsgBox.confirm("You are about to remove this ledger. Continue?")) {
            def route = selectedLedger.route;
            
            def item = entity.ledgers.find{ it.objid == selectedLedger.objid }
            
            if (!entity._removedledger) entity._removedledger = [];
            entity._removedledger.add(item);
            
            if (entity._addedledger) {
                def i = entity._addedledger.find{ it.objid == item.objid };
                if (i) entity._addedledger.remove(i);
            }
            
            entity.ledgers.remove(item);
            
            if (!entity.ledgers.find{ it.route.code == route.code }) {
                entity.routes.remove(route);
            }
            listHandler.reload();
        }
    }
    
    void resetBilling() {
        if (!MsgBox.confirm("You are about to reset this billing. Continue?")) return;
        
        service.resetBilling(entity);
        entity = service.open(entity);
        MsgBox.alert("Resetting billing has been successfully processed.");
    }
    
    void submitForVerification() {
        if (!MsgBox.confirm("You are about to submit this document for verification. Continue?")) return;
        
        entity = service.submitForVerification(entity);
    }
    
    void verify() {
        if (!MsgBox.confirm("You are about to verify this document. Continue?")) return;
        
        entity = service.verify(entity);
    }
    
    void submitForApproval() {
        if (!MsgBox.confirm("You are about to submit this document for approval. Continue?")) return;
        
        entity = service.submitForApproval(entity);
    }
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        entity = service.approveDocument(entity);
    }
    
    def createBilling() {
        if (!MsgBox.confirm("You are about to create billing for this document. Continue?")) return;
                
        def onMessageImpl = { o->
            //println 'onMessage '  + o;
            //println 'EOF ' + AsyncHandler.EOF;
            //loadingOpener.handle.binding.fireNavigation("_close");
            loadingOpener.handle.closeForm();

            if (o == AsyncHandler.EOF) {
                //loadingOpener.handle.binding.fireNavigation("_close");
                return;
            }
            entity = o;
            //entity.putAll(o);
            //def msg = ;
            //if (mode == 'edit') msg = "Follow-up collection updated successfully!";
            //mode = 'read';
            MsgBox.alert("Follow-up collection billing created successfully!");
            binding?.refresh();
            listHandler?.reload();  
        }
        
        loadingOpener = Inv.lookupOpener('popup:loading', [:]);
        def handler = [
            onMessage   : onMessageImpl,
            onError     : { p->
                //loadingOpener.handle.binding.fireNavigation('_close');
                loadingOpener.handle.closeForm();
                
                if (showconfirmation==true) {
                    def msg = p.message;
                    msg += '\nDo you still want to continue to create this billing?';
                    if (MsgBox.confirm(msg)) {
                        showconfirmation = false;
                        def xhandler = { i->
                            def handler2 = [
                                onMessage   : onMessageImpl,
                                onError     : { o->
                                    //loadingOpener.handle.binding.fireNavigation('_close');  
                                    loadingOpener.handle.closeForm();
                                    MsgBox.err(o.message); 
                                }
                            ] as AsyncHandler;
                            service.createNewBillingWithoutLedgerValidation(entity, handler2);
                        }
                        loadingOpener = Inv.lookupOpener('popup:loading', [handler: xhandler]);
                        binding.fireNavigation(loadingOpener);
                    }
                } else {
                    MsgBox.err(p.message);
                }
            }
        ] as AsyncHandler;
        service.createNewBilling(entity, handler);
        return loadingOpener;
    }
    
    def cancelBilling() {
        if (!MsgBox.confirm("You are about to cancel creation for this billing. Continue?")) return;
        
        def handler = { remarks->
            entity.cancelremarks = remarks;
            entity = service.cancelBilling(entity);
            
            MsgBox.alert("Billing successfully cancelled!");
            binding?.refresh();
        }
        return Inv.lookupOpener('remarks:create', [title: 'Reason for cancellation', handler: handler]);
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
    }
}