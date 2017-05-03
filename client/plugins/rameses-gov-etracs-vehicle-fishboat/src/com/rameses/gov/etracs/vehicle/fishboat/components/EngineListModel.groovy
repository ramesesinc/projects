package com.rameses.gov.etracs.vehicle.fishboat.components;


import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

public class EngineListModel extends ComponentBean  {
    
    public def getEntity() {
        return value; 
    }

    def engineListModel = [
        fetchList: { o->
            if( !entity.engines) entity.engines = [];
            return entity.engines;
        },
        addItem: { o->
            o.parentid = entity.objid;
            entity.engines << o;
        },
        onRemoveItem: { o->
            entity.engines.remove(o);
        }
    ] as EditorListModel; 
    
}