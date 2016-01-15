package com.rameses.clfc.ledger.adjustment

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanLedgerAdjustmentController extends CRUDController 
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    String serviceName = "LoanLedgerAdjustmentService";
    String entityName = "ledgeradjustment";
    
    boolean allowApprove = false;
    boolean allowDelete = false;
    boolean allowEdit = false;
    
    Map createPermission = [domain: 'LOAN', role: 'CAO_OFFICER'];
    Map editPermission = [domain: 'LOAN', role: 'CAO_OFFICER'];
    
    def ledger, borrower, modifydate = false, modifyamount = false;
    def borrowerLookupHandler = Inv.lookupOpener('ledgerborrower:lookup', [
        onselect: { o->
            entity.ledgerid = o.objid;
            entity.borrower = o.borrower;
        }
    ]);
    
    def newamount, newdate, ismodify = false;
    
    Map createEntity() {
        return [
            objid       : 'LLA' + new UID(),
            txnstate    : 'DRAFT',
            ledgerid    : ledger?.objid,
            borrower    : ledger?.borrower,
            requesttype : 'ADJUSTMENT'
        ];
    }
    
    //def getMode() {
    //    if (ismodify) return 'modify';
    //    return mode;
    //}
    
    void afterOpen( data ) {
        //if (data.txnstate != 'APPROVED') allowEdit = false;
        //binding?.refresh('formActions');
        if (data.newamount) newamount = data.newamount;
        if (data.newdate) newdate = data.newdate;
        binding?.refresh();
    }
    
    void approveDocument() {
        if (!MsgBox.confirm("You are about to approve this document. Continue?")) return;
        entity = service.approveDocument(entity);
        //if (entity.txnstate=='APPROVED') allowEdit = true;
        caller?.reload();
        binding?.refresh('formActions');
    }
    
    void disapprove() {
        if (!MsgBox.confirm("You are about to disapprove this document. Continue?")) return;
        
        entity = service.disapprove(entity);
        caller?.reload();
        binding?.refresh('formActions');
    }
    
    //void afterSave( data ) {
    //    if (data.txnstate != 'APPROVED') allowEdit = false;
    //   binding?.refresh('formActions');
    //}
    
    def requestForDelete() {
        def handler = { remarks->
            try {
                entity = service.requestForDelete([objid: entity.objid, remarks: remarks]);
                //allowEdit = false;
                EventQueue.invokeLater({
                    caller?.reload();
                    binding?.refresh('formActions');
                });
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        }
        return Inv.lookupOpener("remarks:create", [title: "Reason for Delete", handler: handler]);
    }
    
    void modify() {
        //ismodify = true;
        mode = 'modify';
        entity.requesttype = 'MODIFY';
        entity.txnstate = 'DRAFT';
        newdate = entity.txndate;
        newamount = null;
    }
    
    //void beforeCancel( data ) {
    //    if (data.requesttype == 'MODIFY') {
    //        ismodify = false;
    //    }
        //println 'data ' + data;
    //}
    
    def cancel() {
        if (mode != 'modify') {
            return super.cancel();
        } else {
            mode = 'read';
            modifydate = false;
            modifyamount = false;
            entity.requesttype = 'ADJUSTMENT';
            entity.txnstate = 'APPROVED';
            return null;
        }
    }
    
    def save() {
        if (mode != 'modify') {
            return super.save();
        } else {
            if (!modifydate && !modifyamount)
                throw new Exception("Please select a field to modify.");
                
            def handler = { remarks->
                try {
                    def params = [
                        objid           : entity.objid,
                        modifydate      : modifydate,
                        newdate         : newdate,
                        modifyamount    : modifyamount,
                        newamount       : newamount,
                        modifyremarks   : remarks
                    ];
                    entity = service.update(params);
                    //entity = service.requestForDelete([objid: entity.objid, remarks: remarks]);
                    //allowEdit = false;
                    mode = 'read';
                    modifyamount = false;
                    modifydate = false;
                    EventQueue.invokeLater({
                        caller?.reload();
                        binding?.refresh();
                    });
                } catch (Throwable t) {
                    MsgBox.err(t.message);
                }
            }
            return Inv.lookupOpener("remarks:create", [title: "Reason for Modification", handler: handler]);
            //return null;
        }
    }
    
    def viewModifyRemarks() {
        return Inv.lookupOpener("remarks:open", [title: "Reason for Modification", remarks: entity.modifyremarks]);
    }
    
    def viewDeleteRequest() {
        return Inv.lookupOpener("remarks:open", [title: "Reason for Delete", remarks: entity.deleteremarks]);
    }
    
    void approveDelete() {
        if (!MsgBox.confirm("You are about to approve delete request for this document. Continue?")) return;
        
        entity = service.approveDelete(entity);
        caller?.reload();
        binding?.refresh('formActions');
    }
    
    void disapproveDelete() {
        if (!MsgBox.confirm("You are about to disapprove delete request for this document. Continue?")) return;
        
        entity = service.disapproveDelete(entity);
        //if (entity.txnstate=='APPROVED') allowEdit = true;
        caller?.reload();
        //binding?.refresh('formActions');
        
    }
}

