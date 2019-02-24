package com.rameses.enterprise.treasury.models; 

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;

/******************************************************************************
* The itemaccount is used everywhere
* The lookup criteria is as follows:
*    a) NO client code, no account in collection type
*           Display all itemaccount 
*    b) NO client code, there is account in collection type
*           Display all itemaccount for collection type
*    c) There is client code, no account in collection type
*           Display only itemaccount where orgid = clientcode
*    d) There is client code, there is account in collection type         
*           Display items in itemaccount for collectiontype
*           where orgid is NULL or orgid = clientcode
*******************************************************************************/
class CashReceiptItemLookupModel extends CrudLookupModel {

    def queryFilter;
    def collectiontypeid;
    
    public void beforeQuery( def m ) {
        if( queryFilter ) m._queryFilter = queryFilter;
        //m.debug = true;
    }
    
    public String getSchemaName() {
        if ( query.collectiontype?.hasitems ) {
            return "vw_cashreceipt_itemaccount_collectiontype";
        } else {
            return "vw_cashreceipt_itemaccount";
        }
    }
    
    public String getOrderBy() {
        def o1 = null; 
        def o2 = super.getOrderBy(); 
        if ( query.collectiontype?.hasitems ) {
            o1 = "sortorder, code"; 
        } else {
            o1 = "code"; 
        } 
        return [o1, o2].findAll{( it )}.join(', '); 
    }
    
    public def getCustomFilter() {
        def s = [];
        def parm = [:];
        
        //_orgid is the org id that is not the root
        def _orgid = null;
        if( query.customorgid ) {
            _orgid = query.customorgid;
        }
        else if( OsirisContext.env.ORGROOT != 1 ) {
            _orgid = OsirisContext.env.ORGID;
        } 
        
        if( query.collectiontype?.hasitems ) {
            s << "collectiontypeid = :colltypeid";
            parm.colltypeid = query.collectiontype.objid;
        }
        else if( query.collectiontype.fund?.objid !=null ) {
            s << "fund.objid = :fundid";
            parm.fundid = query.collectiontype.fund?.objid;
        }
        
        //determine org
        if(_orgid) {
            s << "orgid = :orgid";
            parm.orgid = _orgid;
        } else {
            s << "orgid IS NULL";
        }
        s << "NOT(hidefromlookup = 1)";
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