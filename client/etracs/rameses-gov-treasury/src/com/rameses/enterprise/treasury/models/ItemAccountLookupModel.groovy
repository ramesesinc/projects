package com.rameses.enterprise.treasury.models; 

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

/******************************************************************************
* The itemaccount is used everywhere
*******************************************************************************/
class ItemAccountLookupModel extends CrudLookupModel {

    def fundid;
    def queryFilter;

    public void beforeQuery( def m ) {
        m.orgid = OsirisContext.env.ORGID; 
        if( queryFilter ) m._queryFilter = queryFilter;
    }
    
    public def getCustomFilter() {
        def s = [ " state = 'ACTIVE' "];
        def parm = [:];
        if(tag) {
            if(tag.matches('CASH|CASH_IN_BANK') ) {
                s << "type = :atype";
                parm.atype = tag;
            }
            else if( tag.matches(".*REVENUE")) {
                if(tag.startsWith("MAIN")) s << " parentid IS NULL";
                s << "type IN ('REVENUE','NONREVENUE','PAYABLE')";
            }
            else if( tag.matches(".*PAYABLE")) {
                if(tag.startsWith("MAIN")) s << " parentid IS NULL";
                s << "type = 'PAYABLE' ";
            }
        }
        if(fundid) {
            s << "fund.objid = :fundid";
            parm.fundid = fundid;
        }
        return [s.join(" AND "), parm ];
    }
   
    
}