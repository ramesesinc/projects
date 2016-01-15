package com.rameses.clfc.loantype

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class LoanTypeController extends CRUDController
{
    String serviceName = 'LoanTypeService';
    String entityName = 'loantype';
    String prefix = 'LT';
    
    boolean allowDelete = false;
    boolean allowApprove = false;
    
}

