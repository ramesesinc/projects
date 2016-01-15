package com.rameses.clfc.patch.payment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class PaymentTransferController
{
    @Binding
    def binding;

    @Service("PaymentTransferService")
    def service;
    
    @PropertyChangeListener
    def listener = [
        'entity.fromdate': { o->
            refreshPaymentList();
        },
        'entity.todate': { o->
            refreshPaymentList();
        }
    ];
    
    String title = 'Payment Transfer';

    def current;
    def receiving;
    def entity;
    //def sourceLookupHandler = Inv.lookupOpener("payment_ledger:lookup", [:]);
    
    void init() {
        current = [:];
        receiving = [:];
        entity = [:];
        payments = [];
    }
    
    def getSourceLookupHandler() {
        def handler = { o->
            if (receiving?.borrower?.objid == o?.borrower?.objid && receiving?.loanapp?.objid == o?.loanapp?.objid) {
                throw new Exception('Source should not be the same as receiver.');
            }
            
            current = o;
            binding?.refresh();
            refreshPaymentList();
        }
        def op = Inv.lookupOpener('payment_ledger:lookup', [onselect: handler]);
        if (!op) return null;
        return op;
    }

    def getReceivingLookupHandler() {
        //def params = [ borrowerid: current?.borrower.objid];
        def handler = { o->
            if (current?.borrower.objid == o.borrower.objid && current?.loanapp.objid == o.loanapp.objid) {
                throw new Exception("Receiver should not be the same as source.");
            }
            
            receiving = o;
            binding.refresh();
        };
        def op = Inv.lookupOpener("payment_ledger:lookup", [onselect: handler]);
        if (!op) return null;
        return op;
    }
    
    def payments, selectedPayment;
    def listHandler = [
        fetchList: {
            if (!payments) payments = [];
            return payments;
        }
    ] as BasicListModel;
    
    void removePayment() {
        if (!MsgBox.confirm('You are about to remove this payment. Continue?')) return;
        
        payments?.remove(selectedPayment);
        listHandler?.reload();
    }
    
    void refreshPaymentList() {
        payments = [];
        if (entity.fromdate && entity.todate) {
            def params = [
                fromdate: entity.fromdate, 
                todate  : entity.todate,
                ledgerid: current?.objid
            ];
            payments = service.getPayments(params);
        }
        
        listHandler?.reload();
    }
    

    def transfer() {
        String msg = "<html>" +
                    "You are about to transfer payment(s) <br/>Source: <br/>" + 
                    "App. No.: <b>" + current.loanapp.appno + "</b> <br/>" +
                    "Loan Amount: <b>" + current.loanapp.loanamount + "</b> <br/>" +
                    "Borrower: <b>" + current.borrower.name + "</b>" +
                    "<br/> <br/> Receiving: <br/>" +
                    "App. No.: <b>" + receiving.loanapp.appno + "</b> <br/>" + 
                    "Loan Amount: <b>" + receiving.loanapp.loanamount + "</b> <br/>" +
                    "Borrower: <b>" + receiving.borrower.name + "</b>" +
                    "</html>";
        
        if (!MsgBox.confirm(msg)) return;
        
        entity.current = current;
        entity.receiving = receiving;
        entity.payments = payments;
        service.transfer(entity);
        MsgBox.alert("Payments successfully transferred.");
        init();
        binding.refresh("entity.remarks");
        listHandler?.reload();
    }

    def close() {
        return "_close";
    }
}