package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;

class VehicleFranchiseList extends CrudListModel {

    @Service("VehicleFranchiseService")
    def ctrlSvc;

    def clusterList;
    def cluster;
    
    public String getVehicletype() {
        return workunit.info.workunit_properties.vehicletype;
    }

    @PropertyChangeListener
    def listener = [
        "cluster" : { o->
            reload();
        }
    ];
    
    public def getCustomFilter() {
        if( !cluster) return null;
        return [ "clusterid=:clusterid", [clusterid: cluster.objid ] ];
    }
    
    void afterInit() {
        def m = [_schemaname: 'vehicletype_cluster'];
        m.findBy = [vehicletype: vehicletype];
        clusterList = queryService.getList(m);
        cluster = clusterList.find{ it.name == 'DEFAULT' };
    }
    
    def create() {
        if(!vehicletype)
            throw new Exception("Please select a txntype");
        if(!cluster)
            throw new Exception("Cluster is required");
            
        def q = MsgBox.prompt( "Please enter qty to issue");
        if(!q) return; 
        query.qty = q;
        query.vehicletype = vehicletype;
        query.cluster = cluster;
        ctrlSvc.generate(query);
        reload();
    }
    
} 