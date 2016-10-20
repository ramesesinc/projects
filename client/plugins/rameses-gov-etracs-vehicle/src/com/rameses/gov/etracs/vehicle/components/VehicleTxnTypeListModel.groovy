package com.rameses.gov.etracs.vehicle.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;

public class VehicleTxnTypeListModel extends ComponentBean {
    
    @Service("QueryService")
    def querySvc;
    
    def _typeList;
    
    public def getTypeList() {
        if(!_typeList ) {
            def m = [_schemaname: 'vehicle_txntype', _limit: 20];
            _typeList = querySvc.getList(m);
        }
        return _typeList;
    }
    
    public def getType() {
        return super.getValue();
    }
    
    public void setType( def o ) {
        super.setValue( o );
    }
    
}