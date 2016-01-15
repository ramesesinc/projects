package com.rameses.clfc.treasury.amnesty.extension

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class AmnestyExtensionController extends CRUDController
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    String serviceName = "AmnestyExtensionService";
    String entityName = "amnestyextension";
    
    @Service("DateService")
    def dateSvc;
    
    Map createEntity() {
        return [
            objid   : 'AE' + new UID(),
            txnstate: 'DRAFT'
        ]
    }
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = true;
    
    def amnestyLookup = Inv.lookupOpener('amnesty:lookup', [
       onselect: { o->
           entity.amnesty = o;
           entity.amnesty.amount = (o.grantedoffer?.amount? o.grantedoffer.amount : 0);
           entity.borrower = o.borrower;
           binding.refresh('entity.(amnesty|borrower).*')
       },     
       state: 'APPROVED',
       type: 'AVAIL'
    ]);
    
    void afterOpen( data ) {
        if (data.txnstate!='DRAFT') allowEdit = false;
    }
    
    void submitForApproval() {
        if (!MsgBox.confirm("You are to submit this document for approval. Continue?")) return;
        
        entity = service.submitForApproval(entity);
        allowEdit = false;
        EventQueue.invokeLater({ caller?.reload(); });
    }
    
    def approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        
        return Inv.lookupOpener('remarks:create', [
            title: 'Remarks for approval', 
            handler: { remarks-> 
                try {                    
                    entity.posterremarks = remarks;
                    entity = service.approveDocument(entity);
                    EventQueue.invokeLater({ 
                        caller?.reload(); 
                        binding?.refresh();
                    });
                } catch (Throwable t) {
                    MsgBox.err(t.message);
                    //throw new Exception(t.message);
                }
            }
        ])
    }
    
    def disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        return Inv.lookupOpener('remarks:create', [
            title: 'Remarks for disapproval', 
            handler: { remarks-> 
                try {
                    entity.posterremarks = remarks;
                    entity = service.disapprove(entity);
                    EventQueue.invokeLater({ 
                        caller?.reload(); 
                        binding?.refresh();
                    });
                } catch (Throwable t) {
                    MsgBox.err(t.message);
                }
            }
        ])
    }
    
}

