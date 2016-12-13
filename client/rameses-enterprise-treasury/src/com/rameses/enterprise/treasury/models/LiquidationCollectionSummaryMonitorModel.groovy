package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.* 

class LiquidationCollectionSummaryMonitorModel extends DefaultListController { 

    @Service('LiquidationMonitorService') 
    def monitorSvc; 
    
    def monthList = [];
    
    void init() {
        def o = monitorSvc.init(); 
        if ( o.year ) query.year = o.year; 
        if ( o.months ) monthList = o.months; 
    }
} 