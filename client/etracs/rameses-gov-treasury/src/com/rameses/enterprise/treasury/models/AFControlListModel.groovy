package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AFControlListModel extends CrudListModel {

    @Script("User")
    def user;
    
    @PropertyChangeListener
    def listener = [
        "query.*" : { o->
            reload();
        }
    ];
    
    boolean getShowRespcenterFilter() {
        if( tag == "COLLECTION") return false;
        return true;
    }
    
    boolean getShowIssuedToFilter() {
        if( tag == "COLLECTION") return false;
        return true;
    }
    
    def getCustomFilter() {
        def str = [];
        def m = [:];
        if( query.af ) {
            str << " afid=:afid ";
            m.afid = query.af.objid;
        }
        if( tag == 'COLLECTION' ) {
            str << " owner.objid = :userid ";
            m.userid = user.userid;
            /*
            if(OsirisContext.env.ORGROOT != 1 ) {
                str << "respcenter.objid =:orgid";
                m.orgid = OsirisContext.env.ORGID;
            }
            */
        };
        else if( tag == "SUBCOLLECTION" ) {
            str << " assignee.objid = :userid ";
            m.userid = user.userid;
        }
        else {
            if( query.issuedto?.objid ) {
                str << " owner.objid = :userid ";
                m.userid = query.issuedto.objid;
            }
        }
        /*
        else {
            if( query.respcenter?.objid ) {
                str << " respcenter.objid=:respcenterid ";
                m.respcenterid = query.respcenter.objid;
            }
        }
        */
        if( !str )  return null;
        return [ str.join( " AND "), m ];
    }
    
}    