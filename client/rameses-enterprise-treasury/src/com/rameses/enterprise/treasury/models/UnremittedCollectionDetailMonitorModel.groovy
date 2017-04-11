package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.* 

class UnremittedCollectionDetailMonitorModel extends DefaultListController { 

    @Service('UnremittedCollectionMonitorService') 
    def monitorSvc; 

    @Caller 
    def caller; 

    def showItems() { 
        println 'selected item-> ' + caller?.selectedEntity;
        return null; 
    } 
} 