package com.rameses.clfc.inbox;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*
import com.rameses.rcp.framework.ClientContext;

class InboxListController extends ListController 
{
    String serviceName = 'InboxService'; 
    String entityName = 'inbox'; 
    boolean allowCreate = false; 
    int rows = 20; 

    boolean onOpen(Map params) {
        params.entity.inboxid = params.entity.objid;
        params.entity.objid = params.entity.refid;
        return true;
    }
}
