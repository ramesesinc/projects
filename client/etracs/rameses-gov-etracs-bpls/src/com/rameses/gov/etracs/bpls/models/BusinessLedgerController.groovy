package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.*;

class BusinessLedgerController {

    @Service("BusinessApplicationHistoryService")
    def appService;

    @Service("BusinessReceivableService")
    def service;

    @Service("BusinessPaymentService")
    def pmtService;

    @Service("BusinessPaymentOrderService")
    def pmtOrderService;

    @Service("BusinessTaxCreditService")
    def taxCreditService;

    @Service("BusinessBillingService")
    def billSvc;     

    String entityName = "businessinfo:ledger"
    String title = "Business Ledger";

    def entity;
    def selectedReceivable;
    def selectedPayment;
    def selectedTaxCredit;


    def selectedApplication;
    def receivables;

    def applicationListModel = [
        fetchList: { o->
            return appService.getList( [businessid: entity.objid] ).sort{it.appyear*-1};
        },
    ] as BasicListModel;

    def receivableModel = [
        fetchList: { o->
            receivables = [];
            if(selectedApplication) {
                receivables = service.getAllReceivables([ applicationid: selectedApplication.objid ]);
            }
            return receivables;
        }
    ] as BasicListModel;

    def receivablePaymentModel = [
        fetchList: { o->
            if(!selectedReceivable) return [];
            return service.getReceivablePayments([ receivableid: selectedReceivable.objid ]);
        }
    ] as BasicListModel;


    def paymentsModel = [
        fetchList: { o->
            return pmtService.getList( [objid: entity.objid] ); 
        },
        onOpenItem: { o, col-> 
            return viewPayment(); 
            //return InvokerUtil.lookupOpener("business_payment:open", [ entity: [objid: o.objid] ]);
        }
    ] as BasicListModel;

    def viewPayment() {
        if ( !selectedPayment ) return;

        if ( selectedPayment.reftype == 'cashreceipt' ) {
            def op = Inv.lookupOpener( "cashreceiptinfo:open", [entity:[objid: selectedPayment.refid]] );
            op.target = 'popup';
            return op;
        }             
        throw new Exception('No available handler for this type of receipt'); 
    } 

    def taxcreditModel = [
        fetchList: { o->
            return taxCreditService.getList( [businessid: entity.objid]  );
        },
    ] as BasicListModel;

    void reload() {
        receivables = null;
        receivableModel.reload();
        paymentsModel.reload();
        taxcreditModel.reload();
    }

    def addPayment(def mode, def handler, def title) {
        def newItems = receivables.collect{ [
            objid: 'BPMTI'+new UID(),
            iyear: it.iyear,
            receivableid: it.objid,
            account: it.account,
            item: it.account,
            lob: it.lob,
            balance: it.balance,
            amtpaid: 0,
            discount: 0,
            surchargepaid: 0,
            interestpaid: 0,
            txntype:'basic'
        ] }
        return Inv.lookupOpener( "business_payment:add", [items: newItems, mode:mode, handler:handler, title: title] );
    }

    def requestPayment() {
        def h = { o->
            o.reftype = 'request';
            o.business = entity;
            def r = pmtOrderService.create( o );
            MsgBox.alert( 'Transaction id is ' + r.txnid );
        }
        return addPayment( 'request', h, 'Request Payment' ); 
    }

    def capturePayment() {
        def h = { o->
            o.reftype = 'capture';
            o.businessid = entity.objid;
            o.voided = 0;
            o.appyear = entity.activeyear;
            o.paymentmode = 'CAPTURE';
            pmtService.create( o );
            reload();
        }
        return addPayment( 'capture', h, 'Capture Payment' );
    }

    def payTaxCredit() {
        def h = { o->
            o.reftype = 'taxcreditpayment';
            o.businessid = entity.objid;
            pmtService.create( o );
            reload();
        }
        return addPayment( 'taxcredit', h, 'Add Tax Credit' );
    }

    def runBilling() {
        if( selectedApplication == null )
            throw new Exception("Please select an application");
        def m = [:];    
       m.putAll( selectedApplication );
       m.receivables = receivables;
       return Inv.lookupOpener( "business:billing", [entity: m] );
    }

    def addReceivable() {
        if ( !selectedApplication )
            throw new Exception('Please select an application first');
        if ( !selectedApplication.txnmode.toString().matches('CAPTURE|LATERENEWAL') ) 
            throw new Exception('This transaction is only applicable for captured and late renewal applications');
            
        def h = { receivableModel.reload(); }
        def e = [:];
        e.businessid = selectedApplication.business?.objid;
        e.applicationid = selectedApplication.objid;
        e.apptype = selectedApplication.apptype;
        e.activeyear = selectedApplication.appyear;
        e.lobs = entity.lobs?.collect{ [objid:it.lobid, name:it.name] };
        return Inv.lookupOpener( "business_receivable:add", [parent:e, handler:h] );
    }

    def doEditReceivable() {
        return editReceivable(); 
    }
    def editReceivable() { 
        if ( !selectedApplication || !selectedApplication.txnmode.toString().matches('CAPTURE|LATERENEWAL') ) 
            return null; 
            
        if ( !selectedReceivable ) throw new Exception('Please select a receivable'); 
        
        def h = { receivableModel.reload(); }
        def e = [:];
        e.businessid = selectedApplication.business?.objid;
        e.applicationid = selectedApplication.objid;
        e.apptype = selectedApplication.apptype;
        e.activeyear = selectedApplication.appyear;
        e.lobs = entity.lobs?.collect{ [objid:it.lobid, name:it.name] };        
        return Inv.lookupOpener( "business_receivable:edit", [
            entity: selectedReceivable, 
            parent: e, 
            handler: h
        ]);
    }

    def doRemoveReceivable() {
        return removeReceivable(); 
    }
    def removeReceivable() {
        if ( !selectedApplication || !selectedApplication.txnmode.toString().matches('CAPTURE|LATERENEWAL') ) 
            return null; 
        
        if ( !selectedReceivable ) throw new Exception('Please select a receivable'); 
        if ( !MsgBox.confirm('You are about to remove this receivable. Continue?') ) 
            return null;
        
        def h = { receivableModel.reload(); }
        return Inv.lookupOpener( "business_receivable:remove", [
            entity: selectedReceivable, 
            handler: h 
        ]); 
    } 

    void sendSMS() { 
        billSvc.sendSMS([ businessid: entity.objid, applicationid: selectedApplication?.objid ]); 
        MsgBox.alert('Message successfully sent'); 
    }      
}