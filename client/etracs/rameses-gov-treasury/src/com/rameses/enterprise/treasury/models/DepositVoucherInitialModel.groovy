package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositVoucherInitialModel extends CrudListModel {

    @Service("DepositVoucherService")
    def depositSvc;    
    
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    def amount = 0;
   
    def fund;
    def fundList;
    def selectedItem;
    
    void afterInit() {
        def m = [_schemaname: 'collectionvoucher_fund' ];
        m.select = "objid:{fund.objid},code:{fund.code},title:{fund.title}";
        m.where = ["parent.state='POSTED' AND depositvoucherid IS NULL "];
        m.groupBy =  "fund.objid,fund.code,fund.title";
        fundList = queryService.getList( m );
    }
    
    def collectionListModel = [
        isMultiSelect: {
            return true;
        },
        fetchList: { o->
            if(!fund) return null;
            def m = [_schemaname: 'collectionvoucher_fund' ];
            m.where = ["parent.state='POSTED' AND fund.objid = :fundid AND depositvoucherid IS NULL ", [fundid: fund.objid]];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("collectionvoucher_fund:open", [entity: o] );
            op.target = "popup";
            return op;
        },
        afterSelectionChange: { o ->
            if( o.selected ) {
                amount += o.data.amount;
            }
            else {
                amount -= o.data.amount;
            }
            binding.notifyDepends("selectedItem");
        }
    ] as BasicListModel;
    
    def submitForDeposit() {
        def selectedList = collectionListModel.getSelectedValue();
        if(!selectedList) throw new Exception("Please select at least one entry");
        
        def p = MsgBox.confirm("You are about to create a deposit for the selected items. Proceed?");
        if(!p) return null;
        
        def m = [fund:fund];
        m.amount = amount;
        m.items = selectedList;
        def o = depositSvc.create( m );       
        def op = Inv.lookupOpener("depositvoucher:open", [entity: o]); 
        op.target = "self";
        return op;
    }

    
}    