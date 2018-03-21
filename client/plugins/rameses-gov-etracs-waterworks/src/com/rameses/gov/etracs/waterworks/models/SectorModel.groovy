package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

public class SectorModel extends CrudFormModel {
    
    @PropertyChangeListener
    def l = [
        "entity.code" : { o->
            entity.objid = o;
        }
    ];
    
    def getQuery() {
        return [sectorid: entity.objid];
    }

    def handler = [
        createItem: { 
            if ( mode == 'create' ) {
                throw new Exception('Action not allowed. Please save the sector record first.'); 
            }
            
            return [sectorid: entity.objid, sector: entity];
        }
    ];    
}