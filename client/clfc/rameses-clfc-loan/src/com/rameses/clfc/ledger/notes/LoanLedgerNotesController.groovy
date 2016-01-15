package com.rameses.clfc.ledger.notes

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class LoanLedgerNotesController 
{
    @Service('LoanLedgerNoteService')
    def service;

    def entity;
    void open() {
        entity = service.open(entity);
        buildLedgerInfo();
    }
    
    def close() {
        return '_close';
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
                <td>&nbsp;:&nbsp;&nbsp;&nbsp;${entity.loanapp.appno}</td>
            </tr>
            <tr>
                <td valign="top"><b>Borrower Name</b></td>
                <td>&nbsp;:&nbsp;&nbsp;&nbsp;${entity.borrower.name}</td>
            </tr>
            <tr>
                <td valign="top"><b>Borrower Address</b></td>
                <td>&nbsp;:&nbsp;&nbsp;&nbsp;${entity.borrower.address}</td>
            </tr>
            <tr>
                <td valign="top"><b>Route</b></td>
                <td>&nbsp;:&nbsp;&nbsp;&nbsp;${entity.route.code} - ${entity.route.area}</td>
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
}

