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
    
    def getPrintFormData() { 
        def rdata = collSvc.getReportData([ objid: entity.objid ]); 
        if ( rdata ) rdata.putAll( entity ); 
        
        return rdata;
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
    
} 