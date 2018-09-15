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
    def selectedStuboutNode;

    def sectorListHandler;
    def zoneListHandler;
    def stuboutListHandler;
    def stuboutNodeListHandler;
    
    def query = [:];
    
    @PropertyChangeListener
    def listener = [
        "selectedSector" : { o->
            selectedZone = null; selectedStubout = null; selectedStuboutNode = null;
            query.sectorid = selectedSector?.objid;
            query.zoneid = null;
            query.stuboutid = null;
            zoneListHandler.reload();
            stuboutListHandler.reload();
            stuboutNodeListHandler.reload();
        },
        "selectedZone" : { o->
            selectedStubout = null; selectedStuboutNode = null;
            query.zoneid = selectedZone?.objid;
            query.stuboutid = null;
            stuboutListHandler.reload();
            stuboutNodeListHandler.reload();
        },
        "selectedStubout" : {
            selectedStuboutNode = null;
            query.stuboutid = selectedStubout?.objid;
            stuboutNodeListHandler.reload();
        }
    ];
    
   

}