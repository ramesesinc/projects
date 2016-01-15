package com.rameses.clfc.loan.fieldcollection

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class FieldCollectionLoanListController 
{
    @Binding
    def binding;
    
    @Service('LoanFieldCollectionService')
    def service;
    
    String title = '';
    
    def collectionid, entity, state, type;
    def searchtext, selectedPayment;
    
    void open() {
        entity = service.getFieldCollectionList([collectionid: collectionid]);
        state = entity?.state;
        collectionSheetsHandler.reloadAll();
    }
    
    def close() {
        return '_close';
    }
    
    void setSelectedPayment( selectedPayment ) {
        this.selectedPayment = selectedPayment;
        binding?.refresh('formActions');
    }
    
    def collectionSheetsHandler = [
        getColumnList: { return getColumns(); },
        fetchList: {o->
            if (!entity) entity = [:];
            if (!entity.payments) entity.payments = [];
            return entity.payments;
        }
    ] as BasicListModel;
    
    def getColumns() {
        def cols = [];
        if (type != 'route') cols << [name: 'route.name', caption: 'Route'];
        cols << [name: 'payment.state', caption: 'Status'];
        cols << [name: 'loanapp.appno', caption: 'App. No.'];
        cols << [name: 'loanapp.loanamount', caption: 'Loan Amount', type: 'decimal', format: '#,##0.00'];
        cols << [name: 'borrower.name', caption: 'Borrower'];
        cols << [name: 'payment.refno', caption: 'Ref. No.'];
        cols << [name: 'payment.amount', caption: 'Amount', type: 'decimal', format: '#,##0.00'];
        cols << [name: 'payment.dtpaid', caption: 'Date Paid'];
        cols << [name: 'payment.paidby', caption: 'Paid By'];
        cols << [name: 'payment.author.name', caption: 'Collected By'];
        cols << [name: 'remarks', caption: 'Remarks'];
        
        return cols;
    };
    
    void search() {
        def params = [
            searchtext  : searchtext,
            collectionid: collectionid
        ];
        entity.payments = service.getCollectionPaymentList(params);
        //entity.payment = fieldColSvc.getCollectionList([searchtext: searchtext, collectionid: route.objid]);
        collectionSheetsHandler.reload();
    }
}

