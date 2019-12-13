package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class FAASPaymentInfoModel
{
    @Binding
    def binding;

    @Service('QueryService')
    def qrySvc;

    @Service('RPTLedgerPaymentService')
    def svc; 
    
    def entity;
    def selectedItem;
    def payments;

    String title = 'List of Payments'
    
    void init(){
        payments = svc.getPayments(getLedger());
    }

    def getLedger() {
        def filter = [_schemaname: 'rptledger'];
        filter.select = 'objid';
        filter.findBy = [faasid: entity.objid];
        def ledger = qrySvc.findFirst(filter);
        if (!ledger) {
            filter.findBy = [tdno: entity.tdno];
            ledger = qrySvc.findFirst(filter);
        }
        if (!ledger) {
            throw new Exception('No payments found for this FAAS.');
        }
        return ledger;
    }
    
    def listHandler = [
        fetchList :{ payments }
    ] as BasicListModel
    
    
    void refresh(){
        init();
        listHandler.reload();
    }
    
    void viewDetails(){
    }
    
}