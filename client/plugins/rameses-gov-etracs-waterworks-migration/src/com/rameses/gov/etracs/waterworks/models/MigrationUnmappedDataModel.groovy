package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class MigrationUnmappedDataModel {

    @Binding 
    def binding;
    
    def list;
    def title;
    
    void init() { 
    }
    
    def doClose() {
        return '_close'; 
    }
    
    def listHandler = [
        fetchList: { o-> 
            if ( list == null || list.isEmpty()) return list; 
            if ( list.size() > 50 ) {
                return list.substring(0, 50); 
            } else {
                return list; 
            }
        }
    ] as ListPaneModel;
}