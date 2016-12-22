package com.rameses.gov.etracs.vehicle.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class EngineDataTypeModel extends ComponentBean {

    def makeList = [ 'HANSHIN', 'MITSUBISHI', 'TOYOTA' ]
    
    public def getEngine() {
        if( getValue()==null ) {
            setValue([:]);
        }
        def o = getValue();
        return o; 
    }
    
    public void setEngine( def o ) { 
        if(o==null) o = [:];
        setValue( o );
    }
    
}