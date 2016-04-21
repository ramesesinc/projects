package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.reports.*;

class WaterworksAreaModel extends com.rameses.seti2.models.CrudFormModel {

    def getLookupZones() { 
        def params = ['query.sectorid': entity.sectorid, 'query.areaid': entity.objid ];  
        params.onselect = { o-> 
            println o; 
            println 'entity->' + entity;
        }
        return Inv.lookupOpener('waterworks_unassign_sector_zone:lookup', params);
    }
}