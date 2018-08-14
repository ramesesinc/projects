package com.rameses.gov.etracs.rptis.municipality.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.common.*;
import com.rameses.gov.etracs.rptis.util.ServiceLookup;

public class RevisionSettingSyncModel extends com.rameses.gov.etracs.rptis.models.AbstractSyncModel
{
    boolean showry = true;
    
    def getTitle(){
        return 'Synchronize revision setting from Province';
    }
    
    void init(){
        entity = [:];
        entity.schemaname = invoker.properties.schemaName;
    }
    
    public def sync(items){
        return syncSvc.syncSetting(entity);
    }
    
}