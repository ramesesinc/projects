package com.rameses.gov.etracs.landtax.models;

import com.rameses.osiris2.client.*
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;

class RPTTaxCreditListModel extends CrudListModel
{
    @Service('Var')
    def var;

    public boolean isAutoResize(){
        return false;
    }
    
    public def getStates() {
        return [
            [id:null, title:'ALL'],
            [id:'DRAFT', title:'DRAFT'],
            [id:'APPROVED', title:'APPROVED'],
            [id:'POSTED', title:'POSTED'],
        ]
    }
}