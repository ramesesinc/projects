package com.rameses.gov.etracs.rpt.collection.ui;


import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.reports.*;
import com.rameses.gov.etracs.rpt.common.*;


class RPTReceiptCompromiseController extends com.rameses.enterprise.treasury.cashreceipt.AbstractCashReceipt 
{
    @Binding
    def binding;
    
    @Service('RPTReceiptService')
    def svc;
    
    @Service('ReportParameterService')
    def paramSvc 

    @Service('DateService')
    def dtSvc 
    
    def MODE_CREATE         = 'create';
    def MODE_READ           = 'read';
        
    def mode;
    def opener;
    def compromise;
    
    
    void init(){
        super.init();
        entity.txntype = 'rptcompromise';
        entity.amount = 0.0;
        entity.rptitems = [];
        mode = MODE_CREATE;
    }
    
    def getOpener(){
        if (! compromise)
            return null;
        entity.compromise = compromise;
        if (compromise.state == 'FORPAYMENT')
            return InvokerUtil.lookupOpener('rptcompromise:forpayment', [:])
        return InvokerUtil.lookupOpener('rptcompromise:forinstallment', [:])
    }
        
    void calcReceiptAmount(){
        if (entity.amount == null) 
            entity.amount = 0.0
        updateBalances();
    }
     
    
    public void validateBeforePost() {
        super.validateBeforePost();
        if (!entity.compromise)
            throw new Exception('Compromise No. is required.');
    }
   
    
    def printDetail(){
        return InvokerUtil.lookupOpener('rptreceipt:printdetail',[entity:entity])
    }
    
    
    def getLookupCompromise(){
        return InvokerUtil.lookupOpener('rptcompromise:lookup',[
            onselect : {
                if ( it.state == 'DRAFT') 
                    throw new Exception('Compromise record is not yet approved.')
                if ( it.state == 'DEFAULTED') 
                    throw new Exception('Compromise record has already been defaulted.')
                if ( it.state == 'CLOSED') 
                    throw new Exception('Compromise record has already been closed.')
                    
                compromise = it;
                entity.payer = it.taxpayer 
                entity.paidby = it.taxpayer.name 
                entity.paidbyaddress = it.taxpayer.address.replaceAll('[^\\S]',' ')
                binding.refresh('entity.payer|entity.paidby.*');
            },
            onempty: {
                compromise = null;
            }
        ])
    }    
}

