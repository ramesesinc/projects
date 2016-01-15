package com.rameses.clfc.loan.exemption;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

class NewExemptionController
{
    @Service('LoanExemptionService')
    def svc;
    
    @Binding
    def binding;
    
    @Caller
    def caller;
    
    def entity;
    def selectedItem;
    def mode = 'read';
    
    void init() {
        mode = 'create';
        entity = [
            objid: 'EXMPT' + new java.rmi.server.UID(), 
            ledgers:[] 
        ]; 
    }
    
    def close() {
        return '_close'; 
    }
    
    def cancelCreate() {
        if (MsgBox.confirm('You are about to close this window. Continue?')) {
            return '_close'; 
        } else { 
            return null; 
        }
    }
    
    void saveCreate() { 
        def newentity = svc.batchCreate(entity); 
        if (newentity) entity.putAll(newentity);
        
        mode = 'read';
        binding.focus('entity.dtstart'); 
        binding.refresh(); 
        MsgBox.alert('Transaction successfully saved'); 
    }  
    
    void submitForApproval() {
        if (MsgBox.confirm('You are about to submit this transaction for approval. Continue?')) {
            def result = svc.batchSubmitForApproval([items: entity.ledgers]);
            entity.state = result?.state;
            
            EventQueue.invokeLater({ if (caller) caller.reload(); }); 
            binding.refresh(); 
            MsgBox.alert('Transaction successfully submitted'); 
        } 
    } 
    
    def addLedger() {
        def params = [
            onselect: {o-> 
                def results = entity.ledgers.findAll{ (it.borrower.objid==o.borrower.objid) } 
                if (results) throw new Exception('The selected borrower already exist in the list');
                    
                entity.ledgers << [
                    ledger : [
                        objid : o.objid
                    ], 
                    route : [ 
                        code : o.route?.code, 
                        area : o.route?.area, 
                        description : o.route?.description
                    ], 
                    loanapp: [
                        objid  : o.loanapp?.objid,
                        appno  : o.loanapp?.appno
                    ],  
                    borrower: [ 
                        objid  : o.borrower.objid, 
                        name   : o.borrower.name, 
                        address: o.borrower.address 
                    ]
                ]; 
                binding.focus('addLedger'); 
                binding.refresh(); 
            } 
        ];
        return Inv.lookupOpener('ledgerborrower:lookup', params);
    }
    
    void removeLedger() {
        entity.ledgers.remove(selectedItem); 
        selectedItem = null; 
        binding.focus('selectedItem');
        binding.refresh();
    }
    
    def getLedgerInfo() {
        if (!selectedItem) return ''; 
        
        return """ 
            <table cellpadding="0" cellspacing="0">
            <tr>
                <td valign="top"><b>Loan App.No</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${selectedItem.loanapp.appno}</td>
            </tr>
            <tr>
                <td valign="top"><b>Borrower Name</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${selectedItem.borrower.name}</td>
            </tr>
            <tr>
                <td valign="top"><b>Borrower Address</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${selectedItem.borrower.address}</td>
            </tr>
            <tr>
                <td valign="top"><b>Route</b></td>
                <td valign="top">&nbsp;:&nbsp;&nbsp;&nbsp;</td>
                <td>${selectedItem.route.code} - ${selectedItem.route.area}</td>
            </tr>                        
            </table>
        """;
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