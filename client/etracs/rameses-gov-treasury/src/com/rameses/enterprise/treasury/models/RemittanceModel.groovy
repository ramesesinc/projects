package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class RemittanceModel extends CrudFormModel {

    @Service("RemittanceService")
    def remSvc;    
    
    //this is passed 
    def handler;
    def selectedFund;
    def selectedAf;
    
    def numformat = new java.text.DecimalFormat("#,##0.00"); 
    
    def getFormattedAmount() {
        if ( !(entity.amount instanceof Number )) {
            entity.amount = 0.0; 
        } 
        return numformat.format( entity.amount );  
    }
    
    def getTotalNoncash() {
        return (entity.totalcheck + entity.totalcr);
    }
    
    def fundSummaryHandler = [
        fetchList: { o->
            def m = [_schemaname: 'remittance_fund' ];
            m.findBy = [ remittanceid: entity.objid ];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            return viewFundEntry();
        }
    ] as BasicListModel;
    
    def viewFundEntry() {
        if(!selectedFund) throw new Exception("Please select a fund");
        return Inv.lookupOpener("remittance_fund:open", [entity : selectedFund]  );
    }
    
    def updateCash() {
        if(!selectedFund) throw new Exception("Please select a fund");
        if(selectedFund.balance == 0 && selectedFund.totalcash==0 ) 
            throw new Exception("There is no cash remittance for selected item");
        def total = selectedFund.amount - (selectedFund.totalcheck + selectedFund.totalcr);
        def p = [total: total, cashbreakdown: entity.cashbreakdown ];
        p.handler = { o->
            def u = [objid:selectedFund.objid, remittanceid: entity.objid, totalcash: o.total, cashbreakdown: o.cashbreakdown ]; 
            remSvc.updateCash( u );
            fundSummaryHandler.reload();
            binding.refresh();
        }
        return Inv.lookupOpener("cashbreakdown", p );
    }
    
    def afSummaryHandler = [
        fetchList: { o->
           def m = [_schemaname: 'vw_remittance_cashreceipt_afsummary' ];
           m.findBy = [ remittanceid: entity.objid ];
           def list = queryService.getList( m ); 
           list.each{ 
               it.amount = it.amount - it.voidamt; 
           }
           return list; 
        },
        onOpenItem: {o,col->
            return viewReceipts();
        }
    ] as BasicListModel;
    
    def viewReceipts() {
        if(!selectedAf) throw new Exception("Please select an entry");  
        def o = selectedAf;
        def p = [:];
        p.put( "query.afcontrolid", o.controlid );
        p.put( "query.fromseries", o.fromseries );
        p.put( "query.toseries", o.toseries );
        return Inv.lookupOpener("cashreceipt_list:afseries", p );
    }
    
    def checkModel = [
        fetchList: { o->
            def m = [_schemaname: 'cashreceiptpayment_noncash' ];
            m.select = "refid,refno,reftype,refdate,particulars,amount:{SUM(amount)}";
            m.groupBy = "refid,refno,reftype,refdate,particulars";
            m.orderBy = "refdate,refno";
            m.where = [ "receipt.remittanceid = :rid AND amount > 0", [rid: entity.objid ]];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("paymentcheck:open", [entity: [objid: o.refid ]] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    //for printing
    def getPrintFormData() { 
        return remSvc.getReportData([ objid: entity.objid ]);
    } 
    
    /*
    def openPreview() {
        println 'open preview';
        open();
        return preview("remittance:form_report");
    }
    */
    
    void remit() {
        if ( MsgBox.confirm('You are about to submit this for liquidation. Proceed?')) {
            entity = remSvc.submitForLiquidation( entity ); 
        }
    }
    
}    