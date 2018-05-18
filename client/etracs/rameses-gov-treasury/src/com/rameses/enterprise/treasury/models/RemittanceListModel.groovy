package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class RemittanceListModel extends CrudListModel {

    def getCustomFilter() {
        if ( tag == 'COLLECTOR' ) {
            return [
                " collector.objid = :collectorid ", 
                [ collectorid: OsirisContext.env.USERID ] 
            ]; 
        }
        
        return null; 
    }
    
    void beforeRemoveItem() {
        if ( selectedItem.state.toString() != 'DRAFT' ) 
            throw new Exception("You are not allowed to delete this transaction"); 

        if ( selectedItem.collector?.objid != OsirisContext.env.USERID ) 
            throw new Exception("You are not allowed to delete this transaction"); 
    }
}    