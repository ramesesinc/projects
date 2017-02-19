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

    @Binding 
    def binding; 
    
    String entityName = "businessinfo:ledger"
    String title = "Business Ledger";

    def entity;
    def selectedApplication;
    def selectedReceivable;
    def selectedPayment;
    def selectedTaxCredit;

    def applications;
    def receivables;

    void init() { 
        def selappid = selectedApplication?.objid; 
        applications = appService.getList([ businessid: entity.objid ]).sort{ it.appyear*-1 } 
        if ( selappid ) {
            selectedApplication = applications?.find{ it.objid==selappid }
        } else {
            selectedApplication = null; 
        }
    } 
    
    def printBill() {
        if( selectedApplication == null )
            throw new Exception("Please select an application");
        
        def m = [:];    
        m.putAll( selectedApplication );
        m.receivables = receivables; 
        return Inv.lookupOpener( "business:billing", [ entity: m ]);
    }    
    
    def applicationListModel = [
        fetchList: { o-> 
            return applications; 
        } 
    ] as BasicListModel;

    void refresh() {
        init(); 
        applicationListModel.reload(); 
    } 

    def capturePayment() { 
        if ( !selectedApplication ) 
            throw new Exception('Please select an application first'); 

        def params = [:];
        params.entity = [ 
            objid    : selectedApplication.objid,
            appno    : selectedApplication.appno,              
            apptype  : selectedApplication.apptype, 
            appyear  : selectedApplication.appyear, 
            dtfiled  : selectedApplication.dtfiled, 
            business : selectedApplication.business 
        ]; 
        params.handler = { 
            refresh(); 
        } 
        return Inv.lookupOpener('business_payment:capture', params); 
    } 
    
    boolean isCanRemovePayment() {
        if ( selectedPayment == null ) return false; 

        def reftype = selectedPayment.reftype.toString().toLowerCase(); 
        try { 
            return ( Inv.lookup('business_payment:'+ reftype +':remove') ? true : false ); 
        } catch(Throwable t) {  
            return false; 
        }  
    }
    def removePayment() { 
        if ( selectedPayment == null ) return null; 
        if ( !isCanRemovePayment()) return null;
        if ( !MsgBox.confirm('You are about to remove the selected payment. Continue?')) return null; 

        def reftype = selectedPayment.reftype.toString().toLowerCase(); 
        if ( reftype == 'capture' ) {
            pmtService.removePayment([ paymentid : selectedPayment.objid ]); 
            refresh(); 
            return null; 

        } else { 
            def params = [ entity: selectedPayment, handler: { refresh(); }];  
            return Inv.lookupOpener('business_payment:'+ reftype +':remove', params);  
        } 
    } 

    boolean isCanViewPayment() {
        if ( selectedPayment == null ) return false; 

        def reftype = selectedPayment.reftype.toString().toLowerCase(); 
        if ( reftype == 'cashreceipt' ) return true; 

        try { 
            return ( Inv.lookup('business_payment:'+ reftype +':view') ? true : false ); 
        } catch(Throwable t) {  
            return false; 
        }  
    }    
    def viewPayment() {
        if ( !selectedPayment ) return null;
        if ( !isCanViewPayment()) return null; 

        def reftype = selectedPayment.reftype.toString().toLowerCase(); 
        if ( reftype == 'cashreceipt' ) {
            def op = Inv.lookupOpener( "cashreceiptinfo:open", [entity: [ objid: selectedPayment.refid]]);
            op.target = 'popup';
            return op;
        } 

        throw new Exception('No available handler for this type of receipt'); 
    }     
    
    def totalamtdue = 0.0; 
    def totalamtpaid = 0.0;
    def totalbalance = 0.0;
    def receivableModel = [
        fetchList: { o->
            if ( selectedApplication ) {
                receivables = service.getAllReceivables([ applicationid: selectedApplication.objid ]);
            } else {
                receivables = []; 
            } 

            totalamtdue = totalamtpaid = totalbalance = 0.0;  
            receivables.each{
                totalamtdue += (it.amount? it.amount: 0.0);
                totalamtpaid += (it.amtpaid? it.amtpaid: 0.0);
                totalbalance += (it.balance? it.balance: 0.0);
            } 
            binding.refresh('totals');
            return receivables;
        }
    ] as BasicListModel;

    def receivablePaymentModel = [
        fetchList: { o-> 
            if ( !selectedReceivable ) return [];
            return service.getReceivablePayments([ receivableid: selectedReceivable.objid ]);
        }
    ] as BasicListModel;


    def addReceivable() {
        if ( !selectedApplication )
            throw new Exception('Please select an application first'); 
        if ( !selectedApplication.txnmode.toString().matches('CAPTURE|LATERENEWAL')) 
            throw new Exception('This transaction is only applicable for captured and late renewal applications'); 
        
        def params = [:];  
        params.handler = { receivableModel.reload(); }
        params.parent = [ 
            apptype : selectedApplication.apptype, 
            activeyear : selectedApplication.appyear, 
            applicationid : selectedApplication.objid, 
            businessid : selectedApplication.business?.objid, 
            lobs : entity.lobs?.collect{[ objid:it.lobid, name:it.name ]}  
        ]; 
        return Inv.lookupOpener( "business_receivable:add", params ); 
    } 

    boolean isCanEditReceivable() {
        if ( !selectedReceivable ) return false; 
        return selectedApplication?.txnmode.toString().toUpperCase().matches('CAPTURE|LATERENEWAL'); 
    } 
    def doEditReceivable() {
        if ( !selectedReceivable ) throw new Exception('Please select an item first'); 
        if ( !isCanEditReceivable()) return null; 
        
        def params = [ entity: selectedReceivable ]; 
        params.handler = { receivableModel.reload(); } 
        params.parent = [
            applicationid : selectedApplication.objid, 
            businessid    : selectedApplication.business?.objid, 
            activeyear    : selectedApplication.appyear, 
            apptype       : selectedApplication.apptype, 
            lobs          : entity.lobs?.collect{[ objid:it.lobid, name:it.name ]} 
        ];
        return Inv.lookupOpener( "business_receivable:edit", params );
    }

    def doRemoveReceivable() {
        if ( !selectedReceivable ) throw new Exception('Please select an item first'); 
        if ( !isCanEditReceivable()) return null; 

        if ( !MsgBox.confirm('You are about to remove this item. Continue?') ) return null;
        
        def params = [ entity: selectedReceivable ]; 
        params.handler = { receivableModel.reload(); }
        return Inv.lookupOpener( "business_receivable:remove", params); 
    } 

    def numFormatter = new java.text.DecimalFormat('#,##0.00'); 

    def totalpayment = 0.0; 
    def paymentsModel = [
        fetchList: { o-> 
            def list = null; 
            if ( selectedApplication ) {
                list = pmtService.getApplicationPayments([ applicationid: selectedApplication.objid ]); 
                totalpayment = list.sum{( it.amount ? it.amount: 0.0 )} 
            } 
            
            totalpayment = ( totalpayment ? totalpayment : 0.0 ); 
            return list;  
        },
        onOpenItem: { o, col-> 
            return viewPayment(); 
        }
    ] as BasicListModel;


    def totaltaxcredit = 0.0; 
    def taxcreditModel = [ 
        fetchList: { o-> 
            def list = null; 
            if ( entity?.objid ) { 
                list = taxCreditService.getList([ businessid: entity.objid ]); 
                def dr = list.sum{( it.dr ? it.dr : 0.0 )} 
                def cr = list.sum{( it.cr ? it.cr : 0.0 )} 
                totaltaxcredit = (dr ? dr : 0.0)-(cr ? cr : 0.0);  
            } 

            totaltaxcredit = ( totaltaxcredit ? totaltaxcredit : 0.0 ); 
            return list; 
        }  
    ] as BasicListModel;

    void sendSMS() { 
        billSvc.sendSMS([ businessid: entity.objid, applicationid: selectedApplication?.objid ]); 
        MsgBox.alert('Message successfully sent'); 
    }  
}