package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.seti2.models.*;

class RPTLedgerAvDifferenceModel
{
    def svc; 
    def entity; //ledger reference
    def difference;
    def mode;
    def onadd = {};
    def onupdate = {};
    
    void create() {
        difference = [:]
        difference.parent = entity;
        difference.paid = false;
        mode = 'create';
    }
    
    void edit() {
        mode = 'edit';
    }
    
    def save() {
        if (mode == 'create'){
            if (MsgBox.confirm('Add difference?') ){
                svc.createAvDifference(difference);
                onadd();
            }
        } else {
            if (MsgBox.confirm('Update difference?') ){
                svc.updateAvDifference(difference);
                onupdate();
            }
        }
        return '_close';
    }
}