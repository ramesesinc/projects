package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.util.*;

public class SectorModel extends CrudFormModel {
    
    @PropertyChangeListener
    def l = [
        "entity.code" : { o->
            entity.objid = o;
        }
    ];
    
    def getQuery() {
        return [sectorid: entity.objid];
    }

    def handler = [
        createItem: {
            return [sectorid: entity.objid];
        }
    ]
    
}