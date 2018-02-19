package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositInitialModel extends CrudListModel {

    @Service("DepositService")
    def depositSvc;    
    
    def selectedList = new HashSet();
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    
    def liquidationListModel = [
        fetchList: { o->
            def m = [_schemaname: 'liquidation' ];
            m.where = ["depositid IS NULL "];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("liquidation:open", [entity: o] );
            op.target = "popup";
            return op;
        },
        onColumnUpdate: { o, colName ->
            if( o.selected == true ) {
                selectedList << o.objid;
            }
            else {
                selectedList.remove(o.objid);
            }
        }
    ] as EditorListModel;
    
    def submitForDeposit() {
        if(!selectedList) throw new Exception("Please select at least one entry");
        
        def p = MsgBox.confirm("You are about to create a deposit for the selected items. Proceed?");
        if(!p) return null;
        def m = [:]
        m.items = selectedList;
        depositSvc.create( m );
        liquidationListModel.reload();
        
        /*
        
        
        
        m.items = selectedList;
        depositSvc.create( m );
        MsgBox.alert('create success');
        */
    }

    
}    