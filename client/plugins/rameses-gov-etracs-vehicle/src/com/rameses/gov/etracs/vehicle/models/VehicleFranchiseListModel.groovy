package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;

class VehicleFranchiseListModel extends CrudListModel {

    @Service("VehicleFranchiseService")
    def ctrlSvc;

    def vehicleTypeList;

    def cluster;
    def vehicletype;
    

    @PropertyChangeListener
    def listener = [
        "cluster|vehicletype" : { o->
            reload();
        }
    ];
    
    void afterInit() {
        def m = [_schemaname: 'vehicletype'];
        m.where = ["1=1"];
        vehicleTypeList = queryService.getList(m);
    }

    public def getCustomFilter() {
        if( !vehicletype?.objid ) return ["1=0"];
        if(!cluster) {
            return ["vehicletypeid = :vid AND clusterid IS NULL", [vid: vehicletype.objid] ]; 
        }
        else {
            return ["vehicletypeid = :vid AND clusterid=:clusterid", [vid: vehicletype.objid, clusterid: cluster.objid] ]; 
        }
    }
    
    public def getClusterList() {
        if( !vehicletype ) return [];
        def m = [_schemaname: 'vehicletype_cluster'];
        m.findBy = [vehicletype: vehicletype.objid];
        return queryService.getList(m);
    }
    
    def create() {
        if(!vehicletype)
            throw new Exception("Please select a vehicle type");
            
        def q = MsgBox.prompt( "Please enter no. of units to issue");
        if(!q) return; 
        query.qty = q;
        query.vehicletype = vehicletype.objid;
        if( cluster ) 
            query.cluster = cluster;
        else
            query.cluster = null;
        ctrlSvc.generate(query);
        reload();
    }
    
} 