package com.rameses.gov.etracs.rpt.collection.ui;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;


class RPTReceiptNoLedgerController extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt 
{
    @Binding
    def binding;
    
    @Service('RPTReceiptService')
    def svc;
    
    @Service('RPTBillingService')
    def billSvc;
    
    @Service('ReportParameterService')
    def paramSvc 

    @Service('DateService')
    def dtSvc 
    
    def MODE_CREATE         = 'create';
    def MODE_READ           = 'read';
        
    
    def ledgerfaases;
    def selectedLedgerFaas;
    def bill;
    def mode;
    
    
    void init(){
        super.init();
        ledgerfaases = [];
        entity.txntype = 'noledger';
        entity.amount = 0.0;
        clearAllPayments();
        bill = billSvc.initBill(null);
        def d = dtSvc.parseDate( entity.receiptdate, null) 
        bill.billdate = d.date
        mode = MODE_CREATE;
    }
    
    
    public void validateBeforePost() {
        entity.ledgerfaases = ledgerfaases
    }
    
    
    def listHandler = [
        fetchList : { return ledgerfaases },
    ] as BasicListModel
    
   
    void calcReceiptAmount(){
        entity.amount = ledgerfaases.total.sum();
        if (entity.amount == null) 
            entity.amount = 0.0
        updateBalances();
        binding.refresh('totalGeneral|totalSef')
    }
        
    def addLedger(){
        return InvokerUtil.lookupOpener('noledger:create', [
            bill        : bill,
            onAdd       : {ledgerfaas ->
                ledgerfaas.basicnet = ledgerfaas.items.basicnet.sum()
                ledgerfaas.sefnet = ledgerfaas.items.sefnet.sum()
                ledgerfaas.general = ledgerfaas.basicnet
                ledgerfaas.total = ledgerfaas.items.total.sum() 
                ledgerfaases << ledgerfaas;
                listHandler.reload();
                calcReceiptAmount();
            },
        ])
    }
        
    
    void removeLedger(){
        if ( MsgBox.confirm('Delete selected item?') ){
            ledgerfaases.remove(selectedLedgerFaas);
            listHandler.reload();
            calcReceiptAmount();
        }
    }
    
    
    
    def printDetail(){
        return InvokerUtil.lookupOpener('rptreceipt:printdetail',[entity:entity])
    }
    

        
    
    def getTotalGeneral(){
        return ledgerfaases.general.sum();
    }
    
    def getTotalSef(){
        return ledgerfaases.sefnet.sum()
    }
    
    
}

