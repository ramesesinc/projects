package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudFormModel;

class SectorMappingModel extends CrudFormModel {

    String getFormId() { 
        return getSchemaName();
    }
    String getTitle() { 
        return 'Sector Mapping'; 
    }
    
    def getLookupHandler() { 
        return Inv.lookupOpener('waterworks_sector:lookup', [:]); 
    }
}