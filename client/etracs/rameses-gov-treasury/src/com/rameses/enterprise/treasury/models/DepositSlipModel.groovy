package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class DepositSlipModel extends CrudFormModel {

    @Service("DepositSlipService")
    def depositSlipSvc;
    
    def amount;
    def handler;
    def depositvoucherid;
    def fundid; 
    
    boolean editable = true;
    def selectedItems = []; 
    def selectedCheck;

    void afterCreate() {
       entity.state = "DRAFT";
       entity.depositvoucherid = depositvoucherid;
       entity.fundid = fundid;
       entity.amount = amount;
       entity.totalcash = 0;
       entity.totalcheck = 0;
       entity.cashbreakdown = [];
    } 
    
    public void beforeSave(def mode){
        if(mode == "create") {
            if( amount < entity.amount ) throw new Exception("Amount must be less than amount to deposit");
        }
    }
    
    def getBankAccountLookup() {
       def h = { o->
           entity.bankaccount = o;
           binding.refresh("bankaccount.*");
       }
       return Inv.lookupOpener("bankaccount:lookup", [fundid: fundid, onselect: h] );
   } 
    
   def checkListModel = [
        fetchList: { o->
            def m = [_schemaname:'paymentcheck'];
            m.findBy = [depositslipid: entity.objid];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("paymentcheck:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;     
    
    def addCheck() {
        def h = { o->
            def p = [depositslipid: entity.objid];
            p.items = o.collect{ [objid: it.objid ] };
            def z = depositSlipSvc.updateCheckTotal( p );
            entity.totalcheck = z.totalcheck;
            binding.refresh("entity.totalcheck");
            checkListModel.reload();
            handler();
        }
        def p = [multiSelect: true, onselect: h];
        p.put( "query.depositvoucherid", entity.depositvoucherid );
        p.put( "query.fundid", entity.fundid );
        return Inv.lookupOpener( "paymentcheck:undeposited:withoutdepositslip:lookup", p );
    }
    
    def removeCheck() {
        if(!selectedCheck) throw new Exception("Select a check to remove");
        def p = [depositslipid: entity.objid];
        p.items = [ [objid: selectedCheck.objid] ];
        def z = depositSlipSvc.removeCheck( p );
        entity.totalcheck = z.totalcheck;
        binding.refresh("entity.totalcheck");
        handler();
    }
    
    def getCashToDeposit() {
        return entity.amount - entity.totalcheck;
    }
    
    def updateCash() {
        def p = [:];
        p.handler = { o->
            def m = [depositslipid: entity.objid];
            m.totalcash = o.total;
            m.cashbreakdown = o.cashbreakdown;
            depositSlipSvc.updateCash( m );
            entity.totalcash = m.totalcash;
            entity.cashbreakdown = m.cashbreakdown;
            binding.refresh();
            handler();
        }
        p.cashbreakdown = entity.cashbreakdown;
        p.total = getCashToDeposit();
        return Inv.lookupOpener( "cashbreakdown", p );
    }
    
    void approve() {
        depositSlipSvc.approve( [objid: entity.objid ] );
        entity.state = 'APPROVED'
    }
    
    void disapprove() {
        depositSlipSvc.disapprove( [objid: entity.objid ] );
        entity.state = 'DRAFT'
    }
    
    def validate() {
        def h = { o->
            def m = [objid: entity.objid ];
            m.validation = [refno: o.refno, refdate: o.refdate ];
            depositSlipSvc.validate( m );
            entity.state = 'VALIDATED'
            entity.validation = m.validation;
            binding.refresh();
            handler();
        }    
        return Inv.lookupOpener("deposit_validation", [ handler: h ] );
    }
}    