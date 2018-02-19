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
    
    def checkListModel = [
        fetchList: { o->
            def m = [_schemaname: 'paymentcheck' ];
            m.where = ["depositid = :depositid", [depositid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("paymentcheck:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
     def fundTransferListModel = [
        fetchList: { o->
            def m = [_schemaname: 'deposit_fund_transfer' ];
            m.where = ["depositid = :depositid", [depositid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        }
    ] as BasicListModel;
    
    def getChecksWithoutFund() {
        def m = [_schemaname: "paymentcheck"];
        m.findBy = [depositid: entity.objid ];
        m.where = ["depositfundid IS NULL"];
        return queryService.getList( m );
    }
    
    def post() {
        //check first if there are checks with no funds
        def list = getChecksWithoutFund();
        if( list ) {
            boolean pass = false;
            def h = { 
                //pass = true;
            }
            Modal.show( "deposit_check_select_fund", [list: list, depositid: entity.objid, handler : h]);
            if( !pass ) return null;
        }        
        
        def p = MsgBox.confirm("You are about to post this deposit. Proceed?");
        if(!p) return null;
        def m = [objid: entity.objid]
        depositSvc.post( m );
        MsgBox.alert('post success');
    }
    
}    