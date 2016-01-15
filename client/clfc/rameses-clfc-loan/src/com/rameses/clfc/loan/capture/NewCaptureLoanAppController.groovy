package com.rameses.clfc.loan.capture;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.loan.controller.*;

class NewCaptureLoanAppController extends AbstractLoanAppController
{
    def service; 
    
    @PropertyChangeListener
    def listener = [
        "entity.apptype": {o->
            if (o == 'NEW') { 
                entity.previousloans?.clear(); 
                previousLoansHandler.reload(); 
            } 
        }
    ]
            
    NewCaptureLoanAppController() {
        try {
            service = InvokerProxy.instance.create("CaptureLoanAppService");
        } catch (e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    void init() {
        entity = service.initEntity();
        entity.appmode = 'CAPTURE';
        entity.previousloans = [];
        entity.orgid = OsirisContext.env.ORGID;
        productTypes = entity.productTypes;
    }
    
    def save() {
        if(entity.apptype != 'NEW' && entity.previousloans.isEmpty())
            throw new Exception('Previous Loans are required.');
        
        if (previousLoansHandler.hasUncommittedData())
            throw new Exception('Please commit table data before saving.');

        return super.save();
    }
    
    def getTitle() { return 'Capture Loan Application'; }
    protected def getService() { return service }
        
    def previousLoansHandler = [
        fetchList: {o->
            if(!entity.previousloans) entity.previousloans = [];
            return entity.previousloans
        },
        createItem: {
            return [loancount: entity.previousloans.size()+1];
        },
        onAddItem: {o->
            entity.previousloans.add(o)
        },
        onRemoveItem: {o->
            if( MsgBox.confirm("You are about to remove this loan. Continue?") ) {
                entity.previousloans.remove(o)
                return true
            }
            return false
        },
        onColumnUpdate: {o, colName->
            def item = entity.previousloans.find{ it == o }
            if( item ) item = o
        }
    ] as EditorListModel;   
}
