package com.rameses.gov.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class UnremittedCollectionSummaryMonitorModel extends DefaultListController { 

    def showItems() {
        if ( !selectedEntity?.collector?.objid ) return null;  

        def params = [ collectorid: selectedEntity.collector?.objid ]; 
        return Inv.lookupOpener('unremitted-collection-detail-monitor:list', params); 
    } 
} 