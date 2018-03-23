package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

class SelectAFModel extends CrudLookupModel {

    @Service("AFControlService")
    def service;
    
    @Script( "User" )
    def user;

    def entity;

    String title = "Select Stub to use";

    def getCustomFilter() {
        def arr = [];
        arr << "state='ISSUED'";
        arr << "afid = :formno";
        arr << "assignee.objid = :uid";
        arr << "txnmode=:mode";
        arr << "currentseries <= endseries";
        arr << "active=0";
        def p = [:];
        p.formno = entity.formno;
        p.uid = user.userid;
        p.mode = entity.txnmode;
        //arr << "lockid IS NULL"
        if( entity.collectiontype?.fund?.objid ) {
            arr << "( fund.objid = :fundid OR fund.objid IS NULL)";
            p.fundid = entity.collectiontype.fund.objid; 
        }
        else {
            arr << " fund.objid IS NULL ";
        }
        if(OsirisContext.env.ORGROOT == 1 ) {
            arr << " respcenter.objid IS NULL ";
        }
        else {
            arr << " respcenter.objid = :orgid ";
            p.orgid = OsirisContext.env.ORGID;
        }
        return [ arr.join(" AND "), p ];
    }
    
    def doOk() {
        def obj = listHandler.getSelectedValue();
        if( entity.collectiontype?.fund?.objid ) {
            def vfund = entity.collectiontype.fund;
            if( vfund.objid != obj.fund?.objid ) 
                throw new Exception("The selected stub must have a fund that matches the collectiontype"  );
        }
        service.activateSelectedControl( [objid: obj.objid ] );
        return "_close";
        //return doOk();
    }
    
}