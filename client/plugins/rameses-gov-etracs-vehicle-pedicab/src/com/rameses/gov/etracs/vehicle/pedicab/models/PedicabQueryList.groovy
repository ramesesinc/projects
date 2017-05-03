package com.rameses.gov.etracs.vehicle.pedicab.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;

public class PedicabQueryList extends CrudListModel {
    
    String getSchemaName() {
        return "vehicle_pedicab";
    }
    
    def custom = [:];
    
    def getCustomFilter() {
        def arr = [];
        if(custom.ownername) {
            arr << "owner.name LIKE :ownername";
        }
        if(custom.controlno) {
            arr << "control.controlno LIKE :controlno";
        }
        if(custom.plateno) {
            arr << "plateno LIKE :plateno";
        }
        return [ arr.join(" AND "), custom ];
    }
    
    
}