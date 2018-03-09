package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import java.util.concurrent.*;
import com.rameses.treasury.common.models.*;

public class WaterworksBillingModel extends MultiBillingModel {
    
    @Service("DateService")
    def dateSvc;
    
    public void afterCreate() {
        entity.year = dateSvc.getServerYear();
        entity.month = dateSvc.getServerMonth(); 
    }

    public def fetchList( def o ) {
        o._schemaname = 'waterworks_billing_account';
        o.findBy = [parentid: entity.objid, state: 'DRAFT' ];
        return queryService.getList( o );
    }
    
    public void processEntry( def o ) {
        def m = [_schemaname: 'waterworks_billing_account' ];
        m.findBy =[objid:o.objid];
        m.state = 'PROCESSING';
        persistenceService.update( m );
    }
    
    
}