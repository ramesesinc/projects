package com.rameses.clfc.loan.payment.voidrequest;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class PaymentVoidRequestController extends CRUDController
{
    String serviceName = 'LoanVoidRequestService';
    String entityName = 'voidrequest';

    boolean allowCreate = false;
    boolean allowEdit = false;
    boolean allowDelete = false;

    def route, collectionsheet, payment;
    def collector, collectionid, txncode;
    def afterApproveHandler, afterDisapproveHandler;
    def afterSaveHandler;

    Map createEntity() {
        return [
            objid       : 'PVR' + new UID(),
            state       : 'DRAFT',
            route       : route,
            paymentid   : payment.objid,
            payamount   : payment.amount,
            loanapp     : collectionsheet.loanapp,
            borrower    : service.openCustomer(collectionsheet.borrower),
            collector   : collector,
            txncode     : txncode,
            collectionid: collectionid
        ];
    }
    
    def getRoute() {
        if (!entity?.route) return '';
        return entity.route.description + ' ' + (entity.route.area? '- ' + entity.route.area : '');
    }

    void beforeSave(Object data) { 
        data.routecode = data.route.code;
    }

    void afterSave(Object data) {
        //println 'data after save = '+data;
        //println 'afterSaveHandler = '+afterSaveHandler;
        if (afterSaveHandler) afterSaveHandler(data);
    }

    void beforeOpen(Object data) {
        if (payment) {
            data.objid = payment.voidid;
        }
        data.txncode = txncode;
        //println 'data = '+data;
    }
    
    /*
    def approve() {
        super.approve();
        if (entity.state == 'APPROVED') {
            if (afterApproveHandler) afterApproveHandler(entity);
        }
    }
    */
    void approveDocument() {
        if (!MsgBox.confirm('You are about to approve this document. Continue?')) return;
        
        entity = service.approve(entity);
        if (afterApproveHandler) afterApproveHandler(entity);
    }

    void disapprove() {
        if (!MsgBox.confirm('You are about to disapprove this document. Continue?')) return;
        
        entity = service.disapprove(entity);
        if (afterDisapproveHandler) afterDisapproveHandler(entity);
    }
 }