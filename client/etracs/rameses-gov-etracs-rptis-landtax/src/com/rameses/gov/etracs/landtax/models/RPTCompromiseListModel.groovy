package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.seti2.models.*;

class RPTCompromiseListModel extends CrudListModel
{
    public boolean isAutoResize(){
        return false;
    }
    
    public def getStates() {
        return [
            [id:null, title:'ALL'],
            [id:'DRAFT', title:'DRAFT'],
            [id:'FORPAYMENT', title:'FOR PAYMENT'],
            [id:'FORAPPROVAL', title:'FOR APPROVAL'],
            [id:'APPROVED', title:'APPROVED'],
            [id:'CLOSED', title:'CLOSED'],
            [id:'DEFAULTED', title:'DEFAULTED'],
        ]
    }   
}