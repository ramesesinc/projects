package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class CollectionVoucherModel extends CrudFormModel {

    @Service("CollectionVoucherService")
    def collSvc;    
    
    @Service("DepositVoucherService")
    def depositSvc; 
    
    def selectedRemittance;
    def selectedFund;
    
    def numformat = new java.text.DecimalFormat("#,##0.00"); 
    
    def getTotalNoncash() {
        return (entity.totalcheck + entity.totalcr);
    }
    
    def getFormattedAmount() {
        if ( !(entity.amount instanceof Number )) {
            entity.amount = 0.0; 
        } 
        return numformat.format( entity.amount );  
    }
    
    def remittanceListHandler = [
        fetchList: { o->
            def m = [_schemaname:'remittance'];
            m.findBy = [collectionvoucherid: entity.objid ];
            return queryService.getList( m );
        },
        onOpenItem: { o,col->
            return viewRemittance();
        }
    ] as BasicListModel;
    
    def viewRemittance() {
        if( !selectedRemittance ) throw new Exception("Please select an item")
        def o = selectedRemittance;
        def op = Inv.lookupOpener("remittance:open", [entity: o]);
        op.target = "popup";
        return op;
    }
    
    def fundSummaryHandler = [
        fetchList: { o->
            def m = [_schemaname:'collectionvoucher_fund'];
            m.findBy = [parentid: entity.objid ];
            return queryService.getList( m );
        },
        onOpenItem: { o,col->
            return viewFund();
        }
    ] as BasicListModel;
    
    def fundTransferHandler = [
        fetchList: { o->
            def m = [_schemaname:'collectionvoucher_fund_transfer'];
            m.findBy = [parentid: entity.objid];
            return queryService.getList( m );
        },
        onOpenItem: { o,col->
            //
        }
    ] as BasicListModel;
    
    def viewFund() {
        if( !selectedFund ) throw new Exception("Please select an item")
        def o = selectedFund;
        def op = Inv.lookupOpener("collectionvoucher_fund:open", [entity: o]);
        op.target = "popup";
        return op;
    }
    
    def selectedCheck;
    def checkModel = [
        fetchList: { o->
            def m = [_schemaname: 'cashreceiptpayment_noncash' ];
            m.select = "refno,reftype,refdate,particulars,amount:{SUM(amount)}";
            m.groupBy = "refno,reftype,refdate,particulars";
            m.orderBy = "refdate,refno";
            m.where = [ "receipt.remittance.collectionvoucherid = :cvid AND amount > 0", [cvid: entity.objid ]];
            return queryService.getList( m );
        }
    ] as BasicListModel;
    
    def post() {
        if(!MsgBox.confirm("You are about to post this transaction. Proceed?")) return null;
        def o = collSvc.post( entity );
        if ( o ) entity.putAll( o );  
        
        fundSummaryHandler.reload(); 
    }
    
    def decFormat = new java.text.DecimalFormat('0.00'); 
    def getPrintFormData() { 
        def rdata = collSvc.getReportData([ objid: entity.objid ]); 
        if ( rdata ) rdata.putAll( entity ); 
        
        rdata.otherpayments = checkModel.fetchList([:]); 
        
        def list = rdata.cashbreakdown; 
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
        rdata.cashbreakdown = list; 
        return rdata;
    } 
    
    def getReportForm() { 
        def path = 'com/rameses/gov/treasury/liquidation/report/rcd'; 
        return [
            mainreport: path + '/main.jasper', 
            subreports: [
                [name:"remittances", template: path + "/remittances.jasper"],
                [name:"collectionsummary", template: path + "/collectionsummary.jasper"],
                [name:"reportaforms", template: path + "/reportaforms.jasper"],
                [name:"reportnsaforms", template: path + "/reportnsaforms.jasper"],
                [name:"reportnonserialsummary", template: path + "/reportnonserialsummary.jasper"],
                [name:"OtherPayments", template: path + "/otherpayments.jasper"],
                [name:"Denomination", template: path + "/denomination.jasper"]
            ], 
            parameters: [ REPORTTYPE: 'SUMMARY' ] 
        ] 
    } 
    
    public void changeLiqOfficer() {
        def s = { o->
            o.signature = null;
            def m = [_schemaname:"collectionvoucher"];
            m.findBy = [objid: entity.objid];
            m.liquidatingofficer = o;
            persistenceService.update( m );
            reloadEntity();
            binding.refresh();
        };
        Modal.show("liquidatingofficer:lookup", [onselect:s]);
    }
    
    public void updateCashBreakdown() {
        //get latest collectionvoucher from server
        def mm = [_schemaname:"collectionvoucher"];
        mm.findBy = [ objid: entity.objid ];
        mm.select = "cashbreakdown";
        def mz = queryService.findFirst(mm);
        def p = [total: entity.totalcash, cashbreakdown: mz.cashbreakdown ];
        p.handler = { o->
            def m = [_schemaname: 'collectionvoucher'];
            m.findBy = [objid: entity.objid];
            m.cashbreakdown = o.cashbreakdown;
            persistenceService.update( m );
            entity.cashbreakdown = m.cashbreakdown;
            binding.refresh();
        }
        Modal.show("cashbreakdown", p );
    }
    
} 