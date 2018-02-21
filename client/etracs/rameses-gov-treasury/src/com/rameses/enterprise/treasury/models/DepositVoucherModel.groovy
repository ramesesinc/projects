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
    def selectedCollection
        
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
        MsgBox.alert( 'assign check');
    }
    
    def assignCash() {
        if(!selectedFund) throw new Exception("Please select a fund");
        def h= { o->
             MsgBox.alert(o);
        }
        return Inv.lookupOpener("decimal:prompt", [value: selectedFund.totalcash, title:'Enter amount', handler: h ]);
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
    
    
    /*
    def getChecksWithoutFund() {
        def m = [_schemaname: "paymentcheck"];
        m.findBy = [depositid: entity.objid ];
        m.where = ["depositfundid IS NULL"];
        return queryService.getList( m );
    }
    */
    
    /*
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
    */
}    