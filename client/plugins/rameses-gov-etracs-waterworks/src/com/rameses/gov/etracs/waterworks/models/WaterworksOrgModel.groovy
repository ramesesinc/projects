package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.treasury.common.models.*;

class WaterworksOrgModel  {

    def selectedSector;
    def selectedZone;
    def selectedStubout;
    def selectedStuboutnode;
    
    def getSectorQuery() {
        if( !selectedSector?.objid ) return [:];
        return [ sectorid: selectedSector.objid ]
    }
    
    def getZoneQuery() {
        if( !selectedZone?.objid ) return [:];
        return [ zoneid: selectedZone.objid ]
    }
    
    def getStuboutQuery() {
        if( !selectedStubout?.objid ) return [:];
        return [ stuboutid: selectedStubout.objid ]
    }
}