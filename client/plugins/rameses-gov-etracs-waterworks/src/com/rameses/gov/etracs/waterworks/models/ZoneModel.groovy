package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.util.*;

public class ZoneModel extends CrudFormModel {
    
    void afterCreate() {
        if( !caller.selectedSector ) throw new Exception("Please select a sector");
        entity.sector = caller.selectedSector;
        entity.sectorid =  entity.sector.objid;
    }
    
    void afterSave() {
        caller.zoneListHandler.reload();
    }
    
    
    @PropertyChangeListener
    def listener = [
        "entity.schedule" : { o->
            entity.scheduleid = o.objid;
        }
    ]
}