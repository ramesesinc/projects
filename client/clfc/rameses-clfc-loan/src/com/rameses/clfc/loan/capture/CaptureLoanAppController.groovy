package com.rameses.clfc.loan.capture;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.clfc.loan.controller.*;

class CaptureLoanAppController extends LoanAppController 
{
    def getFormId() {
        return 'CLOAN-' + entity.appno;
    }
    
    public boolean isEditButtonVisible() {
        if (mode != 'read') return false; 
        
        return (entity.appmode == 'CAPTURE' && entity.state == 'RELEASED'); 
    }
    
    public boolean isEditableMode() { 
        return (mode == 'edit'); 
    }
    
    //private def menu;
    def viewReport() {
       def menu = new PopupMenuOpener();
       menu.addAll(InvokerUtil.lookupOpeners("loanapp-report:plugin", [loanappid: entity.objid]));
       return menu;
    }
}
