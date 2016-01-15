package com.rameses.clfc.customer;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CustomerConnectionsController 
{
    //feed by the caller 
    def callerContext = [:];
    def entity = [:];
    
    void init() {
    }
    
    
    def selectedConnection;
    def connectionListHandler = [
        fetchList: {
            return callerContext.service.getConnections([principalid: entity.objid]);
        }
    ] as BasicListModel 
} 