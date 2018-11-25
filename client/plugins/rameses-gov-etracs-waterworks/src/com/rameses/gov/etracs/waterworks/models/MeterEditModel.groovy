package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.treasury.common.models.*;

class MeterEditModel extends ChangeInfoModel {

    def meterStates =  ["ACTIVE","DISCONNECTED","DEFECTIVE"];
    
    def getFormFields() {
        def zfields = [];
        if( tag == "state" ) {
            zfields << [name:"state", caption:"Status", datatype: "combo", 
                        items:"meterStates"]            
        }
        else {
            return null;
        }
       return zfields;
    }

    void validateEntry() {
        if( tag == "reading") {
            if( data.lastreading >= entity.capacity.toInteger() )
                throw new Exception("Last reading must be less than capacity");
        }
    }
    
    void beforeInit() {
        handler = {
            caller.reloadEntity();
        }
    }

}
