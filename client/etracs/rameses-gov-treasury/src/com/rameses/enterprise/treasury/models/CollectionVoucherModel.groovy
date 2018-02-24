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
    
    def post() {
        if(!MsgBox.confirm("You are about to post this transaction. Proceed?")) return null;
        def o = collSvc.post( entity );
        if ( o ) entity.state = o.state;
    }

   
    
}    