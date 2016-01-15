package com.rameses.clfc.patch.loan;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanAppCaptureOptionController
{
    @Caller
    def caller;
    
    def entity = [:];
    
    void renew() {
        if (!MsgBox.confirm('You are about to renew this application. Continue?')) return;
        
        def renewhandler = { 
            if (!entity.appno) entity.appno=entity.loanno;

            entity.state = 'CLOSED';            
            caller.binding.refresh(); 
        } 
        def opener = Inv.lookupOpener('loanapp-capture:renew', [entity: entity, handler: renewhandler]); 
        if (!opener) throw new Exception("'loanapp-capture:renew' opener not found"); 
        
        Modal.show(opener); 
        
    } 
} 