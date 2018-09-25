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

    @Service("Var")
    def var;

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
    
    boolean getCashBreakdownByFund() {
        def b = var.getProperty("remittance_breakdown_byfund", "false" );
        if( b.equals("1")) b = "true";
        else if( b.equals("0")) b = "false";
        return Boolean.parseBoolean(b+"");
    }
    
    def updateCashByRemittance() {
        def p = [total: entity.amount - totalNoncash, cashbreakdown: entity.cashbreakdown ];
        p.handler = { o->
            def m = [_schemaname: 'remittance'];
            m.findBy = [objid: entity.objid];
            m.cashbreakdown = o.cashbreakdown;
            m.totalcash = o.cashbreakdown.sum{ it.amount };
            persistenceService.update( m );
            entity.cashbreakdown = m.cashbreakdown;
            entity.totalcash = m.totalcash;
            binding.refresh();
        }
        return Inv.lookupOpener("cashbreakdown", p );
    }
    
    def updateCashByFund() {
        if(!selectedFund) throw new Exception("Please select a fund entry");
        if(selectedFund.totalcash == 0  ) 
            throw new Exception("There is no cash remittance for selected item");
        def p = [total: selectedFund.totalcash, cashbreakdown: selectedFund.cashbreakdown ];
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
           m.orderBy = 'formno, startseries, stubno'; 
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
            def m = [_schemaname: 'vw_cashreceiptpayment_noncash' ];
            m.select = "refid,refno,reftype,refdate,particulars,amount:{SUM(amount)}";
            m.groupBy = "refid,refno,reftype,refdate,particulars";
            m.orderBy = "refdate,refno";
            m.where = [ "remittanceid = :rid AND voided=0 AND amount > 0", [rid: entity.objid ]];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("checkpayment:open", [entity: [objid: o.refid ]] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
   
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
    
    def popupReports( inv ) {
        def popupMenu = new PopupMenuOpener();
        def list = Inv.lookupOpeners( inv.properties.category, [entity:entity] );
        list.each{
            popupMenu.add( it );
        }
        return popupMenu;
    }    
    
    def decFormat = new java.text.DecimalFormat('0.00');     
    def getPrintFormData() { 
        def data = remSvc.getReportData([ objid: entity.objid ]);
        def list = data.cashbreakdown; 
        list.each{
            it.indexno = ((Number) (it.denomination ? it.denomination : 0)).intValue(); 
        }
        list.sort{ -it.indexno }
        list.each { 
            it.caption = it.denomination.toString(); 
            if ( it.denomination instanceof Number ) {
                it.caption = decFormat.format( it.denomination ); 
            }
        } 
        data.cashbreakdown = list; 
        return data; 
    } 
    
    def getReportForm() { 
        def path = 'com/rameses/gov/treasury/remittance/report/rcd'; 
        return [
            mainreport: path + '/rcd_main.jasper', 
            subreports: [
                [name: 'CollectionType', template: path + '/collectiontype.jasper'], 
                [name: 'CollectionSummary', template: path + '/collectionsummary.jasper'], 
                [name: 'RemittedForms', template: path + '/remittedforms.jasper'], 
                [name: 'NonSerialRemittances', template: path + '/nonserialremittances.jasper'], 
                [name: 'NonSerialSummary', template: path + '/nonserialsummary.jasper'], 
                [name: 'OtherPayments', template: path + '/otherpayments.jasper'], 
                [name: 'Denomination', template: path + '/denomination.jasper'], 
                [name: 'CancelSeries', template: path + '/cancelseries.jasper']
            ] 
        ] 
    }     
}    