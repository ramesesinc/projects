package com.rameses.clfc.treasury.checkpayment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class CheckPaymentController extends FormController
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    @Service('CheckPaymentService') 
    def service;

    def title = 'Check Payment'; 
    def entity = [:]; 
    
    void open() {
        def newentity = service.open([objid: entity.objid]);
        if (newentity) entity.putAll(newentity); 
    } 
    
    def close() {
        return '_close'; 
    }
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    } 
    
    def post() {
        if (!MsgBox.confirm('You are about to post this check. Continue?')) return;
        return Inv.lookupOpener('checkpayment:accept', [
            handler: {o-> 
                def data = [
                    objid     : entity.objid, 
                    passbook  : o.passbook,
                    remarks   : o.remarks,
                    dtresolved: o.txndate 
                ];
                data = service.post( data );
                if (data) entity.putAll(data); 

                binding.refresh(); 
                EventQueue.invokeLater({ if (caller) caller.reload(); }); 
            } 
        ]); 
    }
    
    void cleared() {
        if (!MsgBox.confirm("You are about to clear this check. Continue?")) return;
        
        entity = service.cleared(entity);
        
    }
    
    def reject() {
        if (!MsgBox.confirm("You are about to reject this check. Continue?")) return;
        def handler = { remarks->
            try {
                def data = service.reject([objid: entity.objid, remarks: remarks]); 
                if (data) entity.putAll(data);

                binding.refresh(); 
                EventQueue.invokeLater({ caller?.reload(); }); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }  
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Rejection', handler: handler]);
    }    
}
