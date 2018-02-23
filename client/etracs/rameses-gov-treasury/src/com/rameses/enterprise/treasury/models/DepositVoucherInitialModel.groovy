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
    
    def collectionListModel = [
        isMultiSelect: {
            return true;
        },
        fetchList: { o->
            def m = [_schemaname: 'collectionvoucher' ];
            m.where = ["depositvoucherid IS NULL "];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("collectionvoucher:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def submitForDeposit() {
        def selectedList = collectionListModel.getSelectedValue();
        if(!selectedList) throw new Exception("Please select at least one entry");
        
        def p = MsgBox.confirm("You are about to create a deposit for the selected items. Proceed?");
        if(!p) return null;
        def m = [:];
        m.items = selectedList;
        def o = depositSvc.create( m );
        
        return Inv.lookupOpener("depositvoucher:open", [entity: o]); 
    }

    
}    