package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositInitialModel extends CrudListModel {

    @Service("DepositService")
    def depositSvc;    
    
    def fund;
    def fundList;
    def selectedList = new HashSet();
    
    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
    
    @PropertyChangeListener
    def listener = [
        "fund" : { o->
            depositFundListHandler.reload();
        }
    ]
    
    void buildFundList() {
        def m = [_schemaname:'deposit_fund'];
        m.select = "fund.objid,fund.title";
        m.where = ["depositid IS NULL"];
        m.groupBy = "fund.objid,fund.title";
        def lst  = queryService.getList(m);
        if(lst) {
            fundList = lst*.fund;
        }
    }
    
    void afterInit() {
        buildFundList();
        if(!fundList) 
            throw new Exception("There are no items to deposit")
    }
    
    def depositFundListHandler = [
        fetchList: { o->
            if( !fund ) return [];
            def m = [_schemaname: 'deposit_fund' ];
            m.where = ["depositid IS NULL AND fundid = :fundid", [fundid:fund.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("deposit_fund:open", [entity: o] );
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
        def p = MsgBox.confirm("You are about to create a deposit for the selected items. Proceed?");
        if(!p) return null;
        def m = [:]
        m.fundid = fund.objid;
        m.items = selectedList;
        depositSvc.create( m );
        MsgBox.alert('create success');
        buildFundList();
        depositFundListHandler.reload();
    }

    
}    