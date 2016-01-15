package com.rameses.clfc.treasury.depositslip;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class CheckPluginController
{
    @Caller
    def caller;
    
    @Binding
    def binding;
    
    def entity, selectedCheck, mode;
    
    def listHandler = [
        fetchList: { o->
            if (!entity.checks) entity.checks = [];
            return entity.checks;
        }
    ] as BasicListModel;
    
    def getTotalcheck() {
        if (!entity.checks) return 0;
        def amt = entity.checks.amount.sum();
        if (!amt) amt = 0;
        return amt;
    }
    
    def addCheck() {
        def handler = { o->
            def m = entity?.checks?.find{ it.refid == o.objid }
            if (m) throw new Exception('This check has already been selected.');
            
            def item = [
                objid       : o.objid,
                parentid    : entity?.objid,
                refid       : o.objid,
                checkno     : o.checkno,
                checkdate   : o.txndate,
                amount      : o.amount,
                bank        : o.bank
            ];
            if (!entity._addedcheck) entity._addedcheck = [];
            entity._addedcheck.add(item);
            
            entity.checks.add(item);
            listHandler?.reload();
            
            entity.amount += o.amount;
            binding.refresh('totalcheck');
            caller?.refresh('entity.amount');
        }
        
        def op = Inv.lookupOpener('checkaccount:fordepositslip:lookup', [onselect: handler]);
        if (!op) return null;
        return op;
    }
    
    void removeCheck() {
        if (!MsgBox.confirm('You are about to remove this check. Continue?')) return;
        
        entity.amount -= selectedCheck.amount;
        if (!entity._removedcheck) entity._removedcheck = [];
        entity._removedcheck.add(selectedCheck);
        
        if (entity._addedcheck) entity._addedcheck.remove(selectedCheck);
        entity.checks.remove(selectedCheck);
        
        listHandler?.reload();
        
        //entity.amount -= selectedCheck.amount;
        binding.refresh('totalcheck');
        caller?.refresh('entity.amount');
        
    }
}

/*
class CheckPluginController
{
    @Caller
    def caller;

    @Binding
    def binding;

    def entity, selectedCheck, mode;

    def listHandler = [
        fetchList: { o->
            if (!entity.checks) entity.checks = [];
            return entity.checks;
        }
    ] as BasicListModel;
    
    def getTotalcheck() {
        if (!entity?.checks) return 0;
        return entity.checks.amount.sum();
    }

    def addCheck() {
        def handler = { o->
            def m = entity.checks.find{ it.refid == o.objid }
            if (m) throw new Exception("This check has already been selected.");
            //println 'selected check' + o;
            def item = [
                objid       : 'DSCHCK' + new UID(),
                parentid    : entity.objid,
                refid       : o.objid,
                amount      : o.amount,
                checkno     : o.checkno,
                checkdate   : o.checkdate,
                bank        : o.bank
            ];
            if (!entity._checkadded) entity._checkadded = [];
            entity._checkadded.add(item);
            entity.checks.add(item);
            listHandler?.reload();

            entity.amount += o.amount;
            binding.refresh('totalcheck');
            caller?.refresh('entity.amount');
        }
        def params = [
            state   : 'FOR_CLEARING',
            onselect: handler
        ];
        return Inv.lookupOpener("checkpayment:lookup" , params);
    }

    void removeCheck() {
        if (MsgBox.confirm("You are about to remove this check. Continue?")) {
            entity.amount -= selectedCheck.amount;
            if (!entity._checkdeleted) entity._checkdeleted = [];
            entity._checkdeleted.add(selectedCheck);
            entity.checks.remove(selectedCheck);
            listHandler.reload();
            binding.refresh('totalcheck');
            caller?.refresh('entity.amount');
        }
    }

}
*/