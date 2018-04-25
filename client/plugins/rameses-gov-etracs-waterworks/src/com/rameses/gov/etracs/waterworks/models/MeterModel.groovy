package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class MeterModel extends CrudFormModel {
    
    void initView() {
        entity = [objid : caller?.entity?.meter.objid ];
        super.open();
    }
    
    def edit() {
        def mp = new PopupMenuOpener();
        def list = Inv.lookupOpeners( "waterworks_meter:edit", [entity: entity ] );
        list.each { op->
            mp.add( op );
        }
        return mp;
    }

}
