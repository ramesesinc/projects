package com.rameses.clfc.loan.payment.capture;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.common.*;
import java.rmi.server.UID;

class CapturePaymentMappingController 
{
    @Binding
    def binding;

    @Service("LoanCapturePaymentService")
    def service;

    @ChangeLog
    def changeLog;

    def entity, selectedPayment, prevlist;
    def mode = 'read';
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);

    void open() {
        mode = 'read';
        entity = service.open(entity);
        if (!entity.specialcollectionid) entity.specialcollectionid = 'SC' + new UID();
        entity?.list?.each{ o->
            if (!o.scdetailid) o.scdetailid = 'SCD' + new UID();
        }
        //if (!entity.cbsno) mode = 'create';
    }

    def listHandler = [
        fetchList: { o->
            if (!entity.list) entity.list = [];
            return entity.list;
        }
    ] as BasicListModel;

    def ledgerLookupHandler = Inv.lookupOpener("ledgerborrower:lookup", [ 
        onselect: { o-> 
            selectedPayment.ledgerid = o.objid;
            selectedPayment.ledger = o; 
        }
    ]);

    def close() {
        return "_close";
    }

    void remit() {
        if (!MsgBox.confirm("You are about to remit this document. Continue?")) return;
        
        entity = service.remit(entity);
        binding.refresh();
    }

    void save() {
        def unmapped = entity.list.find{ it.ledger == null };
        if (unmapped) {
            throw new Exception("Payment for borrower " + unmapped.borrowername + " has not been mapped.");
        }

        if (!MsgBox.confirm("You are about to save mapped payments. Continue?")) return;
        
        entity = service.save(entity);
        binding.refresh();
        mode = 'read';
        
    }

    void edit() {
        mode = 'edit';
        prevlist = [];
        
        def item;
        /*
        entity.list.each{ o->
            item = [:];
            for (i in o) {
                value = i.value;
                if (i instanceof Map) {
                    def m = [:];
                    m.putAll(i.value);
                    value = m;
                } 
                item[i.key] = value;
            }
            prevlist.add(item);
        }
        */
        entity.list.each{ o->
            item = [:];
            item.putAll(o);
            prevlist.add(item);
        }
    }

    void cancel() {
        if (!MsgBox.confirm("Cancelling will undo changes made. Continue?")) return;
        
        entity.list = [];
        entity.list.addAll(prevlist);

        listHandler.reload();
        if( changeLog.hasChanges() ) {
            changeLog.undoAll()
            changeLog.clear()
        }
        mode = 'read';
    }

    void submitForVerification() {
        if (!MsgBox.confirm("You are about to submit this document for verification. Continue?")) return;
        
        entity = service.submitForVerification(entity);
        binding?.refresh();
    }
    
    def sendBack() {
        def handler = { remarks->
            entity.sendbackremarks = remarks;
            entity = service.sendBack(entity);
            
            MsgBox.alert("Document successfully sent back!");
            binding?.refresh();
        }
        return Inv.lookupOpener('remarks:create', [title: 'Reason for Send Back', handler: handler]);
    }
    
    def viewSendBackRemarks() {
        return Inv.lookupOpener('remarks:open', [title: 'Reason for Send Back', remarks: entity?.sendbackremarks]);
    }
    
    def verify() {
        if (!MsgBox.confirm('You are about to verify this document. Continue?')) return;
        
        
        def handler;
        
        if (!handler) {
            handler = [
                onMessage: { o->
                    //println 'EOF ' + AsyncHandler.EOF;
                    if (o == AsyncHandler.EOF) {
                        loadingOpener.handle.binding.fireNavigation("_close");
                        return;
                    }

                    entity.putAll(o);
                    
                    loadingOpener.handle.binding.fireNavigation("_close");
                    MsgBox.alert("Successfully verified document!");
                    binding.refresh();

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
        service.verify(entity, handler);
        return loadingOpener;
    }
    
    /*
    boolean getAllowSave() {
        def flag = false;
        if (mode != 'read' && entity.state == 'PENDING') flag = true;
        return flag;
    }

    boolean getAllowEdit() {
        def flag = false;
        if (mode == 'read' && entity.state == 'PENDING') flag = true;
        return flag;
    }

    boolean getAllowRemit() {
        def flag = false;
        if (mode == 'read' && entity.specialcollectionid != null && !entity.remitted) flag = true;
        return flag;
    }
    */
}