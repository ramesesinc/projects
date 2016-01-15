package com.rameses.clfc.ledger.payment;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import java.rmi.server.UID;

class LoanLedgerPaymentListController
{
    @Caller
    def caller;
    
    @Binding
    def binding;

    @Service("LoanLedgerPaymentService")
    def svc;

    String title = "Payments";
    def entity;
    def selectedPayment;
    def loadingOpener = Inv.lookupOpener("popup:loading", [:]);

    void open() {
        entity.payments = svc.getList(entity);
    }
    
    void refresh() {
        def res = svc.refresh(entity);
        if (res) {
            entity.payments = res.list;
            entity.lastpageindex = res.lastpageindex;
            //entity.ledgercount = res.ledgercount;
            //if (!entity.ledgercount) entity.ledgercount = 1;
            //entity.rows = caller?.rows;
            //entity.lastpageindex = caller?.getLastPageIndex(entity);
            caller?.binding.refresh('entity.*');
        }
        paymentsHandler.reload();
    }

    /*
    void rebuild() {
        if (paymentsHandler.hasUncommittedData())
            throw new Exception('Please commit table data before saving.');
            
        def loadingActivated = false;
        def handler = {
            loadingActivated = true;
        println 'pass 3';
            for (int i=0; i<2000000; i++) {

            }
            println 'pass 2'
           
            //loadingOpener?.handle.closeForm();
            //for (def i=0; i<1000000; i++) {                
            //}
            //loadingOpener?.handle.closeForm();
        }
        loadingOpener = Inv.lookupOpener("popup:loading", [:]);
        binding.fireNavigation(loadingOpener);
    }
    */
    
    
    def rebuild() {
        if (paymentsHandler.hasUncommittedData())
            throw new Exception('Please commit table data before saving.');

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
                    entity.payments = [];
                    entity.payments.addAll(o.payments);
                    //entity = o;
                    entity._removed = [];
                    entity._added = [];
                    loadingOpener.handle.binding.fireNavigation("_close");
                    MsgBox.alert("Successfully rebuilt collection sheet!");
                    caller?.binding.refresh('entity.*');
                    binding.refresh();
                    paymentsHandler.reload();

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
        svc.rebuild(entity, handler);
        return loadingOpener;
    }

    def paymentsHandler = [
        fetchList: {
            //println 'removed ' + entity._removed;
            //println 'added ' + entity._added;
            if (!entity.payments) entity.payments = [];
            return entity.payments;
        },
        createItem: { return [objid: 'LLP' + new UID()] },
        onAddItem: {o->
            if (!entity._added) entity._added = [];
            entity._added.add(o);
            entity.payments.add(o);
        },
        onRemoveItem: { o->
            /*
            if (o.isproceedcollection == 1) {
                MsgBox.alert("Cannot remove proceed collection.");
                return false;
            }
            if (o.isonline == 1) {
                MsgBox.alert("Cannot remove payment.");
                return false;
            }
            */
           if (o.allowRemove == false) {
               MsgBox.alert("Cannot remove payment.");
               return false;
           }
            if(MsgBox.confirm('You are about to remove this payment. Continue')) {
                //println 'remove ' + o;
                if (!entity._removed) entity._removed = [];
                entity._removed.add(o);
                if (entity._added) entity._added.remove(o);
                entity.payments.remove(o);
                paymentsHandler.reload();
                return true;
            }
            return false;
        }, 
        onColumnUpdate: {itm, colName->
            itm._edited = true;
            //println 'item ' + itm;
        },
        isColumnEditable: { itm, colName->
            if (itm.isproceedcollection == 1 || itm.isonline == 1) return false;
            return true;
        }
    ] as EditorListModel;

    def getTotal() {
        if (!entity.payments) return 0;
        return entity.payments.amount.sum();
    }
}