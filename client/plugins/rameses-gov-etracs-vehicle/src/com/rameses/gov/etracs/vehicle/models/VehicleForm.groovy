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

public class VehicleForm extends CrudFormModel {
    
    String title = "Vehicle";
    
    def handler;

    def save() {
        try {
            super.save();
            if(handler) handler(entity);
            return "_close";
        }
        catch(com.rameses.util.Warning w) {
            if( w.message != "ok" ) {
                MsgBox.err(w.message);
            }
            if(handler) handler(w.info);
            return "_close";
        }
        catch(e) {
            throw e;
        }
    }

    
}