package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositFundModel extends CrudFormModel {

    @Service("DepositService")
    def depositSvc;    
    
    def selectedBankDeposit;

    def bankDepositList = [
        fetchList: { o->
            def m = [_schemaname: 'bankdeposit' ];
            m.where = ["depositfundid = :depositfundid", [depositfundid:entity.objid] ];
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
        def amt = entity.amount - ( entity.totalcheck + entity.totalcash );
        return Inv.lookupOpener("bankdeposit:create", [depositfundid: entity.objid, fundid: entity.fundid, amount: amt, handler: h ] )
    }
    
    def validateDeposit() {
        if( !selectedBankDeposit ) throw new Exception("Please choose a bank deposit entry");
        if( selectedBankDeposit.totalcash + selectedBankDeposit.totalcheck )
            throw new Exception("Please make sure the amount is equal to total cash + total check");
        def h = { o->
            def m = [_schemaname: "bankdeposit"];
            m.findBy = [objid: selectedBankDeposit.objid ];
            m.validation = o;
            m.update( m );
            bankDepositList.reload();
        }    
        return Inv.lookupOpener("deposit_validation", [ handler: h ] );
    }

    def checkListModel = [
        fetchList: { o->
            def m = [_schemaname: 'paymentcheck' ];
            m.where = ["depositfundid = :depositfundid", [depositfundid:entity.objid] ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("paymentcheck:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    
}    