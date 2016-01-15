package com.rameses.clfc.migrationsupport.borrowerloansresolver

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class BorrowerLoansResolverController extends CRUDController
{
    String serviceName = "MigrationBorrowerLoansResolverService";
    
    boolean allowCreate = false;
    boolean allowApprove = false;
    boolean allowDelete = false;
    
    def selectedLoan, prevloans;
    
    def listHandler = [
        fetchList: { o->
            if (!entity.items) entity.items = [];
            return entity.items;
        }
    ] as BasicListModel;
    
    def addLoan() {
        def handler = { o->
            def i = entity.items.find{ it.objid == o.objid }
            if (i) throw new Exception("Loan record already selected.");
            
            def ext = service.getLoanExtendedInfo(o);
            if (ext) o.putAll(ext);
            
            if (!entity._added) entity._added = [];
            entity._added.add(o);
            
            entity.items.add(o);
            listHandler?.reload();
        }
        return Inv.lookupOpener('loanresolver:lookup', [onselect: handler, state: 'RESOLVED']);
    }
    
    void removeLoan() {
        if (!MsgBox.confirm("You are about to remove this item. Continue?")) return;
        
        if (!entity._removed) entity._removed = [];
        entity._removed.add(selectedLoan);
        
        if (entity._added) entity._added.remove(selectedLoan);
        entity.items.remove(selectedLoan);
        listHandler?.reload();
    }
    
    void afterEdit( data ) {
        prevloans = [];
        def item;
        entity.items.each{ o->
            item = [:];
            item.putAll(o);
            prevloans.add(item);
        }
    }
    
    void afterCancel() {
        entity.items = [];
        entity.items.addAll(prevloans);
        entity.remove('_added');
        entity.remove('_removed');
        listHandler?.reload();
    }
}

