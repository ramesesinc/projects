package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositVoucherModel extends CrudFormModel {

    @Service("DepositVoucherService")
    def depositSvc;  
    
    def selectedFund;
    def selectedCollection;
    def selectedItem;

    def numformat = new java.text.DecimalFormat("#,##0.00"); 
    
    def getFormattedAmount() {
        if ( !(entity.amount instanceof Number )) {
            entity.amount = 0.0; 
        } 
        return numformat.format( entity.amount );  
    }
    
    def collectionList = [
        fetchList: { o->
            def m = [_schemaname: 'collectionvoucher' ];
            m.where = ["depositvoucherid = :depositvoucherid", [depositvoucherid:entity.objid] ];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            return viewCollection();
        }
    ] as BasicListModel;
    
    def viewCollection() {
        if(!selectedCollection) throw new Exception("Please select a collection");
        def op = Inv.lookupOpener("collectionvoucher:open", [entity: selectedCollection ] );
        op.target = "popup";
        return op;
    }
    
    def depositFundList = [
        fetchList: { o->
            def m = [_schemaname: 'depositvoucher_fund' ];
            m.where = ["parentid = :depositvoucherid", [depositvoucherid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("depositvoucher_fund:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def assignCheck() {
        if(!selectedFund) throw new Exception("Please select a fund");
        def p = [:]; 
        p.put("query.depositvoucherid", entity.objid );
        p.onselect = { o->
            def m = [depositvoucherid: entity.objid, fundid: selectedFund.fund.objid ];
            m.items = o.collect{ [objid: it.objid] };
            depositSvc.updateCheckToDeposit( m );
            depositFundList.reload();
        }
        return Inv.lookupOpener("paymentcheck:undeposited:withoutfund:lookup", p );        
    }
    
    def unassignCheck() {
        if(!selectedFund) throw new Exception("Please select a fund");
        def p = [:]; 
        p.put("query.depositvoucherid", entity.objid );
        p.put("query.fundid", selectedFund.fund.objid );
        p.onselect = { o->
            def m = [depositvoucherid: entity.objid, fundid: "{NULL}" ];
            m.items = o.collect{ [objid: it.objid] };
            depositSvc.updateCheckToDeposit( m );
            checkListModel.reload();
            binding.refresh();
        }
        return Inv.lookupOpener("paymentcheck:undeposited:withfund:lookup", p );
    }
    
    def assignCash() {
        if(!selectedFund) throw new Exception("Please select a fund");
        def h= { o->
            def m = [depositvoucherid: entity.objid, fundid: selectedFund.fund.objid ];
            m.totalcash = o;
            depositSvc.updateCashToDeposit( m );
            checkListModel.reload();
            binding.refresh();
        }
        def amt = selectedFund.amount - selectedFund.checktodeposit;
        return Inv.lookupOpener("decimal:prompt", [value: amt, title:'Enter amount', handler: h ]);
    }
    
    def assignCashier() {
        if(!selectedFund) throw new Exception("Please select a fund");
        def h= { o->
            def m = [depositvoucherid: entity.objid, fundid: selectedFund.fund.objid ];
            m.cashier = o;
            depositSvc.assignCashier( m );
            checkListModel.reload();
            binding.refresh();
        }
        return Inv.lookupOpener("cashier:lookup", [onselect: h ]);
    }
    
    def checkListModel = [
        fetchList: { o->
            def m = [_schemaname: 'paymentcheck' ];
            m.where = ["depositvoucherid = :depositvoucherid", [depositvoucherid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("paymentcheck:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def depositSlipListModel = [
        fetchList: { o->
            def m = [_schemaname: 'depositslip' ];
            m.where = ["depositvoucherid = :depositvoucherid", [depositvoucherid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("depositslip:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    public void submitForDeposit() {
        if(! MsgBox.confirm("You are about to submit this for deposit. Continue?")) return;
        def m = depositSvc.submitForDeposit( [objid: entity.objid ] );
        if ( m ) entity.putAll( m ); 
    }

    public void post() {
        if(! MsgBox.confirm("You are about to post this voucher. Continue?")) return;
        def m = depositSvc.post( [objid: entity.objid ] );
        if ( m ) entity.putAll( m ); 
    }

}    