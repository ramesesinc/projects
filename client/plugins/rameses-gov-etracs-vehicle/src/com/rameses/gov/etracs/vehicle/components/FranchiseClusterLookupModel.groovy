package com.rameses.gov.etracs.vehicle.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

class FranchiseClusterLookupModel extends ComponentBean {

    def vehicletype;    
    def clusterList = [ 'MAIN', 'CLUSTER1', 'CLUSTER 2' ]
    
    public def getCluster() {
        if( getValue()==null ) {
            setValue([:]);
        }
        def o = getValue();
        return o; 
    }
    
    public void setCluster( def o ) { 
        if(o==null) o = [:];
        setValue( o );
    }
    
}