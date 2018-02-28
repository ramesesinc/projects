package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositVoucherFundModel extends CrudFormModel {

    @Service("DepositVoucherService")
    def depositSvc;    
    
    @Service("DepositSlipService")
    def depositSlipSvc;  
    
    def selectedDepositSlip;
    def selectedCheck;
    def selectedItem;

    def depositSlipList = [
        fetchList: { o->
            def p = [depositvoucherid:entity.parentid, fundid: entity.fund.objid ];
            def m = [_schemaname: 'depositslip' ];
            m.where = ["depositvoucherid = :depositvoucherid AND fund.objid=:fundid" , p ];
            def list = queryService.getList( m );
            return list;
        },
        onOpenItem: {o,col->
            return viewDepositSlip();
        }
    ] as BasicListModel;

    def viewDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        def p = [:];
        p.entity = selectedDepositSlip;
        p.handler = {
            open();
            binding.refresh();
        }
        def op = Inv.lookupOpener("depositslip:open", p );
        op.target = "popup";
        return op;
    }
    
    def addDepositSlip() {
        def amt = entity.amount - ( entity.totalcheck + entity.totalcash );
        def p = [depositvoucherid: entity.parentid, fundid: entity.fund.objid, amount: amt ];
        p.handler = {
            open();
            binding.refresh();
        }
        return Inv.lookupOpener("depositslip:create", p );
    }
    
    void removeDepositSlip() {
        if(!selectedDepositSlip) throw new Exception("Please select a deposit slip");
        if(selectedDepositSlip.state == 'VALIDATED' )
            throw new Exception("Cannot delete this because it is already validated");
        depositSlipSvc.removeDepositSlip( selectedDepositSlip );
        open();
        binding.refresh();
    }
    
    
    def validateDeposit() {
        if( !selectedDepositSlip ) throw new Exception("Please choose a bank deposit entry");
        if( selectedDepositSlip.totalcash + selectedDepositSlip.totalcheck )
            throw new Exception("Please make sure the amount is equal to total cash + total check");
        def h = { o->
            def m = [_schemaname: "depositslip"];
            m.findBy = [objid: selectedDepositSlip.objid ];
            m.validation = o;
            m.update( m );
            bankDepositList.reload();
        }    
        return Inv.lookupOpener("deposit_validation", [ handler: h ] );
    }

    def checkListModel = [
        fetchList: { o->
            def m = [_schemaname: 'paymentcheck' ];
            m.where = ["depositvoucherid = :depositvoucherid AND fundid=:fundid", [depositvoucherid:entity.parentid, fundid: entity.fund.objid] ];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("paymentcheck:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    
    
    /*
    def addCheck() {
        def p = [:]; 
        p.put("query.depositvoucherid", entity.parentid );
        p.onselect = { o->
            def m = [depositvoucherid: entity.parentid, fundid: entity.fund.objid ];
            m.items = o.collect{ [objid: it.objid] };
            depositSvc.updateCheckToDeposit( m );
            checkListModel.reload();
            binding.refresh();
        }
        return Inv.lookupOpener("paymentcheck:undeposited:withoutfund:lookup", p );
    }
    
    def removeCheck() {
        def p = [:]; 
        p.put("query.depositvoucherid", entity.parentid );
        p.put("query.fundid", entity.fund.objid );
        p.onselect = { o->
            def m = [depositvoucherid: entity.parentid, fundid: "{NULL}" ];
            m.items = o.collect{ [objid: it.objid] };
            depositSvc.updateCheckToDeposit( m );
            checkListModel.reload();
            binding.refresh();
        }
        return Inv.lookupOpener("paymentcheck:undeposited:withfund:lookup", p );
    }
    */
}    