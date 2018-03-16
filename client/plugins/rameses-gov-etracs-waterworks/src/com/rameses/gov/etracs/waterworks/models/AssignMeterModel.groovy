package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class AssignMeterModel {
    
    def handler;
    
    def entity = [:];
    
    def doOk() {
        handler( entity );
        return "_close";
    }
    
    def doCancel() {
        return "_close";
    }
    
}