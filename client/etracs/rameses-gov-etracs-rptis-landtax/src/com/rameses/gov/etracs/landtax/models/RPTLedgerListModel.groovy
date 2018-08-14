package com.rameses.gov.etracs.landtax.models;

import com.rameses.osiris2.client.*
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;

class RPTLedgerListModel extends CrudListModel
{
    public boolean isAutoResize(){
        return false;
    }
    
    public def getStates() {
        return [
            [id:null, title:'ALL'],
            [id:'PENDING', title:'PENDING'],
            [id:'APPROVED', title:'APPROVED'],
            [id:'CANCELLED', title:'CANCELLED'],
        ]
    }   
    
    boolean isCreateAllowed() {
        if ('PROVINCE'.equalsIgnoreCase(OsirisContext.env.ORGCLASS)) {
            return false;
        } else {
            return super.isCreateAllowed();
        }
    }
}