package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class MasterListModel extends CrudListModel
{
    // boolean autoResize = false;
    
    boolean allowExport = true;
    boolean allowImport = true;
    
    boolean isAllowSync(){
        if ('municipality'.equalsIgnoreCase(OsirisContext.env?.ORGCLASS))
            return true;
        return false;
    }
    
    def importFile(){
        def entity = [:]
        entity.schemaname = schemaName;
        entity.title = workunit?.info?.workunit_properties?.windowTitle;
        return Inv.lookupOpener('rptis_master:import', [entity:entity]);
    }
    
    def exportFile(){
        def entity = [:]
        entity.schemaname = schemaName;
        entity.title = workunit?.info?.workunit_properties?.windowTitle;
        return Inv.lookupOpener('rptis_master:export', [entity:entity]);
    }
    
    def syncData(){
        def entity = [:]
        entity.title = workunit?.info?.workunit_properties?.windowTitle;
        entity.schemaname = schemaName;
       return Inv.lookupOpener('rptis:master:sync', [entity:entity]);
    }
}
