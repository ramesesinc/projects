package com.rameses.gov.etracs.vehicle.tricycle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

public class TricycleLookupNewForm extends CrudFormModel {
    
    def handler;
    
    def save() {
        try {
            def r = super.save();
            if(handler) handler(entity);
            return "_close";
        }
        catch(Warning w) {
            if( w.message == "ok" ) {
                if(handler) handler(w.info);
                return "_close";
            }
            else {
                def msg = w.message;
                def b = MsgBox.confirm(msg+"\nProceed to use this vehicle instead?");
                if( b ) {
                    if(handler) handler(w.info);
                    return "_close";
                }
            }
            return null;
        }
    }
    
}