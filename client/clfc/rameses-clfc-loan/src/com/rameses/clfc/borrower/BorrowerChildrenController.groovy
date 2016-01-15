package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.borrower.*;
import com.rameses.clfc.util.*;

class BorrowerChildrenController 
{
    //feed by the caller
    def borrowerContext;
    
    @Binding
    def binding;
    
    def htmlBuilder = new BorrowerInfoHtmlBuilder();
    
    void init() {
        borrowerContext.addDataChangeHandler('children', { childrenHandler.reload() });
    }
        
    def selectedChild;
    def childrenHandler = [
        fetchList: {o->
            if( !borrowerContext.borrower.children ) borrowerContext.borrower.children = []
            borrowerContext.borrower.children.each{ it._filetype = "child" }
            return borrowerContext.borrower.children;
        },
        onRemoveItem: {o->
            return removeChildImpl(o);
        },
        getOpenerParams: {o->
            return [ mode: borrowerContext.mode, caller: this ]
        }
    ] as EditorListModel
            
    def addChild() {
        def handler = {child->
            child.borrowerid = borrowerContext.borrower?.objid;
            borrowerContext.borrower.children.add(child);
            childrenHandler.reload();
        }
        return InvokerUtil.lookupOpener("child:create", [handler:handler]);
    }
    
    void removeChild() {
        removeChildImpl(selectedChild); 
    }
    
    boolean removeChildImpl(o) {
        if (borrowerContext.mode == 'read') return false;
        if (MsgBox.confirm("You are about to remove this child. Continue?")) {
            borrowerContext.borrower.children.remove(o);
            return true;
        } else { 
            return false; 
        } 
    }
    
    def getHtmlview() {        
        return htmlBuilder.buildChild(selectedChild);
    }
}
