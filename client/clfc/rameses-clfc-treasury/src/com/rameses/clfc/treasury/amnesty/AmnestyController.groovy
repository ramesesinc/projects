package com.rameses.clfc.treasury.amnesty;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.framework.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import java.rmi.server.UID;

class AmnestyController extends AbstractAmnestyController
{
    String getServiceName() {
        return "AmnestyService";
    }
    
    def createEntity() {
        return [
            objid           : 'AMNSTY' + new UID(),
            txnstate        : 'DRAFT',
            txnmode         : 'ONLINE',
            //txndate         : dateSvc.getServerDateAsString().split(" ")[0],
            iswaivepenalty  : 0,
            iswaiveinterest : 0,
            version         : 1
        ];
    }
}

