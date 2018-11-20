package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

public class MeterModel extends CrudFormModel {
    
    def meterStates = ["ACTIVE","DISCONNECTED","DEFECTIVE"];
    
    def edit() {
        return showDropdownMenu("edit");
    }
   
    void openFormAccount() {
        entity = caller.entity.meter;
        super.open();
    }

}
