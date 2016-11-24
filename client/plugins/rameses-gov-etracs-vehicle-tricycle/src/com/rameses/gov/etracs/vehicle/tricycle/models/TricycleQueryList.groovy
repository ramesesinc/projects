package com.rameses.gov.etracs.vehicle.tricycle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import com.rameses.gov.etracs.vehicle.models.*;

public class TricycleQueryList extends CrudListModel {
    
    String getSchemaName() {
        return "vehicle_account_tricycle";
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
        if(custom.engineno) {
            arr << "engineno LIKE :engineno";
        }
        if(custom.bodyno) {
            arr << "bodyno LIKE :bodyno";
        }
        return [ arr.join(" AND "), custom ];
    }
    
    
}