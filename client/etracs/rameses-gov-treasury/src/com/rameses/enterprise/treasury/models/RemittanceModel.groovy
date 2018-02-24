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
           def m = [_schemaname: 'remittance_af' ];
           m.findBy = [ remittanceid: entity.objid ];
           return queryService.getList( m );
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
        p.put( "query.fromseries", o.issuedstartseries );
        p.put( "query.toseries", o.issuedendseries );
        return Inv.lookupOpener("cashreceipt_list:afseries", p );
    }
    
    def checkModel = [
        fetchList: { o->
            def m = [_schemaname: 'cashreceiptpayment_noncash' ];
            m.select = "refno,reftype,refdate,amount:{SUM(amount)}";
            m.groupBy = "refno,reftype,refdate";
            m.where = [ "receipt.remittanceid = :rid", [rid: entity.objid ]];
            return queryService.getList( m );
        }
    ] as BasicListModel;
    
    //for printing
    def getPrintFormData() { 
        def rdata = remSvc.getReportData([ objid: entity.objid ]); 
        if ( rdata ) { 
            rdata.putAll( entity ); 
            rdata.totalnoncash = rdata.totalcheck + rdata.totalcr;
            rdata.remittancedate = rdata.controldate;
            rdata.txnno = rdata.controlno; 
        }         
        return rdata;
    } 
    
    def openPreview() {
        println 'open preview';
        open();
        return preview("remittance:form_report");
    }
    
    void remit() {
        if ( MsgBox.confirm('You are about to submit this for liquidation. Proceed?')) {
            entity = remSvc.submitForLiquidation( entity ); 
        }
    }
    
}    