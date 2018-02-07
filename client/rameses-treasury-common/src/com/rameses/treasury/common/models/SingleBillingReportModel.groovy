package com.rameses.treasury.common.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.util.*;
import com.rameses.seti2.models.*;
 
public class SingleBillingReportModel extends FormReportModel  {
    
    
    public String getReportId() {
        return "singlebillingreport";
    }
    
    public def getConfInfo() {
        def d = super.getConfInfo();
        d.rulename = getRulename();
        return d;
    }
    
    public String getRulename() {
        String s = invoker.properties.rulename;
        if( s!=null ) {
            return s;
        }
        s = workunit?.info?.workunit_properties?.rulename;
        if( s != null ) return s;
    }
    
    public def preview() {
        def entity =  caller?.entityContext;
        if( entity && !query.objid ) {
            query.objid = entity.objid;
        }
        return super.preview();
    }
}