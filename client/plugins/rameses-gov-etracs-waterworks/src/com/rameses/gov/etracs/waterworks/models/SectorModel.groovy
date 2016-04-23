package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

public class SectorModel extends CrudFormModel {
    
    def getLookupSectorReaders() {
        if( !itemHandlers.zones.selectedItem )
            throw new Exception("Please select a zone first");
        def h = { o->
            itemHandlers.zones.selectedItem.item.reader = o;
            itemHandlers.zones.refresh();
        }
        Inv.lookupOpener("waterworks_sector_reader:lookup", [sectorid: entity.objid, onselect:h]);
    }
    
    
}