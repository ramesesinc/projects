package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.util.*;

public class ZoneModel extends CrudFormModel {
    
    def getQuery() {
        return [zoneid : entity.objid ];
    }

    def handler = [
        createItem: {
            return [zone: entity];
        }
    ];
    
    @PropertyChangeListener
    def listener = [
        "entity.schedule" : { o->
            entity.scheduleid = o.objid;
        }
    ]
}