package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;


class BankDepositModel extends CrudFormModel {

   def depositid; 
   def fundid;
   def amount;
   boolean editable = true;
    
   void afterCreate() {
       entity.depositid = depositid;
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
    
   def checkList = [
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
            MsgBox.alert(o);
        }
        Inv.lookupOpener( "paymentcheck:depositfund:lookup", [multiSelect: true, onselect: h] );
    }
    
    def removeCheck() {
        
    }
    
    def doCancel() {
        return "_close";
    }
    
}    