package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.gov.etracs.vehicle.models.*;
import com.rameses.enterprise.models.*;

public class VehicleTxnMenu extends com.rameses.menu.models.MenuCategoryModel {
    
    def vehicletype;

    String getTitle() {
        return vehicletype.title;
    }
    
    @FormId
    String getFormId() {
        return vehicletype.objid;
    }

    @FormTitle
    String getFormTitle() {
        return vehicletype.title + " Menu";
    }
    
    def openItem( param ) {
        if ( invokers == null || !param?.id ) return null; 
        
        def inv = invokers.get( param.id ); 
        if ( inv == null ) return null; 
        
        def op = Inv.createOpener( inv, [ vehicletype: vehicletype ]); 
        if ( !op.target ) op.target = 'window'; 
        return op; 
    }    
}