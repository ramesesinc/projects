package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import com.rameses.util.*;


/******************************************************************************
* The lookup criteria is as follows:
*    a) NO client code
*           Display all itemaccount in collectiongroup  
*    c) There is client code, no account in collection type
*           Display only itemaccount where orgid = clientcode
*    d) There is client code, there is account in collection type         
*           Display items in itemaccount for collectiontype
*           where orgid is NULL or orgid = clientcode
*******************************************************************************/
class CollectionGroupLookupModel extends CrudLookupModel {
    
    //pass the entity so the collection group lookup can insert the items on it;
    //this entity could be 
    def receipt;

    void afterInit() {
        if(receipt == null ) throw new Exception("receipt must be set in collectiongroup lookup");
    }
    
    public def getCustomFilter() {
        def orgid = null;
        if( query.customorgid ) {
            orgid = query.customorgid;
        }
        else if( OsirisContext.env.ORGROOT != 1 ) {
            orgid =  OsirisContext.env.ORGID;
        }
        if(orgid == null ) {
            return ["orgid IS NULL"]
        }
        else {
            return ["orgid = :orgid", [orgid: orgid]];
        }
    }
    
    //if you will use collection group lookup in rules make 
    def lookupSelectedValue( def o ) {
        if(tag == "rules") return o;
        //lookup items here
        def orgid = null;
        if( query.customorgid ) {
            orgid = query.customorgid;
        }
        else if( OsirisContext.env.ORGROOT != 1 ) {
            orgid =  OsirisContext.env.ORGID;
        }
        
        def m = [_schemaname:"vw_cashreceipt_itemaccount_collectiongroup"];
        m.findBy = [collectiongroupid: o.objid ];
        if( orgid ) {
            m.where = ["(orgid IS NULL OR orgid = :orgid)", [orgid: orgid]];
        }
        def items = queryService.getList(m);
        if(!items)
            throw new Exception("No items defined for this collection group");
         
        boolean has_sharing = ( o.sharing.toString() == "1"); 
        if ( has_sharing ) { 
            def amt = MsgBox.prompt( "Please enter amount" );
            if( amt == null ) return null;

            def sharing_amount = new BigDecimal( amt.toString() );
            items.each {
                it.amount = NumberUtil.round( sharing_amount * (it.defaultvalue ? it.defaultvalue : 0.0));
            }           
        }
        def newitems = []; 
        items.each{ 
            def rci = [objid: 'RCTI-'+ new java.rmi.server.UID()]; 
            if ( it.amount != null ) { 
                rci.amount = it.amount; 
            } else {
                rci.amount = ( it.defaultvalue ? it.defaultvalue : 0.0 );
            }
            rci.item = [ 
                objid : it.objid, 
                code  : it.code, 
                title : it.title, 
                fund  : it.fund, 
                valuetype : it.valuetype, 
                defaultvalue : it.defaultvalue 
            ];                  
            if ( it.valuetype == 'FIXEDUNIT' ) {
                rci.remarks = "qty@1"; 
            } 
            newitems << rci; 
        }
        newitems.sort{( it.orderno ? it.orderno : 0 )} 
        receipt.items.addAll( newitems ); 
        receipt.amount = receipt.items.sum{( it.amount ? it.amount : 0.0 )} 
    }    
    
}    