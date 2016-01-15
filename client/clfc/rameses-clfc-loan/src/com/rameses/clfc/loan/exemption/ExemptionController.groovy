package com.rameses.clfc.loan.exemption;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*

class ExemptionController 
{
    @Binding 
    def binding;
    
    @Caller
    def caller;
    
    @Service('LoanExemptionService')
    def service;

    def title = 'Exemption';
    def listModelHandler;
    def mode = 'read';     
    def entity = [:];
    
    def env = com.rameses.rcp.framework.ClientContext.currentContext.headers;
       
    void create() {
        mode = 'create';  
        entity = [objid: 'EXMPT' + new java.rmi.server.UID()];
    } 
    
    void open() { 
        mode = 'read'; 
        def newentity = service.open([objid: entity.objid]); 
        if (newentity) entity.putAll(newentity); 
        
        buildLedgerInfo();
    }    
    
    def closeForm() {
        return '_close'; 
    }
        
    void edit() { 
        if (entity.author?.username.toString().equalsIgnoreCase(env.USER.toString())) { 
            mode = 'edit'; 
            binding.focus('entity.dtstart');
        } else { 
            throw new Exception('You are not allowed to edit this transaction. The owner of this transaction is ' + env.NAME); 
        } 
    } 
    
    void cancelUpdate() {
        mode = 'read'; 
    }
    
    void saveUpdate() {
        if (!MsgBox.confirm('You are about to update this transaction. Continue?')) return;
        
        def newentity = service.update(entity);  
        if (newentity) entity.putAll(newentity);

        mode = 'read';        
        MsgBox.alert("Transaction successfully updated"); 
        EventQueue.invokeLater({ caller.reload(); }); 
    }
    
    def delete() {
        if (!MsgBox.confirm('You are about to delete this transaction. Continue?')) return;
        
        def handler = { remarks->
            try {
                service.removeEntity([objid: entity.objid, remarks: remarks]); 
                EventQueue.invokeLater({ caller?.reload(); }); 
                binding.fireNavigation('_close'); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Deletion', handler: handler]); 
    }
    
    void submitForApproval() {
        if (MsgBox.confirm('You are about to submit this transaction for approval. Continue?')) {
            entity = service.submitForApproval([objid: entity.objid]);
            EventQueue.invokeLater({ caller.reload(); }); 
            binding.refresh(); 
        } 
    }
    
    def approve() {
        if (!MsgBox.confirm('You are about to approve this transaction. Continue?')) return;
        
        def handler = { remarks-> 
            try {
                entity = service.markAsApproved([objid: entity.objid, remarks: remarks]);
                EventQueue.invokeLater({ caller?.reload(); }); 
                binding.refresh(); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Approval', handler: handler]); 
    }
    
    void disapprove() {
        if (MsgBox.confirm('You are about to disapprove this transaction. Continue?')) {
            entity = service.markAsDisapproved([objid: entity.objid]);
            EventQueue.invokeLater({ caller?.reload(); }); 
            binding.refresh(); 
        } 
    }
    
    def reject() {
        if (!MsgBox.confirm('You are about to reject this transaction. Continue?')) return;
        
        def handler = { remarks-> 
            try {
                entity = service.markAsRejected([objid: entity.objid, remarks: remarks]); 
                EventQueue.invokeLater({ caller?.reload(); }); 
                binding.refresh(); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Rejection', handler: handler]); 
    } 
    
    boolean isTerminateActionVisible() {
        return (mode=='read' && entity.state=='APPROVED' && !entity.dtend);
    }

    def submitForTermination() {
        if (!MsgBox.confirm('You are about to submit this transaction for termination. Continue?')) return;
        
        def handler = { remarks-> 
            try {
                entity = service.submitForTermination([objid: entity.objid, remarks: remarks]); 
                EventQueue.invokeLater({ caller?.reload(); }); 
                binding.refresh(); 
            } catch (Throwable t) {
                MsgBox.err(t.message);
            }
        } 
        return Inv.lookupOpener('remarks:create', [title: 'Remarks for Termination', handler: handler]); 
    } 
    
    def viewLogs() {
        return Inv.lookupOpener('txnlog:open', [query: [txnid: entity.objid]]); 
    }
    
    def ledgerinfo;
    def borrowerLookup = Inv.lookupOpener('ledgerborrower:lookup', [
        onselect: {o-> 
            entity.ledger = [objid: o.objid];
            entity.route = [
                code: o.route?.code, 
                area: o.route?.area, 
                description: o.route?.description
            ]; 
            entity.loanapp = [
                objid:      o.loanapp?.objid,
                appno:      o.loanapp?.appno
            ]; 
            entity.borrower = [
                objid:      o.borrower.objid, 
                name:       o.borrower.name, 
                address:    o.borrower.address 
            ];
            buildLedgerInfo();
            binding.refresh();
        }
    ]);
    
    void buildLedgerInfo() {
        ledgerinfo = """ 
            <table cellpadding="0" cellspacing="0">
            <tr>
                <td valign="top"><b>Loan App.No</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${entity.loanapp.appno}</td>
            </tr>
            <tr>
                <td valign="top"><b>Borrower Name</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${entity.borrower.name}</td>
            </tr>
            <tr>
                <td valign="top"><b>Borrower Address</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${entity.borrower.address}</td>
            </tr>
            <tr>
                <td valign="top"><b>Route</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${entity.route.code} - ${entity.route.area}</td>
            </tr>                        
            </table>
        """;
    }
    
    def getSelectedForm() {
        if (entity.state == 'APPROVED' || entity.state=='CLOSED') {
            return 'approveform';
        } else if (entity.state == 'PENDING') {
            return 'emptyform';
        } else if (mode != 'create') {
            return 'otherform';
        } else {
            return 'emptyform';
        }
    } 
    
    def exemptTypeLookup = Inv.lookupOpener('exemptiontype:lookup', [
        onselect: {o-> 
            entity.type = [ 
                objid       : o.objid, 
                code        : o.code, 
                name        : o.name, 
                description : o.description 
            ]; 
            binding.refresh(); 
        } 
    ]);    
}
