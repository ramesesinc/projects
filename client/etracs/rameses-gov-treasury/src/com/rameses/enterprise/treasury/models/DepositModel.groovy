package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositModel extends CrudFormModel {

    @Service("DepositService")
    def depositSvc;    
        
    def depositFundList = [
        fetchList: { o->
            def m = [_schemaname: 'deposit_fund' ];
            m.where = ["depositid = :depositid", [depositid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("deposit_fund:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def bankDepositList = [
        fetchList: { o->
            def m = [_schemaname: 'bankdeposit' ];
            m.where = ["depositid = :depositid", [depositid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("bankdeposit:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    def addBankDeposit() {
        def h = {
            MsgBox.alert('saved deposit');
        } 
        def amt = (entity.totalcash + entity.totalcheck) - ( entity.totalcashdeposited +entity.totalcheckdeposited );
        return Inv.lookupOpener("bankdeposit:create", [depositid: entity.objid, fundid: entity.fundid, amount: amt, handler: h ] )
    }
    
    def post() {
        def p = MsgBox.confirm("You are about to post this deposit. Proceed?");
        if(!p) return null;
        def m = [objid: entity.objid]
        depositSvc.post( m );
        MsgBox.alert('post success');
    }

    
}    