package com.rameses.clfc.borrower;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.UID;

class BorrowerOtherAccountController
{
    //feed by the caller
    def borrowerContext;    
    
    def entity = [:];
    
    void init() {
        initEntity();
        borrowerContext.addDataChangeHandler('otheracct', { initEntity() });
    }
    
    void initEntity() { 
        if( borrowerContext.borrower?.otheracct == null )
            borrowerContext.borrower.otheracct = [:]
        
        entity = borrowerContext.borrower?.otheracct
        entity.borrowerid = borrowerContext.borrower?.objid;
        entity.objid = 'BOA'+new UID();        
    }
}