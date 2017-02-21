package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class MasterListModel extends CrudListModel
{
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
}
