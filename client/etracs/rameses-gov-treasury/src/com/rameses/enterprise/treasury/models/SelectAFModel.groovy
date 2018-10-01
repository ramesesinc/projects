package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

class SelectAFModel extends CrudLookupModel {

    @Script( "User" )
    def user;

    def entity;
    
    boolean init

    String title = "Select Stub to use";
    
    def getCustomFilter() {
        return CashReceiptAFLookupFilter.getFilter( entity ); 
    }
    
    def doOk() {
        def obj = listHandler.getSelectedValue();
        if( entity.collectiontype?.fund?.objid ) {
            def vfund = entity.collectiontype.fund;
            if( vfund.objid != obj.fund?.objid ) 
                throw new Exception("The selected stub must have a fund that matches the collectiontype"  );
        }
        
        if ( onselect ) onselect( obj ); 
        return "_close";
        //return doOk();
    }
    
}