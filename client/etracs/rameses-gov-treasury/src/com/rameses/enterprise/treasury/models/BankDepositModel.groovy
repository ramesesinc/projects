package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class BankDepositModel extends CrudFormModel {

    @Service("BankDepositService")
    def bankDepositSvc;
    
   def depositfundid; 
   def fundid;
   def amount;
   boolean editable = true;
   def selectedItems = []; 
   def selectedCheck;
    
   void afterCreate() {
       entity.depositfundid = depositfundid;
       entity.fundid = fundid;
       entity.amount = amount;
       entity.totalcash = 0;
       entity.totalcheck = 0;
       entity.cashbreakdown = [];
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
            m.findBy = [bankdepositid: entity.objid];
            return queryService.getList( m );
        },
        onOpenItem: {o,col->
            def op = Inv.lookupOpener("checkpayment:open", [entity: o] );
            op.target = "popup";
            return op;
        }
    ] as BasicListModel;     
    
    def addCheck() {
        def h = { o->
            def p = [bankdepositid: entity.objid];
            p.items = o.collect{ it.objid };
            def z = bankDepositSvc.updateCheckBankDeposit( p );
            entity.totalcheck = z.totalcheck;
            binding.refresh("entity.totalcheck");
            checkListModel.reload();
        }
        def p = [multiSelect: true, onselect: h];
        p.put( "query.depositfundid", entity.depositfundid );
        return Inv.lookupOpener( "paymentcheck:depositfund:lookup", p );
    }
    
    def removeCheck() {
        if(!selectedCheck) throw new Exception("Select a check to remove");
        def p = [bankdepositid: entity.objid];
        p.items = [ selectedCheck.objid ];
        def z = bankDepositSvc.removeCheckBankDeposit( p );
        entity.totalcheck = z.totalcheck;
        binding.refresh("entity.totalcheck");
    }
    
    def updateCash() {
        def p = [:];
        p.handler = { o->
            def m = [bankdepositid: entity.objid];
            m.totalcash = o.total;
            m.cashbreakdown = o.cashbreakdown;
            m = bankDepositSvc.updateCash( m );
            entity.totalcash = m.totalcash;
            entity.cashbreakdown = m.cashbreakdown;
            binding.refresh();
        }
        p.cashbreakdown = entity.cashbreakdown;
        p.total = entity.totalcash;
        return Inv.lookupOpener( "cashbreakdown", p );
    }
    
    def doCancel() {
        return "_close";
    }
    
}    