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

public class VehicleFranchiseModel extends CrudFormModel {
    
    public String getTitle() {
        return entity.controlno ;
    }
    
    public String getWindowTitle() {
        return entity.controlno;
    }
    
    public String getFormId() {
        return entity.objid;
    }
    
    def getQuery() {
        return [controlid: entity.objid ];
    }

    
    def appListModel = [
        fetchList: { o->
            def m = [_schemaname:'vehicle_application_unit'];
            m.findBy = [ controlid: entity.objid ];
            m.orderBy = "app.dtcreated DESC";
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
}