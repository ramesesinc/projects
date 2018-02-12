package com.rameses.gov.etracs.waterworks.models;

import com.rameses.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.constant.*;
import com.rameses.seti2.models.ChangeInfoModel;

public class WaterworksChangeInfoModel extends ChangeInfoModel { 
        
    @Binding 
    def binding; 

    @PropertyChangeListener 
    def changelisteners = [
        'info.metersize' : { o-> 
            updateMeter( null, false ); 
        } 
    ]; 

    public def init() { 
        entity = caller.entity;
        schema = caller.schema;
        handler = { o->
            caller.loadData();
        }
        try { 
            return super.init(); 
        } finally { 
            info._actiontype = invoker?.properties?.caption; 
        } 
    } 

    def getLookupMeter() { 
        def params = [ metersize: info.metersize ]; 
        params.onselect = { o-> 
            updateMeter( o, true ); 
        } 
        params.onempty = {
            updateMeter( null, true ); 
        } 
        return Inv.lookupOpener('waterworks_meter_wo_account:lookup', params); 
    } 

    void updateMeter( o, autorefresh ) {
        info.meter = o; 
        if ( autorefresh ) { 
            binding.refresh('info.meter.*');
        } 
    } 

}   