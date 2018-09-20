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
class CashReceiptItemLookupModel extends CrudLookupModel {

    def fundid;
    def queryFilter;
    
    public void beforeQuery( def m ) {
        m.orgid = OsirisContext.env.ORGID; 
        if( queryFilter ) m._queryFilter = queryFilter;
    }
    
    public def getCustomFilter() {
        def s = [];
        def parm = [:];
        if( OsirisContext.env.ORGROOT == 1 ) {
            s << "parentid IS NULL";
        } 
        else {
            s << "parentid IS NOT NULL AND org.objid =:orgid";
            parm.orgid = OsirisContext.env.ORGID;
        }
        if(fundid) {
            s << "fund.objid = :fundid";
            parm.fundid = fundid;
        }
        s << "type IN ('REVENUE','NONREVENUE','PAYABLE')";
        return [s.join(" AND "), parm ];
    }
    
    def lookupSelectedValue( def obj ) {
        obj.amount = obj.defaultvalue;
        if(obj.valuetype == "FIXEDUNIT") {
            def m = MsgBox.prompt( "Enter QTY : " );
            if( !m || m == "null" ) throw new Exception("Please provide qty"); 
            if( !m.isInteger() ) throw new Exception("Qty must be numeric"); 
            obj.amount = Integer.parseInt( m )*obj.defaultvalue; 
            obj.remarks = "qty@"+Integer.parseInt( m ); 
        } 
        return obj;
    }
}