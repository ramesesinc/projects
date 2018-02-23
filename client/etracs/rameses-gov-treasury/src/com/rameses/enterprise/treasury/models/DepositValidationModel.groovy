package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.rcp.framework.ValidatorException;

class DepositValidationModel  {

    def refno;
    def refdate;
    def handler;
    
    def doCancel() {
        return "_close";
    }

    def doOk() {
        handler( [refno: refno, refdate: refdate ] );
        return "_close";
    }
    
}    