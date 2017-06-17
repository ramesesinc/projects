package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;

public class AuxiliaryPermitListModel extends CrudListModel {

    def permitType;
    
    void afterInit() {
        permitType = invoker?.properties?.permitType?.toLowerCase();
    }
    
    public def getCustomFilter() {
        return [ "type = :type", [type: permitType ]  ];
    }
    
    public def open() {
        return Inv.lookupOpener(schemaName + "_" + permitType + ":open", [entity:selectedItem] );
    }
    
    
}