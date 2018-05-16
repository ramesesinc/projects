package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class DepositVoucherModel extends CrudFormModel {

    @Service("DepositVoucherService")
    def depositSvc;    
    
    @Service("DepositSlipService")
    def depositSlipSvc;  
    
    def selectedDepositSlip;
    def selectedItem;

    def depositSlipList = [
        fetchList: { o->
            def p = [depositvoucherid:entity.objid, fundid: entity.fund.objid ];
            def m = [_schemaname: 'depositslip' ];
            m.where = ["depositvoucherid = :depositvoucherid " , p ];
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
        def p = [depositvoucher: entity, fundid: entity.fund.objid ];
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
    
     def collectionListModel = [
        fetchList: { o->
            def m = [_schemaname: 'collectionvoucher_fund' ];
            m.where = [" depositvoucherid = :depositid", [depositid: entity.objid]];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("collectionvoucher_fund:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;
    
    public void post() {
        if(! MsgBox.confirm("You are about to post this voucher. Continue?")) return;
        depositSvc.post( [objid: entity.objid ] );
    }
    
}    