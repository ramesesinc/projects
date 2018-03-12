package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class AccountModel extends CrudFormModel {
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    
    @PropertyChangeListener 
    def changelistener = [
        'entity.metersize' : { o-> 
            entity.meter = null; 
        }
    ];
    
    
    void afterCreate() {
        entity.address = [:];
        entity.attributes = [];
    }

    
    def assignNode() {
        def h = { o ->
            entity.stuboutnode = o;
            def m = [_schemaname:'waterworks_account'];
            m.findBy = [objid: entity.objid];
            m.stuboutnodeid = o.objid;
            persistenceService.update( m );
        }
        Modal.show("vw_waterworks_stuboutnode_unassigned:lookup", [onselect: h] );  
    }
    
    def getLookupMeter() { 
        def params = [metersize: entity.metersize];
        if ( !params.metersize ) params.metersize = [objid:null]; 
            
        params.onselect = { o-> 
            entity.meter = o; 
            binding.refresh('entity.meter.*');
        }
        params.onempty = {
            entity.meter = null; 
            binding.refresh('entity.meter.*');
        }
        return Inv.lookupOpener('waterworks_meter_wo_account:lookup', params);
    }
    
    
}