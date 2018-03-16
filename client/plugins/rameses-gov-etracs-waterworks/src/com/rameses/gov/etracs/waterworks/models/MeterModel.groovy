package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class MeterModel extends CrudFormModel {
    
    def edit() {
        def mp = new PopupMenuOpener();
        mp.add( new FormAction(caption:'Edit Reading', name:'editReading', context: this )  );
        mp.add( new FormAction(caption:'Change Status', name:'changeStatus', context: this)  );
        return mp;
    }
    
    void updateMeter( def o ) {
        def m = [_schemaname:'waterworks_meter'];
        m.findBy = [objid: entity.objid];
        m.putAll( o );
        persistenceService.update( m );
        entity.putAll( o );
        binding.refresh();
    }
    
    void editReading() {
        def p = [ fields:[] ];
        p.fields << [caption:'Last Reading', name:'lastreading', datatype:'integer', required:true];
        p.fields << [caption:'Last Reading Date', name:'lastreadingdate', datatype:'date', required:true];
        p.data = [lastreading: entity.lastreading, lastreadingdate: entity.lastreadingdate];
        p.handler = { o->
            o.lastreading = o.lastreading.toInteger();
            if( o.lastreading >= entity.capacity.toInteger() ) {
                throw new Exception("Last reading must be less than capacity");
            }
            updateMeter(o);
        }
        Modal.show( "dynamic:form", p, [title:"Enter Reading Information"] );
    }
    
    def meterStatusList = ['ACTIVE', 'DEFECTIVE'];
    void changeStatus() {
        def p = [ fields:[] ];
        p.fields << [caption:'Change Status', name:'state', datatype:'string_array',arrayvalues:['ACTIVE','DEFECTIVE'] ];
        p.handler = { o->
            updateMeter(o);
        }
        Modal.show( "dynamic:form", p, [title:"Change Meter Status" ] );
    }

}
