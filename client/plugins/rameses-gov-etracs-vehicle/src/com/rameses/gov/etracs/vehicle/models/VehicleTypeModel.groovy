package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class VehicleTypeModel extends CrudFormModel {
    
    def selectedCluster;
    
    void addCluster() {
        def h = { m->
            m._schemaname = 'vehicletype_cluster';
            m.vehicletype = entity.objid;
            m.issued = 0;
            persistenceService.create(m);
            clusterList.reload();
        }    
        Modal.show("vehicletype_cluster", [handler: h] );
    }
    
    void removeCluster() {
        if(!selectedCluster?.objid) return;
        def m = [_schemaname: 'vehicletype_cluster'];
        m.objid = selectedCluster.objid;
        persistenceService.removeEntity(m);
        clusterList.reload();
    }
    
    def clusterList = [
        fetchList: { o->
            def m = [_schemaname: 'vehicletype_cluster'];
            m.findBy = [vehicletype: entity.objid];
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
}