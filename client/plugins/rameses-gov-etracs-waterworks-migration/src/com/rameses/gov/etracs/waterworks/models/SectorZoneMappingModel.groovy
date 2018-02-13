package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.CrudFormModel;

class SectorZoneMappingModel extends CrudFormModel {

    @PropertyChangeListener 
    def changeHandler = [
        'entity.sector': { o-> 
            entity.zone = null; 
        }
    ]; 
    
    String getFormId() { 
        return getSchemaName();
    }
    String getTitle() { 
        return 'Sector Zone Mapping'; 
    }
        
    def getLookupSector() { 
        return Inv.lookupOpener('waterworks_sector:lookup', [:]); 
    }  
    
    def getLookupZone() { 
        def query = [:];
        query.where = ["sectorid = :sectorid", [sectorid: entity.sector?.objid]]; 
        return Inv.lookupOpener('waterworks_sector_zone:lookup', [ query: query ]); 
    } 
}