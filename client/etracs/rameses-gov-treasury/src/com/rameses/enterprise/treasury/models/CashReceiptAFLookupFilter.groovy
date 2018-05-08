package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class CashReceiptAFLookupFilter {

    public static def getFilter( param ) {
        def env = OsirisContext.env;
        def p = [:];
        def arr = [];
        arr << "state='ISSUED'";
        arr << "afid = :formno";
        arr << "assignee.objid = :uid";
        arr << "txnmode=:mode";
        arr << "currentseries <= endseries";
        if ( param.active != null ) {
            arr << "active=:active";
            p.active = param.active;
        }
        if ( param.startseries != null && param.startseries > 0 ) {
            arr << " currentseries = :currentseries ";
            p.currentseries = param.startseries; 
        }
        
        p.uid = env.USERID;
        p.formno = param.formno;
        p.mode = param.txnmode;
        
        if( param.collectiontype?.fund?.objid ) {
            arr << "( fund.objid = :fundid OR fund.objid IS NULL)";
            p.fundid = param.collectiontype.fund.objid; 
        }
        else {
            arr << " fund.objid IS NULL ";
        }
        if(env.ORGROOT == 1 ) {
            arr << " respcenter.objid IS NULL ";
        }
        else {
            arr << " respcenter.objid = :orgid ";
            p.orgid = env.ORGID;
        }
        return [ arr.join(" AND "), p ];
    }
}