package com.rameses.clfc.controlseries;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class ControlSeriesController extends CRUDController
{
    String serviceName = "ControlSeriesService";
    String entityName = "controlseries";

    boolean allowDelete = false;
    boolean allowApprove = false;
    
    Map createPermission = [domain: 'LOAN', role: 'ACCT_ASSISTANT'];
    Map editPermission = [domain: 'LOAN', role: 'ACCT_ASSISTANT'];

    def collectorLookupHandler = InvokerUtil.lookupOpener("collector:lookup", [:]);

    Map createEntity() {
        return [ 
            objid       : 'CS'+new java.rmi.server.UID(),
            startseries : 1,
            endseries   : 999999
        ];
    }
}