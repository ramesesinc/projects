package com.rameses.gov.etracs.landtax.models;

import com.rameses.osiris2.client.*
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;

class RPTLedgerListModel extends CrudListModel
{
    @Service('Var')
    def var;

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

    public def getColumnList() {
        def cols = super.getColumnList();
        def beneficiarycol = cols.find{ it.extname == 'beneficiary_name'}
        if (beneficiarycol) {
            beneficiarycol.caption = 'Beneficiary'
        }
        return cols 
    }   
    
    boolean isCreateAllowed() {
        def allowCreate = var.getProperty('landtax_province_allow_create_ledger', false);

        if ('PROVINCE'.equalsIgnoreCase(OsirisContext.env.ORGCLASS) && !allowCreate) {
            return false;
        } else {
            return super.isCreateAllowed();
        }
    }
}