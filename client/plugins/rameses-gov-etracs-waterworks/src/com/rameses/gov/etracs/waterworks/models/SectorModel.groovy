package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.util.*;

public class SectorModel extends CrudFormModel {
    
    @Service("WaterworksBillingCycleService")
    def billingCycleService;
    
    @PropertyChangeListener
    def l = [
        "entity.code" : { o->
            entity.objid = o;
        }
    ];
    
    void afterCreate() {
        billCycleList.reload(); 
    }

    void generateBillingCycle() {
        def h = { o->
            o.sectorid = entity.objid;
            billingCycleService.generateByYear(o);
            billCycleList.reload();
        };
        def fc = [
            [name:'year', datatype:'integer', caption:'Year']
        ];
        Modal.show("dynamic_field:entry", [title:'Enter Bill Date', formControls:fc, handler:h] );
    }
    
    def billCycleList = [
        fetchList: {
            if ( mode.toString() != 'read' ) return []; 

            def m = [_schemaname:'waterworks_billing_cycle'];
            m.findBy = [sectorid: entity.objid];
            m._limit = 1000;
            m._start = 0;
            queryService.getList(m);
        }
    ] as BasicListModel;

}